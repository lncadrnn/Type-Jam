package com.example.typejam;

import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.TranslateTransition;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
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
        // Create toast container
        HBox toast = new HBox();
        toast.setAlignment(Pos.CENTER);
        toast.setMaxWidth(500);
        toast.setMinHeight(60);
        toast.setStyle(getToastStyle(type));

        // Create message text
        Text messageText = new Text(message);
        messageText.setStyle("-fx-fill: white; -fx-font-size: 14px; -fx-font-weight: bold;");
        messageText.setFont(Font.font("Arial", 14));

        toast.getChildren().add(messageText);

        // Position toast at top center, initially hidden above viewport
        StackPane.setAlignment(toast, Pos.TOP_CENTER);
        toast.setTranslateY(-100);

        // Add to container
        container.getChildren().add(toast);

        // Slide in animation
        TranslateTransition slideIn = new TranslateTransition(Duration.millis(300), toast);
        slideIn.setFromY(-100);
        slideIn.setToY(20);

        // Fade in animation
        FadeTransition fadeIn = new FadeTransition(Duration.millis(300), toast);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);

        // Pause
        PauseTransition pause = new PauseTransition(Duration.seconds(3));

        // Fade out animation
        FadeTransition fadeOut = new FadeTransition(Duration.millis(300), toast);
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);

        // Slide up animation
        TranslateTransition slideUp = new TranslateTransition(Duration.millis(300), toast);
        slideUp.setFromY(20);
        slideUp.setToY(-100);

        // Chain animations: slide in + fade in, pause, fade out + slide up
        SequentialTransition sequence = new SequentialTransition(
            slideIn,
            pause,
            fadeOut
        );

        // Run fade in parallel with slide in
        fadeIn.play();

        // Remove toast after animation completes
        sequence.setOnFinished(e -> container.getChildren().remove(toast));

        // Start animation
        sequence.play();
    }

    private static String getToastStyle(ToastType type) {
        String baseStyle = "-fx-background-radius: 15; " +
                          "-fx-padding: 15 30 15 30; " +
                          "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 10, 0.5, 0, 3);";

        switch (type) {
            case SUCCESS:
                return baseStyle + " -fx-background-color: #28a745;";
            case ERROR:
                return baseStyle + " -fx-background-color: #dc3545;";
            case INFO:
            default:
                return baseStyle + " -fx-background-color: #17a2b8;";
        }
    }
}

