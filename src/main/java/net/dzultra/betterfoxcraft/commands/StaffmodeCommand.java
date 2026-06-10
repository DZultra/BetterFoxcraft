package net.dzultra.betterfoxcraft.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.Minecraft;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommands.literal;

public class StaffmodeCommand {
    public static LiteralArgumentBuilder<FabricClientCommandSource> getCommand() {
        return literal("staffmode")
                .then(literal("enable").executes(context -> {
                    Minecraft.getInstance().getConnection().sendCommand("vanish");
                    Minecraft.getInstance().getConnection().sendCommand("gmsp");
                    Minecraft.getInstance().getConnection().sendCommand("fly enable");
                    Minecraft.getInstance().getConnection().sendCommand("hide");
                    return 1;
                }))
                .then(literal("disable").executes(context -> {
                    Minecraft.getInstance().getConnection().sendCommand("vanish");
                    Minecraft.getInstance().getConnection().sendCommand("gms");
                    Minecraft.getInstance().getConnection().sendCommand("fly enable");
                    Minecraft.getInstance().getConnection().sendCommand("hide");
                    return 1;
                }));
    }
}