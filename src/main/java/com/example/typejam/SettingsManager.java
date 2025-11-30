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
    private double sfxVolume; // 0.0 to 1.0
    private double musicVolume; // 0.0 to 1.0

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
            sfxVolume = 0.5;
            musicVolume = 0.5;
            return;
        }

        try (FileReader reader = new FileReader(file)) {
            SettingsData data = GSON.fromJson(reader, SettingsData.class);
            if (data != null) {
                this.soundEffectsEnabled = data.soundEffectsEnabled;
                this.musicEnabled = data.musicEnabled;
                this.sfxVolume = data.sfxVolume;
                this.musicVolume = data.musicVolume;
            } else {
                // Default settings if file is empty
                soundEffectsEnabled = true;
                musicEnabled = true;
                sfxVolume = 0.5;
                musicVolume = 0.5;
            }
        } catch (IOException e) {
            System.err.println("Failed to load settings: " + e.getMessage());
            // Use defaults
            soundEffectsEnabled = true;
            musicEnabled = true;
            sfxVolume = 0.5;
            musicVolume = 0.5;
        }
    }

    public void saveSettings() {
        File file = getSettingsFile();
        SettingsData data = new SettingsData(soundEffectsEnabled, musicEnabled, sfxVolume, musicVolume);

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

    // Inner class for JSON serialization
    private static class SettingsData {
        private boolean soundEffectsEnabled;
        private boolean musicEnabled;
        private double sfxVolume;
        private double musicVolume;

        public SettingsData(boolean soundEffectsEnabled, boolean musicEnabled, double sfxVolume, double musicVolume) {
            this.soundEffectsEnabled = soundEffectsEnabled;
            this.musicEnabled = musicEnabled;
            this.sfxVolume = sfxVolume;
            this.musicVolume = musicVolume;
        }
    }
}

