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
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Utility class for persisting and loading leaderboard entries locally (per device).
 * Data stored as JSON in: src/main/resources/data/typejam-leaderboard.json
 */
public final class LeaderboardStorage {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Type LIST_TYPE = new TypeToken<List<LeaderboardEntry>>(){}.getType();
    private static final long ONE_YEAR_MILLIS = Duration.ofDays(365).toMillis();

    private LeaderboardStorage() {}

    public static File getStorageFile() {
        // Try to get the data directory from resources first (works in both IDE and JAR)
        try {
            java.net.URL resourceUrl = LeaderboardStorage.class.getResource("/data/typing-texts.json");
            if (resourceUrl != null) {
                String resourcePath = resourceUrl.getPath();
                System.out.println("DEBUG: Found resource URL: " + resourceUrl);
                System.out.println("DEBUG: Resource path: " + resourcePath);
                // Handle Windows paths (remove leading slash if present)
                if (resourcePath.startsWith("/") && resourcePath.contains(":")) {
                    resourcePath = resourcePath.substring(1);
                    System.out.println("DEBUG: Cleaned Windows path: " + resourcePath);
                }
                Path dataDir = Paths.get(resourcePath).getParent();
                if (dataDir != null && dataDir.toFile().exists()) {
                    File leaderboardFile = dataDir.resolve("typejam-leaderboard.json").toFile();
                    System.out.println("✓ LEADERBOARD STORAGE LOCATION: " + leaderboardFile.getAbsolutePath());
                    return leaderboardFile;
                }
            }
        } catch (Exception e) {
            System.err.println("Could not locate resources/data directory: " + e.getMessage());
        }

        // Fallback: use project structure path (for development)
        String projectRoot = System.getProperty("user.dir");
        Path dataDir = Paths.get(projectRoot, "src", "main", "resources", "data");
        System.out.println("DEBUG: Using fallback path - user.dir: " + projectRoot);

        // Create data directory if it doesn't exist
        File dataDirFile = dataDir.toFile();
        if (!dataDirFile.exists()) {
            dataDirFile.mkdirs();
            System.out.println("DEBUG: Created data directory: " + dataDirFile.getAbsolutePath());
        }

        File leaderboardFile = dataDir.resolve("typejam-leaderboard.json").toFile();
        System.out.println("✓ LEADERBOARD STORAGE LOCATION (FALLBACK): " + leaderboardFile.getAbsolutePath());
        return leaderboardFile;
    }

    public static synchronized List<LeaderboardEntry> loadEntries() {
        File file = getStorageFile();
        if (!file.exists()) {
            return new ArrayList<>();
        }
        try (FileReader reader = new FileReader(file)) {
            List<LeaderboardEntry> entries = GSON.fromJson(reader, LIST_TYPE);
            if (entries == null) entries = new ArrayList<>();
            // Auto-purge entries older than one year
            long now = Instant.now().toEpochMilli();
            List<LeaderboardEntry> fresh = new ArrayList<>();
            for (LeaderboardEntry e : entries) {
                if (e != null && (now - e.getTimestamp()) <= ONE_YEAR_MILLIS) {
                    fresh.add(e);
                }
            }
            if (fresh.size() != entries.size()) {
                // Write back purged list if any removals happened
                writeEntries(fresh);
            }
            return fresh;
        } catch (IOException e) {
            System.err.println("Failed to read leaderboard file: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Explicitly purge entries older than one year.
     * @return number of entries removed
     */
    public static synchronized int purgeEntriesOlderThanOneYear() {
        List<LeaderboardEntry> entries = loadEntries();
        long now = Instant.now().toEpochMilli();
        int before = entries.size();
        List<LeaderboardEntry> fresh = new ArrayList<>();
        for (LeaderboardEntry e : entries) {
            if ((now - e.getTimestamp()) <= ONE_YEAR_MILLIS) {
                fresh.add(e);
            }
        }
        if (fresh.size() != before) {
            writeEntries(fresh);
        }
        return before - fresh.size();
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
        System.out.println("DEBUG: LeaderboardStorage.getRankedEntries - Total entries loaded: " + entries.size());
        System.out.println("DEBUG: Mode filter: '" + modeFilter + "', Difficulty filter: '" + difficultyFilter + "'");

        List<LeaderboardEntry> filtered = new ArrayList<>();
        for (LeaderboardEntry e : entries) {
            boolean modeOk = (modeFilter == null) || e.mode.equals(modeFilter);
            boolean diffOk = (difficultyFilter == null) || e.difficulty.equals(difficultyFilter);
            System.out.println("DEBUG: Entry - Mode: '" + e.mode + "' (match: " + modeOk + "), " +
                             "Difficulty: '" + e.difficulty + "' (match: " + diffOk + "), " +
                             "Player: " + e.playerName);
            if (modeOk && diffOk) {
                filtered.add(e);
            }
        }
        System.out.println("DEBUG: After filtering: " + filtered.size() + " entries");

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
