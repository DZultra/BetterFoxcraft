package net.dzultra.betterfoxcraft.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.ArrayList;
import java.util.List;

public class IslandChatCommand {
    public static LiteralArgumentBuilder<FabricClientCommandSource> getCommand() {
        return ClientCommandManager.literal("isc")
                .then(ClientCommandManager.argument("text", StringArgumentType.greedyString())
                        .executes(context -> {
                            String text = StringArgumentType.getString(context, "text");
                            try {
                                MinecraftClient client = MinecraftClient.getInstance();

                                client.getNetworkHandler().sendChatCommand("is chat");

                                addDelayedTask(() -> {
                                    client.player.networkHandler.sendChatMessage(text);

                                    addDelayedTask(() -> client.getNetworkHandler().sendChatCommand("is chat"), 10);
                                }, 10);
                            } catch (IllegalArgumentException e) {
                                System.out.println(e.getMessage());
                                MinecraftClient.getInstance().player.sendMessage(Text.literal("Wrong Argument: Please use provide a Text to send!").setStyle(Style.EMPTY
                                                .withColor(Formatting.RED)
                                                .withBold(true))
                                        , false);
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
