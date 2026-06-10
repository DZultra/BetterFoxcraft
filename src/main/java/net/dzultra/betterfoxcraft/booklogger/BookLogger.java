package net.dzultra.betterfoxcraft.booklogger;

import net.dzultra.betterfoxcraft.BetterFoxcraft;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.WritableBookContent;
import net.minecraft.world.item.component.WrittenBookContent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;

public class BookLogger {
    protected static final Set<String> savedBooks = new HashSet<>();

    public static void getBookLogger() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            ClientLevel world = client.level;
            if (world == null || client.player == null) return;

            for (ItemEntity itemEntity : world.getEntitiesOfClass(ItemEntity.class,
                    client.player.getBoundingBox().inflate(64), e -> true)) {

                ItemStack stack = itemEntity.getItem();
                if (stack.is(Items.WRITABLE_BOOK)) {
                    WritableBookContent content = stack.get(DataComponents.WRITABLE_BOOK_CONTENT);

                    if (content != null) {
                        StringBuilder text = new StringBuilder();
                        for (var page : content.pages()) {
                            text.append(page.get(false)).append("\n\n");
                        }

                        String fullText = text.toString();
                        String hash = Integer.toHexString(fullText.hashCode());

                        if (savedBooks.contains(hash)) continue;
                        savedBooks.add(hash);

                        File saveDir = new File(Minecraft.getInstance().gameDirectory, "saved-data");
                        if (!saveDir.exists()) saveDir.mkdirs();

                        File file = new File(saveDir, "book_" + hash + ".txt");
                        try (FileWriter writer = new FileWriter(file, StandardCharsets.UTF_8)) {
                            writer.write(fullText);
                            BetterFoxcraft.LOGGER.info("Saved book to: " + file.getAbsolutePath());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                else if (stack.is(Items.WRITTEN_BOOK)) {
                    WrittenBookContent content = stack.get(DataComponents.WRITTEN_BOOK_CONTENT);

                    if (content != null) {
                        StringBuilder text = new StringBuilder();
                        for (var page : content.pages()) {
                            text.append(page.get(false)).append("\n\n");
                        }

                        String fullText = text.toString();
                        String hash = Integer.toHexString(fullText.hashCode());

                        if (savedBooks.contains(hash)) continue;
                        savedBooks.add(hash);

                        File saveDir = new File(Minecraft.getInstance().gameDirectory, "saved-data");
                        if (!saveDir.exists()) saveDir.mkdirs();

                        File file = new File(saveDir, "book_" + hash + ".txt");
                        try (FileWriter writer = new FileWriter(file, StandardCharsets.UTF_8)) {
                            writer.write(fullText);
                            BetterFoxcraft.LOGGER.info("Saved book to: " + file.getAbsolutePath());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
    }
}
