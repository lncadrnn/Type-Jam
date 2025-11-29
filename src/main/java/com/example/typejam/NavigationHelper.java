package com.example.typejam;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Helper class to manage scene navigation and history
 */
public class NavigationHelper {

    /**
     * Navigate to a new scene and save the current scene to history
     */
    public static void navigateTo(ActionEvent event, String currentScene, String targetScene) throws IOException {
        // Push current scene to navigation history
        GameData.getInstance().pushScene(currentScene);

        // Load and switch to target scene
        switchToScene(event, targetScene);
    }

    /**
     * Navigate back to the previous scene in history
     */
    public static void navigateBack(ActionEvent event) throws IOException {
        // Pop the previous scene from history
        String previousScene = GameData.getInstance().popScene();

        // Load and switch to previous scene
        switchToScene(event, previousScene);
    }

    /**
     * Switch to a scene without affecting navigation history
     */
    public static void switchToScene(ActionEvent event, String fxmlName) throws IOException {
        FXMLLoader loader = new FXMLLoader(NavigationHelper.class.getResource(fxmlName));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 760, 495);
        // Attach global stylesheet
        String css = NavigationHelper.class.getResource("/styles/styles.css").toExternalForm();
        scene.getStylesheets().add(css);
        stage.setScene(scene);
        stage.show();
    }
}
