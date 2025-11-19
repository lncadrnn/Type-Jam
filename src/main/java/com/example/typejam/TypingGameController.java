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

    // Sample texts based on difficulty
    private static final String[] EASY_TEXTS = {
        "The quick brown fox jumps over the lazy dog.",
        "A journey of a thousand miles begins with a single step.",
        "To be or not to be, that is the question."
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

        // Determine endless mode
        endlessMode = mode != null && mode.equalsIgnoreCase("Endless Mode");

        // Configure timer/infinity visuals based on mode
        if (endlessMode) {
            // Show infinity image, hide time text
            if (infinityImage != null) infinityImage.setVisible(true);
            if (timeText != null) timeText.setVisible(false);
        } else {
            // Time challenge or other mode: hide infinity image, show timer
            if (infinityImage != null) infinityImage.setVisible(false);
            if (timeText != null) timeText.setVisible(true);
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

    private void startGame() {
        gameStarted = true;
        if (endlessMode) {
            // Endless mode: no timer needed
            return;
        }
        startTime = System.nanoTime();

        // Start timer (only for timed modes)
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
        timer.start();
    }

    private void updateTextDisplay(String typedText) {
        dataTextFlow.getChildren().clear();

        for (int i = 0; i < targetText.length(); i++) {
            Text charText = new Text(String.valueOf(targetText.charAt(i)));
            charText.setFont(Font.font("Arial", 16));

            if (i < typedText.length()) {
                // Character has been typed
                if (typedText.charAt(i) == targetText.charAt(i)) {
                    // Correct character - green
                    charText.setFill(Color.web("#28a745"));
                } else {
                    // Incorrect character - red
                    charText.setFill(Color.web("#dc3545"));
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

            if (!endlessMode) {
                // Calculate final time only for timed modes
                long elapsedMillis = (System.nanoTime() - startTime) / 1_000_000;
                int seconds = (int) (elapsedMillis / 1000);
                int minutes = seconds / 60;
                seconds = seconds % 60;
                System.out.println("Game completed in " + minutes + ":" + String.format("%02d", seconds));
            } else {
                System.out.println("Endless mode text completed.");
            }
            // You can add a completion dialog or transition here
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
}
