package com.example.typejam;

import javafx.animation.AnimationTimer;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.SVGPath;
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
        if (wpmValue != null) {
            wpmValue.setText(String.valueOf(Math.round(gameData.getWpm())));
        }
        if (accuracyValue != null) {
            accuracyValue.setText(String.format("%.2f%%", gameData.getAccuracy()));
        }

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
        if (this.timeValue != null) {
            this.timeValue.setText(timeText);
        }

        if (charactersValue != null) {
            charactersValue.setText(String.valueOf(gameData.getCharactersTyped()));
        }

        // Calculate star rating based on game results
        int stars = calculateStarRating();
        if (starsContainer != null) {
            displayStars(stars);
        }

        // Save leaderboard entry locally ONLY for Challenge Mode (not for Practice Mode)
        String mode = gameData.getMode();
        System.out.println("DEBUG: Current mode = '" + mode + "'");
        if (mode != null && mode.equalsIgnoreCase("Challenge Mode")) {
            String playerName = safeString(gameData.getPlayerName(), "Player");
            String difficulty = safeString(gameData.getDifficulty(), "Easy");
            System.out.println("DEBUG: Saving leaderboard entry - Name: " + playerName +
                             ", Mode: Challenge Mode, Difficulty: " + difficulty +
                             ", Stars: " + stars + ", WPM: " + gameData.getWpm() +
                             ", Accuracy: " + gameData.getAccuracy());
            LeaderboardStorage.saveEntry(new LeaderboardStorage.LeaderboardEntry(
                    playerName,
                    "Challenge Mode",
                    difficulty,
                    stars,
                    gameData.getWpm(),
                    gameData.getAccuracy(),
                    System.currentTimeMillis()
            ));
            System.out.println("DEBUG: Leaderboard entry saved successfully");
        } else {
            System.out.println("DEBUG: Not saving to leaderboard (mode is not Challenge Mode)");
        }

        // Start confetti animation
        startConfetti();
    }

    private void startConfetti() {
        // Play congratulatory sound with confetti
        SoundManager.getInstance().playCongratulatorySound();

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

        System.out.println("=== Star Rating Calculation ===");
        System.out.println("Accuracy: " + String.format("%.2f", accuracy) + "%");
        System.out.println("WPM: " + String.format("%.0f", wpm));

        // Star rating thresholds based on WPM and Accuracy
        // Both criteria must be met for each star level

        int stars = 0;

        // 5 Stars: Expert Level
        // WPM >= 60 AND Accuracy >= 95%
        if (wpm >= 60 && accuracy >= 95.0) {
            stars = 5;
        }
        // 4 Stars: Advanced Level
        // WPM >= 45 AND Accuracy >= 90%
        else if (wpm >= 45 && accuracy >= 90.0) {
            stars = 4;
        }
        // 3 Stars: Intermediate Level
        // WPM >= 30 AND Accuracy >= 80%
        else if (wpm >= 30 && accuracy >= 80.0) {
            stars = 3;
        }
        // 2 Stars: Beginner Level
        // WPM >= 15 AND Accuracy >= 70%
        else if (wpm >= 15 && accuracy >= 70.0) {
            stars = 2;
        }
        // 1 Star: Needs Practice
        // WPM >= 5 AND Accuracy >= 50%
        else if (wpm >= 5 && accuracy >= 50.0) {
            stars = 1;
        }
        // 0 Stars: Try again
        else {
            stars = 0;
        }

        System.out.println("Stars Awarded: " + stars);
        System.out.println("Criteria for next star:");
        if (stars < 5) {
            String[] nextThresholds = {
                "1 Star: WPM >= 5, Accuracy >= 50%",
                "2 Stars: WPM >= 15, Accuracy >= 70%",
                "3 Stars: WPM >= 30, Accuracy >= 80%",
                "4 Stars: WPM >= 45, Accuracy >= 90%",
                "5 Stars: WPM >= 60, Accuracy >= 95%"
            };
            System.out.println(nextThresholds[stars]);
        } else {
            System.out.println("Perfect! Maximum stars achieved!");
        }
        System.out.println("===============================");

        return stars;
    }

    private void displayStars(int filledStars) {
        if (starsContainer == null) {
            return; // Scene does not include stars; skip
        }
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
            // Restart background music when leaving result scene
            SoundManager.getInstance().startBackgroundMusic();
            GameData.getInstance().clearNavigationHistory();
            NavigationHelper.switchToScene(event, "main-menu.fxml");
        } catch (IOException e) {
            System.err.println("Error loading main-menu.fxml: " + e.getMessage());
        }
    }

    @FXML
    public void onLeaderboard(ActionEvent event) {
        try {
            // Restart background music when leaving result scene
            SoundManager.getInstance().startBackgroundMusic();
            NavigationHelper.navigateTo(event, "result-scene.fxml", "leaderboards.fxml");
        } catch (IOException e) {
            System.err.println("Error loading leaderboards.fxml: " + e.getMessage());
        }
    }

    @FXML
    public void onSettings(ActionEvent event) {
        try {
            // Restart background music when leaving result scene
            SoundManager.getInstance().startBackgroundMusic();
            NavigationHelper.navigateTo(event, "result-scene.fxml", "settings.fxml");
        } catch (IOException e) {
            System.err.println("Error loading settings.fxml: " + e.getMessage());
        }
    }

    @FXML
    public void onAbout(ActionEvent event) {
        try {
            // Restart background music when leaving result scene
            SoundManager.getInstance().startBackgroundMusic();
            NavigationHelper.navigateTo(event, "result-scene.fxml", "about-us.fxml");
        } catch (IOException e) {
            System.err.println("Error loading about-us.fxml: " + e.getMessage());
        }
    }

    @FXML
    public void onLetsType(ActionEvent event) {
        try {
            // Restart background music when leaving result scene
            SoundManager.getInstance().startBackgroundMusic();
            GameData.getInstance().clearNavigationHistory();
            NavigationHelper.switchToScene(event, "select-mode.fxml");
        } catch (IOException e) {
            System.err.println("Error loading select-mode.fxml: " + e.getMessage());
        }
    }

    @FXML
    public void onPracticeAgain(ActionEvent event) {
        try {
            // Restart background music when leaving result scene
            SoundManager.getInstance().startBackgroundMusic();
            GameData.getInstance().clearNavigationHistory();
            NavigationHelper.switchToScene(event, "select-mode.fxml");
        } catch (IOException e) {
            System.err.println("Error loading select-mode.fxml: " + e.getMessage());
        }
    }

    private String safeString(String value, String fallback) {
        return (value == null || value.trim().isEmpty()) ? fallback : value;
    }
}
