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
import net.minecraft.util.Formatting;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;
import java.util.Comparator;
import java.util.Optional;
import java.util.stream.Stream;

public class ImgurCommand {
    private static final String IMGUR_UPLOAD_URL = "https://api.imgur.com/3/image";
    public static URI latestGeneratedImgurLink = URI.create("None");
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

        client.player.sendMessage(Text.literal("Uploading Screenshot to Imgur. This can take a few seconds")
                .setStyle(Style.EMPTY.withColor(Formatting.GREEN)), false);

        // Find latest Screenshot
        Optional<Path> latestScreenshot = getLatestScreenshot(screenshotsDir);
        if (latestScreenshot.isEmpty()) {
            client.execute(() -> client.player.sendMessage(Text.literal("No screenshot found"), false));
            return 1;
        }

        File imageFile = latestScreenshot.get().toFile();

        new Thread(() -> {
            try {
                URI response = URI.create(uploadToImgur(imageFile, title, description));
                latestGeneratedImgurLink = response;
                client.execute(() -> client.player.sendMessage(
                        Text.literal("Image successfully uploaded: " + response)
                                .setStyle(Style.EMPTY
                                        .withClickEvent(new ClickEvent.OpenUrl(response))
                                ), false
                ));
            } catch (IOException | InterruptedException e) {
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

    protected static String uploadToImgur(File imageFile, String title, String description) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newBuilder()
                .connectTimeout(java.time.Duration.ofSeconds(30))
                .build();

        byte[] imageBytes = Files.readAllBytes(imageFile.toPath());
        String base64Image = Base64.getEncoder().encodeToString(imageBytes);

        String boundary = "----ImgurBoundary" + System.currentTimeMillis();
        String body = "--" + boundary + "\r\n" +
                "Content-Disposition: form-data; name=\"image\"\r\n\r\n" + base64Image + "\r\n" +
                "--" + boundary + "\r\n" +
                "Content-Disposition: form-data; name=\"title\"\r\n\r\n" + title + "\r\n" +
                (description.isEmpty() ? "" : "--" + boundary + "\r\n" +
                        "Content-Disposition: form-data; name=\"description\"\r\n\r\n" + description + "\r\n") +
                "--" + boundary + "--\r\n";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(IMGUR_UPLOAD_URL))
                .header("Authorization", "Client-ID " + AutoConfig.getConfigHolder(ModConfig.class).getConfig().clientID)
                .header("Content-Type", "multipart/form-data; boundary=" + boundary)
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            System.err.println("Response Code: " + response.statusCode());
            System.err.println("Response Body: " + response.body());
            throw new IOException("Unexpected response code: " + response.statusCode());
        }
        return parseImgurLink(response.body());
    }

    private static String parseImgurLink(String responseBody) {
        String marker = "\"link\":\"";
        int start = responseBody.indexOf(marker) + marker.length();
        int end = responseBody.indexOf("\"", start);
        return responseBody.substring(start, end);
    }
}


