package net.dzultra.betterfoxcraft.commands.gambling;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommands.literal;

public class CasinoCommand {
    public static LiteralArgumentBuilder<FabricClientCommandSource> getCommand() {
        return literal("casino").executes(context -> {
            Minecraft.getInstance().player.sendSystemMessage(
                    Component.literal("\nYou should not gamble!\n").setStyle(Style.EMPTY
                            .withColor(ChatFormatting.RED)
                            .withBold(true))
                    );
            return 0;
        });
    }
}