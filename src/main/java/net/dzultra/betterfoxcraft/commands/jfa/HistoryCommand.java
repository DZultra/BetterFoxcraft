package net.dzultra.betterfoxcraft.commands.jfa;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import me.shedaniel.autoconfig.AutoConfig;
import net.dzultra.betterfoxcraft.ModConfig;
import net.dzultra.jfa.punishments.PlayerPunishments;
import net.dzultra.jfa.punishments.Punishment;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class HistoryCommand {
    public static LiteralArgumentBuilder<FabricClientCommandSource> getCommand() {
        return ClientCommandManager.literal("hist")
                .then(ClientCommandManager.argument("username", StringArgumentType.word())
                        .executes(ctx -> runHistory(ctx, 1, true))
                        .then(ClientCommandManager.argument("page", IntegerArgumentType.integer(1))
                                .executes(ctx -> runHistory(
                                        ctx,
                                        IntegerArgumentType.getInteger(ctx, "page"),
                                        false
                                ))
                        )
                );
    }

    private static int runHistory(CommandContext<FabricClientCommandSource> ctx, int page, boolean forceFetch) {
        FabricClientCommandSource source = ctx.getSource();
        String username = StringArgumentType.getString(ctx, "username");

        if (username == null || username.isEmpty()) {
            source.sendFeedback(Text.literal("Invalid username.").formatted(Formatting.RED));
            return 0;
        }

        // ---------- USE CACHE ----------
        if (!forceFetch && PunishmentCache.has(username)) {
            List<Punishment> cached = PunishmentCache.get(username);
            displayPage(source, username, cached, page);
            return 1;
        }

        // ---------- FETCH NEW ----------
        CompletableFuture.supplyAsync(() -> {
            try {
                return new PlayerPunishments(username).getPunishments();
            } catch (Exception e) {
                return e;
            }
        }).thenAccept(result -> {
            MinecraftClient client = MinecraftClient.getInstance();
            client.execute(() -> {
                if (result instanceof Exception) {
                    source.sendFeedback(Text.literal("Failed to fetch punishment history." + ((Exception) result).getMessage()).formatted(Formatting.RED));
                    ((Exception) result).printStackTrace();
                    return;
                }

                List<Punishment> punishments = (List<Punishment>) result;

                PunishmentCache.put(username, punishments);
                displayPage(source, username, punishments, page);
            });
        }).exceptionally(ex -> {
            ex.printStackTrace();
            return null;
        });
        return 1;
    }

    private static void displayPage(FabricClientCommandSource source, String username, List<Punishment> punishments, int page) {
        if (punishments == null || punishments.isEmpty()) {
            source.sendFeedback(Text.literal("No punishments found for " + username + ".").formatted(Formatting.RED));
            return;
        }

        int perPage = AutoConfig.getConfigHolder(ModConfig.class).getConfig().punishmentsPerPage;;
        int total = punishments.size();
        int maxPage = (int) Math.ceil((double) total / perPage);

        if (page > maxPage) {
            source.sendFeedback(Text.literal("Page " + page + " does not exist. Max page: " + maxPage).formatted(Formatting.RED));
            return;
        }

        int start = (page - 1) * perPage;
        int end = Math.min(start + perPage, total);

        List<Punishment> sub = punishments.subList(start, end);
        sendHistoryChat(source, username, sub, page, maxPage);
    }

    private static void sendHistoryChat(FabricClientCommandSource source, String username, List<Punishment> punishments, int page, int maxPage) {
        if (punishments == null || punishments.isEmpty()) {
            source.sendFeedback(Text.literal("No punishments found for " + username + ".").formatted(Formatting.RED));
            return;
        }

        // ----- HEADER -----
        source.sendFeedback(Text.literal("┌──────────────────────────┐").formatted(Formatting.DARK_GRAY));
        source.sendFeedback(Text.literal("   Punishment History - " + username + " (" + page + "/" + maxPage + ")").formatted(Formatting.GOLD, Formatting.BOLD));
        source.sendFeedback(Text.literal("├──────────────────────────┤").formatted(Formatting.DARK_GRAY));

        // ----- ENTRIES -----
        for (int i = 0; i < punishments.size(); i++) {
            Punishment p = punishments.get(i);
            source.sendFeedback(formatHeaderLine(i + 1 + ((page - 1) * 10), p));
            source.sendFeedback(formatReasonLine(p));
            source.sendFeedback(formatDateLine(p));
            source.sendFeedback(formatExpiresLine(p));
            if (i < punishments.size() - 1) {
                source.sendFeedback(Text.literal("├──────────────────────────┤").formatted(Formatting.DARK_GRAY));
            }
        }
        source.sendFeedback(Text.literal("└──────────────────────────┘").formatted(Formatting.DARK_GRAY));
    }

    private static Text formatExpiresLine(Punishment p) {
        String expires = (p.expires() == null || p.expires().isEmpty()) ? "Permanent" : p.expires();
        return Text.literal(" Expires: ").formatted(Formatting.YELLOW).append(Text.literal(expires).formatted(Formatting.GRAY));
    }

    private static Text formatHeaderLine(int index, Punishment p) {
        Formatting typeColor = switch (p.type().toLowerCase()) {
            case "ban" -> Formatting.RED;
            case "mute" -> Formatting.LIGHT_PURPLE;
            case "kick" -> Formatting.GOLD;
            case "warn", "warning" -> Formatting.YELLOW;
            default -> Formatting.GRAY;
        };

        return Text.literal(" #" + index + " ").formatted(Formatting.YELLOW)
                .append(Text.literal(p.type().toUpperCase()).formatted(typeColor, Formatting.BOLD))
                .append(Text.literal(" » ").formatted(Formatting.DARK_GRAY))
                .append(Text.literal(p.moderator()).formatted(Formatting.AQUA));
    }

    private static Text formatReasonLine(Punishment p) {
        String reason = (p.reason() == null || p.reason().isEmpty()) ? "No reason specified" : p.reason();

        return Text.literal(" Reason: ").formatted(Formatting.YELLOW)
                .append(Text.literal(reason).formatted(Formatting.WHITE));
    }

    private static Text formatDateLine(Punishment p) {
        return Text.literal(" Date: ").formatted(Formatting.YELLOW)
                .append(Text.literal(p.date()).formatted(Formatting.GRAY));
    }
}

