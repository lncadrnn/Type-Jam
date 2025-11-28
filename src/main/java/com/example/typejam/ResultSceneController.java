package com.example.typejam;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;
import javafx.stage.Stage;

import java.io.IOException;

public class ResultSceneController {

    @FXML
    private HBox starsContainer;

    @FXML
    private Button mainMenuButton;

    @FXML
    private Button leaderboardButton;

    @FXML
    private Button settingsButton;

    @FXML
    private Button aboutButton;

    @FXML
    private Button letsTypeButton;

    @FXML
    public void initialize() {
        // Calculate star rating based on game results
        int stars = calculateStarRating();
        displayStars(stars);
    }

    private int calculateStarRating() {
        GameData gameData = GameData.getInstance();
        double accuracy = gameData.getAccuracy();
        double wpm = gameData.getWpm();
        double timeTaken = gameData.getTimeTaken();

        System.out.println("=== Star Rating Calculation ===");
        System.out.println("Accuracy: " + String.format("%.2f", accuracy) + "%");
        System.out.println("WPM: " + String.format("%.2f", wpm));
        System.out.println("Time Taken: " + String.format("%.2f", timeTaken) + "s");

        // Calculate score based on weighted criteria
        // Accuracy: 40%, WPM: 40%, Time: 20%
        double accuracyScore = (accuracy / 100.0) * 40; // Max 40 points

        // WPM scoring: Excellent (60+), Good (40-60), Average (20-40), Poor (<20)
        double wpmScore;
        if (wpm >= 60) wpmScore = 40;
        else if (wpm >= 40) wpmScore = 30;
        else if (wpm >= 20) wpmScore = 20;
        else wpmScore = wpm / 2; // Scale for low WPM

        // Time scoring: Faster is better (based on difficulty)
        // For now, if completed quickly relative to text length, give bonus
        double timeScore = 20; // Default full score for completion
        if (timeTaken > 120) timeScore = 15; // Longer than 2 minutes
        if (timeTaken > 180) timeScore = 10; // Longer than 3 minutes

        double totalScore = accuracyScore + wpmScore + timeScore;

        System.out.println("Accuracy Score: " + String.format("%.2f", accuracyScore));
        System.out.println("WPM Score: " + String.format("%.2f", wpmScore));
        System.out.println("Time Score: " + String.format("%.2f", timeScore));
        System.out.println("Total Score: " + String.format("%.2f", totalScore));

        // Convert to star rating (0-5 stars)
        // 80-100: 5 stars, 60-79: 4 stars, 40-59: 3 stars, 20-39: 2 stars, 1-19: 1 star
        int stars;
        if (totalScore >= 80) stars = 5;
        else if (totalScore >= 60) stars = 4;
        else if (totalScore >= 40) stars = 3;
        else if (totalScore >= 20) stars = 2;
        else if (totalScore >= 1) stars = 1;
        else stars = 0;

        System.out.println("Stars Awarded: " + stars);
        System.out.println("===============================");

        return stars;
    }

    private void displayStars(int filledStars) {
        starsContainer.getChildren().clear();

        // Create 5 stars
        for (int i = 0; i < 5; i++) {
            SVGPath star = createStar();

            if (i < filledStars) {
                // Filled star
                star.setFill(Color.web("#F6B539"));
            } else {
                // Empty star
                star.setFill(Color.web("#F0F0F0"));
            }

            // Stroke for both
            star.setStroke(Color.BLACK);
            star.setStrokeWidth(1);

            // Make stars bigger
            star.setScaleX(1.9);
            star.setScaleY(1.9);

            starsContainer.getChildren().add(star);
        }
    }

    private SVGPath createStar() {
        // Create a 5-pointed star using SVG path
        SVGPath star = new SVGPath();
        // Star path: centered at origin, scaled to ~40px size
        star.setContent("M 0,-15 L 4.5,-4.5 L 15,-4.5 L 6,3 L 10,15 L 0,7.5 L -10,15 L -6,3 L -15,-4.5 L -4.5,-4.5 Z");
        return star;
    }

    @FXML
    public void onMainMenu(ActionEvent event) {
        try {
            switchTo(event, "main-menu.fxml");
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error loading main-menu.fxml: " + e.getMessage());
        }
    }

    @FXML
    public void onLeaderboard(ActionEvent event) {
        try {
            switchTo(event, "leaderboards.fxml");
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error loading leaderboards.fxml: " + e.getMessage());
        }
    }

    @FXML
    public void onSettings(ActionEvent event) {
        try {
            switchTo(event, "settings.fxml");
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error loading settings.fxml: " + e.getMessage());
        }
    }

    @FXML
    public void onAbout(ActionEvent event) {
        try {
            switchTo(event, "about-us.fxml");
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error loading about-us.fxml: " + e.getMessage());
        }
    }

    @FXML
    public void onLetsType(ActionEvent event) {
        try {
            switchTo(event, "enter-name.fxml");
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error loading enter-name.fxml: " + e.getMessage());
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
