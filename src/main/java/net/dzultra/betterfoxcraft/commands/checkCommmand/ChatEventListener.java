package net.dzultra.betterfoxcraft.commands.checkCommmand;

import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

import java.util.regex.Pattern;

public class ChatEventListener {
    public boolean onGameMessage(Text message, boolean b) {
        if (CheckCommand.isExecuted()) {
            var messageMatcher = Pattern.compile("^(.+) is (.+)$").matcher(message.getString());

            if (messageMatcher.matches()) {
                String realname = messageMatcher.group(2);
                MinecraftClient.getInstance().getNetworkHandler().sendChatCommand("alts " + realname);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                MinecraftClient.getInstance().getNetworkHandler().sendChatCommand("hist " + realname);
            }
            CheckCommand.noLongerExecuted();
        }
        return true; // Sends the message into visible Chat regardless
    }
}
