package net.dzultra.betterfoxcraft.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.dzultra.betterfoxcraft.menu_data.DataGrabber;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.Minecraft;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommands.literal;

public class VisitsTrackCommand {
    public static LiteralArgumentBuilder<FabricClientCommandSource> getCommand() {
        return literal("vtrack").executes(context -> {
            Minecraft.getInstance().getConnection().sendCommand("pwarp");
            DataGrabber.enabled = true;
            return 0;
        });
    }
}
