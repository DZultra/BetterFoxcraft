package net.dzultra.betterfoxcraft.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.dzultra.betterfoxcraft.menu_interactions.DataGrabber;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommands.literal;

public class VisitsTrackCommand {
    public static LiteralArgumentBuilder<FabricClientCommandSource> getCommand() {
        return literal("vtrack").executes(context -> {
            Minecraft client = Minecraft.getInstance();
            if (client.getConnection() == null || client.player == null) throw new NullPointerException("Connection/Player is null");

            client.player.sendOverlayMessage(Component.literal(">> Running Visits Tracker <<")
                    .setStyle(Style.EMPTY.withColor(ChatFormatting.GREEN))
            );
            client.getConnection().sendCommand("pwarp");
            DataGrabber.enabled = true;
            return 0;
        });
    }
}
