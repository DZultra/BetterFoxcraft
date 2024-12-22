package net.dzultra.betterfoxcraft.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;

public class StaffmodeCommand {
    public static LiteralArgumentBuilder<FabricClientCommandSource> getCommand() {
        return literal("staffmode")
                .then(literal("enable").executes(context -> {
                    MinecraftClient.getInstance().getNetworkHandler().sendChatCommand("vanish");
                    MinecraftClient.getInstance().getNetworkHandler().sendChatCommand("gmsp");
                    MinecraftClient.getInstance().getNetworkHandler().sendChatCommand("fly enable");
                    return 1;
                }))
                .then(literal("disable").executes(context -> {
                    MinecraftClient.getInstance().getNetworkHandler().sendChatCommand("vanish");
                    MinecraftClient.getInstance().getNetworkHandler().sendChatCommand("gms");
                    MinecraftClient.getInstance().getNetworkHandler().sendChatCommand("fly enable");
                    return 1;
                }));
    }
}