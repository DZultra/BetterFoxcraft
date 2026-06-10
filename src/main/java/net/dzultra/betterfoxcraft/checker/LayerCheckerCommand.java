package net.dzultra.betterfoxcraft.checker;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import me.shedaniel.autoconfig.AutoConfig;
import net.dzultra.betterfoxcraft.ModConfig;
import net.dzultra.betterfoxcraft.selector.BlockSelector;
import net.fabricmc.fabric.api.client.command.v2.ClientCommands;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

public class LayerCheckerCommand {

    public static LiteralArgumentBuilder<FabricClientCommandSource> getCommand(CommandBuildContext commandRegistryAccess) {
        return ClientCommands.literal("layerchecker")
                .then(ClientCommands.argument("block", StringArgumentType.string())
                        .then(ClientCommands.argument("radius", IntegerArgumentType.integer())
                                // Version with all coordinates
                                .then(ClientCommands.argument("x", IntegerArgumentType.integer())
                                        .then(ClientCommands.argument("y", IntegerArgumentType.integer())
                                                .then(ClientCommands.argument("z", IntegerArgumentType.integer())
                                                        .executes(context -> checkForEmptySpots(
                                                                StringArgumentType.getString(context, "block"),
                                                                IntegerArgumentType.getInteger(context, "radius"),
                                                                IntegerArgumentType.getInteger(context, "x"),
                                                                IntegerArgumentType.getInteger(context, "y"),
                                                                IntegerArgumentType.getInteger(context, "z")
                                                        ))
                                                )
                                        )
                                )
                                // Version without coordinates (Uses selected block)
                                .executes(context -> checkForEmptySpots(
                                        StringArgumentType.getString(context, "block"),
                                        IntegerArgumentType.getInteger(context, "radius")
                                ))
                        )
                );
    }

    public static int checkForEmptySpots(String nameOfBlockToCheckFor, int radius, int x, int y, int z) {
        Identifier blockId = Identifier.parse(nameOfBlockToCheckFor);

        Block blockToCheck = BuiltInRegistries.BLOCK.getValue(blockId);

        int maxRadius = AutoConfig.getConfigHolder(ModConfig.class).getConfig().maxRadius;

        if (radius > maxRadius) {
            String message1 = "\n--- Layer Check Start ---\n";
            String message2 = "Radius out of bound: Cannot be bigger than " + maxRadius;
            String message3 = "\n--- Layer Check End ---\n";

            LayerChecker.sendMessage(message1, ChatFormatting.GOLD);
            LayerChecker.sendMessage(message2, ChatFormatting.RED);
            LayerChecker.sendMessage(message3, ChatFormatting.GOLD);

            return 1;
        }

        // If the block doesn't exist in the registry
        if (blockToCheck == Blocks.AIR && !nameOfBlockToCheckFor.equals("minecraft:air")) {
            String message1 = "\n--- Layer Check Start ---\n";
            String message2 = "Block does not exist: " + nameOfBlockToCheckFor;
            String message3 = "\n--- Layer Check End ---\n";

            LayerChecker.sendMessage(message1, ChatFormatting.GOLD);
            LayerChecker.sendMessage(message2, ChatFormatting.RED);
            LayerChecker.sendMessage(message3, ChatFormatting.GOLD);

            return 1;
        }

        BlockPos pos = new BlockPos(x, y , z);
        LayerChecker.checkLayer(nameOfBlockToCheckFor,blockToCheck, radius, pos);
        return 1;
    }

    public static int checkForEmptySpots(String nameOfBlockToCheckFor, int radius) {
        Identifier blockId = Identifier.parse(nameOfBlockToCheckFor);

        Block blockToCheck = BuiltInRegistries.BLOCK.getValue(blockId);

        int maxRadius = AutoConfig.getConfigHolder(ModConfig.class).getConfig().maxRadius;

        if (radius > maxRadius) {
            String message1 = "\n--- Layer Check Start ---\n";
            String message2 = "Radius out of bound: Cannot be bigger than " + maxRadius;
            String message3 = "\n--- Layer Check End ---\n";

            LayerChecker.sendMessage(message1, ChatFormatting.GOLD);
            LayerChecker.sendMessage(message2, ChatFormatting.RED);
            LayerChecker.sendMessage(message3, ChatFormatting.GOLD);

            return 1;
        }

        // If the block doesn't exist in the registry
        if (blockToCheck == Blocks.AIR && !nameOfBlockToCheckFor.equals("minecraft:air")) {
            String message1 = "\n--- Layer Check Start ---\n";
            String message2 = "Block does not exist: " + nameOfBlockToCheckFor;
            String message3 = "\n--- Layer Check End ---\n";

            LayerChecker.sendMessage(message1, ChatFormatting.GOLD);
            LayerChecker.sendMessage(message2, ChatFormatting.RED);
            LayerChecker.sendMessage(message3, ChatFormatting.GOLD);

            return 1;
        }

        if (!BlockSelector.hasSelection()) {
            String message1 = "\n--- Layer Check Start ---\n";
            String message2 = "No coordinates have been provided and no block has been selected";
            String message3 = "\n--- Layer Check End ---\n";

            LayerChecker.sendMessage(message1, ChatFormatting.GOLD);
            LayerChecker.sendMessage(message2, ChatFormatting.RED);
            LayerChecker.sendMessage(message3, ChatFormatting.GOLD);

            return 1;
        }

        BlockPos startPos = new BlockPos(
                BlockSelector.selectedBlockPos.getX(),
                BlockSelector.selectedBlockPos.getY() + 1,
                BlockSelector.selectedBlockPos.getZ()
        );

        BlockSelector.selectedBlockPos = null; // Deselect/Unmark the selected Block
        BlockSelector.selectedBlock = null; //            -- ^^ --

        LayerChecker.checkLayer(nameOfBlockToCheckFor, blockToCheck, radius, startPos);
        return 1;
    }
}
