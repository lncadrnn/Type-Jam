package com.example.typejam;

import javafx.animation.AnimationTimer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

import java.io.IOException;

public class TypingGameController {

    @FXML
    private TextField typingField;

    @FXML
    private Text playerNameText;

    @FXML
    private Text difficultyText;

    @FXML
    private Text timeText;

    @FXML
    private TextFlow dataTextFlow;

    @FXML
    private javafx.scene.control.Button backButton;

    @FXML
    private ImageView infinityImage;

    private String targetText;
    private long startTime;
    private AnimationTimer timer;
    private boolean gameStarted = false;
    private boolean gameFinished = false;
    private boolean endlessMode = false;
    private boolean timeChallengeMode = false;
    private int timeLimitSeconds = 0;
    private long countdownStartTime = 0;
    private int correctChars = 0;
    private int totalCharsTyped = 0;
    private int errors = 0;
    private int totalIncorrectKeystrokes = 0; // Track all incorrect keystrokes (cumulative)
    private String previousTypedText = ""; // Track previous state to detect changes

    // Sample texts based on difficulty
    private static final String[] EASY_TEXTS = {
        "The",
        "A",
        "To"
    };

    private static final String[] MEDIUM_TEXTS = {
        "In the midst of chaos, there is also opportunity. The wise warrior avoids the battle.",
        "Success is not final, failure is not fatal: it is the courage to continue that counts.",
        "The only impossible journey is the one you never begin. Believe in yourself always."
    };

    private static final String[] HARD_TEXTS = {
        "Programming is the art of telling another human what one wants the computer to do. It requires precision, logic, and creativity in equal measure.",
        "The difference between theory and practice is that in theory there is no difference, but in practice there is. This paradox applies to many areas of life.",
        "Innovation distinguishes between a leader and a follower. Those who dare to think differently often change the world in profound ways."
    };

    @FXML
    public void initialize() {
        // Get data from GameData singleton
        GameData gameData = GameData.getInstance();
        String playerName = gameData.getPlayerName() != null ? gameData.getPlayerName() : "Player";
        String difficulty = gameData.getDifficulty() != null ? gameData.getDifficulty() : "Easy";
        String mode = gameData.getMode();
        String timeLimit = gameData.getTime();

        // Determine game mode
        endlessMode = mode != null && mode.equalsIgnoreCase("Endless Mode");
        timeChallengeMode = mode != null && mode.equalsIgnoreCase("Time Challenge");

        // Parse time limit for Time Challenge mode
        if (timeChallengeMode && timeLimit != null) {
            timeLimitSeconds = parseTimeLimit(timeLimit);
        }

        // Configure timer/infinity visuals based on mode
        if (endlessMode) {
            // Endless mode: hide infinity image, show time counting up from 0:00
            if (infinityImage != null) infinityImage.setVisible(false);
            if (timeText != null) {
                timeText.setVisible(true);
                timeText.setText("0:00");
            }
        } else {
            // Time challenge or other mode: hide infinity image, show timer
            if (infinityImage != null) infinityImage.setVisible(false);
            if (timeText != null) {
                timeText.setVisible(true);
                // Set initial countdown time for Time Challenge mode
                if (timeChallengeMode && timeLimitSeconds > 0) {
                    int minutes = timeLimitSeconds / 60;
                    int seconds = timeLimitSeconds % 60;
                    timeText.setText(String.format("%d:%02d", minutes, seconds));
                }
            }
        }

        // Truncate player name to 30 characters maximum with ellipsis
        if (playerName.length() > 30) {
            playerName = playerName.substring(0, 30) + "...";
        }

        // Set the text values
        playerNameText.setText(playerName);
        difficultyText.setText(difficulty + " Level");

        // Select text based on difficulty
        targetText = selectTextByDifficulty(difficulty);

        // Display the target text
        updateTextDisplay("");

        // Add listener to typing field
        typingField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!gameStarted && !newValue.isEmpty()) {
                startGame();
            }
            if (!gameFinished) {
                updateTextDisplay(newValue);
                checkCompletion(newValue);
            }
        });

        // Focus on typing field
        typingField.requestFocus();
    }

    private String selectTextByDifficulty(String difficulty) {
        String[] texts;
        switch (difficulty.toLowerCase()) {
            case "easy":
                texts = EASY_TEXTS;
                break;
            case "medium":
                texts = MEDIUM_TEXTS;
                break;
            case "hard":
                texts = HARD_TEXTS;
                break;
            default:
                texts = EASY_TEXTS;
        }
        // Select a random text from the array
        return texts[(int) (Math.random() * texts.length)];
    }

    private int parseTimeLimit(String timeLimit) {
        // Parse time limits like "1 Minute", "3 Minutes", "5 Minutes"
        if (timeLimit == null) return 0;

        String[] parts = timeLimit.split(" ");
        if (parts.length > 0) {
            try {
                int minutes = Integer.parseInt(parts[0]);
                return minutes * 60; // Convert to seconds
            } catch (NumberFormatException e) {
                System.err.println("Could not parse time limit: " + timeLimit);
                return 0;
            }
        }
        return 0;
    }

    private void startGame() {
        gameStarted = true;

        startTime = System.nanoTime();
        countdownStartTime = System.nanoTime();

        if (endlessMode) {
            // Endless mode: count up timer starting from 0
            timer = new AnimationTimer() {
                @Override
                public void handle(long now) {
                    long elapsedMillis = (now - startTime) / 1_000_000;
                    int seconds = (int) (elapsedMillis / 1000);
                    int minutes = seconds / 60;
                    seconds = seconds % 60;
                    if (timeText != null) {
                        timeText.setText(String.format("%d:%02d", minutes, seconds));
                    }
                }
            };
        } else if (timeChallengeMode && timeLimitSeconds > 0) {
            // Time Challenge mode: countdown timer
            timer = new AnimationTimer() {
                @Override
                public void handle(long now) {
                    long elapsedMillis = (now - countdownStartTime) / 1_000_000;
                    int elapsedSeconds = (int) (elapsedMillis / 1000);
                    int remainingSeconds = timeLimitSeconds - elapsedSeconds;

                    if (remainingSeconds <= 0) {
                        // Time's up!
                        remainingSeconds = 0;
                        if (timeText != null) {
                            timeText.setText("0:00");
                        }
                        handleTimeUp();
                        return;
                    }

                    int minutes = remainingSeconds / 60;
                    int seconds = remainingSeconds % 60;
                    if (timeText != null) {
                        timeText.setText(String.format("%d:%02d", minutes, seconds));
                    }
                }
            };
        } else {
            // Regular count-up timer for other modes
            timer = new AnimationTimer() {
                @Override
                public void handle(long now) {
                    long elapsedMillis = (now - startTime) / 1_000_000;
                    int seconds = (int) (elapsedMillis / 1000);
                    int minutes = seconds / 60;
                    seconds = seconds % 60;
                    if (timeText != null) {
                        timeText.setText(String.format("%d:%02d", minutes, seconds));
                    }
                }
            };
        }
        timer.start();
    }

    private void handleTimeUp() {
        if (!gameFinished) {
            gameFinished = true;
            if (timer != null) {
                timer.stop();
            }
            typingField.setDisable(true);
            System.out.println("Time's up! Game over.");

            // Calculate partial results
            GameData gameData = GameData.getInstance();
            gameData.setTimeTaken(timeLimitSeconds); // Used full time

            // Calculate accuracy: accounts for all incorrect keystrokes (even if corrected)
            // Total keystrokes = correct keystrokes + incorrect keystrokes
            int totalKeystrokesTyped = totalCharsTyped + totalIncorrectKeystrokes;
            double accuracy = totalKeystrokesTyped > 0 ?
                ((double)totalCharsTyped / totalKeystrokesTyped) * 100 : 0;
            if (accuracy < 0) accuracy = 0; // Ensure non-negative

            gameData.setAccuracy(accuracy);
            gameData.setTotalCharacters(targetText.length());
            gameData.setCorrectCharacters(correctChars);
            gameData.setCharactersTyped(totalCharsTyped);
            gameData.setErrors(totalIncorrectKeystrokes); // Use cumulative errors

            // Calculate WPM = (total characters typed / 5) / time in minutes
            double minutes = timeLimitSeconds / 60.0;
            double wpm = minutes > 0 ? (totalCharsTyped / 5.0) / minutes : 0;
            gameData.setWpm(wpm);

            System.out.println("Accuracy: " + String.format("%.2f", accuracy) + "%");
            System.out.println("WPM: " + String.format("%.2f", wpm));
            System.out.println("Characters Typed: " + totalCharsTyped);
            System.out.println("Total Incorrect Keystrokes: " + totalIncorrectKeystrokes);

            // Navigate to loading screen
            try {
                loadLoadingScreen();
            } catch (IOException e) {
                e.printStackTrace();
                System.err.println("Error loading loading-screen.fxml: " + e.getMessage());
            }
        }
    }

    private void updateTextDisplay(String typedText) {
        dataTextFlow.getChildren().clear();

        // Track new incorrect keystrokes by comparing with previous state
        int minLength = Math.min(typedText.length(), previousTypedText.length());

        // Check for new characters typed or corrections
        if (typedText.length() > previousTypedText.length()) {
            // New character(s) added - check if they're incorrect
            for (int i = minLength; i < typedText.length(); i++) {
                if (i < targetText.length() && typedText.charAt(i) != targetText.charAt(i)) {
                    totalIncorrectKeystrokes++;
                }
            }
        } else if (typedText.length() < previousTypedText.length()) {
            // Character(s) deleted (backspace) - no change to totalIncorrectKeystrokes
            // The error already counted when it was first typed
        } else {
            // Same length but character changed at some position
            for (int i = 0; i < typedText.length(); i++) {
                if (typedText.charAt(i) != previousTypedText.charAt(i)) {
                    // Character changed - if the new one is incorrect, count it
                    if (i < targetText.length() && typedText.charAt(i) != targetText.charAt(i)) {
                        totalIncorrectKeystrokes++;
                    }
                }
            }
        }

        previousTypedText = typedText;

        // Reset counters for current state display
        correctChars = 0;
        errors = 0;
        totalCharsTyped = typedText.length();

        for (int i = 0; i < targetText.length(); i++) {
            Text charText = new Text(String.valueOf(targetText.charAt(i)));
            charText.setFont(Font.font("Arial", 28));

            if (i < typedText.length()) {
                // Character has been typed
                if (typedText.charAt(i) == targetText.charAt(i)) {
                    // Correct character - green
                    charText.setFill(Color.web("#28a745"));
                    correctChars++;
                } else {
                    // Incorrect character - red
                    charText.setFill(Color.web("#dc3545"));
                    errors++;
                }
            } else {
                // Character not yet typed - black
                charText.setFill(Color.BLACK);
            }

            dataTextFlow.getChildren().add(charText);
        }
    }

    private void checkCompletion(String typedText) {
        if (typedText.equals(targetText)) {
            gameFinished = true;
            if (timer != null) {
                timer.stop();
            }
            typingField.setDisable(true);

            // Calculate game results
            GameData gameData = GameData.getInstance();

            double timeTakenSeconds;
            if (endlessMode) {
                // For endless mode, just count the seconds consumed
                long elapsedMillis = (System.nanoTime() - startTime) / 1_000_000;
                timeTakenSeconds = elapsedMillis / 1000.0;
                gameData.setTimeTaken(timeTakenSeconds);

                int seconds = (int) timeTakenSeconds;
                int minutes = seconds / 60;
                seconds = seconds % 60;
                System.out.println("Endless mode completed in " + minutes + ":" + String.format("%02d", seconds));
            } else if (timeChallengeMode) {
                // For timed challenge: time consumed = time spent
                long elapsedMillis = (System.nanoTime() - startTime) / 1_000_000;
                timeTakenSeconds = elapsedMillis / 1000.0; // Time consumed (time spent)
                gameData.setTimeTaken(timeTakenSeconds);

                int seconds = (int) timeTakenSeconds;
                int minutes = seconds / 60;
                seconds = seconds % 60;
                System.out.println("Time challenge completed in " + minutes + ":" + String.format("%02d", seconds));
            } else {
                // For other modes, calculate time spent
                long elapsedMillis = (System.nanoTime() - startTime) / 1_000_000;
                timeTakenSeconds = elapsedMillis / 1000.0;
                gameData.setTimeTaken(timeTakenSeconds);

                int seconds = (int) timeTakenSeconds;
                int minutes = seconds / 60;
                seconds = seconds % 60;
                System.out.println("Game completed in " + minutes + ":" + String.format("%02d", seconds));
            }

            // Calculate accuracy: accounts for all incorrect keystrokes (even if corrected)
            // Total keystrokes = correct keystrokes + incorrect keystrokes
            int totalKeystrokesTyped = totalCharsTyped + totalIncorrectKeystrokes;
            double accuracy = totalKeystrokesTyped > 0 ?
                ((double)totalCharsTyped / totalKeystrokesTyped) * 100 : 0;
            if (accuracy < 0) accuracy = 0; // Ensure non-negative

            gameData.setAccuracy(accuracy);
            gameData.setTotalCharacters(targetText.length());
            gameData.setCorrectCharacters(correctChars);
            gameData.setCharactersTyped(totalCharsTyped);
            gameData.setErrors(totalIncorrectKeystrokes); // Use cumulative errors

            // Calculate WPM = (total characters typed / 5) / time in minutes
            // For time calculation, use actual time spent (not time remaining)
            double timeForWpm;
            if (timeChallengeMode) {
                long elapsedMillis = (System.nanoTime() - startTime) / 1_000_000;
                timeForWpm = elapsedMillis / 1000.0;
            } else {
                timeForWpm = timeTakenSeconds;
            }

            double minutes = timeForWpm / 60.0;
            double wpm = minutes > 0 ? (totalCharsTyped / 5.0) / minutes : 0;
            gameData.setWpm(wpm);

            System.out.println("Accuracy: " + String.format("%.2f", accuracy) + "%");
            System.out.println("WPM: " + String.format("%.2f", wpm));
            System.out.println("Characters Typed: " + totalCharsTyped);
            System.out.println("Total Incorrect Keystrokes: " + totalIncorrectKeystrokes);

            // Navigate to loading screen
            try {
                loadLoadingScreen();
            } catch (IOException e) {
                e.printStackTrace();
                System.err.println("Error loading loading-screen.fxml: " + e.getMessage());
            }
        }
    }

    @FXML
    public void onBack(ActionEvent event) {
        System.out.println("Back button clicked!");
        if (timer != null) {
            timer.stop();
        }
        try {
            switchTo(event, "ready-scene.fxml");
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error loading ready-scene.fxml: " + e.getMessage());
        }
    }

    private void switchTo(ActionEvent event, String fxmlName) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlName));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 760, 495);
        stage.setScene(scene);
        stage.show();
    }

    private void loadLoadingScreen() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("loading-screen.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) typingField.getScene().getWindow();
        Scene scene = new Scene(root, 760, 495);
        stage.setScene(scene);
        stage.show();
    }
}
