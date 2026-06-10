package net.dzultra.betterfoxcraft.commands;

public class DiscordCommand {
//    public static LiteralArgumentBuilder<FabricClientCommandSource> getCommand() {
//        return ClientCommands.literal("dctest")
//                .then(ClientCommands.argument("user", StringArgumentType.word())
//                        .then(ClientCommands.argument("punishment", StringArgumentType.word())
//                            .then(ClientCommands.argument("offence", StringArgumentType.greedyString())
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
