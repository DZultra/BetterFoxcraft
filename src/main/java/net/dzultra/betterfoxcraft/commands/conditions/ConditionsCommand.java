package net.dzultra.betterfoxcraft.commands.conditions;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.fabricmc.fabric.api.client.command.v2.ClientCommands;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommands.literal;

public class ConditionsCommand {
    public static String username;
    public static String condition;
    public static int index;

    public static LiteralArgumentBuilder<FabricClientCommandSource> getCommand() {
        return ClientCommands.literal("condition")
                .then(literal("add").executes(context -> {
                    Minecraft.getInstance().player.sendSystemMessage(
                            Component.literal("Please use /conditon add <user> <text>")
                                    .setStyle(Style.EMPTY.withColor(ChatFormatting.RED)));
                    return 1;
                }).then(ClientCommands.argument("user", StringArgumentType.string())
                        .then(ClientCommands.argument("text", StringArgumentType.greedyString())
                                .executes(context -> {
                                    username = StringArgumentType.getString(context, "user");
                                    condition = StringArgumentType.getString(context, "text");
                                    ConditionsDataBaseManager.getInstance().appendConditions(username, condition);
                                    Minecraft.getInstance().player.sendSystemMessage(
                                            Component.literal("\nSuccessfully added Condition '" + condition + "' for " + username + "\n")
                                                .setStyle(Style.EMPTY.withColor(ChatFormatting.GREEN)));
                                    return 1;
                                })
                        )
                ))

                .then(literal("remove").executes(context -> {
                    Minecraft.getInstance().player.sendSystemMessage(
                            Component.literal("Please use /conditon remove <user> <index>")
                                    .setStyle(Style.EMPTY.withColor(ChatFormatting.RED)));
                    return 1;
                }).then(ClientCommands.argument("user", StringArgumentType.string())
                        .then(ClientCommands.argument("index", IntegerArgumentType.integer())
                                .executes(context -> {
                                    username = StringArgumentType.getString(context, "user");
                                    index = IntegerArgumentType.getInteger(context, "index");
                                    if (ConditionsDataBaseManager.getInstance().removeCondition(username, index-1)) {
                                        Minecraft.getInstance().player.sendSystemMessage(Component.literal("\nSuccessfully removed Condition with Index: " + index + "\n")
                                                .setStyle(Style.EMPTY.withColor(ChatFormatting.GREEN)));
                                    } else {
                                        Minecraft.getInstance().player.sendSystemMessage(Component.literal("\nIndex out of Bound!\n")
                                                .setStyle(Style.EMPTY.withColor(ChatFormatting.RED)));
                                    }
                                    return 1;
                                })
                        )
                ))

                .then(literal("get").executes(context -> {
                    Minecraft.getInstance().player.sendSystemMessage(
                            Component.literal("Please use /conditon get <user>")
                                    .setStyle(Style.EMPTY.withColor(ChatFormatting.RED)));
                    return 1;
                }).then(ClientCommands.argument("user", StringArgumentType.string())
                            .executes(context -> {
                                username = StringArgumentType.getString(context, "user");
                                ConditionsData conditions = ConditionsDataBaseManager.getInstance().getConditions(username);

                                MutableComponent message = Component.literal("\n§b-- Conditions for " + username + " --\n")
                                        .setStyle(Style.EMPTY.withColor(ChatFormatting.PREFIX_CODE));

                                for (String listedCondition : conditions.conditions_list) {
                                    message.append("§a" + listedCondition).setStyle(Style.EMPTY.withColor(ChatFormatting.PREFIX_CODE)).append("\n");
                                }

                                Minecraft.getInstance().player.sendSystemMessage(message);
                                return 1;
                            })
                ));
    }
}
