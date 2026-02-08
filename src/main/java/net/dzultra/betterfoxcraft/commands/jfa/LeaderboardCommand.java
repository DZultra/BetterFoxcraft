package net.dzultra.betterfoxcraft.commands.jfa;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.dzultra.jfa.apidata.ServerLeaderboard;
import net.dzultra.jfa.responses.ServerLeaderboardsResponse;
import net.dzultra.jfa.types.Gamemode;
import net.dzultra.jfa.types.Period;
import net.dzultra.jfa.types.leaderboards.*;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;

public class LeaderboardCommand {

    public static LiteralArgumentBuilder<FabricClientCommandSource> getCommand() {
        return ClientCommandManager.literal("leaderboard")
                .then(ClientCommandManager.argument("gamemode", StringArgumentType.word())
                        .suggests((ctx, builder) -> suggestGamemodes(builder))
                        .then(ClientCommandManager.argument("board", StringArgumentType.word())
                                .suggests(LeaderboardCommand::suggestBoards)
                                .then(ClientCommandManager.argument("period", StringArgumentType.word())
                                        .suggests(LeaderboardCommand::suggestPeriods)
                                        .executes(ctx -> runLeaderboard(ctx, -1))
                                        .then(ClientCommandManager.argument("position", IntegerArgumentType.integer(1, 10))
                                                    .executes(ctx ->
                                                            runLeaderboard(ctx, IntegerArgumentType.getInteger(ctx, "position"))
                                                    )
                                        )
                                )
                        )
                );
    }

    private static int runLeaderboard(CommandContext<FabricClientCommandSource> ctx, int position) {
        FabricClientCommandSource source = ctx.getSource();
        String gamemodeArg = StringArgumentType.getString(ctx, "gamemode");
        String boardArg = StringArgumentType.getString(ctx, "board");
        String periodArg = StringArgumentType.getString(ctx, "period");
        LeaderboardType type = parseBoard(gamemodeArg, boardArg);
        Period period;

        try {
            period = Period.valueOf(periodArg.toUpperCase());
        } catch (IllegalArgumentException e) {
            source.sendFeedback(Text.literal("Invalid period type.").formatted(Formatting.RED));
            return 0;
        }

        if (type == null) {
            source.sendFeedback(Text.literal("Invalid leaderboard type.").formatted(Formatting.RED));
            return 0;
        }

        CompletableFuture.supplyAsync(() -> {
                try {
                    return new ServerLeaderboard(type, period);
                } catch (Exception e) {
                    return e;
                }
        }).thenAccept(result -> {
                MinecraftClient client = MinecraftClient.getInstance();
                client.execute(() -> {
                    if (result instanceof Exception) {
                        source.sendFeedback(Text.literal("Failed to fetch leaderboard.").formatted(Formatting.RED));
                        return;
                    }
                    ServerLeaderboard leaderboard = (ServerLeaderboard) result;
                    sendLeaderboardChat(source, leaderboard, position, gamemodeArg);
                });
        });
        return 1;
    }

    private static void sendLeaderboardChat(
            FabricClientCommandSource source,
            ServerLeaderboard leaderboard,
            int position,
            String gamemodeArg
    ) {
        List<ServerLeaderboardsResponse.LeaderboardEntry> data = leaderboard.getEntries();
        if (data == null || data.isEmpty()) {
            source.sendFeedback(Text.literal("No data found.").formatted(Formatting.RED));
            return;
        }

        source.sendFeedback(Text.literal("┌──────────────────────────┐").formatted(Formatting.DARK_GRAY));
        source.sendFeedback(Text.literal("   " + leaderboard.getTitle() + " - " + capitalizeFirst(gamemodeArg)).formatted(Formatting.GOLD, Formatting.BOLD));
        source.sendFeedback(Text.literal("├──────────────────────────┤").formatted(Formatting.DARK_GRAY));
        if (position > 0) {
            ServerLeaderboardsResponse.LeaderboardEntry entry = leaderboard.getEntry(position);
            if (entry == null) {
                source.sendFeedback(Text.literal("Position not found.").formatted(Formatting.RED));
            } else {
                source.sendFeedback(formatEntry(entry));
            }
            source.sendFeedback(Text.literal("└──────────────────────────┘").formatted(Formatting.DARK_GRAY));
            return;
        }
        for (int i = 0; i < Math.min(10, data.size()); i++) {
            source.sendFeedback(formatEntry(data.get(i)));
        }
        source.sendFeedback(Text.literal("└──────────────────────────┘").formatted(Formatting.DARK_GRAY));
    }

    private static Text formatEntry(ServerLeaderboardsResponse.LeaderboardEntry entry) {
        return Text.literal(" #" + entry.position() + " ").formatted(Formatting.YELLOW)
                .append(Text.literal(entry.username())
                        .formatted(Formatting.AQUA))
                .append(Text.literal(" » ")
                        .formatted(Formatting.DARK_GRAY))
                .append(Text.literal(String.valueOf(entry.value()))
                        .formatted(Formatting.GREEN));
    }

    private static CompletableFuture<Suggestions> suggestGamemodes(SuggestionsBuilder builder) {
        List<Gamemode> gmList = List.of(
                Gamemode.ONEBLOCK, Gamemode.KINGDOMS,
                Gamemode.SURVIVAL, Gamemode.PARKOUR
        );

        for (Gamemode gm : gmList) {
            builder.suggest(gm.name().toLowerCase());
        }

        return builder.buildFuture();
    }

    private static CompletableFuture<Suggestions> suggestBoards(CommandContext<FabricClientCommandSource> ctx, SuggestionsBuilder builder) {
        String gamemode = StringArgumentType.getString(ctx, "gamemode");

        LeaderboardType[] values = switch (gamemode.toLowerCase()) {
            case "kingdoms" -> KingdomsLeaderboards.values();
            case "oneblock" -> OneblockLeaderboards.values();
            case "parkour" -> ParkourLeaderboards.values();
            case "survival" -> SurvivalLeaderboards.values();
            default -> new LeaderboardType[0];
        };

        for (LeaderboardType type : values) {
            String id = ((Enum<?>) type).name().toLowerCase();

            builder.suggest(id, Text.literal(type.getTitle()));
        }

        return builder.buildFuture();
    }

    private static CompletableFuture<Suggestions> suggestPeriods(CommandContext<FabricClientCommandSource> ctx, SuggestionsBuilder builder) {

        for (Period p : Period.values()) {
            builder.suggest(p.name().toLowerCase());
        }

        return builder.buildFuture();
    }

    private static LeaderboardType parseBoard(String gamemode, String board) {
        try {
            return switch (gamemode.toLowerCase()) {
                case "kingdoms" ->
                        KingdomsLeaderboards.valueOf(board.toUpperCase());
                case "oneblock" ->
                        OneblockLeaderboards.valueOf(board.toUpperCase());
                case "parkour" ->
                        ParkourLeaderboards.valueOf(board.toUpperCase());
                case "survival" ->
                        SurvivalLeaderboards.valueOf(board.toUpperCase());
                default -> null;
            };
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    public static String capitalizeFirst(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }

        return input.substring(0, 1).toUpperCase()
                + input.substring(1).toLowerCase();
    }
}


