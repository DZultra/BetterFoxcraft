package net.dzultra.betterfoxcraft;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class ConfigScreenFactory {

    public static Screen create(Screen parent) {
        ModConfig config = AutoConfig.getConfigHolder(ModConfig.class).getConfig();

        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(Component.literal("BetterFoxcraft Settings"))
                .setSavingRunnable(() ->
                        AutoConfig.getConfigHolder(ModConfig.class).save()
                );

        ConfigEntryBuilder entryBuilder = builder.entryBuilder();

        // ─────────────── General ───────────────-
        var general = builder.getOrCreateCategory(Component.literal("General"));

        general.addEntry(entryBuilder
                .startStrField(Component.literal("Client ID"), config.clientID)
                .setSaveConsumer(value -> config.clientID = value)
                .build());

        general.addEntry(entryBuilder
                .startStrField(Component.literal("GUI Name"), config.GUIName)
                .setSaveConsumer(value -> config.GUIName = value)
                .build());

        general.addEntry(entryBuilder
                .startStrField(Component.literal("Discord Token"), config.discordToken)
                .setSaveConsumer(value -> config.discordToken = value)
                .build());

        general.addEntry(entryBuilder
                .startIntField(Component.literal("Punishments per Page"), config.punishmentsPerPage)
                .setSaveConsumer(value -> config.punishmentsPerPage = value)
                .build());

        // ─────────────── Limits ───────────────
        var limits = builder.getOrCreateCategory(Component.literal("Limits"));

        limits.addEntry(entryBuilder
                .startIntField(Component.literal("Max Missing Blocks"), config.maxMissingBlocks)
                .setMin(0)
                .setDefaultValue(20)
                .setSaveConsumer(value -> config.maxMissingBlocks = value)
                .build());

        limits.addEntry(entryBuilder
                .startIntField(Component.literal("Max Radius"), config.maxRadius)
                .setMin(1)
                .setDefaultValue(100)
                .setSaveConsumer(value -> config.maxRadius = value)
                .build());

        limits.addEntry(entryBuilder
                .startIntField(Component.literal("Total Seconds"), config.totalSeconds)
                .setMin(1)
                .setDefaultValue(10)
                .setSaveConsumer(value -> config.totalSeconds = value)
                .build());

        limits.addEntry(entryBuilder
                .startIntField(Component.literal("Ticks Between Updates"), config.ticks_between_updates)
                .setMin(1)
                .setDefaultValue(1)
                .setSaveConsumer(value -> config.ticks_between_updates = value)
                .build());

        // ─────────────── Features ───────────────
        var features = builder.getOrCreateCategory(Component.literal("Features"));

        features.addEntry(entryBuilder
                .startBooleanToggle(Component.literal("Enable Particle Tracker"), config.enableParticleTracker)
                .setDefaultValue(true)
                .setSaveConsumer(value -> config.enableParticleTracker = value)
                .build());

        features.addEntry(entryBuilder
                .startBooleanToggle(Component.literal("Enable Block Selector"), config.enableBlockSelector)
                .setDefaultValue(false)
                .setSaveConsumer(value -> config.enableBlockSelector = value)
                .build());

        features.addEntry(entryBuilder
                .startBooleanToggle(Component.literal("Enable Spectator Tracer"), config.enableSpectatorTracer)
                .setDefaultValue(true)
                .setSaveConsumer(value -> config.enableSpectatorTracer = value)
                .build());

        // ─────────────── Rendering ───────────────
        var rendering = builder.getOrCreateCategory(Component.literal("Rendering"));

        rendering.addEntry(entryBuilder
                .startDoubleField(Component.literal("Box Offset"), config.boxOffset)
                .setMin(0.0)
                .setMax(1.0)
                .setDefaultValue(0.05)
                .setSaveConsumer(value -> config.boxOffset = value)
                .build());

        return builder.build();
    }
}
