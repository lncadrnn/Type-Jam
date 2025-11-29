package com.example.typejam;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Utility class for persisting and loading leaderboard entries locally (per device).
 * Data stored as JSON in user's home directory: typejam-leaderboard.json
 */
public final class LeaderboardStorage {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Type LIST_TYPE = new TypeToken<List<LeaderboardEntry>>(){}.getType();

    private LeaderboardStorage() {}

    public static File getStorageFile() {
        Path path = Paths.get(System.getProperty("user.home"), "typejam-leaderboard.json");
        return path.toFile();
    }

    public static synchronized List<LeaderboardEntry> loadEntries() {
        File file = getStorageFile();
        if (!file.exists()) {
            return new ArrayList<>();
        }
        try (FileReader reader = new FileReader(file)) {
            List<LeaderboardEntry> entries = GSON.fromJson(reader, LIST_TYPE);
            if (entries == null) return new ArrayList<>();
            return entries;
        } catch (IOException e) {
            System.err.println("Failed to read leaderboard file: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public static synchronized void saveEntry(LeaderboardEntry newEntry) {
        List<LeaderboardEntry> entries = loadEntries();
        entries.add(newEntry);
        writeEntries(entries);
    }

    public static synchronized void writeEntries(List<LeaderboardEntry> entries) {
        File file = getStorageFile();
        try (FileWriter writer = new FileWriter(file)) {
            GSON.toJson(entries, LIST_TYPE, writer);
        } catch (IOException e) {
            System.err.println("Failed to write leaderboard file: " + e.getMessage());
        }
    }

    /**
     * Return entries filtered and sorted for display.
     * Sort order: stars DESC, wpm DESC, accuracy DESC, timestamp ASC (older first if tie).
     */
    public static List<LeaderboardEntry> getRankedEntries(String modeFilter, String difficultyFilter) {
        List<LeaderboardEntry> entries = loadEntries();
        List<LeaderboardEntry> filtered = new ArrayList<>();
        for (LeaderboardEntry e : entries) {
            boolean modeOk = (modeFilter == null) || e.mode.equals(modeFilter);
            boolean diffOk = (difficultyFilter == null) || e.difficulty.equals(difficultyFilter);
            if (modeOk && diffOk) {
                filtered.add(e);
            }
        }
        filtered.sort(Comparator
                .comparingInt(LeaderboardEntry::getStars).reversed()
                .thenComparingDouble(LeaderboardEntry::getWpm).reversed()
                .thenComparingDouble(LeaderboardEntry::getAccuracy).reversed()
                .thenComparingLong(LeaderboardEntry::getTimestamp));
        return filtered;
    }

    /**
     * Clears all leaderboard data by deleting the storage file.
     * @return true if data was successfully cleared, false otherwise
     */
    public static synchronized boolean clearAllData() {
        File file = getStorageFile();
        if (file.exists()) {
            return file.delete();
        }
        return true; // No file to delete means data is already clear
    }

    /** Entry data model. */
    public static class LeaderboardEntry {
        private String playerName;
        private String mode; // Time Challenge / Endless Mode / etc.
        private String difficulty; // Easy / Medium / Hard
        private int stars; // 0-5
        private double wpm; // words per minute
        private double accuracy; // percentage 0-100
        private long timestamp; // epoch millis for ordering stability

        public LeaderboardEntry(String playerName, String mode, String difficulty, int stars, double wpm, double accuracy, long timestamp) {
            this.playerName = playerName;
            this.mode = mode;
            this.difficulty = difficulty;
            this.stars = stars;
            this.wpm = wpm;
            this.accuracy = accuracy;
            this.timestamp = timestamp;
        }

        public String getPlayerName() { return playerName; }
        public String getMode() { return mode; }
        public String getDifficulty() { return difficulty; }
        public int getStars() { return stars; }
        public double getWpm() { return wpm; }
        public double getAccuracy() { return accuracy; }
        public long getTimestamp() { return timestamp; }
    }
}

