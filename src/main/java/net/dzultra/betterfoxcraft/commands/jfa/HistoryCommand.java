package net.dzultra.betterfoxcraft.commands.jfa;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.dzultra.jfa.punishments.PlayerPunishments;
import net.dzultra.jfa.punishments.Punishment;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class HistoryCommand {
    public static LiteralArgumentBuilder<FabricClientCommandSource> getCommand() {
        return ClientCommandManager.literal("hist")
                .then(ClientCommandManager.argument("username", StringArgumentType.word())
                        .executes(HistoryCommand::runHistory)
                );
    }

    private static int runHistory(CommandContext<FabricClientCommandSource> ctx) {
        FabricClientCommandSource source = ctx.getSource();
        String username = StringArgumentType.getString(ctx, "username");

        if (username == null || username.isEmpty()) {
            source.sendFeedback(Text.literal("Invalid username.").formatted(Formatting.RED));
            return 0;
        }

        CompletableFuture
                .supplyAsync(() -> {
                    System.out.println("Before request");
                    return new PlayerPunishments(username).getPunishments();
                })
                .thenAccept(result -> {
                    System.out.println("Accepted");
            MinecraftClient client = MinecraftClient.getInstance();
            client.execute(() -> {
                System.out.println("Client");
                if (result instanceof Exception) {
                    source.sendFeedback(Text.literal("Failed to fetch punishment history.").formatted(Formatting.RED));
                    return;
                }

                System.out.println("Before");
                List<Punishment> punishments = (List<Punishment>) result;
                System.out.println("After Sent Output");
                sendHistoryChat(source, username, punishments);
            });
        });
        return 1;
    }

    private static void sendHistoryChat(FabricClientCommandSource source, String username, List<Punishment> punishments) {
        if (punishments == null || punishments.isEmpty()) {
            source.sendFeedback(Text.literal("No punishments found for " + username + ".").formatted(Formatting.RED));
            return;
        }

        source.sendFeedback(Text.literal("┌──────────────────────────┐").formatted(Formatting.DARK_GRAY));
        source.sendFeedback(Text.literal("   Punishment History - " + username).formatted(Formatting.GOLD, Formatting.BOLD));
        source.sendFeedback(Text.literal("├──────────────────────────┤").formatted(Formatting.DARK_GRAY));
        for (int i = 0; i < punishments.size(); i++) {
            Punishment p = punishments.get(i);
            source.sendFeedback(formatHeaderLine(i + 1, p));
            source.sendFeedback(formatReasonLine(p));
            source.sendFeedback(formatDateLine(p));
            if (i < punishments.size() - 1) {
                source.sendFeedback(Text.literal("├──────────────────────────┤").formatted(Formatting.DARK_GRAY));
            }
        }
        source.sendFeedback(Text.literal("└──────────────────────────┘").formatted(Formatting.DARK_GRAY));
    }


    private static Text formatHeaderLine(int index, Punishment p) {
        Formatting typeColor = switch (p.type().toLowerCase()) {
            case "ban" -> Formatting.RED;
            case "mute" -> Formatting.LIGHT_PURPLE;
            case "kick" -> Formatting.GOLD;
            case "blacklist" -> Formatting.DARK_RED;
            default -> Formatting.GRAY;
        };
        return Text.literal(" #" + index + " ").formatted(Formatting.YELLOW)
                .append(Text.literal(p.type().toUpperCase()).formatted(typeColor, Formatting.BOLD))
                .append(Text.literal(" by ").formatted(Formatting.DARK_GRAY))
                .append(Text.literal(p.moderator()).formatted(Formatting.AQUA));
    }

    private static Text formatReasonLine(Punishment p) {
        return Text.literal(" Reason: ")
                .formatted(Formatting.YELLOW)
                .append(Text.literal((p.reason() == null || p.reason().isEmpty()) ? "No reason specified" : p.reason())
                        .formatted(Formatting.WHITE));
    }

    private static Text formatDateLine(Punishment p) {
        String expires = (p.expires() == null || p.expires().isEmpty())
                ? "Permanent"
                : p.expires();
        return Text.literal(" Date: ").formatted(Formatting.YELLOW)
                .append(Text.literal(p.date()).formatted(Formatting.GRAY))
                .append(Text.literal(" | Expires: ").formatted(Formatting.YELLOW))
                .append(Text.literal(expires).formatted(Formatting.GRAY));
    }
}

