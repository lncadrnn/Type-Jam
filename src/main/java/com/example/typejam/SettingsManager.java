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
 * Settings are persisted locally in user's home directory: typejam-settings.json
 */
public class SettingsManager {

    private static SettingsManager instance;
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private boolean soundEffectsEnabled;
    private boolean musicEnabled;

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
        Path path = Paths.get(System.getProperty("user.home"), "typejam-settings.json");
        return path.toFile();
    }

    private void loadSettings() {
        File file = getSettingsFile();
        if (!file.exists()) {
            // Default settings
            soundEffectsEnabled = true;
            musicEnabled = true;
            return;
        }

        try (FileReader reader = new FileReader(file)) {
            SettingsData data = GSON.fromJson(reader, SettingsData.class);
            if (data != null) {
                this.soundEffectsEnabled = data.soundEffectsEnabled;
                this.musicEnabled = data.musicEnabled;
            } else {
                // Default settings if file is empty
                soundEffectsEnabled = true;
                musicEnabled = true;
            }
        } catch (IOException e) {
            System.err.println("Failed to load settings: " + e.getMessage());
            // Use defaults
            soundEffectsEnabled = true;
            musicEnabled = true;
        }
    }

    public void saveSettings() {
        File file = getSettingsFile();
        SettingsData data = new SettingsData(soundEffectsEnabled, musicEnabled);

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

    // Inner class for JSON serialization
    private static class SettingsData {
        private boolean soundEffectsEnabled;
        private boolean musicEnabled;

        public SettingsData(boolean soundEffectsEnabled, boolean musicEnabled) {
            this.soundEffectsEnabled = soundEffectsEnabled;
            this.musicEnabled = musicEnabled;
        }
    }
}

