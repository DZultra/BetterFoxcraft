package net.dzultra.betterfoxcraft.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.fabricmc.fabric.api.client.command.v2.ClientCommands;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;

public class StaffChatDisableCommand {
    public static LiteralArgumentBuilder<FabricClientCommandSource> getCommand() {
        return ClientCommands.literal("sc")
                .then(ClientCommands.argument("message", StringArgumentType.greedyString())
                        .executes(context -> {
                            String message = StringArgumentType.getString(context, "message");
                                Minecraft.getInstance().player.sendSystemMessage(
                                        Component.literal("Wrong Command: Use /stc <text>")
                                                .setStyle(Style.EMPTY
                                                    .withColor(ChatFormatting.RED)
                                                    .withBold(true))
                                                );
                            return 1;
                        })
                );
    }
}
