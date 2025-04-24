package net.dzultra.betterfoxcraft.discord;

import me.shedaniel.autoconfig.AutoConfig;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import net.dzultra.betterfoxcraft.ModConfig;

public class DiscordBotManager {
//    private static JDA jda;
//
//    public static void start() {
//        try {
//            jda = JDABuilder.createDefault(AutoConfig.getConfigHolder(ModConfig.class).getConfig().discordToken)
//                    .addEventListeners(new DiscordSlashCommandHandler())
//                    .build();
//
//            jda.awaitReady();
//
//            SlashCommandData userCommand = Commands.slash("report", "Report a punishment")
//                    .setGuildOnly(false) // Allow use in DMs
//                    .setDefaultPermissions(DefaultMemberPermissions.ENABLED);
//
//            jda.updateCommands().addCommands(
//                userCommand
//            ).queue();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    public static JDA getJda() {
//        return jda;
//    }
}

