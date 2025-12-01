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
    private javafx.scene.text.Text congratsTitle;

    @FXML
    private javafx.scene.text.Text congratsSubtitle;

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

        // Set dynamic congratulatory messages based on mode
        String mode = gameData.getMode();
        updateCongratulatoryMessages(mode, stars);

        // Save leaderboard entry locally ONLY for Challenge Mode (not for Practice Mode)
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

    // --- New scoring utility methods ---
    private double calculateWPM(int charsTyped, double timeSeconds) {
        // WPM = (charsTyped / 5) / (timeSeconds / 60)
        if (timeSeconds <= 0) return 0.0;
        return (charsTyped / 5.0) / (timeSeconds / 60.0);
    }

    private double calculateAccuracy(int correctChars, int totalChars) {
        // accuracy = (correctChars / totalChars) * 100
        if (totalChars <= 0) return 0.0;
        return (correctChars / (double) totalChars) * 100.0;
    }

    private int calculateStars(double wpm, double accuracy, int charsTyped) {
        // First tier: character-based cap
        int maxStars;
        if (charsTyped < 30) {
            maxStars = 0; // 0-star edge case
        } else if (charsTyped < 50) {
            maxStars = 1;
        } else if (charsTyped < 100) {
            maxStars = 2;
        } else if (charsTyped < 150) {
            maxStars = 3;
        } else if (charsTyped < 200) {
            maxStars = 4;
        } else {
            maxStars = 5;
        }

        // Second tier: WPM + Accuracy thresholds
        int earnedStars;
        if (wpm >= 55 && accuracy >= 95) {
            earnedStars = 5;
        } else if (wpm >= 45 && accuracy >= 90) {
            earnedStars = 4;
        } else if (wpm >= 30 && accuracy >= 85) {
            earnedStars = 3;
        } else if (wpm >= 15 && accuracy >= 80) {
            earnedStars = 2;
        } else {
            earnedStars = 1;
        }

        int finalStars = Math.min(maxStars, earnedStars);

        // Debug output
        System.out.println("--- Two-Tier Star Calculation ---");
        System.out.println("Characters Typed: " + charsTyped + " -> Max Stars Allowed: " + maxStars);
        System.out.println("WPM: " + String.format("%.2f", wpm) + ", Accuracy: " + String.format("%.2f", accuracy) + "% -> Earned Stars: " + earnedStars);
        if (finalStars < earnedStars) {
            System.out.println("Note: Star rating capped by characters typed.");
        }
        System.out.println("Final Stars Awarded: " + finalStars + " / 5");
        System.out.println("----------------------------------");
        return finalStars;
    }

    private double getFinalScore(double wpm, double accuracy) {
        // score = (wpm * 0.6) + (accuracy * 0.4), capped at 100
        double score = (wpm * 0.6) + (accuracy * 0.4);
        return Math.min(100.0, score);
    }

    private int calculateStarRating() {
        GameData gameData = GameData.getInstance();
        double accuracy = gameData.getAccuracy();
        double wpm = gameData.getWpm();
        int charactersTyped = gameData.getCharactersTyped();

        // If WPM/Accuracy were not set (defensive), compute from raw data
        if (Double.isNaN(wpm) || wpm <= 0) {
            wpm = calculateWPM(charactersTyped, gameData.getTimeTaken());
        }
        if (Double.isNaN(accuracy) || accuracy <= 0) {
            accuracy = calculateAccuracy(gameData.getCorrectCharacters(), gameData.getTotalCharacters());
        }

        System.out.println("=== Star Rating Calculation (Two-Tier) ===");
        System.out.println("Accuracy: " + String.format("%.2f", accuracy) + "%");
        System.out.println("WPM: " + String.format("%.2f", wpm));
        System.out.println("Characters Typed: " + charactersTyped);

        // Use two-tier star calculation
        int stars = calculateStars(wpm, accuracy, charactersTyped);

        // Optional: compute final score for display or future use
        double finalScore = getFinalScore(wpm, accuracy);
        System.out.println("Final Weighted Score (0-100): " + String.format("%.2f", finalScore));
        System.out.println("=========================================");

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

    // Message pool for Challenge Mode (keyed by star rating 0-5)
    private static final java.util.Map<Integer, List<MessagePair>> challengeModeMessages = new java.util.HashMap<>();

    // Message pool for Practice Mode
    private static final List<MessagePair> practiceModeMessages = new ArrayList<>();

    // Static initializer to populate message pools
    static {
        // Challenge Mode: 0 Stars (Encouraging)
        challengeModeMessages.put(0, java.util.Arrays.asList(
            new MessagePair("Keep Trying!", "Every expert was once a beginner."),
            new MessagePair("Don't Give Up!", "Progress takes time and practice."),
            new MessagePair("You've Got This!", "Small steps lead to big improvements."),
            new MessagePair("Stay Positive!", "Learning is a journey, not a race."),
            new MessagePair("Never Stop Learning!", "Each attempt brings you closer to success."),
            new MessagePair("Believe in Yourself!", "Your potential is unlimited."),
            new MessagePair("Start Strong!", "The first step is always the hardest."),
            new MessagePair("Keep Pushing!", "Greatness comes from persistence."),
            new MessagePair("You Can Do It!", "Practice makes progress, not perfection."),
            new MessagePair("Stay Motivated!", "Every challenge is an opportunity to grow.")
        ));

        // Challenge Mode: 1 Star (Supportive)
        challengeModeMessages.put(1, java.util.Arrays.asList(
            new MessagePair("Good Start!", "You're on the right track."),
            new MessagePair("Nice Effort!", "Keep practicing to improve your skills."),
            new MessagePair("You're Learning!", "Every attempt makes you better."),
            new MessagePair("Keep Going!", "Consistency is the key to success."),
            new MessagePair("Solid Beginning!", "You're building a strong foundation."),
            new MessagePair("Making Progress!", "Your improvement is just beginning."),
            new MessagePair("Way to Go!", "You're developing valuable skills."),
            new MessagePair("Great Initiative!", "Your effort is commendable."),
            new MessagePair("Keep It Up!", "You're heading in the right direction."),
            new MessagePair("Nice Job!", "Every practice session counts.")
        ));

        // Challenge Mode: 2 Stars (Supportive)
        challengeModeMessages.put(2, java.util.Arrays.asList(
            new MessagePair("Well Done!", "You're making steady progress."),
            new MessagePair("Great Effort!", "Your skills are developing nicely."),
            new MessagePair("Keep It Up!", "You're getting better with each try."),
            new MessagePair("Nice Work!", "Practice is paying off."),
            new MessagePair("Good Progress!", "You're on your way to mastery."),
            new MessagePair("Getting Better!", "Your dedication is showing results."),
            new MessagePair("Fine Work!", "You're building momentum now."),
            new MessagePair("Keep Practicing!", "Your skills are clearly improving."),
            new MessagePair("Doing Great!", "You're developing consistency."),
            new MessagePair("Moving Forward!", "Each session brings improvement.")
        ));

        // Challenge Mode: 3 Stars (Motivational)
        challengeModeMessages.put(3, java.util.Arrays.asList(
            new MessagePair("Good Job!", "You're hitting your stride now."),
            new MessagePair("Impressive!", "You're showing real improvement."),
            new MessagePair("Great Progress!", "You're mastering the basics well."),
            new MessagePair("Solid Performance!", "Keep pushing to reach the next level."),
            new MessagePair("Nice Achievement!", "Your dedication is showing results."),
            new MessagePair("Well Executed!", "You're becoming more confident."),
            new MessagePair("Strong Performance!", "You're in the middle tier now."),
            new MessagePair("Good Work!", "Your skills are really developing."),
            new MessagePair("Commendable!", "You're making significant strides."),
            new MessagePair("Keep Rising!", "You're halfway to excellence.")
        ));

        // Challenge Mode: 4 Stars (Celebratory)
        challengeModeMessages.put(4, java.util.Arrays.asList(
            new MessagePair("Excellent Work!", "You're almost at the top!"),
            new MessagePair("Outstanding!", "Your typing skills are impressive."),
            new MessagePair("Fantastic Job!", "You're very close to perfection."),
            new MessagePair("Amazing Performance!", "Just one more push to five stars."),
            new MessagePair("Superb Typing!", "You're in the expert range now."),
            new MessagePair("Brilliant!", "You're demonstrating real mastery."),
            new MessagePair("Exceptional!", "Your precision is outstanding."),
            new MessagePair("Remarkable!", "You're typing like a pro."),
            new MessagePair("Stellar Work!", "Excellence is within reach."),
            new MessagePair("Magnificent!", "You're among the best typists.")
        ));

        // Challenge Mode: 5 Stars (Celebratory)
        challengeModeMessages.put(5, java.util.Arrays.asList(
            new MessagePair("Perfect Score!", "You've achieved typing mastery!"),
            new MessagePair("Flawless!", "Your typing skills are exceptional."),
            new MessagePair("Incredible!", "You've reached the pinnacle of typing."),
            new MessagePair("Phenomenal!", "You're a true typing champion!"),
            new MessagePair("Masterful!", "Five stars, absolute perfection!"),
            new MessagePair("Legendary!", "You've set the gold standard!"),
            new MessagePair("Unstoppable!", "Your performance is extraordinary!"),
            new MessagePair("World-Class!", "You're at the peak of excellence!"),
            new MessagePair("Spectacular!", "You've achieved the impossible!"),
            new MessagePair("Unbeatable!", "You're a typing virtuoso!")
        ));

        // Practice Mode: General Motivational (7 variations)
        practiceModeMessages.add(new MessagePair("Practice Complete!", "Consistency builds speed and accuracy."));
        practiceModeMessages.add(new MessagePair("Great Session!", "Every practice session sharpens your skills."));
        practiceModeMessages.add(new MessagePair("Nice Work!", "Regular practice leads to mastery."));
        practiceModeMessages.add(new MessagePair("Keep Practicing!", "Your dedication will pay off."));
        practiceModeMessages.add(new MessagePair("Well Done!", "Each session makes you stronger."));
        practiceModeMessages.add(new MessagePair("Good Effort!", "Progress happens one practice at a time."));
        practiceModeMessages.add(new MessagePair("Excellent Practice!", "You're building valuable skills today."));
    }

    // Inner class to hold message pairs
    private static class MessagePair {
        final String title;
        final String subtitle;

        MessagePair(String title, String subtitle) {
            this.title = title;
            this.subtitle = subtitle;
        }
    }

    private void updateCongratulatoryMessages(String mode, int stars) {
        // Null safety checks for Text elements
        if (congratsTitle == null || congratsSubtitle == null) {
            return;
        }

        MessagePair selectedMessage;

        if (mode != null && mode.equalsIgnoreCase("Challenge Mode")) {
            // Challenge Mode: select message based on star rating
            List<MessagePair> messages = challengeModeMessages.get(stars);
            if (messages != null && !messages.isEmpty()) {
                selectedMessage = messages.get(random.nextInt(messages.size()));
            } else {
                // Fallback message if star rating not found
                selectedMessage = new MessagePair("Great Effort!", "Keep practicing to improve.");
            }
        } else {
            // Practice Mode: select from general motivational messages
            if (!practiceModeMessages.isEmpty()) {
                selectedMessage = practiceModeMessages.get(random.nextInt(practiceModeMessages.size()));
            } else {
                // Fallback message if list is empty
                selectedMessage = new MessagePair("Practice Complete!", "Keep going to build your skills.");
            }
        }

        // Update the Text elements
        congratsTitle.setText(selectedMessage.title);
        congratsSubtitle.setText(selectedMessage.subtitle);
    }
}
