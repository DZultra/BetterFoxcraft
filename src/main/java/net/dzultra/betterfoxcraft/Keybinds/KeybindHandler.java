package net.dzultra.betterfoxcraft.Keybinds;

import me.shedaniel.autoconfig.AutoConfig;
import net.dzultra.betterfoxcraft.ModConfig;
import net.dzultra.betterfoxcraft.slotswitcher.SlotSwitcher;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.fabricmc.fabric.api.client.screen.v1.ScreenKeyboardEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.HandledScreen;

@Environment(EnvType.CLIENT)
public class KeybindHandler {
    public static void register() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (ModKeyBinds.openConfigKeybind.wasPressed()) {
                if (client.player != null) {
                    MinecraftClient.getInstance().setScreen(
                            AutoConfig.getConfigScreen(ModConfig.class, MinecraftClient.getInstance().currentScreen).get()
                    );
                }
            }
        });

        ScreenEvents.BEFORE_INIT.register((client, screen, w, h) -> {
            ScreenKeyboardEvents.afterKeyPress(screen).register((scr, key, scancode, modifiers) -> {
                if (ModKeyBinds.moveKeybind.matchesKey(key, scancode) && scr instanceof HandledScreen<?> hs) {
                    String title = hs.getTitle().getString();
                    if (AutoConfig.getConfigHolder(ModConfig.class).getConfig().GUIName.equals(title)) {
                        SlotSwitcher.scheduleConfiguredMoves(client, hs.getScreenHandler());
                    }
                }
            });
        });
    }
}
