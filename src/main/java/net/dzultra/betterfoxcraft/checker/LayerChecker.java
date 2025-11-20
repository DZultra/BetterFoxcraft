package net.dzultra.betterfoxcraft.checker;

import me.shedaniel.autoconfig.AutoConfig;
import net.dzultra.betterfoxcraft.ModConfig;
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
    public static Block currentBlockToCheck;

    public static void checkLayer(String nameOfBlockToCheckFor, Block blockToCheck, int radius, BlockPos centerPos) {
        currentBlockToCheck = blockToCheck;
        String startMessage = "\n--- Layer Check Start ---\n";
        sendMessage(startMessage, Formatting.GOLD);

        if (MinecraftClient.getInstance().player == null) return;

        World world = MinecraftClient.getInstance().player.getEntityWorld();
        boolean allBlocks = true;
        int missingBlocks = 0;
        int maxMissingBlocks = AutoConfig.getConfigHolder(ModConfig.class).getConfig().maxMissingBlocks;

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
                        String message = "Missing Block at " + unpackedBlock + " --> " + wrongBlock;

                        sendMessage(message, Formatting.RED);

                        // Only spawn particles if we're under the max missing blocks limit
                        if (missingBlocks < maxMissingBlocks) {
                            ParticleTracker.addPosition(currentPos);
                        }
                    }
                }
                if (missingBlocks >= maxMissingBlocks) {
                    break;
                }
            }
            if (missingBlocks >= maxMissingBlocks) {
                String message1 = "\nThe LayerCheck has been canceled because there can have been more than " + maxMissingBlocks + " missing Blocks detected to avoid spam.\n";
                String message2 = "\nThis value can be configured in the Settings!\n";

                sendMessage(message1, Formatting.DARK_RED);
                sendMessage(message2, Formatting.GREEN);
                break;
            }
        }

        if (allBlocks) {
            onAllCarrots(centerPos, nameOfBlockToCheckFor,radius);
        } else {
            onMissingCarrots(centerPos, nameOfBlockToCheckFor,radius, missingBlocks, maxMissingBlocks);
        }
    }

    private static void onAllCarrots(BlockPos center, String nameOfBlockToCheckFor,int radius) {
        String block = "Block X: " + center.getX() + " Y: " + center.getY() + " Z: " + center.getZ();
        String message1 = "\nAll blocks within radius of " + radius + " from " + block + " are " + nameOfBlockToCheckFor + "!\n";
        String message2 = "\n--- Layer Check End ---\n";
        sendMessage(message1, Formatting.GREEN);
        sendMessage(message2, Formatting.GOLD);

    }

    private static void onMissingCarrots(BlockPos center, String nameOfBlockToCheckFor,int radius, int missingCount, int maxMissingBlocks) {

        if (missingCount >= maxMissingBlocks) {

        } else {
            String block = "Block X: " + center.getX() + " Y: " + center.getY() + " Z: " + center.getZ();
            String message1 = "\n" + missingCount + " non-" + nameOfBlockToCheckFor + " blocks found within radius of " + radius + " from " + block;
            sendMessage(message1, Formatting.DARK_RED);
        }

        String message2 = "\n--- Layer Check End ---\n";
        sendMessage(message2, Formatting.GOLD);
    }


    protected static void sendMessage(String message, Formatting color) {
        Text messageWithColor = Text.literal(message).setStyle(Style.EMPTY.withColor(color));
        MinecraftClient.getInstance().player.sendMessage(messageWithColor, false);
    }
    protected static void sendMessage(String message, Formatting color, boolean bold) {
        Text messageWithColor = Text.literal(message).setStyle(Style.EMPTY.withColor(color).withBold(bold));
        MinecraftClient.getInstance().player.sendMessage(messageWithColor, false);
    }
}