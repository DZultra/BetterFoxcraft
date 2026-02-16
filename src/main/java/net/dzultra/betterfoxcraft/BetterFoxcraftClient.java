package net.dzultra.betterfoxcraft;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.dzultra.betterfoxcraft.commands.jfa.HistoryCommand;
import net.dzultra.betterfoxcraft.commands.jfa.LeaderboardCommand;
import net.dzultra.betterfoxcraft.commands.jfa.PlayerStatsCommand;
import net.dzultra.betterfoxcraft.keybinds.KeybindHandler;
import net.dzultra.betterfoxcraft.keybinds.ModKeyBinds;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;

public class BetterFoxcraftClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        AutoConfig.register(ModConfig.class, GsonConfigSerializer::new);
        ClientCommandRegistrationCallback.EVENT.register(((dispatcher, registryAccess) -> {
            dispatcher.register(LeaderboardCommand.getCommand());
            dispatcher.register(PlayerStatsCommand.getCommand());
            dispatcher.register(HistoryCommand.getCommand());
        }));
        ModKeyBinds.register();
        KeybindHandler.register();
    }
}