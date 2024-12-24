package net.dzultra.betterfoxcraft.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class StaffChatAlternativeCommand {
    public static LiteralArgumentBuilder<FabricClientCommandSource> getCommand() {
        return ClientCommandManager.literal("stc")
                .then(ClientCommandManager.argument("message", StringArgumentType.greedyString())
                        .executes(context -> {
                            String message = StringArgumentType.getString(context, "message");
                            try {
                                MinecraftClient.getInstance().getNetworkHandler().sendChatCommand("staffchat " + message);
                            } catch (IllegalArgumentException e) {
                                System.out.println(e.getMessage());
                                MinecraftClient.getInstance().player.sendMessage(Text.literal("Wrong Argument: /stc <text>").setStyle(Style.EMPTY
                                                .withColor(Formatting.RED)
                                                .withBold(true))
                                        , false);
                            }
                            return 1;
                        })
                );
    }
}
