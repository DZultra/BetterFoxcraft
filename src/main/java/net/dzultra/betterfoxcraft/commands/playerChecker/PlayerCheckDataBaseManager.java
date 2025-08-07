package net.dzultra.betterfoxcraft.commands.playerChecker;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class PlayerCheckDataBaseManager {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path FILE_PATH = Path.of(System.getProperty("user.home"), ".minecraft", "playercheck", "playersToCheck.json");

    // Appends a username to the JSON list
    public static void addPlayer(String username) {
        try {
            List<String> players = getAllPlayers();

            // Add if not already present
            if (!players.contains(username)) {
                players.add(username);
                savePlayers(players);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Removes a username from the JSON list
    public static void removePlayer(String username) {
        try {
            List<String> players = getAllPlayers();

            // Remove if present
            if (players.remove(username)) {
                savePlayers(players);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Returns all usernames from the JSON file
    public static List<String> getAllPlayers() {
        try {
            if (Files.notExists(FILE_PATH)) {
                return new ArrayList<>();
            }

            try (Reader reader = new FileReader(FILE_PATH.toFile())) {
                Type listType = new TypeToken<List<String>>() {}.getType();
                List<String> players = GSON.fromJson(reader, listType);
                return players != null ? players : new ArrayList<>();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    // Saves the list back to the file
    private static void savePlayers(List<String> players) throws IOException {
        // Create directories if necessary
        Files.createDirectories(FILE_PATH.getParent());

        try (Writer writer = new FileWriter(FILE_PATH.toFile())) {
            GSON.toJson(players, writer);
        }
    }
}

