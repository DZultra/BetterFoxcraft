package net.dzultra.betterfoxcraft;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public class ConfigScreenFactory {

    public static Screen create(Screen parent) {
        ModConfig config = AutoConfig.getConfigHolder(ModConfig.class).getConfig();

        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(Text.literal("BetterFoxcraft Settings"))
                .setSavingRunnable(() ->
                        AutoConfig.getConfigHolder(ModConfig.class).save()
                );

        ConfigEntryBuilder entryBuilder = builder.entryBuilder();

        // ─────────────── General ───────────────-
        var general = builder.getOrCreateCategory(Text.literal("General"));

        general.addEntry(entryBuilder
                .startIntField(Text.literal("Punishments per Page"), config.punishmentsPerPage)
                .setSaveConsumer(value -> config.punishmentsPerPage = value)
                .build());

        return builder.build();
    }
}
