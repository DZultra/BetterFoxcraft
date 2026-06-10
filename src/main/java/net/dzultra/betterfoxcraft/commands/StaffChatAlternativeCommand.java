package net.dzultra.betterfoxcraft.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.fabricmc.fabric.api.client.command.v2.ClientCommands;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;

public class StaffChatAlternativeCommand {
    public static LiteralArgumentBuilder<FabricClientCommandSource> getCommand() {
        return ClientCommands.literal("stc")
                .then(ClientCommands.argument("message", StringArgumentType.greedyString())
                        .executes(context -> {
                            String message = StringArgumentType.getString(context, "message");
                            try {
                                Minecraft.getInstance().getConnection().sendCommand("staffchat " + message);
                            } catch (IllegalArgumentException e) {
                                System.out.println(e.getMessage());
                                Minecraft.getInstance().player.sendSystemMessage(Component.literal("Wrong Argument: /stc <text>").setStyle(Style.EMPTY
                                                .withColor(ChatFormatting.RED)
                                                .withBold(true))
                                        );
                            }
                            return 1;
                        })
                );
    }
}
