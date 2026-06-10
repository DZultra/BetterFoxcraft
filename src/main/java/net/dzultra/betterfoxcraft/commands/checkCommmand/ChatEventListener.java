package net.dzultra.betterfoxcraft.commands.checkCommmand;

import java.util.regex.Pattern;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

public class ChatEventListener {
    public boolean onGameMessage(Component message, boolean b) {
        if (CheckCommand.isExecuted()) {
            var messageMatcher = Pattern.compile("^(.+) is (.+)$").matcher(message.getString());

            if (messageMatcher.matches()) {
                String realname = messageMatcher.group(2);
                Minecraft.getInstance().getConnection().sendCommand("alts " + realname);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                Minecraft.getInstance().getConnection().sendCommand("hist " + realname);
            }
            CheckCommand.noLongerExecuted();
        }
        return true; // Sends the message into visible Chat regardless
    }
}
