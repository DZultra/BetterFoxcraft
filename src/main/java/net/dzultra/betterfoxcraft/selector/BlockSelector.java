package net.dzultra.betterfoxcraft.selector;

import me.shedaniel.autoconfig.AutoConfig;
import net.dzultra.betterfoxcraft.ModConfig;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockSelector {
    public static BlockPos selectedBlockPos = null;
    public static Block selectedBlock = null;

    public static void getBlockSelector(){
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.world != null && selectedBlockPos != null && AutoConfig.getConfigHolder(ModConfig.class).getConfig().enableParticleTracker) {
                spawnParticlesForPosition(selectedBlockPos, selectedBlock);
            }
        });
    }

    public static void getUseBlockCallback() {
        UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
            if (!AutoConfig.getConfigHolder(ModConfig.class).getConfig().enableBlockSelector || !player.getStackInHand(hand).isEmpty()) return ActionResult.PASS;


            if (world.isClient && hand == Hand.MAIN_HAND && hitResult.getType() == HitResult.Type.BLOCK) {
                BlockPos pos = hitResult.getBlockPos();
                BlockState state = world.getBlockState(pos);

                if (pos.equals(selectedBlockPos)) {
                    // Deselect if clicking the same block
                    selectedBlockPos = null;
                    selectedBlock = null;
                } else {
                    // Select new block
                    selectedBlockPos = pos;
                    selectedBlock = state.getBlock();
                }
            }
            return ActionResult.PASS;
        });
    }

    private static void spawnParticlesForPosition(BlockPos pos, Block block) {
        MinecraftClient client = MinecraftClient.getInstance();
        World world = client.world;

        if (world == null) return;
        if (!AutoConfig.getConfigHolder(ModConfig.class).getConfig().enableBlockSelector) {
            selectedBlockPos = null;
            selectedBlock = null;
            return;
        }

        // Use GLOW particles - they're brighter and more visible than WAX_ON
        ParticleEffect particleType = ParticleTypes.WAX_ON;

        // Make the box slightly bigger (0.1 blocks outside the block)
        double BOX_OFFSET = AutoConfig.getConfigHolder(ModConfig.class).getConfig().boxOffset;
        double minX = pos.getX() - BOX_OFFSET;
        double minY = pos.getY() - BOX_OFFSET;
        double minZ = pos.getZ() - BOX_OFFSET;
        double maxX = pos.getX() + 1 + BOX_OFFSET;
        double maxY = pos.getY() + 1 + BOX_OFFSET;
        double maxZ = pos.getZ() + 1 + BOX_OFFSET;

        // More edge positions for denser particles
        double[] edgePositions = {0.0, 0.25, 0.5, 0.75, 1.0};
        double[] heightPositions = {0.25, 0.5, 0.75}; // Additional vertical positions

        // Horizontal edges (top and bottom)
        for (double edgePos : edgePositions) {
            double xPos = minX + (maxX - minX) * edgePos;
            double zPos = minZ + (maxZ - minZ) * edgePos;

            // Bottom edges
            world.addParticle(particleType, xPos, minY, minZ, 0, 0, 0);
            world.addParticle(particleType, minX, minY, zPos, 0, 0, 0);
            world.addParticle(particleType, xPos, minY, maxZ, 0, 0, 0);
            world.addParticle(particleType, maxX, minY, zPos, 0, 0, 0);

            // Top edges
            world.addParticle(particleType, xPos, maxY, minZ, 0, 0, 0);
            world.addParticle(particleType, minX, maxY, zPos, 0, 0, 0);
            world.addParticle(particleType, xPos, maxY, maxZ, 0, 0, 0);
            world.addParticle(particleType, maxX, maxY, zPos, 0, 0, 0);
        }

        // Vertical edges (more of them at different heights)
        for (double heightPos : heightPositions) {
            double yPos = minY + (maxY - minY) * heightPos;

            world.addParticle(particleType, minX, yPos, minZ, 0, 0, 0);
            world.addParticle(particleType, maxX, yPos, minZ, 0, 0, 0);
            world.addParticle(particleType, minX, yPos, maxZ, 0, 0, 0);
            world.addParticle(particleType, maxX, yPos, maxZ, 0, 0, 0);
        }

        // Corner particles for better definition
        world.addParticle(particleType, minX, minY, minZ, 0, 0, 0);
        world.addParticle(particleType, maxX, minY, minZ, 0, 0, 0);
        world.addParticle(particleType, minX, minY, maxZ, 0, 0, 0);
        world.addParticle(particleType, maxX, minY, maxZ, 0, 0, 0);
        world.addParticle(particleType, minX, maxY, minZ, 0, 0, 0);
        world.addParticle(particleType, maxX, maxY, minZ, 0, 0, 0);
        world.addParticle(particleType, minX, maxY, maxZ, 0, 0, 0);
        world.addParticle(particleType, maxX, maxY, maxZ, 0, 0, 0);
    }

    public static boolean hasSelection() {
        return selectedBlock != null;
    }
}
