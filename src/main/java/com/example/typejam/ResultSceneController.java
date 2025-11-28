package com.example.typejam;

import javafx.animation.AnimationTimer;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.SVGPath;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
    private javafx.scene.text.Text wpmValue;

    @FXML
    private javafx.scene.text.Text accuracyValue;

    @FXML
    private javafx.scene.text.Text timeValue;

    @FXML
    private javafx.scene.text.Text charactersValue;

    @FXML
    private AnchorPane rootPane;

    private Pane confettiPane;
    private List<ConfettiParticle> confettiParticles;
    private AnimationTimer confettiTimer;
    private Random random = new Random();

    @FXML
    public void initialize() {
        // Get game data
        GameData gameData = GameData.getInstance();

        // Display statistics
        wpmValue.setText(String.valueOf(Math.round(gameData.getWpm())));
        accuracyValue.setText(String.format("%.2f%%", gameData.getAccuracy()));

        // Format time consumed in words
        double timeConsumed = gameData.getTimeTaken();
        int minutes = (int) timeConsumed / 60;
        int seconds = (int) timeConsumed % 60;

        String timeText;
        if (minutes > 0 && seconds > 0) {
            timeText = minutes + (minutes == 1 ? " minute " : " minutes ") + seconds + (seconds == 1 ? " second" : " seconds");
        } else if (minutes > 0) {
            timeText = minutes + (minutes == 1 ? " minute" : " minutes");
        } else {
            timeText = seconds + (seconds == 1 ? " second" : " seconds");
        }
        timeValue.setText(timeText);

        charactersValue.setText(String.valueOf(gameData.getCharactersTyped()));

        // Calculate star rating based on game results
        int stars = calculateStarRating();
        displayStars(stars);

        // Start confetti animation
        startConfetti();
    }

    private void startConfetti() {
        // Create confetti pane overlay
        confettiPane = new Pane();
        confettiPane.setMouseTransparent(true);
        confettiPane.setPrefSize(760, 495);

        // Add confetti pane on top of root pane
        if (rootPane != null) {
            rootPane.getChildren().add(confettiPane);
        }

        // Initialize confetti particles
        confettiParticles = new ArrayList<>();
        Color[] colors = {
            Color.web("#F6B539"), // Gold
            Color.web("#2B5237"), // Green
            Color.web("#FF6B6B"), // Red
            Color.web("#4ECDC4"), // Cyan
            Color.web("#FFE66D"), // Yellow
            Color.web("#A8E6CF"), // Light green
            Color.web("#FF8B94"), // Pink
            Color.web("#C7CEEA")  // Purple
        };

        // Create confetti particles
        for (int i = 0; i < 150; i++) {
            double x = random.nextDouble() * 760;
            double y = -random.nextDouble() * 200 - 50; // Start above screen
            Circle circle = new Circle(random.nextDouble() * 4 + 2); // Random size 2-6
            circle.setFill(colors[random.nextInt(colors.length)]);
            circle.setOpacity(0.8);

            ConfettiParticle particle = new ConfettiParticle(
                circle, x, y,
                random.nextDouble() * 2 + 1, // Falling speed
                random.nextDouble() * 4 - 2, // Horizontal drift
                random.nextDouble() * 360     // Rotation
            );

            confettiParticles.add(particle);
            confettiPane.getChildren().add(circle);
        }

        // Start animation
        confettiTimer = new AnimationTimer() {
            private long lastUpdate = 0;

            @Override
            public void handle(long now) {
                if (lastUpdate == 0) {
                    lastUpdate = now;
                    return;
                }

                double deltaTime = (now - lastUpdate) / 1_000_000_000.0;
                lastUpdate = now;

                boolean allDone = true;
                for (ConfettiParticle particle : confettiParticles) {
                    if (particle.update(deltaTime)) {
                        allDone = false;
                    }
                }

                // Stop animation when all particles are done
                if (allDone) {
                    this.stop();
                    // Fade out confetti pane
                    FadeTransition fade = new FadeTransition(Duration.seconds(1), confettiPane);
                    fade.setFromValue(1.0);
                    fade.setToValue(0.0);
                    fade.setOnFinished(e -> {
                        if (confettiPane.getParent() instanceof AnchorPane) {
                            ((AnchorPane) confettiPane.getParent()).getChildren().remove(confettiPane);
                        }
                    });
                    fade.play();
                }
            }
        };
        confettiTimer.start();
    }

    // Confetti particle class
    private class ConfettiParticle {
        private Circle circle;
        private double x, y;
        private double velocityY;
        private double velocityX;
        private double rotation;
        private double rotationSpeed;

        public ConfettiParticle(Circle circle, double x, double y, double velocityY, double velocityX, double rotation) {
            this.circle = circle;
            this.x = x;
            this.y = y;
            this.velocityY = velocityY;
            this.velocityX = velocityX;
            this.rotation = rotation;
            this.rotationSpeed = random.nextDouble() * 360 - 180;
            updatePosition();
        }

        public boolean update(double deltaTime) {
            // Update position
            y += velocityY * 60 * deltaTime;
            x += velocityX * 60 * deltaTime;
            rotation += rotationSpeed * deltaTime;

            // Add gravity
            velocityY += 2 * deltaTime;

            // Update visual position
            updatePosition();

            // Return true if still visible
            return y < 550; // Keep animating until below screen
        }

        private void updatePosition() {
            circle.setCenterX(x);
            circle.setCenterY(y);
            circle.setRotate(rotation);
        }
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
