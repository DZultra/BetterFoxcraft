package net.dzultra.betterfoxcraft.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.dzultra.betterfoxcraft.BetterFoxcraft;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.*;
import net.minecraft.util.Formatting;

import java.util.HashMap;
import java.util.Map;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;

public class DurationCommand {
    public static final HashMap<String, String> DURA_MAP = new HashMap<>();
    static {
        DURA_MAP.put("","");
        DURA_MAP.put("spams","| 1: Warn |\n| 2: 15m Mute |\n| 3: 30m Mute |\n| 4: 1h Mute |\n| 5: 2h Mute |\n| 6: 6h Mute |\n| 7: 12h Mute |\n| 8: 1d Mute |\n| 9: 1w Mute |\n");
        DURA_MAP.put("caps","| 1: Verbal Warn |\n| 2: Warn |\n| 3: 5m Mute |\n| Consecutive: +10m Mute |\n");
        DURA_MAP.put("staff_impersonation","| 1: Warn |\n| 2: 10m Mute |\n| 3: 30m Mute |\n| 4: 1h Mute |\n| 5: Report + @Admin |\n");
        DURA_MAP.put("inappropriate_content","| 1: 15m Mute |\n| 2: 30m Mute |\n| 3: 1h Mute |\n| 4: 6h Mute |\n| 5: 12h Mute |\n| 6: 1d Mute |\n| 7: 3d Mute |\n| 8: Perm Mute |\n");
        DURA_MAP.put("inappropriate_username","| 1: 30d Ban -s |\n| 2: Perm Ban -s |\n");
        DURA_MAP.put("inappropriate_builds","| 1: 30m Ban |\n| 2: 6h Ban |\n| 3: 1d Ban |\n| 4: 3d Ban |\n| 5: 7d Ban |\n");
        DURA_MAP.put("inappropriate_names","| 1: Warn |\n| 2: 1h Ban |\n| 3: 6h Ban |\n| 4: 1d Ban |\n| 5: Report + @CM |\n");
        DURA_MAP.put("inappropriate_skins","| 1: Verbal Warn |\n| 2: Kick |\n| 3: 1h Ban |\n| 4: 6h Ban |\n| 5: 1d Ban |\n| 6: 3d Ban |\n| 7: Perm Ban |\n");
        DURA_MAP.put("disrespecting","| 1: 1h Mute |\n| 2: 3h Mute |\n| 3: 6h Mute |\n| 4: 1d Mute |\n 5: 7d Mute |\n| 6: Perm Mute |\n");
        DURA_MAP.put("player_harassment","| 1: 1h Mute |\n| 2: 4h Mute |\n| 3: 6h Mute |\n| 4: 12h Mute |\n| 5: 1d Mute |\n| 6: Report + @CM\n");
        DURA_MAP.put("staff_disrespect","| 1: 1h Mute |\n| 2: 4h Mute |\n| 3: 6h Mute |\n| 4: 12h Mute |\n| 5: 1d Mute |\n| 6: Report + @CM\n");
        DURA_MAP.put("fascist_propaganda","| 1: 1h Mute |\n| 2: 6h Mute |\n| 3: 3d Ban |\n| 4: Perm Ban |\n");
        DURA_MAP.put("death_threats","| 1: 1h Mute |\n| 2: 6h Mute |\n| 3: 12h Mute |\n| 4: Perm Mute |\n");
        DURA_MAP.put("death_wishes","| 1: 1h Mute |\n| 2: 6h Mute |\n| 3: 12h Mute |\n| 4: Perm Mute |\n");
        DURA_MAP.put("encouraging_self_harm","| 1: 1h Mute |\n| 2: 6h Mute |\n| 3: 12h Mute |\n| 4: Perm Mute |\n");
        DURA_MAP.put("suicidal_encouraging","| 1: 1h Mute |\n| 2: 6h Mute |\n| 3: 12h Mute |\n| 4: Perm Mute |\n");
        DURA_MAP.put("combat_glitching","| 1: Warn |\n| 2: 30m Ban |\n| 3: 1h Ban |\n| 4: 2h Ban |\n| 5: 6h Ban |\n| 6: 12h Ban |\n| 7: 1d Ban |\n| 8: 1w Ban |\n");
        DURA_MAP.put("claim_exploiting","| 1: Warn |\n| 2: 30m Ban |\n| 3: 1h Ban |\n| 4: 2h Ban |\n| 5: 6h Ban |\n| 6: 12h Ban |\n| 7: 1d Ban |\n| 8: 1w Ban |\n");
        DURA_MAP.put("player_trapping","| 1: 7d Ban |\n| 2: 14d Ban |\n| 3: 30d Ban |\n| 4: Perm Ban |\n");
        DURA_MAP.put("discrimination","| 1: 1h Mute |\n| 2: 3h Mute |\n| 3: 6h Mute |\n| 4: 1d Mute |\n| 5: 7d Mute |\n| 6: Perm Mute |\n");
        DURA_MAP.put("irl_trading","| 1: Warn |\n| 2: 1h Ban |\n| 3: 2h Ban |\n| 4: 6h Ban |\n| 5: 1d Ban |\n| 6: Perm Ban |\n");
        DURA_MAP.put("scamming","| 1: 3d Ban |\n| 2: 7d Ban |\n| 3: 14d Ban |\n| 4: 30d Ban |\n| 5: Perm Ban |\n");
        DURA_MAP.put("minor_griefing","| 1: 2d Ban |\n| 2: 5d Ban |\n| 3: 10d Ban |\n| 4: 20d Ban |\n| 5: 30d Ban |\n| 6: Perm Ban |\n");
        DURA_MAP.put("major_griefing","| 1: 7d Ban |\n| 2: 14d Ban |\n| 3: 30d Ban |\n| 4: Perm Ban |\n");
        DURA_MAP.put("mute_evasion","| 1: Ban equal to Mute Duration |\n| 2: Ban double to Mute Duration |\n| 3: Perm Ban |\n| 4: Report + @Sr.Mod |\n");
        DURA_MAP.put("ban_evasion","| 1: Ban equal to Ban Duration |\n| 2: Ban double to Ban Duration |\n| 3: Perm Ban |\n| 4: Report + @Sr.Mod |\n");
        DURA_MAP.put("mute_luring","| 1: 30m Mute |\n| 2: 1h Mute |\n| 3: 6h Mute |\n| 4: 1d Ban |\n| 5: 3d ban |\n| 6: Report + @Admin |\n");
        DURA_MAP.put("ban_luring","| 1: 30m Ban |\n| 2: 1h Ban |\n| 3: 6h Ban |\n| 4: 1d Ban |\n 5:| 3d Ban |\n| 6: Report + @Admin |\n");
        DURA_MAP.put("afk_smth","| 1: Kick |\n| 2: 6h Ban |\n| 3: 1d Ban |\n| 4: 7d Ban |\n| 5: 30d Ban |\n");
        DURA_MAP.put("map_exploiting","| 1: Verbal Warn + /spawn + Remove Access |\n| 2: Kick + /spawn + Remove Access |\n| 3: Report + @Admin |\n| 4: Report + @Admin |\n");
        DURA_MAP.put("hacked_client","| 1: Perm Ban |\n");
        DURA_MAP.put("x-ray","| 1: 7d Ban |\n| 2: 14d Ban |\n| 3: 30d Ban |\n| 4: Perm Ban |\n");
        DURA_MAP.put("autoclicker","| 1: 7d Ban |\n| 2: 14d Ban |\n| 3: 30d Ban |\n| 4: Perm Ban |\n");
        DURA_MAP.put("doxxing","| 1: Perm Ban |\n");
        DURA_MAP.put("ddossing","| 1: Perm Ban |\n");
        DURA_MAP.put("extreme_toxicity","| 1: Report + @Sr.Mod |\n");
        DURA_MAP.put("exploiting_glitches","| 1: 72h Ban -s |\n");
    }

    public static LiteralArgumentBuilder<FabricClientCommandSource> getCommand() {
        var durationCommand = literal("dura").executes(context -> {
            MinecraftClient.getInstance().player.sendMessage(Text.literal("\n|- No specific duration has been chosen -|\n")
                    .setStyle(Style.EMPTY.withColor(Formatting.RED).withBold(true)), false);
            return 0;
        });

        DURA_MAP.keySet().forEach(k -> {
            durationCommand.then(explanations(k, DURA_MAP));
        });

        return durationCommand;
    }

    public static LiteralArgumentBuilder<FabricClientCommandSource> explanations(String durationKey, Map<String, String> durations) {
        return literal(durationKey).executes(context -> {
            String duration = durations.getOrDefault(durationKey, "No duration found");
            MutableText durationMessage = Text.literal("\n|- The Duration for " + durationKey + " -|\n" + duration);
            if(!durations.containsKey(durationKey)) {
                BetterFoxcraft.LOGGER.info("Could not register Duration: {} No duration found", durationKey);
                durationMessage = Text.literal("\n|- Could not find duration -|\n");
            }
            MinecraftClient.getInstance().player.sendMessage(durationMessage
                    .setStyle(Style.EMPTY.withColor(Formatting.GOLD)), false);
            return 0;
        });
    }
}
