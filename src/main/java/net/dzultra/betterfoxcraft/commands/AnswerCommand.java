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

public class AnswerCommand {
    public static final HashMap<String, String> ANSWER_MAP = new HashMap<>();
    static {
        ANSWER_MAP.put("how_to_get_to_island", "You can navigate to your island by running either of the following commands: [/is] | [/ob].");
        ANSWER_MAP.put("how_to_get_to_spawn", "You can navigate to spawn by running to command: [/spawn].");
        ANSWER_MAP.put("how_to_get_to_pinata", "The Pinata spawns at the PvP Area. You can navigate there by running the command: [/warp pvp].");
        ANSWER_MAP.put("how_to_go_to_nether", "You can access the Nether by building a Nether Portal on your island. Inside you won't find the normal Nether but another Oneblock which spawns nether blocks.");
        ANSWER_MAP.put("how_to_go_to_end", "You can navigate to the End Portal by running the command [/warp end]. However, you need to have one Prestige to access.");
        ANSWER_MAP.put("how_to_teleport_to_pwarp", "You can navigate to a Player Warp by running either of the following commands: [/pwarp go <name>] or [/wwarp go <name>].");
        ANSWER_MAP.put("how_to_get_to_crates", "You can navigate to crates by running the command: [/crates].");
        ANSWER_MAP.put("how_to_get_money", "You can gather Money for example by building a bamboo farm and selling the bamboo to /shop. In addition, you can do jobs in [/jobsmenu]. Also possible is starting with [/farm] a Plugin to make good starter money.");
        ANSWER_MAP.put("how_to_get_rank", "You can buy ranks on the [/store] or buy Rank Vouchers off other players for in-game money.");
        ANSWER_MAP.put("how_to_get_minions", "You can buy minions on the [/store] or buy them off other players for in-game money.");
        ANSWER_MAP.put("how_to_get_backpacks","Backpacks are obtain from Crates. However, you are always able to buy them off other player using in-game money.");
        ANSWER_MAP.put("how_to_get_spawners", "You can buy spawners on the [/store] or buy them off other players for in-game money.");
        ANSWER_MAP.put("how_to_get_title_points", "You can get Title Points from Crates or buy vouchers off other Players");
        ANSWER_MAP.put("how_to_get_ce", "You can get Custom Enchants in [/ce]. There you buy the different rarity's for different amounts of Xp. Of course, you are always able to buy Custom Enchants from other players.");
        ANSWER_MAP.put("how_to_make_pwarp", "You can create a Player Warp by placing any type of sign and writing [welcome] in the first line. Make sure to include the Brackets. In addition, in the bottom three lines you can write a description of your pwarp.");
        ANSWER_MAP.put("how_to_sell_items", "You can sell items in [/shop] or create a pwarp with chestshops. Additionally, you can put items up for auction: [/ah]");
        ANSWER_MAP.put("how_to_invite_others", "You can invite other players by running the command: [/ob team invite <name>]. Note: The Player who joins your Team will lose their entire island.");
        ANSWER_MAP.put("how_to_give_perms", "You can give a Player different Permissions on your island by first doing [/ob team trust <name>] and then second allowing the desired settings in [/ob settings].");
        ANSWER_MAP.put("how_to_like_island", "You can like or dislike an island by running the command [/like].");
        ANSWER_MAP.put("how_to_favourite_pwarp", "To save a pwarp for another time you can favourite it. You do so by running the command: [/pwarp favourite <name_of_the_owner>]. You'll find it in [/pwarp].");
        ANSWER_MAP.put("how_to_switch_phases", "The only way to switch a phase is finishing the current and advancing to the next. It may say in [/ob phases] that you can change it by click. However, that is a know bug.");
        ANSWER_MAP.put("how_to_join_dc", "You can join the Discord Server by running the command: [/discord]. Then you need to scan the QR-Code or open the link.");
        ANSWER_MAP.put("how_to_create_chestshop", "You can create a chestshop by punching a chest or barrel while holding your item you want to sell inside. For more info do [/qs help].");
        ANSWER_MAP.put("how_to_see_ce", "You can access a list of all Custom Enchantments by running the command [/enchants].");
        ANSWER_MAP.put("how_to_display_item", "You can display an item by typing [i ] in chat while holding your desired item in your hand. Make sure to type it without the space. You can also display your inventory with [inv ] and applying the same logic.");
        ANSWER_MAP.put("how_to_display_inv", "You can display your inventory by typing [inv ] in chat. Make sure to type it without the space. You can also display and item with [i ] while holding your desired item in hand and applying the same logic.");
        ANSWER_MAP.put("how_to_get_upgrade_points", "You receive  one upgrade point for every Prestige you do. To achieve a Prestige you need to mine through all [/ob phases].");
        ANSWER_MAP.put("how_to_get_increase_island_size", "You can increase your island size by mining your oneblock. For every two phases you complete your island gets bigger by one block in each direction. You can also spend money in [/ob upgrade] to increase island size.");
        ANSWER_MAP.put("how_to_get_custom_color", "You can get different colors with MC Color Codes. For a full list, google 'MC Color Codes'.");
        ANSWER_MAP.put("how_to_get_island_points", "You receive one island points for [/ob upgrades] for every prestige you complete.");
        ANSWER_MAP.put("how_does_a_slayer_work", "A Slayer kills Mobs in its radius and puts their Loot into a Chest. Depending on the Tier the radius and the speed of killing gets faster.");
        ANSWER_MAP.put("how_does_a_miner_work", "A Miner is usually put in front of a Cobble Generator. There it mines the blocks up front. Depending on the Tier, the reach of the Miner is extended.");
        ANSWER_MAP.put("how_does_a_farmer_work", "A Farmer can farm crops like Wheat in a radius around itself and put them inside a chest (The Crops are replanted). This can be used to create cool automatic farms.");
        ANSWER_MAP.put("how_does_a_fisher_work","A Fisher is placed in front of a water source or a pond. There it fishes and puts the loot inside a Chest.");
        ANSWER_MAP.put("how_does_a_sorter_work","A Sorter works for example like this: You have a Chest with all 4 kinds of fish. The Sorter is able to put the different kinds of fish in its own chest. So in the end you have 4 Chests with only one kind of fish.");
        ANSWER_MAP.put("how_does_a_seller_work","A Seller automatically sells Items from a Chest to [/shop]. However, this only works if the item you want to sell is currently inside of the [/shop].");
        ANSWER_MAP.put("how_does_a_collector_work","A Collector collects items in its radius and puts the collected items into a chest. Depending on the Tier the radius is extended.");
        ANSWER_MAP.put("how_does_a_lumber_work","A Lumberjack mines Tree in its radius and put the wood inside a chest. Depending on the Tier the radius of the Lumber is extended.");
        ANSWER_MAP.put("how_to_vote","You can get Information about Voting here: https://www.mcfoxcraft.com/help/voting/");
        ANSWER_MAP.put("how_do_crates_work","Only Vote Keys exist in physical form (sometimes also Pinata). All the other types of crates are stored in [/keys]. You can open a Crate by just clicking the chest of it (If you have the physical key you need to hold it).");
        ANSWER_MAP.put("how_to_get_lava", "You can get free Water and Lava at my pwarp | Just run the command [/pwarp go DZultra] and look around to find the free Water and Lava!");
        ANSWER_MAP.put("how_to_get_water", "You can get free Water and Lava at my pwarp | Just run the command [/pwarp go DZultra] and look around to find the free Water and Lava!");

        ANSWER_MAP.put("what_is_pinata", "The Pinata is a Llama that spawns once 125 Votes have been made. It spawns at the Pvp Area which you can navigate to by running the command [/warp pvp]. If you hit it at least once you get a Pinata Key at the end which you can redeem at [/crates].");
        ANSWER_MAP.put("what_is_a_prestige", "You prestige whenever you mined through all [/ob phases]. Once you do so, you will get a Pinata Key and a Upgrade Point which you can spend in [/ob upgrades].");
        ANSWER_MAP.put("what_is_a_title_point", "In TAB you might see some people having a Custom Tag after their name. That is a 'Title' which you can get with a Title Point!");
        ANSWER_MAP.put("what_is_a_vote_point", "Whenever you vote you received a Vote Point. If you have gathered enough you can use them to crate a Giftcard for the [/store].");
        ANSWER_MAP.put("what_is_a_backpack","A Backpack is an item which functions like a Shulker Box. However, the backpack does not need to be placed. Additionally, Backpacks can have different sizes.");
        ANSWER_MAP.put("what_is_fish","The PyroFishing Plugin (/fish) is a fun plugin with which you can earn money by fishing.");
        ANSWER_MAP.put("what_is_farm","The PyroFarming Plugin (/farm) is a fun plugin with which you can earn money by harvesting Bushes and planting seeds in Growstations. It does require some grind to earn a bunch of money.");
        ANSWER_MAP.put("what_is_a_portable_phase_block","A Portable Phase Block is a portable Oneblock which you can place and pick up everywhere on your island. The amount of blocks can be infinite or have a set amount of blocks. Each Phase Block only has one biome.");

        ANSWER_MAP.put("free_stuff", "You can get free items at my Player Warp. To get there run the command: [/pwarp go DZultra]. Enter the Shop Area and look around to find free and cheap items. If you want to support my pwarp do [/like].");
        ANSWER_MAP.put("xp_farm", "You can go to my xp farm. Just run the command: [/pwarp go DZultra]. Enter the Xp Area and start killing some mobs.");
        ANSWER_MAP.put("is_litematica_allowed", "Yes, Litematica is allowed. However, you are not allowed to use the Easy Place Feature.");
        ANSWER_MAP.put("helper","I am a Helper in terms of answering questions and doing simple moderation. Not helping to build or something similar.");

        ANSWER_MAP.put("guide", "https://www.mcfoxcraft.com/threads/a-comprehensive-oneblock-guide-pyrofarming.38325/");
        ANSWER_MAP.put("bug", "https://www.mcfoxcraft.com/form/bug-report.1/select");
        ANSWER_MAP.put("feedback", "https://www.mcfoxcraft.com/forums/feedback-suggestions.25/");
        ANSWER_MAP.put("suggestion", "https://www.mcfoxcraft.com/forums/feedback-suggestions.25/");
        ANSWER_MAP.put("appeal", "https://www.mcfoxcraft.com/form/appeal.5/select");
        ANSWER_MAP.put("player", "https://www.mcfoxcraft.com/form/report-player.4/select");
        ANSWER_MAP.put("application", "https://www.mcfoxcraft.com/form/staff-application.6/select");
        ANSWER_MAP.put("payment", "https://www.mcfoxcraft.com/form/customer-support.3/select");
        ANSWER_MAP.put("store", "https://store.foxcraft.net/");
        ANSWER_MAP.put("forums", "https://www.mcfoxcraft.com/");
        ANSWER_MAP.put("vote", "https://www.mcfoxcraft.com/help/voting/");
    }

    public static LiteralArgumentBuilder<FabricClientCommandSource> getCommand() {
        var answerCommand = literal("answer").executes(context -> {
            MinecraftClient.getInstance().player.sendMessage(Text.literal("\n|- No specific answer has been chosen -|\n")
                    .setStyle(Style.EMPTY.withColor(Formatting.RED).withBold(true)), false);
            return 0;
        });

        ANSWER_MAP.keySet().forEach(k -> {
            answerCommand.then(faq(k, ANSWER_MAP));
        });

        return answerCommand;
    }

    public static LiteralArgumentBuilder<FabricClientCommandSource> faq(String faqKey, Map<String, String> answers) {
        return literal(faqKey).executes(context -> {
            String faqMessage = "\n|- Click to get answer -|\n";
            if(!answers.containsKey(faqKey)) {
                BetterFoxcraft.LOGGER.info("could not register FAQ: {} no answer found", faqKey);
                faqMessage = "\n|- Could not find answer -|\n";
            }
            String answer = answers.getOrDefault(faqKey, "No answer found");
            MinecraftClient.getInstance().player.sendMessage(Text.literal(faqMessage)
                    .setStyle(Style.EMPTY.withClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, answer))
                            .withColor(Formatting.GOLD)
                            .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,Text.literal("Copy")))
                    ), false);
            return 0;
        });
    }
}