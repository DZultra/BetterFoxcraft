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
import net.fabricmc.fabric.api.client.command.v2.ClientCommands;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class PlayerStatsCommand {
    public static LiteralArgumentBuilder<FabricClientCommandSource> getCommand() {
        return ClientCommands.literal("playerstats")
            .then(ClientCommands.argument("username", StringArgumentType.word())
                    .executes(PlayerStatsCommand::runPlayerStats)
                    .then(ClientCommands.argument("gamemode", StringArgumentType.word())
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
            Minecraft client = Minecraft.getInstance();
            client.execute(() -> {
                if (result instanceof InvalidResponseException) {
                    source.sendFeedback(Component.literal("User not found!").withStyle(ChatFormatting.RED));
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
            source.sendFeedback(Component.literal("You provided an invalid gamemode!\nPlease select any of the suggested.").withStyle(ChatFormatting.RED));
            return 0;
        }
        CompletableFuture.supplyAsync(() -> {
                try {
                    return new PlayerStatistics(userArg, true);
                } catch (InvalidResponseException e) {
                    return e;
                }
        }).thenAccept(result -> {
                Minecraft client = Minecraft.getInstance();
                client.execute(() -> {
                    if (result instanceof InvalidResponseException) {
                        source.sendFeedback(Component.literal("User not found!").withStyle(ChatFormatting.RED));
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

        source.sendFeedback(Component.literal("┌──────────────────────────┐").withStyle(ChatFormatting.DARK_GRAY));
        source.sendFeedback(Component.literal("   Player Statistics - " + username).withStyle(ChatFormatting.GOLD, ChatFormatting.BOLD));
        source.sendFeedback(Component.literal("├──────────────────────────┤").withStyle(ChatFormatting.DARK_GRAY));
        source.sendFeedback(Component.literal(" Online: ").withStyle(ChatFormatting.YELLOW).append(Component.literal(onlineText).withStyle(formatColor(isOnline))));
        source.sendFeedback(Component.literal(" Bedrock: ").withStyle(ChatFormatting.YELLOW).append(Component.literal(bedrockText).withStyle(formatColor(isBedrock))));
        source.sendFeedback(Component.literal(" Hidden: ").withStyle(ChatFormatting.YELLOW).append(Component.literal(hiddenText).withStyle(formatColor(isHidden))));
        source.sendFeedback(Component.literal(" Current Gamemode: ").withStyle(ChatFormatting.YELLOW).append(Component.literal(gamemodeText.toUpperCase()).withStyle(ChatFormatting.LIGHT_PURPLE)));
        if (lastSeen != null) {
            source.sendFeedback(Component.literal("├──────────────────────────┤").withStyle(ChatFormatting.DARK_GRAY));
            source.sendFeedback(Component.literal(" " + lastSeen).withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
        }
        source.sendFeedback(Component.literal("└──────────────────────────┘").withStyle(ChatFormatting.DARK_GRAY));
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

        source.sendFeedback(Component.literal("┌──────────────────────────┐").withStyle(ChatFormatting.DARK_GRAY));
        source.sendFeedback(Component.literal("   " + capitalizeFirst(gamemodeArg) + " Statistics - " + playerStats.username()).withStyle(ChatFormatting.GOLD, ChatFormatting.BOLD));
        source.sendFeedback(Component.literal("├──────────────────────────┤").withStyle(ChatFormatting.DARK_GRAY));

        if (parkourStats != null) {
            source.sendFeedback(statLine("Time Played", parkourStats.getTimePlayed().asString()));
            source.sendFeedback(statLine("Distance Traveled", parkourStats.getDistanceTraveled().asString()));
            source.sendFeedback(statLineInt("Parkours Completed", parkourStats.getParkoursCompleted()));
            source.sendFeedback(statLineInt("World Records", parkourStats.getWorldRecords()));
            source.sendFeedback(statLineInt("Parkours Built", parkourStats.getParkoursBuilt()));
            InitialGamemodeJoinDateStat dateStat = parkourStats.getInitialGamemodeJoinDate();
            if (dateStat.getDay() != null || dateStat.getMonth() != null || dateStat.getYear() != null) {
                source.sendFeedback(Component.literal("├──────────────────────────┤").withStyle(ChatFormatting.DARK_GRAY));
                source.sendFeedback(Component.literal(" " + dateStat.asString()).withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
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
                source.sendFeedback(Component.literal("├──────────────────────────┤").withStyle(ChatFormatting.DARK_GRAY));
                source.sendFeedback(Component.literal(" " + dateStat.asString()).withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
            }
        } else {
            source.sendFeedback(Component.literal("No statistics found.").withStyle(ChatFormatting.RED));
        }
        source.sendFeedback(Component.literal("└──────────────────────────┘").withStyle(ChatFormatting.DARK_GRAY));
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

    private static Component statLine(String label, String value) {
        return Component.literal(" " + label + ": ")
                .withStyle(ChatFormatting.YELLOW)
                .append(Component.literal(value)
                        .withStyle(ChatFormatting.AQUA));
    }

    private static Component statLineInt(String label, int value) {
        return Component.literal(" " + label + ": ")
                .withStyle(ChatFormatting.YELLOW)
                .append(Component.literal(String.valueOf(value))
                        .withStyle(ChatFormatting.GREEN));
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

    private static ChatFormatting formatColor(boolean bool) {
        return bool ? ChatFormatting.GREEN : ChatFormatting.RED;
    }
}
