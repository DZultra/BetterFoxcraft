package net.dzultra.betterfoxcraft;

import net.dzultra.betterfoxcraft.commands.gambling.CasinoCommand;
import net.dzultra.betterfoxcraft.commands.gambling.Coinflip2Command;
import net.dzultra.betterfoxcraft.commands.gambling.CoinflipCommand;
import net.dzultra.betterfoxcraft.commands.gambling.RockPaperScissorsCommand;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;

public class BetterFoxcraftClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ClientCommandRegistrationCallback.EVENT.register(((dispatcher, registryAccess) -> {
            dispatcher.register(CasinoCommand.getCommand());
            dispatcher.register(CoinflipCommand.getCommand());
            dispatcher.register(RockPaperScissorsCommand.getCommand());
            dispatcher.register(Coinflip2Command.getCommand());
        }));
    }

}