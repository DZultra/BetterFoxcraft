package net.dzultra.betterfoxcraft.checker;

import me.shedaniel.autoconfig.AutoConfig;
import net.dzultra.betterfoxcraft.ModConfig;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import java.util.HashMap;
import java.util.Map;

public class ParticleTracker {
    private static final Map<BlockPos, Integer> trackedPositions = new HashMap<>();
    private static int tickCounter = 0;

    public static void addPosition(BlockPos pos) {
        int totalTicks = AutoConfig.getConfigHolder(ModConfig.class).getConfig().totalSeconds * 20;
        trackedPositions.put(pos, totalTicks);
    }

    public static void tick(Block blockToCheck) {
        int ticks_between_updates = AutoConfig.getConfigHolder(ModConfig.class).getConfig().ticks_between_updates;
        tickCounter++;
        if (tickCounter < ticks_between_updates) {
            return;
        }
        tickCounter = 0;

        trackedPositions.entrySet().removeIf(entry -> {
            int remainingTicks = entry.getValue() - ticks_between_updates;
            if (remainingTicks <= 0) {
                return true;
            }
            entry.setValue(remainingTicks);
            spawnParticlesForPosition(entry.getKey(), blockToCheck);
            return false;
        });
    }

    public static void getParticleTracker() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.level != null && client.player != null && AutoConfig.getConfigHolder(ModConfig.class).getConfig().enableParticleTracker) {
                ParticleTracker.tick(LayerChecker.currentBlockToCheck);
            }
        });
    }

    private static void spawnParticlesForPosition(BlockPos pos, Block blockToCheck) {
        Minecraft client = Minecraft.getInstance();
        Level world = client.level;

        if (world == null) return;
        if (world.getBlockState(pos) == blockToCheck.defaultBlockState()) {
            return;
        }

        double x = pos.getX();
        double y = pos.getY();
        double z = pos.getZ();

        ParticleOptions particleType = ParticleTypes.ELECTRIC_SPARK;

        double[] edgePositions = {0.0, 0.5, 1.0};

        // Horizontal edges (top and bottom)
        for (double edgePos : edgePositions) {
            // Bottom edges
            world.addParticle(particleType, x + edgePos, y,     z,     0, 0, 0); // Front
            world.addParticle(particleType, x,     y,     z + edgePos, 0, 0, 0); // Right
            world.addParticle(particleType, x + edgePos, y,     z + 1, 0, 0, 0); // Back
            world.addParticle(particleType, x + 1, y,     z + edgePos, 0, 0, 0); // Left

            // Top edges
            world.addParticle(particleType, x + edgePos, y + 1, z,     0, 0, 0); // Front
            world.addParticle(particleType, x,     y + 1, z + edgePos, 0, 0, 0); // Right
            world.addParticle(particleType, x + edgePos, y + 1, z + 1, 0, 0, 0); // Back
            world.addParticle(particleType, x + 1, y + 1, z + edgePos, 0, 0, 0); // Left
        }

        // Vertical edges (skip corners since they're already done)
        world.addParticle(particleType, x,     y + 0.5, z,     0, 0, 0); // Front-right
        world.addParticle(particleType, x + 1, y + 0.5, z,     0, 0, 0); // Front-left
        world.addParticle(particleType, x,     y + 0.5, z + 1, 0, 0, 0); // Back-right
        world.addParticle(particleType, x + 1, y + 0.5, z + 1, 0, 0, 0); // Back-left

    }
}
