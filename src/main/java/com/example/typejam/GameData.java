package com.example.typejam;

/**
 * Singleton class to hold game session data across scenes
 */
public class GameData {
    private static GameData instance;

    private String playerName;
    private String mode;
    private String difficulty;
    private String time;

    // Game results
    private double timeTaken; // in seconds
    private double accuracy; // percentage (0-100)
    private double wpm; // words per minute
    private int totalCharacters;
    private int correctCharacters;
    private int charactersTyped; // total characters typed by user
    private int errors; // incorrect characters typed

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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public double getTimeTaken() {
        return timeTaken;
    }

    public void setTimeTaken(double timeTaken) {
        this.timeTaken = timeTaken;
    }

    public double getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(double accuracy) {
        this.accuracy = accuracy;
    }

    public double getWpm() {
        return wpm;
    }

    public void setWpm(double wpm) {
        this.wpm = wpm;
    }

    public int getTotalCharacters() {
        return totalCharacters;
    }

    public void setTotalCharacters(int totalCharacters) {
        this.totalCharacters = totalCharacters;
    }

    public int getCorrectCharacters() {
        return correctCharacters;
    }

    public void setCorrectCharacters(int correctCharacters) {
        this.correctCharacters = correctCharacters;
    }

    public int getCharactersTyped() {
        return charactersTyped;
    }

    public void setCharactersTyped(int charactersTyped) {
        this.charactersTyped = charactersTyped;
    }

    public int getErrors() {
        return errors;
    }

    public void setErrors(int errors) {
        this.errors = errors;
    }

    public void reset() {
        playerName = null;
        mode = null;
        difficulty = null;
        time = null;
        timeTaken = 0;
        accuracy = 0;
        wpm = 0;
        totalCharacters = 0;
        correctCharacters = 0;
        charactersTyped = 0;
        errors = 0;
    }
}

