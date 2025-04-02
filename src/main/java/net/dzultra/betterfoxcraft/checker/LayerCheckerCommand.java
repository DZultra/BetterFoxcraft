package net.dzultra.betterfoxcraft.checker;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import me.shedaniel.autoconfig.AutoConfig;
import net.dzultra.betterfoxcraft.ModConfig;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.registry.Registries;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

public class LayerCheckerCommand {

    public static LiteralArgumentBuilder<FabricClientCommandSource> getCommand(CommandRegistryAccess commandRegistryAccess) {
        return ClientCommandManager.literal("layerchecker")
                .then(ClientCommandManager.argument("block", StringArgumentType.string())
                        .then(ClientCommandManager.argument("radius", IntegerArgumentType.integer())
                                .then(ClientCommandManager.argument("x", IntegerArgumentType.integer())
                                        .then(ClientCommandManager.argument("y", IntegerArgumentType.integer())
                                                .then(ClientCommandManager.argument("z", IntegerArgumentType.integer())
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
                        )
                );
    }

    public static int checkForEmptySpots(String nameOfBlockToCheckFor, int radius, int x, int y, int z) {
        Identifier blockId = Identifier.of(nameOfBlockToCheckFor);

        Block blockToCheck = Registries.BLOCK.get(blockId);

        int maxRadius = AutoConfig.getConfigHolder(ModConfig.class).getConfig().maxRadius;

        if (radius > maxRadius) {
            String message1 = "\n--- Layer Check Start ---\n";
            String message2 = "Radius out of bound: Cannot be bigger than " + maxRadius;
            String message3 = "\n--- Layer Check End ---\n";

            LayerChecker.sendMessage(message1, Formatting.GOLD);
            LayerChecker.sendMessage(message2, Formatting.RED);
            LayerChecker.sendMessage(message3, Formatting.GOLD);

            return 1;
        }

        // If the block doesn't exist in the registry
        if (blockToCheck == Blocks.AIR && !nameOfBlockToCheckFor.equals("minecraft:air")) {
            String message1 = "\n--- Layer Check Start ---\n";
            String message2 = "Block does not exist: " + nameOfBlockToCheckFor;
            String message3 = "\n--- Layer Check End ---\n";

            LayerChecker.sendMessage(message1, Formatting.GOLD);
            LayerChecker.sendMessage(message2, Formatting.RED);
            LayerChecker.sendMessage(message3, Formatting.GOLD);

            return 1;
        }

        BlockPos pos = new BlockPos(x, y , z);
        LayerChecker.checkLayer(nameOfBlockToCheckFor,blockToCheck, radius, pos);
        return 1;
    }
}
