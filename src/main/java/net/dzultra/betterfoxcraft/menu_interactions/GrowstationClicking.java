package net.dzultra.betterfoxcraft.menu_interactions;

import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.ContainerScreen;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.world.inventory.ContainerInput;

import java.util.List;

public class GrowstationClicking {
    private static final String containerTitle = "Growstation Main Menu";
    public static boolean enabled = false;

    public static void register() {
        ScreenEvents.AFTER_INIT.register((client, screen, scaledWidth, scaledHeight) -> {
            if (!enabled) return;
            // Runs every tick inside the Screen
            ScreenEvents.afterTick(screen).register(currentScreen -> {
                // We have a Screen
                if (!(currentScreen instanceof ContainerScreen containerScreen)) return;
                // We have a Container Screen
                if(!MenuHelper.isCorrectTitle(containerScreen, containerTitle)) return;
                // We have a Container Screen with the correct title
                doEmptyClicks(client, containerScreen, List.of(21, 49));
            });
        });
    }
    private static void doEmptyClicks(Minecraft client, ContainerScreen containerScreen, List<Integer> slots) {
        MultiPlayerGameMode gameMode = client.gameMode;
        int containerId = containerScreen.getMenu().containerId;

        for (Integer slot : slots) {
            gameMode.handleContainerInput(containerId, slot, 0, ContainerInput.THROW, client.player);
        }
    }

}