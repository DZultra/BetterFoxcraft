package net.dzultra.betterfoxcraft.commands;

import com.mojang.brigadier.arguments.IntegerArgumentType;
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
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ImgurCommand {
    private static final String IMGUR_UPLOAD_URL = "https://api.imgur.com/3/album";

    public static LiteralArgumentBuilder<FabricClientCommandSource> getCommand() {
        return ClientCommandManager.literal("imgur")
                .then(ClientCommandManager.argument("count", IntegerArgumentType.integer(1))
                        .then(ClientCommandManager.argument("title", StringArgumentType.string())
                                .then(ClientCommandManager.argument("description", StringArgumentType.greedyString())
                                        .executes(context -> uploadScreenshots(context,
                                                IntegerArgumentType.getInteger(context, "count"),
                                                StringArgumentType.getString(context, "title"),
                                                StringArgumentType.getString(context, "description"))))));
    }

    private static int uploadScreenshots(CommandContext<?> context, int count, String title, String description) {
        MinecraftClient client = MinecraftClient.getInstance();
        Path screenshotsDir = client.runDirectory.toPath().resolve("screenshots");

        List<Path> latestScreenshots = getLatestScreenshots(screenshotsDir, count);
        if (latestScreenshots.isEmpty()) {
            client.execute(() -> client.player.sendMessage(Text.literal("No screenshots found"), false));
            return 1;
        }

        new Thread(() -> {
            try {
                String response = uploadToImgur(latestScreenshots, title, description);
                client.execute(() -> client.player.sendMessage(
                        Text.literal("Images successfully uploaded: " + response)
                                .setStyle(Style.EMPTY.withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, response)))
                ));
            } catch (IOException | InterruptedException e) {
                client.execute(() -> client.player.sendMessage(Text.literal("Error while uploading: " + e.getMessage()), false));
                e.printStackTrace();
            }
        }).start();

        return 1;
    }

    private static List<Path> getLatestScreenshots(Path screenshotsDir, int count) {
        try (Stream<Path> files = Files.list(screenshotsDir)) {
            return files
                    .filter(Files::isRegularFile)
                    .filter(file -> file.toString().endsWith(".png"))
                    .sorted(Comparator.comparingLong(file -> -file.toFile().lastModified()))
                    .limit(count)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
            return List.of();
        }
    }

    private static String uploadToImgur(List<Path> imageFiles, String title, String description) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newBuilder()
                .connectTimeout(java.time.Duration.ofSeconds(30))
                .build();

        String boundary = "----ImgurBoundary" + System.currentTimeMillis();
        StringBuilder bodyBuilder = new StringBuilder();

        for (int i = 0; i < imageFiles.size(); i++) {
            byte[] imageBytes = Files.readAllBytes(imageFiles.get(i));
            String base64Image = Base64.getEncoder().encodeToString(imageBytes);

            bodyBuilder.append("--").append(boundary).append("\r\n")
                    .append("Content-Disposition: form-data; name=\"image\"\r\n\r\n")
                    .append(base64Image).append("\r\n");
        }

        bodyBuilder.append("--").append(boundary).append("\r\n")
                .append("Content-Disposition: form-data; name=\"title\"\r\n\r\n")
                .append(title).append("\r\n")
                .append("--").append(boundary).append("\r\n")
                .append("Content-Disposition: form-data; name=\"description\"\r\n\r\n")
                .append(description).append("\r\n")
                .append("--").append(boundary).append("--\r\n");

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(IMGUR_UPLOAD_URL))
                .header("Authorization", "Client-ID " + AutoConfig.getConfigHolder(ModConfig.class).getConfig().clientID)
                .header("Content-Type", "multipart/form-data; boundary=" + boundary)
                .POST(HttpRequest.BodyPublishers.ofString(bodyBuilder.toString()))
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
