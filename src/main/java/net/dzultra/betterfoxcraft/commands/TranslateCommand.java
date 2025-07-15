package net.dzultra.betterfoxcraft.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.suuft.libretranslate.Language;
import net.suuft.libretranslate.Translator;

public class TranslateCommand {
    public static LiteralArgumentBuilder<FabricClientCommandSource> getCommand() {
        return ClientCommandManager.literal("translate")
                .then(ClientCommandManager.argument("language_text", StringArgumentType.string())
                        .then(ClientCommandManager.argument("language_translate", StringArgumentType.string())
                                .then(ClientCommandManager.argument("text", StringArgumentType.greedyString())
                                        .executes(context -> {
                                            String language_text = StringArgumentType.getString(context, "language_text");
                                            String language_translate = StringArgumentType.getString(context, "language_translate");
                                            String text = StringArgumentType.getString(context, "text");
                                            try {
                                                Language from = Language.valueOf(language_text.toUpperCase());
                                                Language to = Language.valueOf(language_translate.toUpperCase());
                                                String result = (Translator.translate(from, to, text));
                                                MinecraftClient.getInstance().player.sendMessage(Text.literal("\nTranslation: " + result + "\n")
                                                        .setStyle(Style.EMPTY.withClickEvent(new ClickEvent.CopyToClipboard(result))
                                                                .withColor(Formatting.GOLD))
                                                        , false);

                                            } catch (IllegalArgumentException e) {
                                                System.out.println(e.getMessage());
                                                MinecraftClient.getInstance().player.sendMessage(Text.literal("Can't recognize the language!"), false);
                                            }
                                            return 1;
                                        })
                                )
                        )
                );
    }
}