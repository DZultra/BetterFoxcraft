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

        // ─────────────── General ───────────────
        var general = builder.getOrCreateCategory(Text.literal("General"));

        general.addEntry(entryBuilder
                .startStrField(Text.literal("Client ID"), config.clientID)
                .setSaveConsumer(value -> config.clientID = value)
                .build());

        general.addEntry(entryBuilder
                .startStrField(Text.literal("GUI Name"), config.GUIName)
                .setSaveConsumer(value -> config.GUIName = value)
                .build());

        general.addEntry(entryBuilder
                .startStrField(Text.literal("Discord Token"), config.discordToken)
                .setSaveConsumer(value -> config.discordToken = value)
                .build());

        // ─────────────── Limits ───────────────
        var limits = builder.getOrCreateCategory(Text.literal("Limits"));

        limits.addEntry(entryBuilder
                .startIntField(Text.literal("Max Missing Blocks"), config.maxMissingBlocks)
                .setMin(0)
                .setDefaultValue(20)
                .setSaveConsumer(value -> config.maxMissingBlocks = value)
                .build());

        limits.addEntry(entryBuilder
                .startIntField(Text.literal("Max Radius"), config.maxRadius)
                .setMin(1)
                .setDefaultValue(100)
                .setSaveConsumer(value -> config.maxRadius = value)
                .build());

        limits.addEntry(entryBuilder
                .startIntField(Text.literal("Total Seconds"), config.totalSeconds)
                .setMin(1)
                .setDefaultValue(10)
                .setSaveConsumer(value -> config.totalSeconds = value)
                .build());

        limits.addEntry(entryBuilder
                .startIntField(Text.literal("Ticks Between Updates"), config.ticks_between_updates)
                .setMin(1)
                .setDefaultValue(1)
                .setSaveConsumer(value -> config.ticks_between_updates = value)
                .build());

        // ─────────────── Features ───────────────
        var features = builder.getOrCreateCategory(Text.literal("Features"));

        features.addEntry(entryBuilder
                .startBooleanToggle(Text.literal("Enable Particle Tracker"), config.enableParticleTracker)
                .setDefaultValue(true)
                .setSaveConsumer(value -> config.enableParticleTracker = value)
                .build());

        features.addEntry(entryBuilder
                .startBooleanToggle(Text.literal("Enable Block Selector"), config.enableBlockSelector)
                .setDefaultValue(false)
                .setSaveConsumer(value -> config.enableBlockSelector = value)
                .build());

        features.addEntry(entryBuilder
                .startBooleanToggle(Text.literal("Enable Spectator Tracer"), config.enableSpectatorTracer)
                .setDefaultValue(true)
                .setSaveConsumer(value -> config.enableSpectatorTracer = value)
                .build());

        // ─────────────── Rendering ───────────────
        var rendering = builder.getOrCreateCategory(Text.literal("Rendering"));

        rendering.addEntry(entryBuilder
                .startDoubleField(Text.literal("Box Offset"), config.boxOffset)
                .setMin(0.0)
                .setMax(1.0)
                .setDefaultValue(0.05)
                .setSaveConsumer(value -> config.boxOffset = value)
                .build());

        return builder.build();
    }
}
