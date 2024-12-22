package net.dzultra.betterfoxcraft.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;

public class StartCommand {
    public static LiteralArgumentBuilder<FabricClientCommandSource> getCommand() {
        return literal("start").executes(context -> {
            MinecraftClient.getInstance().getNetworkHandler().sendChatCommand("redeem");
            MinecraftClient.getInstance().getNetworkHandler().sendChatCommand("redeemkey");
            MinecraftClient.getInstance().getNetworkHandler().sendChatCommand("daily");
            return 0;
        });
    }
}