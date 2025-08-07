package net.dzultra.betterfoxcraft.commands.conditions;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.client.MinecraftClient;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ConditionsDataBaseManager {
    private static ConditionsDataBaseManager INSTANCE;

    public static ConditionsDataBaseManager getInstance() {
        if (INSTANCE != null) return INSTANCE;
        INSTANCE = new ConditionsDataBaseManager();
        return INSTANCE;
    }

    Gson gson = new GsonBuilder().setPrettyPrinting().create();

    Path baseDirectory = Path.of(MinecraftClient.getInstance().runDirectory.getAbsolutePath(), "conditions");

    private ConditionsDataBaseManager() {
        try {
            if (!Files.exists(baseDirectory)) {
                Files.createDirectory(baseDirectory);
            }
        } catch (IOException e) {
            throw new RuntimeException("Error while creating conditions directory.", e);
        }
    }

    public void appendConditions(String username, String newConditions) {
        ConditionsData existingData = getConditions(username);
        existingData.conditions_list.add(newConditions);
        writeSpecificFile(username, existingData);
    }

    public ConditionsData getConditions(String username) {
        ConditionsData loadedData = loadFile(username);
        if(loadedData == null) {
            loadedData = new ConditionsData();
        }
        return loadedData;
    }

    public boolean removeCondition(String username, int index) {
        ConditionsData loadedData = getConditions(username);
        try {
            loadedData.conditions_list.remove(index);
            writeSpecificFile(username, loadedData);
            return true;
        } catch (IndexOutOfBoundsException e) {
            return false;
        }
    }

    private void writeSpecificFile(String username, ConditionsData conditionsData) {
        Path filePath = baseDirectory.resolve(username + ".json");
        String jsonString = gson.toJson(conditionsData);

        try {
            if(!Files.exists(baseDirectory)) {
                Files.createDirectory(baseDirectory);
            }
            if(!Files.exists(filePath)) {
                Files.createFile(filePath);
            }

            Files.writeString(filePath, jsonString);
        } catch (IOException e) {
            throw new RuntimeException("Error while writing to file: " + filePath, e);
        }
    }

    private ConditionsData loadFile(String username) {
        Path filePath = baseDirectory.resolve(username + ".json");
        try {
            if(!Files.exists(filePath)) {
                return null;
            }
            return gson.fromJson(Files.readString(filePath), ConditionsData.class);
        } catch (IOException e) {
            throw new RuntimeException("Error while loading file: " + filePath, e);
        }
    }
}
