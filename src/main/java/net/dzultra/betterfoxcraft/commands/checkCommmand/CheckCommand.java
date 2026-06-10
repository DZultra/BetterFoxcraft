package net.dzultra.betterfoxcraft.commands.checkCommmand;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.fabricmc.fabric.api.client.command.v2.ClientCommands;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.Minecraft;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommands.literal;

public class CheckCommand {
    private static boolean isExecuted = false;

    public static LiteralArgumentBuilder<FabricClientCommandSource> getCommand() {
        return literal("check")
                .then(ClientCommands.argument("user", StringArgumentType.string())
                        .executes(context -> handleCommand(StringArgumentType.getString(context, "user"))
                )
        );
    }

    private static int handleCommand(String user) {
        isExecuted = true;
        Minecraft.getInstance().getConnection().sendCommand("realname " + user);
        return 0;
    }

    public static boolean isExecuted() {
        return isExecuted;
    }
    public static void noLongerExecuted() {
        isExecuted = false;
    }
}
