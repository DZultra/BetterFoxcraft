package net.dzultra.betterfoxcraft;

import com.mojang.brigadier.arguments.StringArgumentType;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.suuft.libretranslate.Language;
import net.suuft.libretranslate.Translator;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;

public class BetterFoxcraftClient implements ClientModInitializer {
    @Override

    public void onInitializeClient() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> {
            dispatcher.register(ClientCommandManager.literal("translate")
                    .then(ClientCommandManager.argument("language_text", StringArgumentType.string())
                            .then(ClientCommandManager.argument("language_translate", StringArgumentType.string())
                                    .then(ClientCommandManager.argument("text", StringArgumentType.greedyString())
                                            .executes(context -> {
                                                String language_text = StringArgumentType.getString(context, "language_text");
                                                String language_translate = StringArgumentType.getString(context, "language_translate");
                                                String text = StringArgumentType.getString(context, "text");
                                                try {
                                                    Language from = Language.valueOf(language_text.toUpperCase());
                                                    Language to = Language.valueOf(language_translate.toUpperCase());
                                                    String result = (Translator.translate(from, to, text));
                                                    MinecraftClient.getInstance().player.sendMessage(Text.literal(result).setStyle(Style.EMPTY.withClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, result))), false);
                                                } catch (IllegalArgumentException e) {
                                                    System.out.println(e.getMessage());
                                                    MinecraftClient.getInstance().player.sendMessage(Text.literal("Can't recognize the language!"), false);
                                                }
                                                return 1;
                                            })))));
        });
        ClientCommandRegistrationCallback.EVENT.register(((dispatcher, registryAccess) -> {
            dispatcher.register(
                    literal("start").executes(context -> {
                        MinecraftClient.getInstance().getNetworkHandler().sendChatCommand("redeem");
                        MinecraftClient.getInstance().getNetworkHandler().sendChatCommand("redeemkey");
                        MinecraftClient.getInstance().getNetworkHandler().sendChatCommand("daily");
                        return 0;
                    })
            );
        }));
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> {
            dispatcher.register(ClientCommandManager.literal("oc")
                    .then(ClientCommandManager.argument("text", StringArgumentType.string())
                        .executes(context -> {
                            String text = StringArgumentType.getString(context, "language_text");
                            try {
                                MinecraftClient.getInstance().getNetworkHandler().sendChatCommand("ob chat" + text);
                            } catch (IllegalArgumentException e) {
                                System.out.println(e.getMessage());
                                MinecraftClient.getInstance().player.sendMessage(Text.literal("Error Code: 1"), false);
                            }
                            return 1;
                        })
                    ));
        });
        ClientCommandRegistrationCallback.EVENT.register(((dispatcher, registryAccess) -> {
            dispatcher.register(
                    literal("link").executes(context -> {
                        MinecraftClient.getInstance().player.sendMessage(Text.literal("\n|- No specific link has been chosen -|\n")
                                .setStyle(Style.EMPTY.withColor(Formatting.RED).withBold(true)), false);
                        return 0;
                    }).then(literal("bug").executes(context -> {
                        MinecraftClient.getInstance().player.sendMessage(Text.literal("\n|- Bug Report: https://www.mcfoxcraft.com/forums/bug-reports.26/ -|\n")
                                .setStyle(Style.EMPTY.withClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, "https://www.mcfoxcraft.com/forums/bug-reports.26/"))
                                        .withColor(Formatting.GOLD)
                                        .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,Text.literal("Click to Copy")))), false);
                        return 0;
                    })).then(literal("player").executes(context -> {
                        MinecraftClient.getInstance().player.sendMessage(Text.literal("\n|- Player Report: https://www.mcfoxcraft.com/forums/player-reports.23/ -|\n")
                                .setStyle(Style.EMPTY.withClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, "https://www.mcfoxcraft.com/forums/player-reports.23/"))
                                        .withColor(Formatting.GOLD)
                                        .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,Text.literal("Click to Copy")))), false);
                        return 0;
                    })).then(literal("feedback").executes(context -> {
                        MinecraftClient.getInstance().player.sendMessage(Text.literal("\n|- Feedback:  https://feedback.foxcraft.net/ -|\n")
                                .setStyle(Style.EMPTY.withClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, " https://feedback.foxcraft.net/"))
                                        .withColor(Formatting.GOLD)
                                        .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,Text.literal("Click to Copy")))), false);
                        return 0;
                    })).then(literal("forums").executes(context -> {
                        MinecraftClient.getInstance().player.sendMessage(Text.literal("\n|- Forums: https://www.mcfoxcraft.com/ -|\n")
                                .setStyle(Style.EMPTY.withClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, "https://www.mcfoxcraft.com/"))
                                        .withColor(Formatting.GOLD)
                                        .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,Text.literal("Click to Copy")))), false);
                        return 0;
                    })).then(literal("payment").executes(context -> {
                        MinecraftClient.getInstance().player.sendMessage(Text.literal("\n|- Payment Support: https://www.mcfoxcraft.com/forums/donation-support.22// -|\n")
                                .setStyle(Style.EMPTY.withClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, "https://www.mcfoxcraft.com/forums/donation-support.22/"))
                                        .withColor(Formatting.GOLD)
                                        .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,Text.literal("Click to Copy")))), false);
                        return 0;
                    })).then(literal("appeal").executes(context -> {
                        MinecraftClient.getInstance().player.sendMessage(Text.literal("\n|- Appeal: https://www.mcfoxcraft.com/forums/appeals.24/ -|\n")
                                .setStyle(Style.EMPTY.withClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, "https://www.mcfoxcraft.com/forums/appeals.24/"))
                                        .withColor(Formatting.GOLD)
                                        .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,Text.literal("Click to Copy")))), false);
                        return 0;
                    })).then(literal("application").executes(context -> {
                        MinecraftClient.getInstance().player.sendMessage(Text.literal("\n|- Staff Application: https://www.mcfoxcraft.com/forums/staff-applications.27/ -|\n")
                                .setStyle(Style.EMPTY.withClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, "https://www.mcfoxcraft.com/forums/staff-applications.27/"))
                                        .withColor(Formatting.GOLD)
                                        .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,Text.literal("Click to Copy")))), false);
                        return 0;
                    })).then(literal("store").executes(context -> {
                        MinecraftClient.getInstance().player.sendMessage(Text.literal("\n|- Store:  https://store.foxcraft.net/ -|\n")
                                .setStyle(Style.EMPTY.withClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, " https://store.foxcraft.net/"))
                                        .withColor(Formatting.GOLD)
                                        .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,Text.literal("Click to Copy")))), false);
                        return 0;
                    })).then(literal("guide").executes(context -> {
                        MinecraftClient.getInstance().player.sendMessage(Text.literal("\n|- Guide:  https://www.mcfoxcraft.com/threads/a-comprehensive-oneblock-guide-pyrofarming.38325/ -|\n")
                                .setStyle(Style.EMPTY.withClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, " https://www.mcfoxcraft.com/threads/a-comprehensive-oneblock-guide-pyrofarming.38325/"))
                                        .withColor(Formatting.GOLD)
                                        .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,Text.literal("Click to Copy")))), false);
                        return 0;
                    }))
            );
        }));
        ClientCommandRegistrationCallback.EVENT.register(((dispatcher, registryAccess) -> {
            dispatcher.register(
                    literal("answer").executes(context -> {
                        MinecraftClient.getInstance().player.sendMessage(Text.literal("\n|- No specific answer has been chosen -|\n")
                                .setStyle(Style.EMPTY.withColor(Formatting.RED).withBold(true)), false);
                        return 0;
                    }).then(literal("to_island").executes(context -> {
                        MinecraftClient.getInstance().player.sendMessage(Text.literal("\n|- Click to get answer -|\n")
                                .setStyle(Style.EMPTY.withClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, "You can navigate to your island by running either of the following commands: [/is] | [/ob]."))
                                        .withColor(Formatting.GOLD)
                                        .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,Text.literal("Copy")))), false);
                        return 0;
                    })).then(literal("to_spawn").executes(context -> {
                        MinecraftClient.getInstance().player.sendMessage(Text.literal("\n|- Click to get answer -|\n")
                                .setStyle(Style.EMPTY.withClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, "You can navigate to spawn by running to command: [/spawn]."))
                                        .withColor(Formatting.GOLD)
                                        .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,Text.literal("Copy")))), false);
                        return 0;
                    })).then(literal("to_pinata").executes(context -> {
                        MinecraftClient.getInstance().player.sendMessage(Text.literal("\n|- Click to get answer -|\n")
                                .setStyle(Style.EMPTY.withClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, "The Pinata Spawns at the PvP Area. You can navigate there by running the command: [/warp pvp]."))
                                        .withColor(Formatting.GOLD)
                                        .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,Text.literal("Copy")))), false);
                        return 0;
                    })).then(literal("to_nether").executes(context -> {
                        MinecraftClient.getInstance().player.sendMessage(Text.literal("\n|- Click to get answer -|\n")
                                .setStyle(Style.EMPTY.withClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, "You can access the Nether by building a Nether Portal on your Island. Inside you won't find the normal Nether but another Oneblock which spawns Nether Blocks."))
                                        .withColor(Formatting.GOLD)
                                        .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,Text.literal("Copy")))), false);
                        return 0;
                    })).then(literal("to_end").executes(context -> {
                        MinecraftClient.getInstance().player.sendMessage(Text.literal("\n|- Click to get answer -|\n")
                                .setStyle(Style.EMPTY.withClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, "You can navigate to the End Portal by running the command [/warp end]. However, you need to have one Prestige to access."))
                                        .withColor(Formatting.GOLD)
                                        .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.literal("Copy")))), false);
                        return 0;
                    })).then(literal("to_pwarp").executes(context -> {
                        MinecraftClient.getInstance().player.sendMessage(Text.literal("\n|- Click to get answer -|\n")
                                .setStyle(Style.EMPTY.withClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, "You can navigate to a Player Warp by running either of the following commands: [/pwarp go <name>] or [/wwarp go <name>]."))
                                        .withColor(Formatting.GOLD)
                                        .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.literal("Copy")))), false);
                        return 0;
                    })).then(literal("to_crates").executes(context -> {
                        MinecraftClient.getInstance().player.sendMessage(Text.literal("\n|- Click to get answer -|\n")
                                .setStyle(Style.EMPTY.withClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, "You can navigate to crates by running the command: [/crates]."))
                                        .withColor(Formatting.GOLD)
                                        .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.literal("Copy")))), false);
                        return 0;
                    })).then(literal("what_is_pinata").executes(context -> {
                        MinecraftClient.getInstance().player.sendMessage(Text.literal("\n|- Click to get answer -|\n")
                                .setStyle(Style.EMPTY.withClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, "The Pinata is Llama that spawns once 125 Votes have been made. It spawns at the Pvp Area which you can navigate to by running the command [/warp pvp]. If you hit it at least once you get a Pinata Key at the end which you can redeem at [/crates]."))
                                        .withColor(Formatting.GOLD)
                                        .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,Text.literal("Copy")))), false);
                        return 0;
                    })).then(literal("get_money").executes(context -> {
                        MinecraftClient.getInstance().player.sendMessage(Text.literal("\n|- Click to get answer -|\n")
                                .setStyle(Style.EMPTY.withClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, "You can get Money for example by building a bamboo farm and selling the bamboo to /shop. In addition, you can do jobs in [/jobsmenu]. Also possible is starting with [/farm] a Plugin to make good starter money."))
                                        .withColor(Formatting.GOLD)
                                        .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.literal("Copy")))), false);
                        return 0;
                    })).then(literal("get_rank").executes(context -> {
                        MinecraftClient.getInstance().player.sendMessage(Text.literal("\n|- Click to get answer -|\n")
                                .setStyle(Style.EMPTY.withClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, "You can buy ranks on the [/store] or buy Rank Vouchers of other players for in-game money."))
                                        .withColor(Formatting.GOLD)
                                        .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.literal("Copy")))), false);
                        return 0;
                    })).then(literal("get_minions").executes(context -> {
                        MinecraftClient.getInstance().player.sendMessage(Text.literal("\n|- Click to get answer -|\n")
                                .setStyle(Style.EMPTY.withClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, "You can buy minions on the [/store] or buy them of other players for in-game money."))
                                        .withColor(Formatting.GOLD)
                                        .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.literal("Copy")))), false);
                        return 0;
                    })).then(literal("get_spawners").executes(context -> {
                        MinecraftClient.getInstance().player.sendMessage(Text.literal("\n|- Click to get answer -|\n")
                                .setStyle(Style.EMPTY.withClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, "You can buy spawners on the [/store] or buy them of other players for in-game money."))
                                        .withColor(Formatting.GOLD)
                                        .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.literal("Copy")))), false);
                        return 0;
                    })).then(literal("get_ce").executes(context -> {
                        MinecraftClient.getInstance().player.sendMessage(Text.literal("\n|- Click to get answer -|\n")
                                .setStyle(Style.EMPTY.withClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD,"You can get Custom Enchants in [/ce]. There you buy the different rarity's for different amounts of Xp. Of course, you are always able to buy Custom Enchants from other players."))
                                        .withColor(Formatting.GOLD)
                                        .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.literal("Copy")))), false);
                        return 0;
                    })).then(literal("how_to_make_pwarp").executes(context -> {
                        MinecraftClient.getInstance().player.sendMessage(Text.literal("\n|- Click to get answer -|\n")
                                .setStyle(Style.EMPTY.withClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, "You can create a Player Warp by placing any type of sign and writing [welcome] in the first line. Make sure to include the Brackets. In addition, in the bottom three lines you can write a description of your pwarp."))
                                        .withColor(Formatting.GOLD)
                                        .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.literal("Copy")))), false);
                        return 0;
                    })).then(literal("how_to_sell_items").executes(context -> {
                        MinecraftClient.getInstance().player.sendMessage(Text.literal("\n|- Click to get answer -|\n")
                                .setStyle(Style.EMPTY.withClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, "You can sell items in [/shop] or create a pwarp with chestshops. Additionally, you can put items up for auction: [/ah]"))
                                        .withColor(Formatting.GOLD)
                                        .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.literal("Copy")))), false);
                        return 0;
                    })).then(literal("how_to_invite").executes(context -> {
                        MinecraftClient.getInstance().player.sendMessage(Text.literal("\n|- Click to get answer -|\n")
                                .setStyle(Style.EMPTY.withClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, "You can invite other players by running the command: [/ob team invite <name>]. Note: The Player who joins your Team will lose there entire island."))
                                        .withColor(Formatting.GOLD)
                                        .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.literal("Copy")))), false);
                        return 0;
                    })).then(literal("how_to_give_perms").executes(context -> {
                        MinecraftClient.getInstance().player.sendMessage(Text.literal("\n|- Click to get answer -|\n")
                                .setStyle(Style.EMPTY.withClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, "You can give a Player different Permissions on your Island by first doing [/ob team trust <name>] and then second allowing the desired settings in [/ob settings]."))
                                        .withColor(Formatting.GOLD)
                                        .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.literal("Copy")))), false);
                        return 0;
                    })).then(literal("how_to_like_island").executes(context -> {
                        MinecraftClient.getInstance().player.sendMessage(Text.literal("\n|- Click to get answer -|\n")
                                .setStyle(Style.EMPTY.withClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, "You can like or dislike an island by running the commabd [/like]."))
                                        .withColor(Formatting.GOLD)
                                        .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.literal("Copy")))), false);
                        return 0;
                    })).then(literal("how_to_favourite_pwarp").executes(context -> {
                        MinecraftClient.getInstance().player.sendMessage(Text.literal("\n|- Click to get answer -|\n")
                                .setStyle(Style.EMPTY.withClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, "To save a pwarp for another time you can favourite it. You do so by running the command: [/pwarp favourite <name_of_the_owner>]. You'll find it in [/pwarp]."))
                                        .withColor(Formatting.GOLD)
                                        .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.literal("Copy")))), false);
                        return 0;
                    })).then(literal("how_to_switch_phases").executes(context -> {
                        MinecraftClient.getInstance().player.sendMessage(Text.literal("\n|- Click to get answer -|\n")
                                .setStyle(Style.EMPTY.withClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, "The only way to switch a phase is finishing the current and advancing to the next. It may say in [/ob phases] that you can change it by click. However, that is a know bug."))
                                        .withColor(Formatting.GOLD)
                                        .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.literal("Copy")))), false);
                        return 0;
                    })).then(literal("how_to_join_dc").executes(context -> {
                        MinecraftClient.getInstance().player.sendMessage(Text.literal("\n|- Click to get answer -|\n")
                                .setStyle(Style.EMPTY.withClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, "You can join the Discord Server by running the command: [/discord]. Then you need to scan the QR-Code or open the link."))
                                        .withColor(Formatting.GOLD)
                                        .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.literal("Copy")))), false);
                        return 0;
                    })).then(literal("how_to_create_chestshop").executes(context -> {
                        MinecraftClient.getInstance().player.sendMessage(Text.literal("\n|- Click to get answer -|\n")
                                .setStyle(Style.EMPTY.withClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, "You can create a chestshop by punshing a chest or barrel while holding your item you want to sell inside. For more info do [/qs help]."))
                                        .withColor(Formatting.GOLD)
                                        .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.literal("Copy")))), false);
                        return 0;
                    })).then(literal("how_to_see_ce").executes(context -> {
                        MinecraftClient.getInstance().player.sendMessage(Text.literal("\n|- Click to get answer -|\n")
                                .setStyle(Style.EMPTY.withClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, "You can access a list of all Custom Enchantments by running the commabd [/enchants]."))
                                        .withColor(Formatting.GOLD)
                                        .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.literal("Copy")))), false);
                        return 0;
                    })).then(literal("how_to_display_item").executes(context -> {
                        MinecraftClient.getInstance().player.sendMessage(Text.literal("\n|- Click to get answer -|\n")
                                .setStyle(Style.EMPTY.withClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, "You can display an item by typing [i ] in chat while holding your desired item in your hand. Make sure to type it without the space. You can also display your inventory with [inv ] and applying the same logic."))
                                        .withColor(Formatting.GOLD)
                                        .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.literal("Copy")))), false);
                        return 0;
                    })).then(literal("how_to_display_inv").executes(context -> {
                        MinecraftClient.getInstance().player.sendMessage(Text.literal("\n|- Click to get answer -|\n")
                                .setStyle(Style.EMPTY.withClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, "You can display your inventory by typing [inv ] in chat. Make sure to type it without the space. You can also display and item with [i ] while holding your desired item in hand and applying the same logic."))
                                        .withColor(Formatting.GOLD)
                                        .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.literal("Copy")))), false);
                        return 0;
                    })).then(literal("how_to_get_free_stuff").executes(context -> {
                        MinecraftClient.getInstance().player.sendMessage(Text.literal("\n|- Click to get answer -|\n")
                                .setStyle(Style.EMPTY.withClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, "You can get free items at my Player Warp. To get there run the command: [/pwarp go DZultra]. Enter the Shop Area and look around to find free and cheap items. If you want to support my pwarp do [/like]."))
                                        .withColor(Formatting.GOLD)
                                        .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.literal("Copy")))), false);
                        return 0;
                    })).then(literal("how_to_get_to_xp_farm").executes(context -> {
                        MinecraftClient.getInstance().player.sendMessage(Text.literal("\n|- Click to get answer -|\n")
                                .setStyle(Style.EMPTY.withClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, "You can go to my xp farm. Just run the command: [/pwarp go DZultra]. Enter the Xp Area and starting killing some mobs."))
                                        .withColor(Formatting.GOLD)
                                        .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.literal("Copy")))), false);
                        return 0;
                    })).then(literal("how_to_get_upgradepoints").executes(context -> {
                        MinecraftClient.getInstance().player.sendMessage(Text.literal("\n|- Click to get answer -|\n")
                                .setStyle(Style.EMPTY.withClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, "You get one upgrade point for every Prestige you do. To achieve a Prestige you need to mine through all [/ob phases]."))
                                        .withColor(Formatting.GOLD)
                                        .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.literal("Copy")))), false);
                        return 0;
                    })).then(literal("how_to_increase_island_size").executes(context -> {
                        MinecraftClient.getInstance().player.sendMessage(Text.literal("\n|- Click to get answer -|\n")
                                .setStyle(Style.EMPTY.withClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, "You can increase your island size by mining your oneblock. For every two phases you complete your island gets bigger by one block in each direction. You can also spend money in [/ob upgrade] to increase island size."))
                                        .withColor(Formatting.GOLD)
                                        .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.literal("Copy")))), false);
                        return 0;
                    })).then(literal("what_is_a_prestige").executes(context -> {
                        MinecraftClient.getInstance().player.sendMessage(Text.literal("\n|- Click to get answer -|\n")
                                .setStyle(Style.EMPTY.withClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, "You prestige whenever you mined through all [/ob phases]. Once you do so, you will get a Pinata Key and a Upgrade Point which you can spend in [/ob upgrades]."))
                                        .withColor(Formatting.GOLD)
                                        .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.literal("Copy")))), false);
                        return 0;
                    }))
            );
        }));
    }
}
