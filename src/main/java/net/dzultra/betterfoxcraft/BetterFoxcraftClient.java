package net.dzultra.betterfoxcraft;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.dzultra.betterfoxcraft.commands.*;
import net.dzultra.betterfoxcraft.commands.gambling.CasinoCommand;
import net.dzultra.betterfoxcraft.commands.gambling.CoinflipCommand;
import net.dzultra.betterfoxcraft.commands.gambling.RockPaperScissorsCommand;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;

public class BetterFoxcraftClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        AutoConfig.register(ModConfig.class, GsonConfigSerializer::new);
        ClientCommandRegistrationCallback.EVENT.register(((dispatcher, registryAccess) -> {
            dispatcher.register(CasinoCommand.getCommand());
            dispatcher.register(CoinflipCommand.getCommand());
            dispatcher.register(RockPaperScissorsCommand.getCommand());
            dispatcher.register(AnswerCommand.getCommand());
            dispatcher.register(ImgurCommand.getCommand());
            dispatcher.register(OneblockChatCommand.getCommand());
            dispatcher.register(StaffmodeCommand.getCommand());
            dispatcher.register(StartCommand.getCommand());
            dispatcher.register(TranslateCommand.getCommand());
            dispatcher.register(IslandChatCommand.getCommand());
            dispatcher.register(StaffChatAlternativeCommand.getCommand());
            dispatcher.register(StaffChatDisableCommand.getCommand());
            dispatcher.register(RulesCommand.getCommand());
        }));
    }

}