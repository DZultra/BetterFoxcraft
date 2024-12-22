package net.dzultra.betterfoxcraft.commands.gambling;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;

public class CasinoCommand {
    public static LiteralArgumentBuilder<FabricClientCommandSource> getCommand() {
        return literal("casino").executes(context -> {
            MinecraftClient.getInstance().player.sendMessage(
                    Text.literal("\nYou should not gamble!\n").setStyle(Style.EMPTY
                            .withColor(Formatting.RED)
                            .withBold(true))
                    , false);
            return 0;
        });
    }
}