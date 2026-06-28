package net.dzultra.betterfoxcraft.menu_data;

import net.dzultra.betterfoxcraft.BetterFoxcraft;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.ContainerScreen;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

public class DataGrabber {
    private static final String containerTitle = "Foxcraft Player Warps";
    private static boolean didClicks = false;
    private static boolean grabbedData = false;
    private static int tickCount = 0;
    public static boolean enabled = false;

    public static void register() {
        ScreenEvents.AFTER_INIT.register((client, screen, scaledWidth, scaledHeight) -> {
            if (!enabled) return;
            // Runs every tick inside the Screen
            ScreenEvents.afterTick(screen).register(currentScreen -> {
                // We have a Screen
                if (!(currentScreen instanceof ContainerScreen containerScreen)) return;
                // We have a Container Screen
                if(!isCorrectTitle(containerScreen)) return;
                tickCount++;
                // We have a Container Screen with the correct Title

                if (!didClicks && tickCount == 1) {
                    doFilterClicks(client, containerScreen, 53);
                } else {
                    if(grabbedData || tickCount < 3) return;
                    getContainerItems(containerScreen);
                    client.setScreen(null);
                    grabbedData = false;
                    tickCount = 0;
                    enabled = false;
                }
            });

        });
    }

    private static boolean isCorrectTitle(ContainerScreen containerScreen) {
        return containerScreen.getTitle().getString().equals(containerTitle);
    }

    private static void doFilterClicks(Minecraft client, ContainerScreen containerScreen, int slotNum) {
        MultiPlayerGameMode gameMode = client.gameMode;
        int containerId = containerScreen.getMenu().containerId;

        for (int i = 0; i < 3; i++) {
            gameMode.handleContainerInput(containerId, slotNum, 0, ContainerInput.THROW, client.player);
        }
        didClicks = true;
    }

    public static void getContainerItems(ContainerScreen screen) {
        ChestMenu menu = screen.getMenu();
        Minecraft mc = Minecraft.getInstance();

        int containerSize = menu.getContainer().getContainerSize();
        BetterFoxcraft.LOGGER.info("Container Size: {}", containerSize);

        for (int i = 0; i < containerSize; i++) {
            ItemStack itemStack = menu.getContainer().getItem(i);
            BetterFoxcraft.LOGGER.info("Slot {}: ItemStack: {}", i, itemStack);
            if (itemStack.isEmpty()) {
                continue;
            }

            List<Component> tooltipLines = itemStack.getTooltipLines(
                    Item.TooltipContext.EMPTY,
                    mc.player,
                    TooltipFlag.NORMAL
            );
            BetterFoxcraft.LOGGER.info("ID: {} | Name: {}", i, itemStack.getItemName());
            BetterFoxcraft.LOGGER.info("Tooltip:");

            for (Component line : tooltipLines) {
                System.out.println(line.getString());
            }
        }
        grabbedData = true;
    }
}
