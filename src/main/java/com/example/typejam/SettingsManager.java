package com.example.typejam;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Singleton class to manage application settings like sound effects and music preferences.
 * Settings are persisted locally in: src/main/resources/data/typejam-settings.json
 */
public class SettingsManager {

    private static SettingsManager instance;
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private boolean soundEffectsEnabled;
    private boolean musicEnabled;
    private double sfxVolume; // 0.0 to 1.0
    private double musicVolume; // 0.0 to 1.0
    private boolean firstLaunch; // new: turn on SFX/music by default on first run

    private SettingsManager() {
        // Load settings from file or use defaults
        loadSettings();
    }

    public static SettingsManager getInstance() {
        if (instance == null) {
            instance = new SettingsManager();
        }
        return instance;
    }

    private File getSettingsFile() {
        // Try to get the data directory from resources first (works in both IDE and JAR)
        try {
            java.net.URL resourceUrl = SettingsManager.class.getResource("/data/typing-texts.json");
            if (resourceUrl != null) {
                String resourcePath = resourceUrl.getPath();
                // Handle Windows paths (remove leading slash if present)
                if (resourcePath.startsWith("/") && resourcePath.contains(":")) {
                    resourcePath = resourcePath.substring(1);
                }
                Path dataDir = Paths.get(resourcePath).getParent();
                if (dataDir != null && dataDir.toFile().exists()) {
                    File settingsFile = dataDir.resolve("typejam-settings.json").toFile();
                    System.out.println("✓ SETTINGS STORAGE LOCATION: " + settingsFile.getAbsolutePath());
                    return settingsFile;
                }
            }
        } catch (Exception e) {
            System.err.println("Could not locate resources/data directory: " + e.getMessage());
        }

        // Fallback: use project structure path (for development)
        String projectRoot = System.getProperty("user.dir");
        Path dataDir = Paths.get(projectRoot, "src", "main", "resources", "data");

        // Create data directory if it doesn't exist
        File dataDirFile = dataDir.toFile();
        if (!dataDirFile.exists()) {
            dataDirFile.mkdirs();
        }

        File settingsFile = dataDir.resolve("typejam-settings.json").toFile();
        System.out.println("✓ SETTINGS STORAGE LOCATION (FALLBACK): " + settingsFile.getAbsolutePath());
        return settingsFile;
    }

    private void loadSettings() {
        File file = getSettingsFile();
        if (!file.exists()) {
            // Default settings on first launch
            soundEffectsEnabled = true;
            musicEnabled = true;
            sfxVolume = 0.5;
            musicVolume = 0.5;
            firstLaunch = true; // mark first launch until app starts
            return;
        }

        try (FileReader reader = new FileReader(file)) {
            SettingsData data = GSON.fromJson(reader, SettingsData.class);
            if (data != null) {
                this.soundEffectsEnabled = data.soundEffectsEnabled;
                this.musicEnabled = data.musicEnabled;
                this.sfxVolume = data.sfxVolume;
                this.musicVolume = data.musicVolume;
                this.firstLaunch = data.firstLaunch;
                // Ensure defaults enabled on first launch
                if (this.firstLaunch) {
                    this.soundEffectsEnabled = true;
                    this.musicEnabled = true;
                }
            } else {
                // Default settings if file is empty
                soundEffectsEnabled = true;
                musicEnabled = true;
                sfxVolume = 0.5;
                musicVolume = 0.5;
                firstLaunch = true;
            }
        } catch (IOException e) {
            System.err.println("Failed to load settings: " + e.getMessage());
            // Use defaults
            soundEffectsEnabled = true;
            musicEnabled = true;
            sfxVolume = 0.5;
            musicVolume = 0.5;
            firstLaunch = true;
        }
    }

    public void saveSettings() {
        File file = getSettingsFile();
        SettingsData data = new SettingsData(soundEffectsEnabled, musicEnabled, sfxVolume, musicVolume, firstLaunch);
        try (FileWriter writer = new FileWriter(file)) {
            GSON.toJson(data, writer);
        } catch (IOException e) {
            System.err.println("Failed to save settings: " + e.getMessage());
        }
    }

    public boolean isSoundEffectsEnabled() {
        return soundEffectsEnabled;
    }

    public void setSoundEffectsEnabled(boolean enabled) {
        this.soundEffectsEnabled = enabled;
        saveSettings();
    }

    public boolean isMusicEnabled() {
        return musicEnabled;
    }

    public void setMusicEnabled(boolean enabled) {
        this.musicEnabled = enabled;
        saveSettings();
    }

    public double getSfxVolume() {
        return sfxVolume;
    }

    public void setSfxVolume(double volume) {
        this.sfxVolume = Math.max(0.0, Math.min(1.0, volume)); // Clamp between 0 and 1
        saveSettings();
    }

    public double getMusicVolume() {
        return musicVolume;
    }

    public void setMusicVolume(double volume) {
        this.musicVolume = Math.max(0.0, Math.min(1.0, volume)); // Clamp between 0 and 1
        saveSettings();
    }

    // Accessors for firstLaunch
    public boolean isFirstLaunch() {
        return firstLaunch;
    }

    public void setFirstLaunch(boolean firstLaunch) {
        this.firstLaunch = firstLaunch;
        saveSettings();
    }

    // Inner class for JSON serialization
    private static class SettingsData {
        private boolean soundEffectsEnabled;
        private boolean musicEnabled;
        private double sfxVolume;
        private double musicVolume;
        private boolean firstLaunch; // persist firstLaunch

        public SettingsData(boolean soundEffectsEnabled, boolean musicEnabled, double sfxVolume, double musicVolume, boolean firstLaunch) {
            this.soundEffectsEnabled = soundEffectsEnabled;
            this.musicEnabled = musicEnabled;
            this.sfxVolume = sfxVolume;
            this.musicVolume = musicVolume;
            this.firstLaunch = firstLaunch;
        }
    }
}
