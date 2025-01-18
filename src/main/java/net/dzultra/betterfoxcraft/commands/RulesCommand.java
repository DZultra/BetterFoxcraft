package net.dzultra.betterfoxcraft.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.dzultra.betterfoxcraft.BetterFoxcraft;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.HashMap;
import java.util.Map;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;

public class RulesCommand {
    public static final HashMap<String, String> RULES_MAP = new HashMap<>();
    static {
        RULES_MAP.put("extreme_toxicity","This punishment is given if a player receives 4 mutes longer than 30 minutes in a week. This punishment is limited to toxicity punishments.");
        RULES_MAP.put("hacked_client","Using hacks/illegal software on the server is considered hacked client. This includes, but is not limited to; any form of hacking, HP indicators, baritone, macro's, minimaps with entities, printer modifications,  ...");
        RULES_MAP.put("advertisment","Advertising is not permitted. This includes, but is not limited to sharing other server IP's or scam websites, sending malicious links, ...");
        RULES_MAP.put("doxxing_threats","Doxxing is when a player shares someone's personal information without permission. This is strictly forbidden and will lead to a permanent ban. Threatening to share this information also falls under this category.");
        RULES_MAP.put("ban_evasion","Ban evasion is when you log in on another account while having a banned account on your IP. This includes, but is not limited to; using alt accounts, chatting in the synced discord channels, ...");
        RULES_MAP.put("mute_evasion","Mute evasion is when a player finds a way to avoid a mute. This includes, but is not limited to; talking on signs, chatting in the synced discord channels, ...");
        RULES_MAP.put("xray","The use of X-Ray, including the use of X-Ray resource packs, is not allowed.");
        RULES_MAP.put("griefing","Griefing is when a player finds a way around the server's protection barriers and destroys another player's work. Griefing can also include griefing (close to) someone's claim. Griefing is allowed on kingdoms on the world Olympus");
        RULES_MAP.put("mute_luring","Luring players into doing mutable actions is considered mute luring");
        RULES_MAP.put("ban_luring","Luring players into doing bannable actions is considered ban luring, this includes asking for a server IP");
        RULES_MAP.put("in_game_scamming","Any form of scamming is not allowed on the server, with the exception of the Kingdoms gamemode, where scamming is permitted due to the nature of the game. This includes, but is not limited to;  /ah scams, chest shop scams, trade scams, ...");
        RULES_MAP.put("autoclicker","Autoclicker stands for automatic clicking. This is prohibited and will lead to a ban.");
        RULES_MAP.put("trapping","Trapping players is not allowed. Trapping includes, but is not limited to; TP-Trapping, Portal trapping, Using explosive equipment to trap/kill a player, ...");
        RULES_MAP.put("fascist_propaganda","Fascist propaganda is any action that promotes fascist or anti-sematic ideologies. This includes but it not limited to fascist expressions in chat or builds that portray fascist actions or symbols.");
        RULES_MAP.put("encouraging_self_harm","Encouraging Self-Harm is encouraging a player to hurt themselves IRL.");
        RULES_MAP.put("death_threats","Death threats is threatening to kill a player IRL. ");
        RULES_MAP.put("death_wishes","Death wishing is to wish/hope/pray for someone to die IRL.");
        RULES_MAP.put("suicidal_encouragement","Suicidal encouragement is encouraging players to commit/attempt suicide IRL. ");
        RULES_MAP.put("discrimination","Slurs or comments that discriminate against individuals or groups based on their identity. this includes but is not limited to factors such as race, sex, ...");
        RULES_MAP.put("disrespecting_illnesses","disrespecting or referring to illnesses in a derogatory manner is not allowed.");
        RULES_MAP.put("disrespecting_disabilities","Disrespecting or referring to disabilities in a derogatory manner is not allowed.");
        RULES_MAP.put("player_harassment","Harassing players is not allowed and is considered player harassment. ");
        RULES_MAP.put("staff_disrespect","Disrespecting staff members is not allowed.");
        RULES_MAP.put("afk_auto_walk","AFK Auto-Walk is when you automatically walk while being AFK. ");
        RULES_MAP.put("afk_selling","AFK Selling is when you automatically sell your items to /shop. ");
        RULES_MAP.put("afk_farming","AFK Farming is when you automatically fish, kill entities or mine blocks. (Note: using autoclickers will lead to a different punishment)");
        RULES_MAP.put("map_exploiting","Map exploiting is getting into forbidden locations. This includes, but is not limited to; walking on barrier blocks, glitching under spawn, getting into a disabled dimension, ... ");
        RULES_MAP.put("offering_irl_trades", "IRL Trading is when you sell ingame goods for IRL currency. Selling ingame goods for /buy goods is allowed.");
        RULES_MAP.put("claim_exploiting","Claim exploiting is using glitches/bugs in a claim. This includes, but is not limited to; glitching through walls, dodging claim bans, ...");
        RULES_MAP.put("combat_glitching", "Combat glitching is the use of commands/glitches in the PVP zone. This includes, but is not limited to; /disguise, /speed, tridents in combiation with /pweather, sitting on a player's head, Using elytras during combat, /sit, using pets in pvp, ...");
        RULES_MAP.put("staff_impersonation","Staff impersonation is when a player nicknames themselves similair to a staff member. ");
        RULES_MAP.put("inappropriate_content", "Inappropriate Content is sending chat messages that are considered not family-friendly.");
        RULES_MAP.put("death_message_spam", "Dead message spam is the intentional spamming of dead messages. This punishment is issued in cases where a player dies more than 8 times intentionally within 60 seconds.");
        RULES_MAP.put("msg_spam","Spamming players in /tell and /r is considered message spam. Staff members punish only in cases where this was reported in a player report.");
        RULES_MAP.put("character_spam","Character spam is spamming 25 or more characters in 1 line of chat.");
        RULES_MAP.put("command_spam","Command spam is spamming a command 4 times in a short period of time. Spamming /pwarp <name> and [item] are examples of this rule.");
        RULES_MAP.put("chat_flood","Chat Flood is when a player sends 6 lines or more of consecutive chat messages, each containing a maximum of 2 words. ");
        RULES_MAP.put("spam", "Sending the same message three times or more in a short period is considered spam. ");
        RULES_MAP.put("excessive_caps", "Sending multiple consecutive lines with multiple words in all capital letters is considered excessive caps. Occasional use of capitalization is allowed.");
    }

    public static LiteralArgumentBuilder<FabricClientCommandSource> getCommand() {
        var answerCommand = literal("rule").executes(context -> {
            MinecraftClient.getInstance().player.sendMessage(Text.literal("\n|- No specific answer has been chosen -|\n")
                    .setStyle(Style.EMPTY.withColor(Formatting.RED).withBold(true)), false);
            return 0;
        });

        RULES_MAP.keySet().forEach(k -> {
            answerCommand.then(explanations(k, RULES_MAP));
        });

        return answerCommand;
    }

    public static LiteralArgumentBuilder<FabricClientCommandSource> explanations(String ruleKey, Map<String, String> explanations) {
        return literal(ruleKey).executes(context -> {
            String explanationMessage = "\n|- Click to get answer -|\n";
            if(!explanations.containsKey(ruleKey)) {
                BetterFoxcraft.LOGGER.info("could not register FAQ: {} no answer found", ruleKey);
                explanationMessage = "\n|- Could not find answer -|\n";
            }
            String answer = explanations.getOrDefault(ruleKey, "No answer found");
            MinecraftClient.getInstance().player.sendMessage(Text.literal(explanationMessage)
                    .setStyle(Style.EMPTY.withClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, answer))
                            .withColor(Formatting.GOLD)
                            .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,Text.literal("Copy")))
                    ), false);
            return 0;
        });
    }
}