package net.dzultra.betterfoxcraft.checker;

import me.shedaniel.autoconfig.AutoConfig;
import net.dzultra.betterfoxcraft.ModConfig;
import net.minecraft.block.Block;
import net.minecraft.client.MinecraftClient;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

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

    private static void spawnParticlesForPosition(BlockPos pos, Block blockToCheck) {
        MinecraftClient client = MinecraftClient.getInstance();
        World world = client.world;

        if (world == null) return;
        if (world.getBlockState(pos) == blockToCheck.getDefaultState()) {
            return;
        }

        double x = pos.getX();
        double y = pos.getY();
        double z = pos.getZ();

        ParticleEffect particleType = ParticleTypes.ELECTRIC_SPARK;

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
