package net.dzultra.betterfoxcraft.slotswitcher;

import java.util.List;

public class SlotSwitcherRules {
    private static final boolean INV = false;
    private static final boolean GUI = true;

    public record SlotMoveRule(int fromSlot, boolean gui1, int toSlot, boolean gui2) {}

    public static final List<SlotMoveRule> rules1 = List.of(
            new SlotMoveRule(26, GUI, 9, INV), // PvP Helmet
            new SlotMoveRule(35, GUI, 10, INV), // PvP Chestplate
            new SlotMoveRule(44, GUI, 11, INV), // PvP Leggings
            new SlotMoveRule(53, GUI, 12, INV), // PvP Boots

            new SlotMoveRule(32, INV, 26, GUI), // Casual Helmet
            new SlotMoveRule(33, INV, 35, GUI), // Casual Chestplate
            new SlotMoveRule(34, INV, 44, GUI), // Casual Leggings
            new SlotMoveRule(35, INV, 53, GUI), // Casual Boots

            new SlotMoveRule(27, GUI, 4, INV), // Milk Bucket 1
            new SlotMoveRule(36, GUI, 5, INV), // Milk Bucket 2
            new SlotMoveRule(45, GUI, 6, INV), // Milk Bucket 3

            new SlotMoveRule(28, GUI, 14, INV), // Strength Pot 1
            new SlotMoveRule(37, GUI, 23, INV), // Strength Pot 2
            new SlotMoveRule(46, GUI, 32, INV), // Strength Pot 3

            new SlotMoveRule(27, INV, 45, GUI), // Casual Sword
            new SlotMoveRule(28, INV, 36, GUI), // Casual Pickaxe
            new SlotMoveRule(29, INV, 27, GUI), // Casual Axe
            new SlotMoveRule(30, INV, 28, GUI), // Casual Shovel
            new SlotMoveRule(31, INV, 37, GUI), // Casual Hoe


            new SlotMoveRule(0, GUI, 7, INV), // Dmg Arrow
            new SlotMoveRule(9, GUI, 8, INV), // Slow Falling Arrow

            new SlotMoveRule(0, INV, 4, GUI), // Water Bucket
            new SlotMoveRule(1, INV, 46, GUI), // Steak
            new SlotMoveRule(2, INV, 51, GUI), // Trident
            new SlotMoveRule(3, INV, 13, GUI), // Glass Pane

            new SlotMoveRule(22, GUI, 0, INV), // Secondary Helmet
            new SlotMoveRule(31, GUI, 1, INV), // Secondary Chestplate
            new SlotMoveRule(40, GUI, 2, INV), // Secondary Leggings
            new SlotMoveRule(49, GUI, 3, INV), // Secondary Boots

            new SlotMoveRule(52, GUI, 27, INV), // PvP Sword
            new SlotMoveRule(15, GUI, 28, INV), // Mace
            new SlotMoveRule(6, GUI, 29, INV), // Elytra
            new SlotMoveRule(42, GUI, 30, INV), // Bow
            new SlotMoveRule(14, GUI, 31, INV), // Gaps
            new SlotMoveRule(41, GUI, 35, INV) // Wind Charge

    );

    public static final List<SlotMoveRule> rules2 = List.of(
            new SlotMoveRule(35, INV, 41, GUI), // Wind Charge
            new SlotMoveRule(31, INV, 14, GUI), // Gaps
            new SlotMoveRule(30, INV, 42, GUI), // Bow
            new SlotMoveRule(29, INV, 6, GUI),  // Elytra
            new SlotMoveRule(28, INV, 15, GUI), // Mace
            new SlotMoveRule(27, INV, 52, GUI), // PvP Sword

            new SlotMoveRule(3, INV, 49, GUI),  // Secondary Boots
            new SlotMoveRule(2, INV, 40, GUI),  // Secondary Leggings
            new SlotMoveRule(1, INV, 31, GUI),  // Secondary Chestplate
            new SlotMoveRule(0, INV, 22, GUI),  // Secondary Helmet

            new SlotMoveRule(13, GUI, 3, INV),  // Glass Pane
            new SlotMoveRule(51, GUI, 2, INV),  // Trident
            new SlotMoveRule(46, GUI, 1, INV),  // Steak
            new SlotMoveRule(4, GUI, 0, INV),   // Water Bucket

            new SlotMoveRule(8, INV, 9, GUI),   // Slow Falling Arrow
            new SlotMoveRule(7, INV, 0, GUI),   // Dmg Arrow

            new SlotMoveRule(37, GUI, 31, INV), // Casual Hoe
            new SlotMoveRule(28, GUI, 30, INV), // Casual Shovel
            new SlotMoveRule(27, GUI, 29, INV), // Casual Axe
            new SlotMoveRule(36, GUI, 28, INV), // Casual Pickaxe
            new SlotMoveRule(45, GUI, 27, INV), // Casual Sword

            new SlotMoveRule(32, INV, 46, GUI), // Strength Pot 3
            new SlotMoveRule(23, INV, 37, GUI), // Strength Pot 2
            new SlotMoveRule(14, INV, 28, GUI), // Strength Pot 1

            new SlotMoveRule(6, INV, 45, GUI),  // Milk Bucket 3
            new SlotMoveRule(5, INV, 36, GUI),  // Milk Bucket 2
            new SlotMoveRule(4, INV, 27, GUI),  // Milk Bucket 1

            new SlotMoveRule(53, GUI, 35, INV), // Casual Boots
            new SlotMoveRule(44, GUI, 34, INV), // Casual Leggings
            new SlotMoveRule(35, GUI, 33, INV), // Casual Chestplate
            new SlotMoveRule(26, GUI, 32, INV), // Casual Helmet

            new SlotMoveRule(12, INV, 53, GUI), // PvP Boots
            new SlotMoveRule(11, INV, 44, GUI), // PvP Leggings
            new SlotMoveRule(10, INV, 35, GUI), // PvP Chestplate
            new SlotMoveRule(9, INV, 26, GUI)   // PvP Helmet
    );
}
