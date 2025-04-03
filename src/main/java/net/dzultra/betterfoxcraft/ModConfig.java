package net.dzultra.betterfoxcraft;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;

@Config(name = BetterFoxcraft.MOD_ID)
public class ModConfig implements ConfigData {
    public String clientID = "";
    public Integer maxMissingBlocks = 20;
    public Integer maxRadius = 100;
    public Integer totalSeconds = 10;
    public Integer ticks_between_updates = 1;
    public Boolean enableParticleTracker = true;
    public Boolean enableBlockSelector = false;
    public Double boxOffset = 0.05;
}