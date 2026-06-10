package net.dzultra.betterfoxcraft.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.Minecraft;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommands.literal;

public class GoCommand {
    public static LiteralArgumentBuilder<FabricClientCommandSource> getCommand() {
        return literal("go").executes(context -> {
            Minecraft.getInstance().getConnection().sendCommand("ob");
            Minecraft.getInstance().getConnection().sendCommand("redeem");
            Minecraft.getInstance().getConnection().sendCommand("redeemkey");
            Minecraft.getInstance().getConnection().sendCommand("kit foxgod");
            Minecraft.getInstance().getConnection().sendCommand("kit foxking");
            Minecraft.getInstance().getConnection().sendCommand("kit mysticfox");
            Minecraft.getInstance().getConnection().sendCommand("crates");
            return 0;
        });
    }
}