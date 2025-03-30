package net.dzultra.betterfoxcraft.checker;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.registry.Registries;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class LayerChecker {
    public static void checkLayer(String nameOfBlockToCheckFor,Block blockToCheck, int radius, BlockPos centerPos) {
        MinecraftClient.getInstance().player.sendMessage(Text.literal("\n--- Layer Check Start ---\n")
                .setStyle(Style.EMPTY.withColor(Formatting.GOLD))
        );

        World world = MinecraftClient.getInstance().player.getWorld();
        boolean allBlocks = true;
        int missingBlocks = 0;

        // Calculate the bounds of the square area
        int minX = centerPos.getX() - radius;
        int maxX = centerPos.getX() + radius;
        int minZ = centerPos.getZ() - radius;
        int maxZ = centerPos.getZ() + radius;

        // Get the Y coordinate (assuming same Y level for farming)
        int y = centerPos.getY();

        // Iterate through all blocks in the square area
        for (int x = minX; x <= maxX; x++) {
            for (int z = minZ; z <= maxZ; z++) {
                BlockPos currentPos = new BlockPos(x, y, z);

                // Skip the center position
                if (currentPos.equals(centerPos)) {
                    continue;
                }

                if (!world.getBlockState(currentPos).isOf(blockToCheck)) {
                    if (!world.getBlockState(currentPos.down()).isOf(Blocks.WATER)) {
                        allBlocks = false;
                        missingBlocks++;

                        String unpackedBlock = " (X: " + currentPos.getX() + " | Y: " + currentPos.getY() + " | Z: " + currentPos.getZ() + ")";
                        String wrongBlock = Registries.BLOCK.getId(world.getBlockState(currentPos).getBlock()).toString();
                        MinecraftClient.getInstance().player.sendMessage(Text.literal("Missing Block at " + unpackedBlock + " --> " + wrongBlock)
                                .setStyle(Style.EMPTY.withColor(Formatting.RED))
                        );
                    }
                }
            }
        }

        if (allBlocks) {
            onAllCarrots(centerPos, nameOfBlockToCheckFor,radius);
        } else {
            onMissingCarrots(centerPos, nameOfBlockToCheckFor,radius, missingBlocks);
        }
    }

    private static void onAllCarrots(BlockPos center, String nameOfBlockToCheckFor,int radius) {
        String block = "Block X: " + center.getX() + " Y: " + center.getY() + " Z: " + center.getZ();

        MinecraftClient.getInstance().player.sendMessage(
                Text.literal("\nAll blocks within radius of " + radius + " from " + block + " are " + nameOfBlockToCheckFor + "!\n")
                        .setStyle(Style.EMPTY.withColor(Formatting.GREEN))
        );

        MinecraftClient.getInstance().player.sendMessage(Text.literal("\n--- Layer Check End ---\n")
                .setStyle(Style.EMPTY.withColor(Formatting.GOLD))
        );
    }

    private static void onMissingCarrots(BlockPos center, String nameOfBlockToCheckFor,int radius, int missingCount) {
        String block = "Block X: " + center.getX() + " Y: " + center.getY() + " Z: " + center.getZ();
        MinecraftClient.getInstance().player.sendMessage(
                Text.literal("\n" + missingCount + " non-" + nameOfBlockToCheckFor + " blocks found within radius of " + radius + " from " + block)
                        .setStyle(Style.EMPTY.withColor(Formatting.DARK_RED))
        );

        MinecraftClient.getInstance().player.sendMessage(Text.literal("\n--- Layer Check End ---\n")
                .setStyle(Style.EMPTY.withColor(Formatting.GOLD))
        );
    }
}
