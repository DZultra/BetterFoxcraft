package net.dzultra.betterfoxcraft.selector;

import me.shedaniel.autoconfig.AutoConfig;
import net.dzultra.betterfoxcraft.ModConfig;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.HitResult;

public class BlockSelector {
    public static BlockPos selectedBlockPos = null;
    public static Block selectedBlock = null;

    public static void getBlockSelector(){
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.level != null && selectedBlockPos != null && AutoConfig.getConfigHolder(ModConfig.class).getConfig().enableParticleTracker) {
                spawnParticlesForPosition(selectedBlockPos, selectedBlock);
            }
        });
    }

    public static void getUseBlockCallback() {
        UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
            if (!AutoConfig.getConfigHolder(ModConfig.class).getConfig().enableBlockSelector || !player.getItemInHand(hand).isEmpty()) return InteractionResult.PASS;


            if (world.isClientSide() && hand == InteractionHand.MAIN_HAND && hitResult.getType() == HitResult.Type.BLOCK) {
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
            return InteractionResult.PASS;
        });
    }

    private static void spawnParticlesForPosition(BlockPos pos, Block block) {
        Minecraft client = Minecraft.getInstance();
        Level world = client.level;

        if (world == null) return;
        if (!AutoConfig.getConfigHolder(ModConfig.class).getConfig().enableBlockSelector) {
            selectedBlockPos = null;
            selectedBlock = null;
            return;
        }

        // Use GLOW particles - they're brighter and more visible than WAX_ON
        ParticleOptions particleType = ParticleTypes.WAX_ON;

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
