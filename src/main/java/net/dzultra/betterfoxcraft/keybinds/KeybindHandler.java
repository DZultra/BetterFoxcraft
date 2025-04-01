package net.dzultra.betterfoxcraft.keybinds;

import me.shedaniel.autoconfig.AutoConfig;
import net.dzultra.betterfoxcraft.ModConfig;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;

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
    }
}
