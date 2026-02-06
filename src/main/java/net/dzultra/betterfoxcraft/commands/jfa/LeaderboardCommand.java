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
                            .suggests((ctx, builder) ->
                                    suggestBoards(
                                            builder,
                                            StringArgumentType.getString(ctx, "gamemode")
                                    )
                            )
                            .then(ClientCommandManager.argument("period", StringArgumentType.word())
                                    .suggests((ctx, builder) -> suggestPeriods(builder))
                                    .executes(ctx -> runLeaderboard(ctx, -1))
                                    .then(ClientCommandManager.argument("position",
                                                    IntegerArgumentType.integer(1))
                                            .executes(ctx ->
                                                    runLeaderboard(
                                                            ctx,
                                                            IntegerArgumentType.getInteger(ctx, "position")
                                                    )
                                            )
                                    )
                            )
                    )
            );
    }

    private static int runLeaderboard(
            CommandContext<FabricClientCommandSource> ctx,
            int position
    ) {

        String gamemodeArg = StringArgumentType.getString(ctx, "gamemode");
        String boardArg = StringArgumentType.getString(ctx, "board");
        String periodArg = StringArgumentType.getString(ctx, "period");

        LeaderboardType type = parseBoard(gamemodeArg, boardArg);
        Period period = Period.valueOf(periodArg.toUpperCase());

        if (type == null) {
            ctx.getSource().sendError(Text.literal("Invalid leaderboard type."));
            return 0;
        }

        // Run async
        CompletableFuture.runAsync(() -> {

            try {

                ServerLeaderboard leaderboard =
                        new ServerLeaderboard(type, period);

                // Switch back to client thread
                MinecraftClient.getInstance().execute(() -> {
                    displayLeaderboard(ctx, leaderboard, position);
                });

            } catch (Exception e) {

                MinecraftClient.getInstance().execute(() -> {
                    ctx.getSource().sendError(
                            Text.literal("Failed to fetch leaderboard.")
                    );
                });

                e.printStackTrace();
            }

        });

        return 1;
    }

    private static void displayLeaderboard(
            CommandContext<FabricClientCommandSource> ctx,
            ServerLeaderboard leaderboard,
            int position
    ) {

        List<ServerLeaderboardsResponse.LeaderboardEntry> data = leaderboard.getEntries();

        if (data == null || data.isEmpty()) {
            ctx.getSource().sendError(Text.literal("No data found."));
            return;
        }

        ctx.getSource().sendFeedback(
                Text.literal("\n§6§l" + leaderboard.getTitle())
        );

        if (position > 0) {
            var entry = leaderboard.getEntry(position);

            if (entry == null) {
                ctx.getSource().sendError(Text.literal("Position not found."));
                return;
            }

            ctx.getSource().sendFeedback(formatEntry(entry));
            return;
        }

        for (int i = 0; i < Math.min(10, data.size()); i++) {
            ctx.getSource().sendFeedback(formatEntry(data.get(i)));
        }
    }

    private static Text formatEntry(
            ServerLeaderboardsResponse.LeaderboardEntry entry
    ) {
        return Text.literal(
                "§e#" + entry.position() +
                        " §b" + entry.username() +
                        " §7» §a" + entry.value()
        );
    }

    private static CompletableFuture<Suggestions>
    suggestGamemodes(SuggestionsBuilder builder) {

        List<Gamemode> gmList = List.of(Gamemode.ONEBLOCK, Gamemode.KINGDOMS, Gamemode.SURVIVAL, Gamemode.PARKOUR);

        for (Gamemode gm :gmList) {
            builder.suggest(gm.name().toLowerCase());
        }

        return builder.buildFuture();
    }

    private static CompletableFuture<Suggestions>
    suggestBoards(SuggestionsBuilder builder, String gamemode) {

        LeaderboardType[] values = switch (gamemode.toLowerCase()) {
            case "kingdoms" -> KingdomsLeaderboards.values();
            case "oneblock" -> OneblockLeaderboards.values();
            case "parkour" -> ParkourLeaderboards.values();
            case "survival" -> SurvivalLeaderboards.values();
            default -> new LeaderboardType[0];
        };

        for (LeaderboardType type : values) {

            // Suggest enum ID (command-safe)
            String id = ((Enum<?>) type).name().toLowerCase();

            // Show title as tooltip
            builder.suggest(
                    id,
                    Text.literal(type.getTitle())
            );
        }

        return builder.buildFuture();
    }

    private static CompletableFuture<com.mojang.brigadier.suggestion.Suggestions>
    suggestPeriods(SuggestionsBuilder builder) {

        for (Period p : Period.values()) {
            builder.suggest(p.name().toLowerCase());
        }

        return builder.buildFuture();
    }

    private static LeaderboardType parseBoard(
            String gamemode,
            String board
    ) {
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
}

