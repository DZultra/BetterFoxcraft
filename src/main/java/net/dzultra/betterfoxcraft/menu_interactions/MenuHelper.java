package net.dzultra.betterfoxcraft.menu_interactions;

import net.minecraft.client.gui.screens.inventory.ContainerScreen;

public class MenuHelper {

    public static boolean isCorrectTitle(ContainerScreen containerScreen, String containerTitle) {
        return containerScreen.getTitle().getString().contains(containerTitle);
    }
}
