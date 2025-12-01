package com.example.typejam;

import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;
import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;

/**
 * Utility class for displaying toast notifications with animations.
 */
public class ToastUtil {

    public enum ToastType {
        SUCCESS, ERROR, INFO
    }

    /**
     * Show a toast notification that slides in from top, pauses, then fades out.
     *
     * @param container The StackPane container where toast should be displayed
     * @param message   The message to display
     * @param type      The type of toast (SUCCESS, ERROR, INFO)
     */
    public static void showToast(StackPane container, String message, ToastType type) {
        // Create full-screen toast overlay
        StackPane toast = new StackPane();
        toast.setStyle(getToastStyle(type));
        toast.setPrefSize(Double.MAX_VALUE, Double.MAX_VALUE);
        toast.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

        // Create message text
        Text messageText = new Text(message);
        messageText.setStyle("-fx-fill: white; -fx-font-size: 18px; -fx-font-weight: bold;");
        messageText.setFont(Font.font("Arial", 18));

        toast.getChildren().add(messageText);
        StackPane.setAlignment(messageText, Pos.CENTER);

        // Initially set opacity to 0 for fade-in effect
        toast.setOpacity(0);

        // Add to container
        container.getChildren().add(toast);

        // Fade in animation
        FadeTransition fadeIn = new FadeTransition(Duration.millis(300), toast);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);

        // Pause
        PauseTransition pause = new PauseTransition(Duration.seconds(1.5));

        // Fade out animation
        FadeTransition fadeOut = new FadeTransition(Duration.millis(300), toast);
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);

        // Chain animations: fade in, pause, fade out
        SequentialTransition sequence = new SequentialTransition(
            fadeIn,
            pause,
            fadeOut
        );


        // Remove toast after animation completes
        sequence.setOnFinished(e -> container.getChildren().remove(toast));

        // Start animation
        sequence.play();
    }

    private static String getToastStyle(ToastType type) {
        // Use full opacity backgrounds for full-screen overlay
        switch (type) {
            case SUCCESS:
                return "-fx-background-color: rgba(40, 167, 69, 1.0);";
            case ERROR:
                return "-fx-background-color: rgba(220, 53, 69, 1.0);";
            case INFO:
            default:
                return "-fx-background-color: rgba(23, 162, 184, 1.0);";
        }
    }
}

