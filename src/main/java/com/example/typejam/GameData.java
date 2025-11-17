package com.example.typejam;

/**
 * Singleton class to hold game session data across scenes
 */
public class GameData {
    private static GameData instance;

    private String playerName;
    private String mode;
    private String difficulty;

    private GameData() {
        // Private constructor for singleton
    }

    public static GameData getInstance() {
        if (instance == null) {
            instance = new GameData();
        }
        return instance;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public void reset() {
        playerName = null;
        mode = null;
        difficulty = null;
    }
}

