package net.dzultra.betterfoxcraft.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.dzultra.betterfoxcraft.discord.DiscordSlashCommandHandler;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;


public class DiscordCommand {
//    public static LiteralArgumentBuilder<FabricClientCommandSource> getCommand() {
//        return ClientCommandManager.literal("dctest")
//                .then(ClientCommandManager.argument("user", StringArgumentType.word())
//                        .then(ClientCommandManager.argument("punishment", StringArgumentType.word())
//                            .then(ClientCommandManager.argument("offence", StringArgumentType.greedyString())
//                                .executes(ctx -> reportToDiscord(
//                                        StringArgumentType.getString(ctx,"user"),
//                                        StringArgumentType.getString(ctx,"punishment"),
//                                        StringArgumentType.getString(ctx,"offence")
//                                ))
//                            )
//                        )
//                );
//    }
//
//    private static int reportToDiscord(String user, String punishment, String offence) {
//        ClientPlayerEntity player = MinecraftClient.getInstance().player;
//        DiscordSlashCommandHandler.currentReportMsg =
//                "- **Player:** " + user + "\n" +
//                "- **Offence:** " + offence + "\n" +
//                "- **Punishment:** " + punishment + "\n" +
//                "- **Evidence:** " + ImgurCommand.latestGeneratedImgurLink;
//
//        player.sendMessage(Text.literal("Updated Report Msg").setStyle(Style.EMPTY.withColor(Formatting.GREEN)), false);
//
//        return 0;
//    }
}
