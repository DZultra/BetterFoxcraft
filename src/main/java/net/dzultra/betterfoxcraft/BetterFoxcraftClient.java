package net.dzultra.betterfoxcraft;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.dzultra.betterfoxcraft.commands.*;
import net.dzultra.betterfoxcraft.keybinds.KeybindHandler;
import net.dzultra.betterfoxcraft.keybinds.ModKeyBinds;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;

public class BetterFoxcraftClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        AutoConfig.register(ModConfig.class, GsonConfigSerializer::new);
        ClientCommandRegistrationCallback.EVENT.register(((dispatcher, registryAccess) -> {
            dispatcher.register(ImgurCommand.getCommand());
        }));
        ModKeyBinds.register();
        KeybindHandler.register();
    }
}