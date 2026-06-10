package net.dzultra.betterfoxcraft.commands;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.fabricmc.fabric.api.client.command.v2.ClientCommands;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.Style;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

public class TranslateCommand {

    private static final HttpClient httpClient = HttpClient.newHttpClient();
    private static final String API_KEY = "";

    public static LiteralArgumentBuilder<FabricClientCommandSource> getCommand() {
        return ClientCommands.literal("translate")
                .then(ClientCommands.argument("language_text", StringArgumentType.string())
                        .then(ClientCommands.argument("language_translate", StringArgumentType.string())
                                .then(ClientCommands.argument("text", StringArgumentType.greedyString())
                                        .executes(context -> {
                                            String fromLang = StringArgumentType.getString(context, "language_text");
                                            String toLang = StringArgumentType.getString(context, "language_translate");
                                            String text = StringArgumentType.getString(context, "text");

                                            new Thread(() -> {
                                                try {
                                                    String translated = translate(fromLang, toLang, text);
                                                    Minecraft.getInstance().execute(() -> {
                                                        Minecraft.getInstance().player.sendSystemMessage(
                                                                Component.literal("\nTranslation: " + translated + "\n")
                                                                        .setStyle(Style.EMPTY
                                                                                .withClickEvent(new ClickEvent.CopyToClipboard(translated))
                                                                                .withHoverEvent(new HoverEvent.ShowText(Component.literal("Click to Copy!")
                                                                                        .setStyle(Style.EMPTY.withColor(ChatFormatting.GREEN))))
                                                                                .withColor(ChatFormatting.GOLD))
                                                        );
                                                    });
                                                } catch (Exception e) {
                                                    Minecraft.getInstance().execute(() ->
                                                            Minecraft.getInstance().player.sendSystemMessage(Component.literal("Error translating: " + e.getMessage()))
                                                    );
                                                }
                                            }).start();

                                            return 1;
                                        })
                                )
                        )
                );
    }

    private static String translate(String fromLang, String toLang, String text) throws Exception {
        String encodedText = URLEncoder.encode(text, StandardCharsets.UTF_8);
        String langPair = URLEncoder.encode(fromLang + "|" + toLang, StandardCharsets.UTF_8);
        String url = "https://api.mymemory.translated.net/get?q=" + encodedText +
                "&langpair=" + langPair +
                (API_KEY.isEmpty() ? "" : "&key=" + API_KEY);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new Exception("HTTP error: " + response.statusCode());
        }

        String body = response.body();
        JsonObject json = JsonParser.parseString(body).getAsJsonObject();
        return json.getAsJsonObject("responseData").get("translatedText").getAsString();
    }
}