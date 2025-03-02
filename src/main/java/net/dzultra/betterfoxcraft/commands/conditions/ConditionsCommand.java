package net.dzultra.betterfoxcraft.commands.conditions;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;

public class ConditionsCommand {
    public static String username;
    public static String condition;
    public static int index;

    public static LiteralArgumentBuilder<FabricClientCommandSource> getCommand() {
        return ClientCommandManager.literal("condition")
                .then(literal("add").executes(context -> {
                    MinecraftClient.getInstance().player.sendMessage(
                            Text.literal("Please use /conditon add <user> <text>")
                                    .setStyle(Style.EMPTY.withColor(Formatting.RED)));
                    return 1;
                }).then(ClientCommandManager.argument("user", StringArgumentType.string())
                        .then(ClientCommandManager.argument("text", StringArgumentType.greedyString())
                                .executes(context -> {
                                    username = StringArgumentType.getString(context, "user");
                                    condition = StringArgumentType.getString(context, "text");
                                    DataBaseManager.getInstance().appendConditions(username, condition);
                                    MinecraftClient.getInstance().player.sendMessage(Text.literal("\nSuccessfully added Condition '" + condition + "' for " + username + "\n")
                                            .setStyle(Style.EMPTY.withColor(Formatting.GREEN)));
                                    return 1;
                                })
                        )
                ))

                .then(literal("remove").executes(context -> {
                    MinecraftClient.getInstance().player.sendMessage(
                            Text.literal("Please use /conditon remove <user> <index>")
                                    .setStyle(Style.EMPTY.withColor(Formatting.RED)));
                    return 1;
                }).then(ClientCommandManager.argument("user", StringArgumentType.string())
                        .then(ClientCommandManager.argument("index", IntegerArgumentType.integer())
                                .executes(context -> {
                                    username = StringArgumentType.getString(context, "user");
                                    index = IntegerArgumentType.getInteger(context, "index");
                                    if (DataBaseManager.getInstance().removeCondition(username, index-1)) {
                                        MinecraftClient.getInstance().player.sendMessage(Text.literal("\nSuccessfully removed Condition with Index: " + index + "\n")
                                                .setStyle(Style.EMPTY.withColor(Formatting.GREEN)));
                                    } else {
                                        MinecraftClient.getInstance().player.sendMessage(Text.literal("\nIndex out of Bound!\n")
                                                .setStyle(Style.EMPTY.withColor(Formatting.RED)));
                                    }
                                    return 1;
                                })
                        )
                ))

                .then(literal("get").executes(context -> {
                    MinecraftClient.getInstance().player.sendMessage(
                            Text.literal("Please use /conditon get <user>")
                                    .setStyle(Style.EMPTY.withColor(Formatting.RED)));
                    return 1;
                }).then(ClientCommandManager.argument("user", StringArgumentType.string())
                            .executes(context -> {
                                username = StringArgumentType.getString(context, "user");
                                ConditionsData conditions = DataBaseManager.getInstance().getConditions(username);

                                MutableText message = Text.literal("\n§b-- Conditions for " + username + " --\n")
                                        .setStyle(Style.EMPTY.withColor(Formatting.FORMATTING_CODE_PREFIX));

                                for (String listedCondition : conditions.conditions_list) {
                                    message.append("§a" + listedCondition).setStyle(Style.EMPTY.withColor(Formatting.FORMATTING_CODE_PREFIX)).append("\n");
                                }

                                MinecraftClient.getInstance().player.sendMessage(message);
                                return 1;
                            })
                ));
    }
}
