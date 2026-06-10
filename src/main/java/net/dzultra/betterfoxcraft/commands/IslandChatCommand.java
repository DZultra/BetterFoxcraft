package net.dzultra.betterfoxcraft.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.fabricmc.fabric.api.client.command.v2.ClientCommands;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import java.util.ArrayList;
import java.util.List;

public class IslandChatCommand {
    public static LiteralArgumentBuilder<FabricClientCommandSource> getCommand() {
        return ClientCommands.literal("ilsc")
                .then(ClientCommands.argument("text", StringArgumentType.greedyString())
                        .executes(context -> {
                            String text = StringArgumentType.getString(context, "text");
                            try {
                                Minecraft client = Minecraft.getInstance();

                                client.getConnection().sendCommand("is chat");

                                addDelayedTask(() -> {
                                    client.player.connection.sendChat(text);

                                    addDelayedTask(() -> client.getConnection().sendCommand("is chat"), 10);
                                }, 10);
                            } catch (IllegalArgumentException e) {
                                System.out.println(e.getMessage());
                                Minecraft.getInstance().player.sendSystemMessage(Component.literal("Wrong Argument: Please use provide a Text to send!").setStyle(Style.EMPTY
                                                .withColor(ChatFormatting.RED)
                                                .withBold(true))
                                        );
                            }
                            return 1;
                        })
                );
    }

    private static final List<DelayedTask> delayedTasks = new ArrayList<>();

    private static void addDelayedTask(Runnable task, int delayTicks) {
        delayedTasks.add(new DelayedTask(task, delayTicks));
    }

    static {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            List<DelayedTask> tasksCopy = new ArrayList<>(delayedTasks);

            for (DelayedTask delayedTask : tasksCopy) {
                delayedTask.ticksLeft--;
                if (delayedTask.ticksLeft <= 0) {
                    delayedTask.task.run();
                    delayedTasks.remove(delayedTask);
                }
            }
        });
    }

    private static class DelayedTask {
        private final Runnable task;
        private int ticksLeft;

        public DelayedTask(Runnable task, int ticksLeft) {
            this.task = task;
            this.ticksLeft = ticksLeft;
        }
    }
}
