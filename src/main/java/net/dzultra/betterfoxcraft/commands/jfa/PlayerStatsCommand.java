package net.dzultra.betterfoxcraft.commands.jfa;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.dzultra.jfa.apidata.PlayerStatistics;
import net.dzultra.jfa.apidata.PlayerStats;
import net.dzultra.jfa.exceptions.InvalidResponseException;
import net.dzultra.jfa.types.Gamemode;
import net.dzultra.jfa.types.statistics.InitialGamemodeJoinDateStat;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class PlayerStatsCommand {
    public static LiteralArgumentBuilder<FabricClientCommandSource> getCommand() {
        return ClientCommandManager.literal("playerstats")
            .then(ClientCommandManager.argument("username", StringArgumentType.word())
                    .executes(PlayerStatsCommand::runPlayerStats)
                    .then(ClientCommandManager.argument("gamemode", StringArgumentType.word())
                        .suggests(PlayerStatsCommand::suggestGamemodes)
                        .executes(PlayerStatsCommand::runGamemodeStats)
                    )
            );
    }

    private static int runPlayerStats(CommandContext<FabricClientCommandSource> ctx) {
        FabricClientCommandSource source = ctx.getSource();
        String userArg = StringArgumentType.getString(ctx, "username");
        if (userArg == null) return 0;
        CompletableFuture.supplyAsync(() -> {
            try {
                return new PlayerStatistics(userArg, true);
            } catch (InvalidResponseException e) {
                return e;
            }
        }).thenAccept(result -> {
            MinecraftClient client = MinecraftClient.getInstance();
            client.execute(() -> {
                if (result instanceof InvalidResponseException) {
                    source.sendFeedback(Text.literal("User not found!").formatted(Formatting.RED));
                    return;
                }
                PlayerStatistics statsObj = (PlayerStatistics) result;
                PlayerStats stats = statsObj.getPlayerStats();
                sendPlayerStatsChat(source, userArg, stats);
            });
        });
        return 1;
    }

    private static int runGamemodeStats(CommandContext<FabricClientCommandSource> ctx) {
        FabricClientCommandSource source = ctx.getSource();
        String userArg = StringArgumentType.getString(ctx, "username");
        String gamemodeArg = StringArgumentType.getString(ctx, "gamemode");

        if (userArg == null || gamemodeArg == null || Gamemode.getGamemodeByName(gamemodeArg) == null) {
            source.sendFeedback(Text.literal("You provided an invalid gamemode!\nPlease select any of the suggested.").formatted(Formatting.RED));
            return 0;
        }
        CompletableFuture.supplyAsync(() -> {
                try {
                    return new PlayerStatistics(userArg, true);
                } catch (InvalidResponseException e) {
                    return e;
                }
        }).thenAccept(result -> {
                MinecraftClient client = MinecraftClient.getInstance();
                client.execute(() -> {
                    if (result instanceof InvalidResponseException) {
                        source.sendFeedback(Text.literal("User not found!").formatted(Formatting.RED));
                        return;
                    }
                    PlayerStatistics statsObj = (PlayerStatistics) result;
                    PlayerStats playerStats = statsObj.getPlayerStats();

                    sendGamemodeStatsChat(source, gamemodeArg, playerStats);
                });
        });
        return 1;
    }

    private static void sendPlayerStatsChat(
            FabricClientCommandSource source,
            String username,
            PlayerStats playerStats
    ) {
        boolean isOnline = playerStats.isOnline();
        boolean isBedrock = playerStats.isBedrock();
        boolean isHidden = playerStats.isHidden();
        Gamemode gamemode = playerStats.currentGamemode();
        String lastSeen = playerStats.lastSeen();

        String onlineText = formatBool(isOnline);
        String bedrockText = formatBool(isBedrock);
        String hiddenText = formatBool(isHidden);
        String gamemodeText = (gamemode != null) ? gamemode.getName() : "Unknown";

        source.sendFeedback(Text.literal("┌──────────────────────────┐").formatted(Formatting.DARK_GRAY));
        source.sendFeedback(Text.literal("   Player Statistics - " + username).formatted(Formatting.GOLD, Formatting.BOLD));
        source.sendFeedback(Text.literal("├──────────────────────────┤").formatted(Formatting.DARK_GRAY));
        source.sendFeedback(Text.literal(" Online: ").formatted(Formatting.YELLOW).append(Text.literal(onlineText).formatted(formatColor(isOnline))));
        source.sendFeedback(Text.literal(" Bedrock: ").formatted(Formatting.YELLOW).append(Text.literal(bedrockText).formatted(formatColor(isBedrock))));
        source.sendFeedback(Text.literal(" Hidden: ").formatted(Formatting.YELLOW).append(Text.literal(hiddenText).formatted(formatColor(isHidden))));
        source.sendFeedback(Text.literal(" Current Gamemode: ").formatted(Formatting.YELLOW).append(Text.literal(gamemodeText.toUpperCase()).formatted(Formatting.LIGHT_PURPLE)));
        if (lastSeen != null) {
            source.sendFeedback(Text.literal("├──────────────────────────┤").formatted(Formatting.DARK_GRAY));
            source.sendFeedback(Text.literal(" " + lastSeen).formatted(Formatting.GRAY, Formatting.ITALIC));
        }
        source.sendFeedback(Text.literal("└──────────────────────────┘").formatted(Formatting.DARK_GRAY));
    }

    private static void sendGamemodeStatsChat(
            FabricClientCommandSource source,
            String gamemodeArg,
            PlayerStats playerStats
    ) {
        PlayerStats.ParkourStats parkourStats = null;
        PlayerStats.GenericStat gmStat = null;
        String gmLower = gamemodeArg.toLowerCase();

        if (gmLower.equals("parkour")) {
            parkourStats = playerStats.parkourStats();
        } else {
            switch (gmLower) {
                case "oneblock" -> gmStat = playerStats.oneblockStats();
                case "survival" -> gmStat = playerStats.survivalStats();
                case "kingdoms" -> gmStat = playerStats.kingdomsStats();
                case "skyblock" -> gmStat = playerStats.skyblockStats();
                case "creative" -> gmStat = playerStats.creativeStats();
                case "prison" -> gmStat = playerStats.prisonStats();
            }
        }

        source.sendFeedback(Text.literal("┌──────────────────────────┐").formatted(Formatting.DARK_GRAY));
        source.sendFeedback(Text.literal("   " + capitalizeFirst(gamemodeArg) + " Statistics - " + playerStats.username()).formatted(Formatting.GOLD, Formatting.BOLD));
        source.sendFeedback(Text.literal("├──────────────────────────┤").formatted(Formatting.DARK_GRAY));

        if (parkourStats != null) {
            source.sendFeedback(statLine("Time Played", parkourStats.getTimePlayed().asString()));
            source.sendFeedback(statLine("Distance Traveled", parkourStats.getDistanceTraveled().asString()));
            source.sendFeedback(statLineInt("Parkours Completed", parkourStats.getParkoursCompleted()));
            source.sendFeedback(statLineInt("World Records", parkourStats.getWorldRecords()));
            source.sendFeedback(statLineInt("Parkours Built", parkourStats.getParkoursBuilt()));
            InitialGamemodeJoinDateStat dateStat = parkourStats.getInitialGamemodeJoinDate();
            if (dateStat.getDay() != null || dateStat.getMonth() != null || dateStat.getYear() != null) {
                source.sendFeedback(Text.literal("├──────────────────────────┤").formatted(Formatting.DARK_GRAY));
                source.sendFeedback(Text.literal(" " + dateStat.asString()).formatted(Formatting.GRAY, Formatting.ITALIC));
            }
        } else if (gmStat != null) {
            source.sendFeedback(statLine("Time Played", gmStat.getTimePlayed().asString()));
            source.sendFeedback(statLineInt("Blocks Broken", gmStat.getBlocksBroken()));
            source.sendFeedback(statLineInt("Blocks Placed", gmStat.getBlocksPlaced()));
            source.sendFeedback(statLineInt("Items Crafted", gmStat.getItemsCrafted()));
            source.sendFeedback(statLineInt("Tools Broken", gmStat.getToolsBroken()));
            source.sendFeedback(statLineInt("Highest Killstreak", gmStat.getHighestKillstreak()));
            source.sendFeedback(statLineInt("Players Killed", gmStat.getPlayersKilled()));
            source.sendFeedback(statLineInt("Deaths", gmStat.getDeaths()));
            source.sendFeedback(statLineInt("Arrows Shot", gmStat.getArrowsShot()));
            source.sendFeedback(statLine("Distance Traveled", gmStat.getDistanceTraveled().asString()));
            InitialGamemodeJoinDateStat dateStat = gmStat.getInitialGamemodeJoinDate();
            if (dateStat.getDay() != null || dateStat.getMonth() != null || dateStat.getYear() != null) {
                source.sendFeedback(Text.literal("├──────────────────────────┤").formatted(Formatting.DARK_GRAY));
                source.sendFeedback(Text.literal(" " + dateStat.asString()).formatted(Formatting.GRAY, Formatting.ITALIC));
            }
        } else {
            source.sendFeedback(Text.literal("No statistics found.").formatted(Formatting.RED));
        }
        source.sendFeedback(Text.literal("└──────────────────────────┘").formatted(Formatting.DARK_GRAY));
    }

    private static CompletableFuture<Suggestions> suggestGamemodes(
            CommandContext<FabricClientCommandSource> ctx,
            SuggestionsBuilder builder
    ) {
        List<Gamemode> suggestedGms = List.of(
                Gamemode.ONEBLOCK, Gamemode.SURVIVAL,
                Gamemode.KINGDOMS, Gamemode.CREATIVE,
                Gamemode.SKYBLOCK, Gamemode.PRISON,
                Gamemode.PARKOUR
        );

        for (Gamemode gm : suggestedGms) {
            builder.suggest(gm.getName());
        }

        return builder.buildFuture();
    }

    private static Text statLine(String label, String value) {
        return Text.literal(" " + label + ": ")
                .formatted(Formatting.YELLOW)
                .append(Text.literal(value)
                        .formatted(Formatting.AQUA));
    }

    private static Text statLineInt(String label, int value) {
        return Text.literal(" " + label + ": ")
                .formatted(Formatting.YELLOW)
                .append(Text.literal(String.valueOf(value))
                        .formatted(Formatting.GREEN));
    }

    public static String capitalizeFirst(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }

        return input.substring(0, 1).toUpperCase()
                + input.substring(1).toLowerCase();
    }

    private static String formatBool(boolean bool) {
        return bool ? "Yes" : "No";
    }

    private static Formatting formatColor(boolean bool) {
        return bool ? Formatting.GREEN : Formatting.RED;
    }
}
