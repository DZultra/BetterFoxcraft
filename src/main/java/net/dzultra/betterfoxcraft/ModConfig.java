package net.dzultra.betterfoxcraft;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;

@Config(name = BetterFoxcraft.MOD_ID)
public class ModConfig implements ConfigData {
    public Integer punishmentsPerPage = 10;
}