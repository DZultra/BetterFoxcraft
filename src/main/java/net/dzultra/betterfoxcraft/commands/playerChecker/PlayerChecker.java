package net.dzultra.betterfoxcraft.commands.playerChecker;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.minecraft.client.MinecraftClient;

import java.util.List;

public class PlayerChecker {
    public static boolean isExecuted = false;

    public static void registerPlayerChecker() {
        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
            String serverAddress = getCurrentServerAddress();

            if (serverAddress != null && serverAddress.contains("mcfoxcraft.net")) {
                List<String> players = PlayerCheckDataBaseManager.getAllPlayers();
                for (String name : players) {
                    MinecraftClient.getInstance().getNetworkHandler().sendChatCommand("find " + name);
                }
            }
        });
    }

    private static String getCurrentServerAddress() {
        if (MinecraftClient.getInstance().getCurrentServerEntry() != null) {
            return MinecraftClient.getInstance().getCurrentServerEntry().address.toLowerCase();
        }
        return null;
    }
}
