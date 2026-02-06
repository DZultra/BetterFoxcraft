package net.dzultra.betterfoxcraft.commands.jfa;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;

public class PlayerStatsCommand {
    public static LiteralArgumentBuilder<FabricClientCommandSource> getCommand() {
        return ClientCommandManager.literal("playerstats")
            .then(ClientCommandManager.argument("username", StringArgumentType.word())
                    .executes(ctx -> runStats(ctx, null))
                    .then(ClientCommandManager.argument("gamemode", StringArgumentType.word())
                        .suggests(PlayerStatsCommand::suggestGamemodes)
                        .executes(ctx ->
                                runStats(ctx, StringArgumentType.getString(ctx, "gamemode"))
                        )
                    )
            );
    }
}
