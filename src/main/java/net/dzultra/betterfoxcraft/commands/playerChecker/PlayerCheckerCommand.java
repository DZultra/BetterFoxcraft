package net.dzultra.betterfoxcraft.commands.playerChecker;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.List;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;

public class PlayerCheckerCommand {
    public static String username;

    public static LiteralArgumentBuilder<FabricClientCommandSource> getCommand() {
        return ClientCommandManager.literal("playercheck")
                .then(literal("add").executes(context -> {
                    MinecraftClient.getInstance().player.sendMessage(
                            Text.literal("Please use /playercheck add <user>")
                                    .setStyle(Style.EMPTY.withColor(Formatting.RED)), false);
                    return 1;
                }).then(ClientCommandManager.argument("user", StringArgumentType.string())
                    .executes(context -> {
                        username = StringArgumentType.getString(context, "user");
                        PlayerCheckDataBaseManager.addPlayer(username);

                        MinecraftClient.getInstance().player.sendMessage(
                                Text.literal("\nAdded " + username + " to Player Online Checker\n")
                                        .setStyle(Style.EMPTY.withColor(Formatting.GREEN)),false);

                        return 1;
                    })
                ))

                .then(literal("remove").executes(context -> {
                    MinecraftClient.getInstance().player.sendMessage(
                            Text.literal("Please use /playercheck remove <user>")
                                    .setStyle(Style.EMPTY.withColor(Formatting.RED)), false);
                    return 1;
                }).then(ClientCommandManager.argument("user", StringArgumentType.string())
                    .executes(context -> {
                        username = StringArgumentType.getString(context, "user");
                        PlayerCheckDataBaseManager.removePlayer(username);

                        MinecraftClient.getInstance().player.sendMessage(Text.literal("\nRemoved "+ username + " from Player Online Checker\n")
                                .setStyle(Style.EMPTY.withColor(Formatting.GREEN)), false);
                        return 1;
                    })
                ))

                .then(literal("list").executes(context -> {
                    MinecraftClient client = MinecraftClient.getInstance();

                    List<String> players = PlayerCheckDataBaseManager.getAllPlayers();

                    if (players.isEmpty()) {
                        client.player.sendMessage(Text.literal("\nNo players are currently in the list.\n")
                                .setStyle(Style.EMPTY.withColor(Formatting.GRAY)), false);
                    } else {
                        client.player.sendMessage(
                                Text.literal(">> All Players currently being checked for <<\n")
                                        .setStyle(Style.EMPTY.withColor(Formatting.GREEN)), false);
                        for (String name : players) {
                            client.player.sendMessage(Text.literal("|- " + name + "\n")
                                    .setStyle(Style.EMPTY.withColor(Formatting.YELLOW)), false);
                        }
                        client.player.sendMessage(
                                Text.literal(">> ---------------------------------- <<")
                                        .setStyle(Style.EMPTY.withColor(Formatting.GREEN)), false);
                    }

                    return 1;
                }))

                .then(literal("check").executes(context -> {
                    MinecraftClient client = MinecraftClient.getInstance();
                    List<String> players = PlayerCheckDataBaseManager.getAllPlayers();


                    client.player.sendMessage(
                            Text.literal(">> Checking for Players <<")
                                    .setStyle(Style.EMPTY.withColor(Formatting.GREEN)), false);

                    for (String name : players) {
                        client.getNetworkHandler().sendChatCommand("find " + name);
                    }
                    return 1;
                }));
    }
}
