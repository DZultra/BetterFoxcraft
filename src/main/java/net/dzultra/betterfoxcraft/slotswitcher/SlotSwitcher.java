package net.dzultra.betterfoxcraft.slotswitcher;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

public class SlotSwitcher {
    private static final Deque<PendingMove> queue = new ArrayDeque<>();
    private static boolean tickRegistered = false;
    private static final int ticksBetweenMoves = 2;
    private static int cooldown = 0;

    private record PendingMove(AbstractContainerMenu handler, int fromIndex, int toIndex) {}

    private static void ensureTickRegistered() {
        if (tickRegistered) return;
        tickRegistered = true;

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (queue.isEmpty()) return;

            // if player closed screen or not in a HandledScreen -> cancel pending moves
            if (!(client.gui.screen() instanceof AbstractContainerScreen<?> hs)) {
                queue.clear();
                cooldown = 0;
                return;
            }

            AbstractContainerMenu currentHandler = hs.getMenu();

            if (cooldown > 0) {
                cooldown--;
                return;
            }

            PendingMove next = queue.peekFirst();
            if (next == null) return;

            // if the handler changed since scheduling, abort all (prevents desync)
            if (next.handler != currentHandler) {
                queue.clear();
                cooldown = 0;
                return;
            }

            queue.pollFirst();
            if (next.fromIndex >= 0 && next.fromIndex < currentHandler.slots.size()
                    && next.toIndex >= 0 && next.toIndex < currentHandler.slots.size()) {

                Slot from = currentHandler.slots.get(next.fromIndex);
                Slot to = currentHandler.slots.get(next.toIndex);

                if (from.hasItem()) {
                    doClickMove(client, currentHandler.containerId, from.index, to.index);
                }
            }

            cooldown = ticksBetweenMoves;
        });
    }

    public static void scheduleConfiguredMoves(Minecraft client, AbstractContainerMenu handler) {
        int playerInventoryStart = handler.slots.size() - 36;
        int slotIndex = playerInventoryStart + 35; // slot 35 in player inventory

        if (slotIndex >= 0 && slotIndex < handler.slots.size()) {
            Slot slot = handler.slots.get(slotIndex);
            if (slot.hasItem()) {
                ItemStack stack = slot.getItem();
                if (stack.getItem() == Items.NETHERITE_BOOTS) { // Switch to PvP Inv
                    buildQueueEntries(SlotSwitcherRules.rules1, handler);
                } else if (stack.getItem() == Items.WIND_CHARGE) { // Switch to Casual Inv
                    buildQueueEntries(SlotSwitcherRules.rules2, handler);
                }
            }
        }
    }

    private static void buildQueueEntries(List<SlotSwitcherRules.SlotMoveRule> rules, AbstractContainerMenu handler) {
        // Build the queue entries from the rules
        for (SlotSwitcherRules.SlotMoveRule rule : rules) {
            int fromIndex = getSlotIndex(handler, rule.gui1(), rule.fromSlot());
            int toIndex = getSlotIndex(handler, rule.gui2(), rule.toSlot());

            if (fromIndex >= 0 && toIndex >= 0) {
                queue.addLast(new PendingMove(handler, fromIndex, toIndex));
            }
        }

        ensureTickRegistered();
    }

    private static int getSlotIndex(AbstractContainerMenu handler, boolean isGui, int logicalIndex) {
        int containerSize = handler.slots.size() - 36;
        if (isGui) {
            return logicalIndex;
        } else {
            return containerSize + logicalIndex;
        }
    }

    private static void doClickMove(Minecraft client, int syncId, int fromSlotId, int toSlotId) {
        client.gameMode.handleContainerInput(syncId, fromSlotId, 0, ContainerInput.PICKUP, client.player);
        client.gameMode.handleContainerInput(syncId, toSlotId, 0, ContainerInput.PICKUP, client.player);
    }
}
