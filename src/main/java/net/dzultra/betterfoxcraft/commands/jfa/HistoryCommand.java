package net.dzultra.betterfoxcraft.commands.jfa;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import me.shedaniel.autoconfig.AutoConfig;
import net.dzultra.betterfoxcraft.ModConfig;
import net.dzultra.jfa.punishments.PlayerPunishments;
import net.dzultra.jfa.punishments.Punishment;
import net.fabricmc.fabric.api.client.command.v2.ClientCommands;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class HistoryCommand {
    public static LiteralArgumentBuilder<FabricClientCommandSource> getCommand() {
        return ClientCommands.literal("hist")
                .then(ClientCommands.argument("username", StringArgumentType.word())
                        .executes(ctx -> runHistory(ctx, 1, true))
                        .then(ClientCommands.argument("page", IntegerArgumentType.integer(1))
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
            source.sendFeedback(Component.literal("Invalid username.").withStyle(ChatFormatting.RED));
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
            Minecraft client = Minecraft.getInstance();
            client.execute(() -> {
                if (result instanceof Exception) {
                    source.sendFeedback(Component.literal("Failed to fetch punishment history.").withStyle(ChatFormatting.RED));
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
            source.sendFeedback(Component.literal("No punishments found for " + username + ".").withStyle(ChatFormatting.RED));
            return;
        }

        int perPage = AutoConfig.getConfigHolder(ModConfig.class).getConfig().punishmentsPerPage;;
        int total = punishments.size();
        int maxPage = (int) Math.ceil((double) total / perPage);

        if (page > maxPage) {
            source.sendFeedback(Component.literal("Page " + page + " does not exist. Max page: " + maxPage).withStyle(ChatFormatting.RED));
            return;
        }

        int start = (page - 1) * perPage;
        int end = Math.min(start + perPage, total);

        List<Punishment> sub = punishments.subList(start, end);
        sendHistoryChat(source, username, sub, page, maxPage);
    }

    private static void sendHistoryChat(FabricClientCommandSource source, String username, List<Punishment> punishments, int page, int maxPage) {
        if (punishments == null || punishments.isEmpty()) {
            source.sendFeedback(Component.literal("No punishments found for " + username + ".").withStyle(ChatFormatting.RED));
            return;
        }

        // ----- HEADER -----
        source.sendFeedback(Component.literal("┌──────────────────────────┐").withStyle(ChatFormatting.DARK_GRAY));
        source.sendFeedback(Component.literal("   Punishment History - " + username + " (" + page + "/" + maxPage + ")").withStyle(ChatFormatting.GOLD, ChatFormatting.BOLD));
        source.sendFeedback(Component.literal("├──────────────────────────┤").withStyle(ChatFormatting.DARK_GRAY));

        // ----- ENTRIES -----
        for (int i = 0; i < punishments.size(); i++) {
            Punishment p = punishments.get(i);
            source.sendFeedback(formatHeaderLine(i + 1 + ((page - 1) * 10), p));
            source.sendFeedback(formatReasonLine(p));
            source.sendFeedback(formatDateLine(p));
            source.sendFeedback(formatExpiresLine(p));
            if (i < punishments.size() - 1) {
                source.sendFeedback(Component.literal("├──────────────────────────┤").withStyle(ChatFormatting.DARK_GRAY));
            }
        }
        source.sendFeedback(Component.literal("└──────────────────────────┘").withStyle(ChatFormatting.DARK_GRAY));
    }

    private static Component formatExpiresLine(Punishment p) {
        String expires = (p.expires() == null || p.expires().isEmpty()) ? "Permanent" : p.expires();
        return Component.literal(" Expires: ").withStyle(ChatFormatting.YELLOW).append(Component.literal(expires).withStyle(ChatFormatting.GRAY));
    }

    private static Component formatHeaderLine(int index, Punishment p) {
        ChatFormatting typeColor = switch (p.type().toLowerCase()) {
            case "ban" -> ChatFormatting.RED;
            case "mute" -> ChatFormatting.LIGHT_PURPLE;
            case "kick" -> ChatFormatting.GOLD;
            case "warn", "warning" -> ChatFormatting.YELLOW;
            default -> ChatFormatting.GRAY;
        };

        return Component.literal(" #" + index + " ").withStyle(ChatFormatting.YELLOW)
                .append(Component.literal(p.type().toUpperCase()).withStyle(typeColor, ChatFormatting.BOLD))
                .append(Component.literal(" » ").withStyle(ChatFormatting.DARK_GRAY))
                .append(Component.literal(p.moderator()).withStyle(ChatFormatting.AQUA));
    }

    private static Component formatReasonLine(Punishment p) {
        String reason = (p.reason() == null || p.reason().isEmpty()) ? "No reason specified" : p.reason();

        return Component.literal(" Reason: ").withStyle(ChatFormatting.YELLOW)
                .append(Component.literal(reason).withStyle(ChatFormatting.WHITE));
    }

    private static Component formatDateLine(Punishment p) {
        return Component.literal(" Date: ").withStyle(ChatFormatting.YELLOW)
                .append(Component.literal(p.date()).withStyle(ChatFormatting.GRAY));
    }
}

