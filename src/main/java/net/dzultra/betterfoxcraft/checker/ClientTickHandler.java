package net.dzultra.betterfoxcraft.checker;

import me.shedaniel.autoconfig.AutoConfig;
import net.dzultra.betterfoxcraft.ModConfig;
import net.dzultra.betterfoxcraft.other.ModSpectatorTracer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

public class ClientTickHandler {
    public static void register() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.world != null && client.player != null && AutoConfig.getConfigHolder(ModConfig.class).getConfig().enableParticleTracker) {
                ParticleTracker.tick(LayerChecker.currentBlockToCheck);
            }
        });

        ModSpectatorTracer.getSpectatorTracer();
    }
}
