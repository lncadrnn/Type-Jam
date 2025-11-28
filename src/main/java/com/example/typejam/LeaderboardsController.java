package com.example.typejam;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class LeaderboardsController {

    @FXML
    private Button back_btn;

    @FXML
    private Button modeBtn;

    @FXML
    private Button modeResetBtn;

    @FXML
    private Button difficultyBtn;

    @FXML
    private Button difficultyResetBtn;

    @FXML
    private Button timeChallengeBtn;

    @FXML
    private Button endlessModeBtn;

    @FXML
    private Button easyBtn;

    @FXML
    private Button mediumBtn;

    @FXML
    private Button hardBtn;

    @FXML
    private VBox modeDropdown;

    @FXML
    private VBox difficultyDropdown;

    @FXML
    private VBox leaderboardContent;

    private String selectedMode = null;
    private String selectedDifficulty = null;

    @FXML
    public void initialize() {
        // Load all leaderboard data by default (no filters applied)
        loadLeaderboardData();
    }

    @FXML
    private void onBack(ActionEvent event) {
        try {
            switchTo(event, "main-menu.fxml");
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error navigating to main menu: " + e.getMessage());
        }
    }

    @FXML
    private void toggleModeDropdown(ActionEvent event) {
        boolean isVisible = modeDropdown.isVisible();
        hideAllDropdowns();
        if (!isVisible) {
            modeDropdown.setVisible(true);
            modeDropdown.setManaged(true);
        }
    }

    @FXML
    private void toggleDifficultyDropdown(ActionEvent event) {
        boolean isVisible = difficultyDropdown.isVisible();
        hideAllDropdowns();
        if (!isVisible) {
            difficultyDropdown.setVisible(true);
            difficultyDropdown.setManaged(true);
        }
    }

    @FXML
    private void onTimeChallengeFilter(ActionEvent event) {
        selectedMode = "Time Challenge";
        modeBtn.setText("Time Challenge");
        modeResetBtn.setVisible(true);
        modeResetBtn.setManaged(true);
        hideAllDropdowns();
        loadLeaderboardData();
    }

    @FXML
    private void onEndlessModeFilter(ActionEvent event) {
        selectedMode = "Endless Mode";
        modeBtn.setText("Endless Mode");
        modeResetBtn.setVisible(true);
        modeResetBtn.setManaged(true);
        hideAllDropdowns();
        loadLeaderboardData();
    }

    @FXML
    private void onEasyFilter(ActionEvent event) {
        selectedDifficulty = "Easy";
        difficultyBtn.setText("Easy");
        difficultyResetBtn.setVisible(true);
        difficultyResetBtn.setManaged(true);
        hideAllDropdowns();
        loadLeaderboardData();
    }

    @FXML
    private void onMediumFilter(ActionEvent event) {
        selectedDifficulty = "Medium";
        difficultyBtn.setText("Medium");
        difficultyResetBtn.setVisible(true);
        difficultyResetBtn.setManaged(true);
        hideAllDropdowns();
        loadLeaderboardData();
    }

    @FXML
    private void onHardFilter(ActionEvent event) {
        selectedDifficulty = "Hard";
        difficultyBtn.setText("Hard");
        difficultyResetBtn.setVisible(true);
        difficultyResetBtn.setManaged(true);
        hideAllDropdowns();
        loadLeaderboardData();
    }

    @FXML
    private void resetModeFilter(ActionEvent event) {
        selectedMode = null;
        modeBtn.setText("MODE ▼");
        modeResetBtn.setVisible(false);
        modeResetBtn.setManaged(false);
        loadLeaderboardData();
    }

    @FXML
    private void resetDifficultyFilter(ActionEvent event) {
        selectedDifficulty = null;
        difficultyBtn.setText("DIFFICULTY ▼");
        difficultyResetBtn.setVisible(false);
        difficultyResetBtn.setManaged(false);
        loadLeaderboardData();
    }

    private void hideAllDropdowns() {
        modeDropdown.setVisible(false);
        modeDropdown.setManaged(false);
        difficultyDropdown.setVisible(false);
        difficultyDropdown.setManaged(false);
    }

    private void loadLeaderboardData() {
        // Clear current leaderboard content
        leaderboardContent.getChildren().clear();

        // Build filter string for logging
        String filterDescription = "all entries";
        if (selectedMode != null && selectedDifficulty != null) {
            filterDescription = "Mode: " + selectedMode + ", Difficulty: " + selectedDifficulty;
        } else if (selectedMode != null) {
            filterDescription = "Mode: " + selectedMode;
        } else if (selectedDifficulty != null) {
            filterDescription = "Difficulty: " + selectedDifficulty;
        }

        System.out.println("Loading leaderboard data for: " + filterDescription);

        // TODO: Implement actual leaderboard data loading based on the filters
        // You can add logic here to:
        // 1. Query a database or JSON file for leaderboard entries
        // 2. Filter entries based on selectedMode and/or selectedDifficulty
        // 3. Display the filtered entries in the leaderboardContent VBox

        // Example filter logic:
        // - If selectedMode is "Time Challenge" and selectedDifficulty is "Easy"
        //   -> Show only Time Challenge + Easy entries
        // - If only selectedMode is set -> Show all entries for that mode
        // - If only selectedDifficulty is set -> Show all entries for that difficulty
        // - If neither is set -> Show all entries
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

