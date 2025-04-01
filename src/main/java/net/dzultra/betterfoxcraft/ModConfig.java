package net.dzultra.betterfoxcraft;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;

@Config(name = BetterFoxcraft.MOD_ID)
public class ModConfig implements ConfigData {
    public String clientID = "";
    public Integer maxMissingBlocks = 20;
    public Integer maxRadius = 100;
}