package net.dzultra.betterfoxcraft.checker;

import net.dzultra.betterfoxcraft.booklogger.BookLogger;
import net.dzultra.betterfoxcraft.other.ModSpectatorTracer;
import net.dzultra.betterfoxcraft.selector.BlockSelector;

public class ClientTickHandler {
    public static void register() {
        ParticleTracker.getParticleTracker();
        BlockSelector.getBlockSelector();
        BlockSelector.getUseBlockCallback();
        ModSpectatorTracer.getSpectatorTracer();
        //BookLogger.getBookLogger();
    }
}
