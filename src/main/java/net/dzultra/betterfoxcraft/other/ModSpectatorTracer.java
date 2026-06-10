package net.dzultra.betterfoxcraft.other;

import me.shedaniel.autoconfig.AutoConfig;
import net.dzultra.betterfoxcraft.ModConfig;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public class ModSpectatorTracer {
    public static void getSpectatorTracer() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.level == null) return;
            if (!AutoConfig.getConfigHolder(ModConfig.class).getConfig().enableSpectatorTracer) return;

            client.level.entitiesForRendering().forEach(entity -> {
                if (entity instanceof Player player && player.isSpectator()) {
                    Vec3 pos = player.position();

                    client.level.addParticle(ParticleTypes.END_ROD,
                            pos.x, pos.y + 0.5, pos.z,
                            0, 0, 0);
                }
            });
        });
    }
}
