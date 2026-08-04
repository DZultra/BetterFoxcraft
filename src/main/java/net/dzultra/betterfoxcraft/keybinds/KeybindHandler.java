package net.dzultra.betterfoxcraft.keybinds;

import me.shedaniel.autoconfig.AutoConfig;
import net.dzultra.betterfoxcraft.ConfigScreenFactory;
import net.dzultra.betterfoxcraft.ModConfig;
import net.dzultra.betterfoxcraft.slotswitcher.SlotSwitcher;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.fabricmc.fabric.api.client.screen.v1.ScreenKeyboardEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;

@Environment(EnvType.CLIENT)
public class KeybindHandler {
    public static void register() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (ModKeyBinds.openConfigKeybind.consumeClick()) {
                if (client.player != null) {
                    Minecraft.getInstance().gui.setScreen(ConfigScreenFactory.create(Minecraft.getInstance().gui.screen()));
                }
            }
        });

        ScreenEvents.BEFORE_INIT.register((client, screen, w, h) -> {
            ScreenKeyboardEvents.afterKeyPress(screen).register((scr, key) -> {
                if (ModKeyBinds.moveKeybind.matches(key) && scr instanceof AbstractContainerScreen<?> hs) {
                    String title = hs.getTitle().getString();
                    if (AutoConfig.getConfigHolder(ModConfig.class).getConfig().GUIName.equals(title)) {
                        SlotSwitcher.scheduleConfiguredMoves(client, hs.getMenu());
                    }
                }
            });
        });
    }
}
