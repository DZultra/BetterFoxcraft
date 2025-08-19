package net.dzultra.betterfoxcraft.checker;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.registry.Registries;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
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

        if (radius > 100) {
            MinecraftClient.getInstance().player.sendMessage(Text.literal("\n--- Layer Check Start ---\n")
                    .setStyle(Style.EMPTY.withColor(Formatting.GOLD)), false
            );

            MinecraftClient.getInstance().player.sendMessage(Text.literal("Radius out of bound: Cannot be bigger than 100")
                    .setStyle(Style.EMPTY.withColor(Formatting.RED)), false
            );

            MinecraftClient.getInstance().player.sendMessage(Text.literal("\n--- Layer Check End ---\n")
                    .setStyle(Style.EMPTY.withColor(Formatting.GOLD)), false
            );
            return 1;
        }

        // If the block doesn't exist in the registry
        if (blockToCheck == Blocks.AIR && !nameOfBlockToCheckFor.equals("minecraft:air")) {
            MinecraftClient.getInstance().player.sendMessage(Text.literal("\n--- Layer Check Start ---\n")
                    .setStyle(Style.EMPTY.withColor(Formatting.GOLD)), false
            );

            MinecraftClient.getInstance().player.sendMessage(Text.literal("Block does not exist: " + nameOfBlockToCheckFor)
                    .setStyle(Style.EMPTY.withColor(Formatting.RED)), false
            );

            MinecraftClient.getInstance().player.sendMessage(Text.literal("\n--- Layer Check End ---\n")
                    .setStyle(Style.EMPTY.withColor(Formatting.GOLD)), false
            );

            return 1;
        }

        BlockPos pos = new BlockPos(x, y , z);
        LayerChecker.checkLayer(nameOfBlockToCheckFor,blockToCheck, radius, pos);
        return 1;
    }
}
