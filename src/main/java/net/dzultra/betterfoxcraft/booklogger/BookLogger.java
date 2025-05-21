package net.dzultra.betterfoxcraft.booklogger;

import net.dzultra.betterfoxcraft.BetterFoxcraft;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.WritableBookContentComponent;
import net.minecraft.component.type.WrittenBookContentComponent;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.text.Text;

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
            ClientWorld world = client.world;
            if (world == null || client.player == null) return;

            for (ItemEntity itemEntity : world.getEntitiesByClass(ItemEntity.class,
                    client.player.getBoundingBox().expand(64), e -> true)) {

                ItemStack stack = itemEntity.getStack();
                if (stack.isOf(Items.WRITABLE_BOOK)) {
                    WritableBookContentComponent content = stack.get(DataComponentTypes.WRITABLE_BOOK_CONTENT);

                    if (content != null) {
                        StringBuilder text = new StringBuilder();
                        for (var page : content.pages()) {
                            text.append(page.get(false)).append("\n\n");
                        }

                        String fullText = text.toString();
                        String hash = Integer.toHexString(fullText.hashCode());

                        if (savedBooks.contains(hash)) continue;
                        savedBooks.add(hash);

                        File saveDir = new File(MinecraftClient.getInstance().runDirectory, "saved-data");
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
                else if (stack.isOf(Items.WRITTEN_BOOK)) {
                    WrittenBookContentComponent content = stack.get(DataComponentTypes.WRITTEN_BOOK_CONTENT);

                    if (content != null) {
                        StringBuilder text = new StringBuilder();
                        for (var page : content.pages()) {
                            text.append(page.get(false)).append("\n\n");
                        }

                        String fullText = text.toString();
                        String hash = Integer.toHexString(fullText.hashCode());

                        if (savedBooks.contains(hash)) continue;
                        savedBooks.add(hash);

                        File saveDir = new File(MinecraftClient.getInstance().runDirectory, "saved-data");
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
