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

        // TODO: Replace this with actual database/JSON data loading
        // For now, load sample data
        List<LeaderboardEntry> entries = getSampleLeaderboardData();

        // Populate the leaderboard UI
        populateLeaderboard(entries);
    }

    /**
     * Creates a visual leaderboard entry HBox
     */
    private HBox createLeaderboardEntryUI(int rank, String playerName, int stars) {
        HBox entryBox = new HBox();
        entryBox.setAlignment(Pos.CENTER_LEFT);
        entryBox.setPrefHeight(40.0);
        entryBox.setPrefWidth(600.0);
        entryBox.setSpacing(15.0);
        entryBox.setStyle("-fx-background-color: white; -fx-background-radius: 15; " +
                         "-fx-border-color: #2b5237; -fx-border-radius: 15; -fx-border-width: 2; " +
                         "-fx-padding: 5 15 5 15;");

        // Rank as image view
        Node rankNode = createRankGraphic(rank);

        Text nameText = new Text(playerName);
        nameText.setFill(javafx.scene.paint.Color.web("#2b5237"));
        nameText.setFont(Font.font("Arial", FontWeight.BOLD, 16));

        String starString = getStarString(stars);
        Text starsText = new Text(starString);
        starsText.setFill(javafx.scene.paint.Color.web("#f6b539"));
        starsText.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        starsText.setTextAlignment(javafx.scene.text.TextAlignment.RIGHT);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        entryBox.getChildren().addAll(rankNode, nameText, spacer, starsText);
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
    private void populateLeaderboard(List<LeaderboardEntry> entries) {
        leaderboardContent.getChildren().clear();

        int rank = 1;
        for (LeaderboardEntry entry : entries) {
            HBox entryUI = createLeaderboardEntryUI(rank, entry.playerName, entry.stars);
            leaderboardContent.getChildren().add(entryUI);
            rank++;

            // Limit to top 10 entries now
            if (rank > 10) break;
        }

        // If no entries, show a message
        if (entries.isEmpty()) {
            Text noDataText = new Text("No leaderboard data available");
            noDataText.setFill(javafx.scene.paint.Color.web("#2b5237"));
            noDataText.setFont(Font.font("Arial", FontWeight.BOLD, 16));
            leaderboardContent.getChildren().add(noDataText);
        }
    }

    /**
     * TODO: Replace with actual data loading from database/JSON
     * Sample data for demonstration
     */
    private List<LeaderboardEntry> getSampleLeaderboardData() {
        List<LeaderboardEntry> entries = new ArrayList<>();

        // Sample data - replace with actual data loading logic
        entries.add(new LeaderboardEntry("PlayerName1", 5, "Time Challenge", "Easy"));
        entries.add(new LeaderboardEntry("PlayerName2", 4, "Endless Mode", "Medium"));
        entries.add(new LeaderboardEntry("PlayerName3", 4, "Time Challenge", "Hard"));
        entries.add(new LeaderboardEntry("PlayerName4", 3, "Endless Mode", "Easy"));
        entries.add(new LeaderboardEntry("PlayerName5", 3, "Time Challenge", "Medium"));
        entries.add(new LeaderboardEntry("PlayerName6", 2, "Endless Mode", "Hard"));
        entries.add(new LeaderboardEntry("PlayerName7", 2, "Time Challenge", "Easy"));
        entries.add(new LeaderboardEntry("PlayerName8", 1, "Endless Mode", "Medium"));
        entries.add(new LeaderboardEntry("PlayerName9", 1, "Time Challenge", "Hard"));
        entries.add(new LeaderboardEntry("PlayerName10", 1, "Endless Mode", "Easy"));

        // Filter entries based on selected mode and difficulty
        List<LeaderboardEntry> filteredEntries = new ArrayList<>();
        for (LeaderboardEntry entry : entries) {
            boolean matchesMode = selectedMode == null || entry.mode.equals(selectedMode);
            boolean matchesDifficulty = selectedDifficulty == null || entry.difficulty.equals(selectedDifficulty);

            if (matchesMode && matchesDifficulty) {
                filteredEntries.add(entry);
            }
        }

        return filteredEntries;
    }

    /**
     * Inner class to represent a leaderboard entry
     */
    private static class LeaderboardEntry {
        String playerName;
        int stars;
        String mode;
        String difficulty;

        public LeaderboardEntry(String playerName, int stars, String mode, String difficulty) {
            this.playerName = playerName;
            this.stars = stars;
            this.mode = mode;
            this.difficulty = difficulty;
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
