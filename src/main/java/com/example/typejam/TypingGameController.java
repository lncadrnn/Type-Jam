package com.example.typejam;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.animation.AnimationTimer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

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

    @FXML
    private javafx.scene.control.Button endPracticeButton;

    private String targetText;
    private long startTime;
    private AnimationTimer timer;
    private boolean gameStarted = false;
    private boolean gameFinished = false;
    private boolean endlessMode = false; // will represent Practice Mode
    private boolean timeChallengeMode = false; // will represent Challenge Mode
    private int timeLimitSeconds = 0;
    private long countdownStartTime = 0;
    private int correctChars = 0;
    private int totalCharsTyped = 0;
    private int errors = 0;
    private int totalIncorrectKeystrokes = 0; // Track all incorrect keystrokes (cumulative)
    private String previousTypedText = ""; // Track previous state to detect changes
    private int totalCharactersShown = 0; // Track total characters across all passages
    private int cumulativeCharsTyped = 0; // Cumulative characters typed across all passages
    private int cumulativeCorrectChars = 0; // Track correct characters across passages
    private javafx.beans.value.ChangeListener<String> typingFieldListener; // Store listener to remove/add it
    private boolean isLoadingNewText = false; // Flag to prevent listener execution during text reload
    private String lastUsedText = null; // Track last text to avoid immediate repetition

    // Text data loaded from JSON file
    private Map<String, List<String>> typingTexts;

    @FXML
    public void initialize() {
        // Stop background music during typing game
        SoundManager.getInstance().stopBackgroundMusic();
        SoundManager.getInstance().resetCountdown();

        // Load typing texts from JSON file
        loadTypingTexts();

        // Get data from GameData singleton
        GameData gameData = GameData.getInstance();
        String playerName = gameData.getPlayerName() != null ? gameData.getPlayerName() : "Player";
        String difficulty = gameData.getDifficulty() != null ? gameData.getDifficulty() : "Easy";
        String mode = gameData.getMode();
        String timeLimit = gameData.getTime();

        // Determine game mode with new names
        endlessMode = mode != null && mode.equalsIgnoreCase("Practice Mode");
        timeChallengeMode = mode != null && mode.equalsIgnoreCase("Challenge Mode");

        // Toggle End Practice button visibility for Practice Mode only
        if (endPracticeButton != null) {
            endPracticeButton.setVisible(endlessMode);
            endPracticeButton.setManaged(endlessMode);
        }

        // Parse time limit for Challenge Mode
        if (timeChallengeMode && timeLimit != null) {
            timeLimitSeconds = parseTimeLimit(timeLimit);
        }

        // Configure visuals
        if (endlessMode) {
            // Hide the infinity icon completely in Practice Mode
            if (infinityImage != null) {
                infinityImage.setVisible(false);
                infinityImage.setManaged(false);
            }
            if (timeText != null) {
                timeText.setVisible(true);
                timeText.setText("0:00");
            }
        } else {
            if (infinityImage != null) {
                infinityImage.setVisible(false);
                infinityImage.setManaged(false);
            }
            if (timeText != null) {
                timeText.setVisible(true);
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
        lastUsedText = targetText; // Track the initial text

        // Initialize total characters shown (for tracking across multiple passages)
        totalCharactersShown = targetText.length();

        // Display the target text
        updateTextDisplay("");

        // Create and store the listener for typing field
        typingFieldListener = (observable, oldValue, newValue) -> {
            // Skip listener execution if we're loading new text
            if (isLoadingNewText) {
                return;
            }

            // Play sound when new character is added
            if (newValue.length() > oldValue.length()) {
                int newCharIndex = newValue.length() - 1;
                if (newCharIndex < targetText.length()) {
                    char typedChar = newValue.charAt(newCharIndex);
                    char expectedChar = targetText.charAt(newCharIndex);

                    if (typedChar == expectedChar) {
                        // Correct character - play typing sound
                        SoundManager.getInstance().playTypingSound();
                    } else {
                        // Incorrect character - play error sound
                        SoundManager.getInstance().playErrorSound();
                    }
                }
            }

            if (!gameStarted && !newValue.isEmpty()) {
                startGame();
            }
            if (!gameFinished) {
                updateTextDisplay(newValue);
                checkCompletion(newValue);
            }
        };

        // Add listener to typing field
        typingField.textProperty().addListener(typingFieldListener);

        // Focus on typing field
        typingField.requestFocus();
    }

    private void loadTypingTexts() {
        try {
            InputStream inputStream = getClass().getResourceAsStream("/data/typing-texts.json");
            if (inputStream == null) {
                System.err.println("Could not find typing-texts.json file!");
                // Fallback to default texts
                typingTexts = Map.of(
                    "easy", List.of("The", "A", "To"),
                    "medium", List.of("Practice makes perfect."),
                    "hard", List.of("Programming requires precision and creativity.")
                );
                return;
            }

            InputStreamReader reader = new InputStreamReader(inputStream);
            Gson gson = new Gson();
            Type type = new TypeToken<Map<String, List<String>>>(){}.getType();
            typingTexts = gson.fromJson(reader, type);

            System.out.println("Successfully loaded typing texts from JSON file");
            System.out.println("Easy texts: " + typingTexts.get("easy").size());
            System.out.println("Medium texts: " + typingTexts.get("medium").size());
            System.out.println("Hard texts: " + typingTexts.get("hard").size());

            reader.close();
        } catch (IOException e) {
            System.err.println("Error loading typing texts: " + e.getMessage());
            e.printStackTrace();
            // Fallback to default texts
            typingTexts = Map.of(
                "easy", List.of("The", "A", "To"),
                "medium", List.of("Practice makes perfect."),
                "hard", List.of("Programming requires precision and creativity.")
            );
        }
    }

    private String selectTextByDifficulty(String difficulty) {
        return selectTextByDifficulty(difficulty, false);
    }

    private String selectTextByDifficulty(String difficulty, boolean avoidLastText) {
        if (typingTexts == null || typingTexts.isEmpty()) {
            System.err.println("Typing texts not loaded! Using fallback.");
            return "Type this text.";
        }

        List<String> texts = typingTexts.get(difficulty.toLowerCase());
        if (texts == null || texts.isEmpty()) {
            System.err.println("No texts found for difficulty: " + difficulty);
            // Fallback to easy texts
            texts = typingTexts.get("easy");
            if (texts == null || texts.isEmpty()) {
                return "Type this text.";
            }
        }

        // If we need to avoid the last text and there's more than one option
        if (avoidLastText && lastUsedText != null && texts.size() > 1) {
            // Create a list without the last used text
            List<String> availableTexts = new java.util.ArrayList<>(texts);
            availableTexts.remove(lastUsedText);

            // If removal was successful, select from the filtered list
            if (!availableTexts.isEmpty()) {
                String selectedText = availableTexts.get((int) (Math.random() * availableTexts.size()));
                lastUsedText = selectedText;
                return selectedText;
            }
        }

        // Select a random text from the full list
        String selectedText = texts.get((int) (Math.random() * texts.size()));
        lastUsedText = selectedText;
        return selectedText;
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
            // Practice Mode: count up timer
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

                    // Play countdown timer sound when 4 seconds remain
                    if (remainingSeconds == 4) {
                        SoundManager.getInstance().playCountdownTimer();
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

            // In Time Challenge mode, add current typing field chars to cumulative total
            int finalTotalCharsTyped = cumulativeCharsTyped + totalCharsTyped;
            int finalCorrectChars = cumulativeCorrectChars + correctChars;

            // Calculate accuracy: accounts for all incorrect keystrokes (even if corrected)
            // Total keystrokes = correct keystrokes + incorrect keystrokes
            int totalKeystrokesTyped = finalTotalCharsTyped + totalIncorrectKeystrokes;
            double accuracy = totalKeystrokesTyped > 0 ?
                ((double)finalTotalCharsTyped / totalKeystrokesTyped) * 100 : 0;
            if (accuracy < 0) accuracy = 0; // Ensure non-negative

            gameData.setAccuracy(accuracy);
            // In Time Challenge mode, use totalCharactersShown which includes all passages
            gameData.setTotalCharacters(totalCharactersShown);
            gameData.setCorrectCharacters(finalCorrectChars);
            gameData.setCharactersTyped(finalTotalCharsTyped);
            gameData.setErrors(totalIncorrectKeystrokes); // Use cumulative errors

            // Calculate WPM = (total characters typed / 5) / time in minutes
            double minutes = timeLimitSeconds / 60.0;
            double wpm = minutes > 0 ? (finalTotalCharsTyped / 5.0) / minutes : 0;
            gameData.setWpm(wpm);

            System.out.println("Accuracy: " + String.format("%.2f", accuracy) + "%");
            System.out.println("WPM: " + String.format("%.2f", wpm));
            System.out.println("Characters Typed: " + finalTotalCharsTyped);
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
                    // Add centered red rectangle for space-related errors
                    if (targetText.charAt(i) == ' ' || typedText.charAt(i) == ' ') {
                        // Create a StackPane to hold the space and centered rectangle
                        StackPane spaceContainer = new StackPane();

                        // Create a red vertical rectangle
                        Rectangle errorRect = new Rectangle(3, 25);
                        errorRect.setFill(Color.web("#dc3545"));

                        // Create invisible space text to maintain spacing
                        Text spaceText = new Text(" ");
                        spaceText.setFont(Font.font("Arial", 28));

                        // Add both to the StackPane (rectangle will be centered)
                        spaceContainer.getChildren().addAll(spaceText, errorRect);

                        // Add the StackPane to the TextFlow
                        dataTextFlow.getChildren().add(spaceContainer);
                        errors++;
                        continue; // Skip adding charText below
                    }
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
            // Practice Mode: load next text instead of finishing
            if (endlessMode && !gameFinished) {
                isLoadingNewText = true;
                cumulativeCharsTyped += totalCharsTyped; // add finished passage typed chars
                cumulativeCorrectChars += correctChars; // accumulate correct chars
                GameData gameData = GameData.getInstance();
                String difficulty = gameData.getDifficulty() != null ? gameData.getDifficulty() : "Easy";
                // Select new text, avoid repeating last one
                targetText = selectTextByDifficulty(difficulty, true);
                totalCharactersShown += targetText.length();
                previousTypedText = "";
                javafx.application.Platform.runLater(() -> {
                    typingField.clear();
                    updateTextDisplay("");
                    isLoadingNewText = false;
                    typingField.requestFocus();
                });
                System.out.println("Practice Mode: Loaded new text, continuing...");
                return;
            }

            // Challenge Mode: load next text and continue until timer ends
            if (timeChallengeMode && !gameFinished) {
                isLoadingNewText = true;
                cumulativeCharsTyped += totalCharsTyped;
                cumulativeCorrectChars += correctChars; // accumulate correct chars
                GameData gameData = GameData.getInstance();
                String difficulty = gameData.getDifficulty() != null ? gameData.getDifficulty() : "Easy";
                // Select new text, avoid repeating last one
                targetText = selectTextByDifficulty(difficulty, true);
                // Track total characters shown across passages
                totalCharactersShown += targetText.length();
                // Reset state for new passage
                previousTypedText = "";
                correctChars = 0;
                errors = 0;
                totalCharsTyped = 0;
                javafx.application.Platform.runLater(() -> {
                    typingField.clear();
                    updateTextDisplay("");
                    isLoadingNewText = false;
                    typingField.requestFocus();
                });
                System.out.println("Challenge Mode: Loaded new text, continuing until time is up...");
                return;
            }

            // For any other mode, finish and compute results
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
            double minutes = timeTakenSeconds / 60.0;
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

        // Restart background music when leaving typing game
        SoundManager.getInstance().startBackgroundMusic();

        try {
            NavigationHelper.navigateBack(event);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error navigating back: " + e.getMessage());
        }
    }

    private void loadLoadingScreen() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("loading-screen.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) typingField.getScene().getWindow();
        Scene scene = new Scene(root, 760, 495);
        stage.setScene(scene);
        stage.show();
    }

    private void loadResultScene() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("result-scene.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) typingField.getScene().getWindow();
        Scene scene = new Scene(root, 760, 495);
        stage.setScene(scene);
        stage.show();
    }

    // New: Load practice result scene
    private void loadPracticeResultScene() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("practice-result-scene.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) typingField.getScene().getWindow();
        Scene scene = new Scene(root, 760, 495);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void onEndPractice(ActionEvent event) {
        // Allows user to end Practice Mode manually and show results
        if (endlessMode && !gameFinished) {
            gameFinished = true;
            if (timer != null) timer.stop();
            typingField.setDisable(true);

            GameData gameData = GameData.getInstance();
            // Time taken = elapsed time; if game never started, force 0
            double timeTakenSeconds;
            if (!gameStarted || startTime <= 0) {
                timeTakenSeconds = 0.0;
            } else {
                long elapsedMillis = (System.nanoTime() - startTime) / 1_000_000;
                timeTakenSeconds = elapsedMillis / 1000.0;
            }
            gameData.setTimeTaken(timeTakenSeconds);

            // Include current field into cumulative count
            int finalTotalCharsTyped = cumulativeCharsTyped + totalCharsTyped;
            int finalCorrectChars = cumulativeCorrectChars + correctChars;
            int totalKeystrokesTyped = finalTotalCharsTyped + totalIncorrectKeystrokes;
            double accuracy = totalKeystrokesTyped > 0 ? ((double) finalTotalCharsTyped / totalKeystrokesTyped) * 100 : 0;
            if (accuracy < 0) accuracy = 0;
            gameData.setAccuracy(accuracy);
            gameData.setTotalCharacters(totalCharactersShown);
            gameData.setCorrectCharacters(finalCorrectChars);
            gameData.setCharactersTyped(finalTotalCharsTyped);
            gameData.setErrors(totalIncorrectKeystrokes);

            double minutes = timeTakenSeconds / 60.0;
            double wpm = minutes > 0 ? (finalTotalCharsTyped / 5.0) / minutes : 0;
            gameData.setWpm(wpm);

            // Navigate directly to result scene
            try {
                loadPracticeResultScene();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
