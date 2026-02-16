package net.dzultra.betterfoxcraft.commands.jfa;

import net.dzultra.jfa.punishments.Punishment;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PunishmentCache {

    private static final Map<String, List<Punishment>> CACHE = new ConcurrentHashMap<>();

    public static void put(String username, List<Punishment> data) {
        if (username == null || data == null) return;
        CACHE.put(username.toLowerCase(), data);
    }

    public static List<Punishment> get(String username) {
        if (username == null) return null;
        return CACHE.get(username.toLowerCase());
    }

    public static boolean has(String username) {
        if (username == null) return false;
        return CACHE.containsKey(username.toLowerCase());
    }

    public static void clear(String username) {
        if (username == null) return;
        CACHE.remove(username.toLowerCase());
    }

    public static void clearAll() {
        CACHE.clear();
    }
}
