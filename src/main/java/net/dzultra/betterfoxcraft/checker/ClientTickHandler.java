package net.dzultra.betterfoxcraft.checker;

import net.dzultra.betterfoxcraft.menu_interactions.DataGrabber;
import net.dzultra.betterfoxcraft.menu_interactions.GrowstationClicking;
import net.dzultra.betterfoxcraft.other.ModSpectatorTracer;
import net.dzultra.betterfoxcraft.selector.BlockSelector;

public class ClientTickHandler {
    public static void register() {
        ParticleTracker.getParticleTracker();
        BlockSelector.getBlockSelector();
        BlockSelector.getUseBlockCallback();
        ModSpectatorTracer.getSpectatorTracer();
        DataGrabber.register();
        GrowstationClicking.register();
        //BookLogger.getBookLogger();
    }
}
