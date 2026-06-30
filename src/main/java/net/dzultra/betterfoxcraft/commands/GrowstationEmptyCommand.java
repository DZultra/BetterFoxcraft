package net.dzultra.betterfoxcraft.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.dzultra.betterfoxcraft.menu_interactions.GrowstationClicking;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommands.literal;

public class GrowstationEmptyCommand {
    public static LiteralArgumentBuilder<FabricClientCommandSource> getCommand() {
        return literal("growtoggle").executes(context -> {
            GrowstationClicking.enabled = !GrowstationClicking.enabled;
            return 0;
        });
    }
}
