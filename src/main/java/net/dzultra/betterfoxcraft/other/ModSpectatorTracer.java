package net.dzultra.betterfoxcraft.other;

import net.dzultra.betterfoxcraft.BetterFoxcraft;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.Vec3d;

public class ModSpectatorTracer {
    public static void getSpectatorTracer() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.world == null) return;

            client.world.getEntities().forEach(entity -> {
                if (entity instanceof PlayerEntity player && player.isSpectator()) {
                    Vec3d pos = player.getPos();

                    // Partikel an der Position des Spectators spawnen
                    client.world.addParticle(ParticleTypes.END_ROD,
                            pos.x, pos.y + 0.5, pos.z,
                            0, 0, 0);
                }
            });
        });
    }
}
