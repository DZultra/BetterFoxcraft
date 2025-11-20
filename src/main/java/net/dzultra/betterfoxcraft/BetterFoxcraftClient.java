package net.dzultra.betterfoxcraft;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.dzultra.betterfoxcraft.commands.checkCommmand.ChatEventListener;
import net.dzultra.betterfoxcraft.commands.checkCommmand.CheckCommand;
import net.dzultra.betterfoxcraft.checker.ClientTickHandler;
import net.dzultra.betterfoxcraft.checker.LayerCheckerCommand;
import net.dzultra.betterfoxcraft.commands.*;
import net.dzultra.betterfoxcraft.commands.conditions.ConditionsCommand;
import net.dzultra.betterfoxcraft.commands.gambling.CasinoCommand;
import net.dzultra.betterfoxcraft.commands.gambling.Coinflip2Command;
import net.dzultra.betterfoxcraft.commands.gambling.CoinflipCommand;
import net.dzultra.betterfoxcraft.commands.gambling.RockPaperScissorsCommand;
import net.dzultra.betterfoxcraft.commands.playerChecker.PlayerCheckerCommand;
import net.dzultra.betterfoxcraft.keybinds.KeybindHandler;
import net.dzultra.betterfoxcraft.keybinds.ModKeyBinds;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;

public class BetterFoxcraftClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        var chatEventListener = new ChatEventListener();
        ClientReceiveMessageEvents.ALLOW_GAME.register(chatEventListener::onGameMessage);
        AutoConfig.register(ModConfig.class, GsonConfigSerializer::new);
        ClientCommandRegistrationCallback.EVENT.register(((dispatcher, registryAccess) -> {
            dispatcher.register(CasinoCommand.getCommand());
            dispatcher.register(CoinflipCommand.getCommand());
            dispatcher.register(Coinflip2Command.getCommand());
            dispatcher.register(RockPaperScissorsCommand.getCommand());
            dispatcher.register(AnswerCommand.getCommand());
            dispatcher.register(ImgurCommand.getCommand());
            dispatcher.register(OneblockChatCommand.getCommand());
            //dispatcher.register(StaffmodeCommand.getCommand());
            dispatcher.register(StartCommand.getCommand());
            dispatcher.register(TranslateCommand.getCommand());
            dispatcher.register(IslandChatCommand.getCommand());
            //dispatcher.register(StaffChatAlternativeCommand.getCommand());
            //dispatcher.register(StaffChatDisableCommand.getCommand());
            dispatcher.register(RulesCommand.getCommand());
            dispatcher.register(DurationCommand.getCommand());
            dispatcher.register(ConditionsCommand.getCommand());
            dispatcher.register(LayerCheckerCommand.getCommand(registryAccess));
//            dispatcher.register(DiscordCommand.getCommand());
            //dispatcher.register(CheckCommand.getCommand());
            dispatcher.register(PlayerCheckerCommand.getCommand());
        }));
        ClientTickHandler.register();
        ModKeyBinds.register();
        KeybindHandler.register();
        //DiscordBotManager.start();
    }
}