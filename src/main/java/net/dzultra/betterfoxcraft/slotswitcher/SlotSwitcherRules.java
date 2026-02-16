package net.dzultra.betterfoxcraft.slotswitcher;

import java.util.List;

public class SlotSwitcherRules {
    private static final boolean INV = false;
    private static final boolean GUI = true;

    public record SlotMoveRule(int fromSlot, boolean gui1, int toSlot, boolean gui2) {}

    public static final List<SlotMoveRule> rules1 = List.of(
            new SlotMoveRule(0, GUI, 28, INV), // Axe
            new SlotMoveRule(1, GUI, 29, INV), // Bow
            new SlotMoveRule(2, GUI, 30, INV), // Fishing Rod
            new SlotMoveRule(3, GUI, 31, INV), // Boots
            new SlotMoveRule(4, GUI, 33, INV), // Mace
            new SlotMoveRule(5, GUI, 35, INV), // Gaps 1
            new SlotMoveRule(6, GUI, 26, INV), // Gaps 2
            new SlotMoveRule(7, GUI, 17, INV), // Totem 1
            new SlotMoveRule(8, GUI, 8, INV), // Totem 2
            new SlotMoveRule(9, GUI, 32, INV), // Wind Charge 1
            new SlotMoveRule(10, GUI, 23, INV), // Wind Charge 2
            new SlotMoveRule(11, GUI, 14, INV), // Wind Charge 3
            new SlotMoveRule(12, GUI, 5, INV), // Wind Charge 4
            new SlotMoveRule(13, GUI, 0, INV), // Arrows
            new SlotMoveRule(14, GUI, 9, INV), // Slow Falling Arrow 1
            new SlotMoveRule(15, GUI, 18, INV), // Slow Falling Arrow 2
            new SlotMoveRule(16, GUI, 34, INV), // XP Bottles
            new SlotMoveRule(17, GUI, 25, INV), // XP Bottles
            new SlotMoveRule(18, GUI, 16, INV), // XP Bottles
            new SlotMoveRule(19, GUI, 7, INV), // XP Bottles
            new SlotMoveRule(20, GUI, 6, INV), // XP Bottles
            new SlotMoveRule(21, GUI, 15, INV), // XP Bottles
            new SlotMoveRule(22, GUI, 24, INV), // XP Bottles
            new SlotMoveRule(23, GUI, 10, INV), // XP Bottles
            new SlotMoveRule(24, GUI, 11, INV), // XP Bottles
            new SlotMoveRule(25, GUI, 12, INV), // XP Bottles
            new SlotMoveRule(26, GUI, 13, INV), // XP Bottles
            new SlotMoveRule(27, GUI, 19, INV), // XP Bottles
            new SlotMoveRule(28, GUI, 20, INV), // XP Bottles
            new SlotMoveRule(29, GUI, 21, INV), // XP Bottles
            new SlotMoveRule(30, GUI, 22, INV) // XP Bottles
    );

    public static final List<SlotMoveRule> rules2 = List.of(
            new SlotMoveRule(22, INV, 30, GUI), // XP Bottles
            new SlotMoveRule(21, INV, 29, GUI), // XP Bottles
            new SlotMoveRule(20, INV, 28, GUI), // XP Bottles
            new SlotMoveRule(19, INV, 27, GUI), // XP Bottles
            new SlotMoveRule(13, INV, 26, GUI), // XP Bottles
            new SlotMoveRule(12, INV, 25, GUI), // XP Bottles
            new SlotMoveRule(11, INV, 24, GUI), // XP Bottles
            new SlotMoveRule(10, INV, 23, GUI), // XP Bottles
            new SlotMoveRule(24, INV, 22, GUI), // XP Bottles
            new SlotMoveRule(15, INV, 21, GUI), // XP Bottles
            new SlotMoveRule(6, INV, 20, GUI),  // XP Bottles
            new SlotMoveRule(7, INV, 19, GUI),  // XP Bottles
            new SlotMoveRule(16, INV, 18, GUI), // XP Bottles
            new SlotMoveRule(25, INV, 17, GUI), // XP Bottles
            new SlotMoveRule(34, INV, 16, GUI), // XP Bottles
            new SlotMoveRule(18, INV, 15, GUI), // Slow Falling Arrow 2
            new SlotMoveRule(9, INV, 14, GUI),  // Slow Falling Arrow 1
            new SlotMoveRule(0, INV, 13, GUI),  // Arrows
            new SlotMoveRule(5, INV, 12, GUI),  // Wind Charge 4
            new SlotMoveRule(14, INV, 11, GUI), // Wind Charge 3
            new SlotMoveRule(23, INV, 10, GUI), // Wind Charge 2
            new SlotMoveRule(32, INV, 9, GUI),  // Wind Charge 1
            new SlotMoveRule(8, INV, 8, GUI),   // Totem 2
            new SlotMoveRule(17, INV, 7, GUI),  // Totem 1
            new SlotMoveRule(26, INV, 6, GUI),  // Gaps 2
            new SlotMoveRule(35, INV, 5, GUI),  // Gaps 1
            new SlotMoveRule(33, INV, 4, GUI),  // Mace
            new SlotMoveRule(31, INV, 3, GUI),  // Boots
            new SlotMoveRule(30, INV, 2, GUI),  // Fishing Rod
            new SlotMoveRule(29, INV, 1, GUI),  // Bow
            new SlotMoveRule(28, INV, 0, GUI)   // Axe
    );
}
