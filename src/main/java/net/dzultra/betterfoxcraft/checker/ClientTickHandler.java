package net.dzultra.betterfoxcraft.checker;

import net.dzultra.betterfoxcraft.menu_data.DataGrabber;
import net.dzultra.betterfoxcraft.other.ModSpectatorTracer;
import net.dzultra.betterfoxcraft.selector.BlockSelector;

public class ClientTickHandler {
    public static void register() {
        ParticleTracker.getParticleTracker();
        BlockSelector.getBlockSelector();
        BlockSelector.getUseBlockCallback();
        ModSpectatorTracer.getSpectatorTracer();
        DataGrabber.register();
        //BookLogger.getBookLogger();
    }
}
