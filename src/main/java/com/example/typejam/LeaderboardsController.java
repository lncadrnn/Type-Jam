package com.example.typejam;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Priority;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.application.Platform;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

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
            NavigationHelper.navigateBack(event);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error navigating back: " + e.getMessage());
        }
    }

    @FXML
    private void toggleModeDropdown(ActionEvent event) {
        boolean isVisible = modeDropdown.isVisible();
        hideAllDropdowns();
        if (!isVisible) {
            modeDropdown.setVisible(true);
            modeDropdown.toFront();
        }
    }

    @FXML
    private void toggleDifficultyDropdown(ActionEvent event) {
        boolean isVisible = difficultyDropdown.isVisible();
        hideAllDropdowns();
        if (!isVisible) {
            difficultyDropdown.setVisible(true);
            difficultyDropdown.toFront();
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
        difficultyDropdown.setVisible(false);
    }

    private void loadLeaderboardData() {
        leaderboardContent.getChildren().clear();
        List<LeaderboardStorage.LeaderboardEntry> entries = LeaderboardStorage.getRankedEntries(selectedMode, selectedDifficulty);
        populateLeaderboard(entries);
    }

    private HBox createLeaderboardEntryUI(int rank, LeaderboardStorage.LeaderboardEntry entry) {
        HBox entryBox = new HBox();
        entryBox.setAlignment(Pos.CENTER_LEFT);
        entryBox.setPrefHeight(40.0);
        entryBox.setPrefWidth(600.0);
        entryBox.setSpacing(15.0);
        entryBox.setStyle("-fx-background-color: white; -fx-background-radius: 15; " +
                "-fx-border-color: #2b5237; -fx-border-radius: 15; -fx-border-width: 2; " +
                "-fx-padding: 5 15 5 15;");

        Node rankNode = createRankGraphic(rank);

        Text nameText = new Text(entry.getPlayerName());
        nameText.setFill(javafx.scene.paint.Color.web("#2b5237"));
        nameText.setFont(Font.font("Arial", FontWeight.BOLD, 16));

        Text modeText = new Text(entry.getMode());
        modeText.setFill(javafx.scene.paint.Color.web("#2b5237"));
        modeText.setFont(Font.font("Arial", FontWeight.NORMAL, 12));

        Text diffText = new Text(entry.getDifficulty());
        diffText.setFill(javafx.scene.paint.Color.web("#2b5237"));
        diffText.setFont(Font.font("Arial", FontWeight.NORMAL, 12));

        String starString = getStarString(entry.getStars());
        Text starsText = new Text(starString);
        starsText.setFill(javafx.scene.paint.Color.web("#f6b539"));
        starsText.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        starsText.setTextAlignment(javafx.scene.text.TextAlignment.RIGHT);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        entryBox.getChildren().addAll(rankNode, nameText, modeText, diffText, spacer, starsText);
        return entryBox;
    }

    private Node createRankGraphic(int rank) {
        String fileName = getRankImageFileName(rank);
        URL url = getClass().getResource("/assets/" + fileName);
        if (url != null) {
            ImageView iv = new ImageView(new Image(url.toExternalForm()));
            iv.setFitHeight(32);
            iv.setFitWidth(32);
            iv.setPreserveRatio(true);
            return iv;
        }
        // Fallback to text if image not found
        Text fallback = new Text(String.valueOf(rank));
        fallback.setFill(javafx.scene.paint.Color.web("#2b5237"));
        fallback.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        return fallback;
    }

    private String getRankImageFileName(int rank) {
        switch (rank) {
            case 1: return "1st-place.png";
            case 2: return "2nd-place.png";
            case 3: return "3rd-place.png";
            default: return "4th-below-place.png"; // ranks 4-10
        }
    }

    /**
     * Converts a star rating (1-5) to a star string
     */
    private String getStarString(int stars) {
        if (stars < 1) stars = 1;
        if (stars > 5) stars = 5;

        StringBuilder starString = new StringBuilder();
        for (int i = 0; i < 5; i++) {
            if (i < stars) {
                starString.append("★"); // Filled star
            } else {
                starString.append("☆"); // Empty star
            }
        }
        return starString.toString();
    }

    /**
     * Populates the leaderboard with entries
     */
    private void populateLeaderboard(List<LeaderboardStorage.LeaderboardEntry> entries) {
        leaderboardContent.getChildren().clear();

        int rank = 1;
        for (LeaderboardStorage.LeaderboardEntry entry : entries) {
            HBox entryUI = createLeaderboardEntryUI(rank, entry);
            leaderboardContent.getChildren().add(entryUI);
            rank++;

            // Limit to top 10 entries now
            if (rank > 10) break;
        }

        // If no entries, show a message
        if (entries.isEmpty()) {
            Text noDataText = new Text("No leaderboard data yet. Play a game to populate!");
            noDataText.setFill(javafx.scene.paint.Color.web("#2b5237"));
            noDataText.setFont(Font.font("Arial", FontWeight.BOLD, 16));
            leaderboardContent.getChildren().add(noDataText);
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
