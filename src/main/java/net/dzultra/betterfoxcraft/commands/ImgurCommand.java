package net.dzultra.betterfoxcraft.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import me.shedaniel.autoconfig.AutoConfig;
import net.dzultra.betterfoxcraft.ModConfig;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import okhttp3.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.Optional;
import java.util.stream.Stream;

public class ImgurCommand {
    private static final String IMGUR_UPLOAD_URL = "https://api.imgur.com/3/image";

    public static LiteralArgumentBuilder<FabricClientCommandSource> getCommand() {
        return ClientCommandManager.literal("imgur")
                .then(ClientCommandManager.argument("title", StringArgumentType.string())
                        .then(ClientCommandManager.argument("description", StringArgumentType.greedyString())
                                .executes(context -> uploadScreenshot(context,
                                        StringArgumentType.getString(context, "title"),
                                        StringArgumentType.getString(context, "description")))));
    }

    private static int uploadScreenshot(CommandContext<?> context, String title, String description) {
        MinecraftClient client = MinecraftClient.getInstance();
        Path screenshotsDir = client.runDirectory.toPath().resolve("screenshots");

        // Find latest Screenshot
        Optional<Path> latestScreenshot = getLatestScreenshot(screenshotsDir);
        if (latestScreenshot.isEmpty()) {
            client.execute(() -> client.player.sendMessage(Text.literal("No screeenshot found"), false));
            return 1;
        }

        File imageFile = latestScreenshot.get().toFile();

        new Thread(() -> {
            try {
                String response = uploadToImgur(imageFile, title, description);
                client.execute(() -> client.player.sendMessage(
                        Text.literal("Image succesfully uploaded: " + response)
                                .setStyle(Style.EMPTY.withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, response)))
                ));
            } catch (IOException e) {
                client.execute(() -> client.player.sendMessage(Text.literal("Error while uploading: " + e.getMessage()), false));
                e.printStackTrace();
            }
        }).start();

        return 1;
    }
    private static Optional<Path> getLatestScreenshot(Path screenshotsDir) {
        try (Stream<Path> files = Files.list(screenshotsDir)) {
            return files
                    .filter(Files::isRegularFile)
                    .filter(file -> file.toString().endsWith(".png"))
                    .max(Comparator.comparingLong(file -> file.toFile().lastModified()));
        } catch (IOException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    private static String uploadToImgur(File imageFile, String title, String description) throws IOException {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
                .writeTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
                .readTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
                .build();

        byte[] imageBytes = Files.readAllBytes(imageFile.toPath());
        String base64Image = java.util.Base64.getEncoder().encodeToString(imageBytes);

        RequestBody body = new FormBody.Builder()
                .add("image", base64Image)
                .add("title", title)
                .add("description", description)
                .build();

        Request request = new Request.Builder()
                .url(IMGUR_UPLOAD_URL)
                .addHeader("Authorization", "Client-ID " + AutoConfig.getConfigHolder(ModConfig.class).getConfig().clientID)
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                System.err.println("Response Code: " + response.code());
                System.err.println("Response Body: " + response.body().string());
                throw new IOException("Unexpectected code: " + response);
            }
            String responseBody = response.body().string();
            return parseImgurLink(responseBody);}
    }

    private static String parseImgurLink(String responseBody) {
        String marker = "\"link\":\"";
        int start = responseBody.indexOf(marker) + marker.length();
        int end = responseBody.indexOf("\"", start);
        return responseBody.substring(start, end);
    }
}