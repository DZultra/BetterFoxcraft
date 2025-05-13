package net.dzultra.betterfoxcraft.checker;

import net.dzultra.betterfoxcraft.selector.BlockSelector;

public class ClientTickHandler {
    public static void register() {
        ParticleTracker.getParticleTracker();
        BlockSelector.getBlockSelector();
        BlockSelector.getUseBlockCallback();
    }
}
