package net.dzultra.betterfoxcraft.commands.playerChecker;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.fabricmc.fabric.api.client.command.v2.ClientCommands;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import java.util.List;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommands.literal;

public class PlayerCheckerCommand {
    public static String username;

    public static LiteralArgumentBuilder<FabricClientCommandSource> getCommand() {
        return ClientCommands.literal("playercheck")
                .then(literal("add").executes(context -> {
                    Minecraft.getInstance().player.sendSystemMessage(
                            Component.literal("Please use /playercheck add <user>")
                                    .setStyle(Style.EMPTY.withColor(ChatFormatting.RED)));
                    return 1;
                }).then(ClientCommands.argument("user", StringArgumentType.string())
                    .executes(context -> {
                        username = StringArgumentType.getString(context, "user");
                        PlayerCheckDataBaseManager.addPlayer(username);

                        Minecraft.getInstance().player.sendSystemMessage(
                                Component.literal("\nAdded " + username + " to Player Online Checker\n")
                                        .setStyle(Style.EMPTY.withColor(ChatFormatting.GREEN)));

                        return 1;
                    })
                ))

                .then(literal("remove").executes(context -> {
                    Minecraft.getInstance().player.sendSystemMessage(
                            Component.literal("Please use /playercheck remove <user>")
                                    .setStyle(Style.EMPTY.withColor(ChatFormatting.RED)));
                    return 1;
                }).then(ClientCommands.argument("user", StringArgumentType.string())
                    .executes(context -> {
                        username = StringArgumentType.getString(context, "user");
                        PlayerCheckDataBaseManager.removePlayer(username);

                        Minecraft.getInstance().player.sendSystemMessage(Component.literal("\nRemoved "+ username + " from Player Online Checker\n")
                                .setStyle(Style.EMPTY.withColor(ChatFormatting.GREEN)));
                        return 1;
                    })
                ))

                .then(literal("list").executes(context -> {
                    Minecraft client = Minecraft.getInstance();

                    List<String> players = PlayerCheckDataBaseManager.getAllPlayers();

                    if (players.isEmpty()) {
                        client.player.sendSystemMessage(Component.literal("\nNo players are currently in the list.\n")
                                .setStyle(Style.EMPTY.withColor(ChatFormatting.GRAY)));
                    } else {
                        client.player.sendSystemMessage(
                                Component.literal(">> All Players currently being checked for <<\n")
                                        .setStyle(Style.EMPTY.withColor(ChatFormatting.GREEN)));
                        for (String name : players) {
                            client.player.sendSystemMessage(Component.literal("|- " + name + "\n")
                                    .setStyle(Style.EMPTY.withColor(ChatFormatting.YELLOW)));
                        }
                        client.player.sendSystemMessage(
                                Component.literal(">> ---------------------------------- <<")
                                        .setStyle(Style.EMPTY.withColor(ChatFormatting.GREEN)));
                    }

                    return 1;
                }))

                .then(literal("check").executes(context -> {
                    Minecraft client = Minecraft.getInstance();
                    List<String> players = PlayerCheckDataBaseManager.getAllPlayers();


                    client.player.sendSystemMessage(
                            Component.literal(">> Checking for Players <<")
                                    .setStyle(Style.EMPTY.withColor(ChatFormatting.GREEN)));

                    for (String name : players) {
                        client.getConnection().sendCommand("find " + name);
                    }
                    return 1;
                }));
    }
}
