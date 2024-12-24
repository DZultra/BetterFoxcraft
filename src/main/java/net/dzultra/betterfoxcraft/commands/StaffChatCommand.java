package net.dzultra.betterfoxcraft.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;

public class StaffChatCommand {
    public static LiteralArgumentBuilder<FabricClientCommandSource> getCommand() {
        return literal("sc")
                .then(literal("msg")
                        .then(net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.argument("text", StringArgumentType.string())
                                .executes(context -> { // Right Syntax
                                    String message = StringArgumentType.getString(context, "text");
                                    MinecraftClient.getInstance().getNetworkHandler().sendChatCommand("sc " + message);
                                    return 1;
                                })))
                                .executes(context -> { // Wrong Syntax
                                    MinecraftClient.getInstance().player.sendMessage(
                                            Text.literal("\nError: Wrong Syntax\n/sc msg <text>\n").setStyle(Style.EMPTY
                                                    .withColor(Formatting.RED)
                                                    .withBold(true))
                                            , false);
                                    return 0;
                                });
    }
}
