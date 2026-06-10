package net.dzultra.betterfoxcraft.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.Minecraft;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommands.literal;

public class StartCommand {
    public static LiteralArgumentBuilder<FabricClientCommandSource> getCommand() {
        return literal("start").executes(context -> {
            Minecraft.getInstance().getConnection().sendCommand("redeem");
            Minecraft.getInstance().getConnection().sendCommand("redeemkey");
            Minecraft.getInstance().getConnection().sendCommand("daily");
            Minecraft.getInstance().getConnection().sendCommand("pwarp bump");
            Minecraft.getInstance().getConnection().sendCommand("playercheck check");
            return 0;
        });
    }
}