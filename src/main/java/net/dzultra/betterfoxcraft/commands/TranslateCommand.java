package net.dzultra.betterfoxcraft.commands;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

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
        return ClientCommandManager.literal("translate")
                .then(ClientCommandManager.argument("language_text", StringArgumentType.string())
                        .then(ClientCommandManager.argument("language_translate", StringArgumentType.string())
                                .then(ClientCommandManager.argument("text", StringArgumentType.greedyString())
                                        .executes(context -> {
                                            String fromLang = StringArgumentType.getString(context, "language_text");
                                            String toLang = StringArgumentType.getString(context, "language_translate");
                                            String text = StringArgumentType.getString(context, "text");

                                            new Thread(() -> {
                                                try {
                                                    String translated = translate(fromLang, toLang, text);
                                                    MinecraftClient.getInstance().execute(() -> {
                                                        MinecraftClient.getInstance().player.sendMessage(
                                                                Text.literal("\nTranslation: " + translated + "\n")
                                                                        .setStyle(Style.EMPTY
                                                                                .withClickEvent(new ClickEvent.CopyToClipboard(translated))
                                                                                .withHoverEvent(new HoverEvent.ShowText(Text.literal("Click to Copy!")
                                                                                        .setStyle(Style.EMPTY.withColor(Formatting.GREEN))))
                                                                                .withColor(Formatting.GOLD)),
                                                                false
                                                        );
                                                    });
                                                } catch (Exception e) {
                                                    MinecraftClient.getInstance().execute(() ->
                                                            MinecraftClient.getInstance().player.sendMessage(Text.literal("Error translating: " + e.getMessage()), false)
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