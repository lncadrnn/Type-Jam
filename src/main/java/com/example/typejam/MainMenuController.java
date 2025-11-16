package com.example.typejam;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainMenuController {

    @FXML
    private void onPlay(ActionEvent event) throws IOException {
        switchTo(event, "enter-name.fxml");
    }

    @FXML
    private void onLeaderboard(ActionEvent event) throws IOException {
        switchTo(event, "leaderboards.fxml");
    }

    @FXML
    private void onAbout(ActionEvent event) throws IOException {
        switchTo(event, "about-us.fxml");
    }

    @FXML
    private void onSettings(ActionEvent event) throws IOException {
        switchTo(event, "settings.fxml");
    }

    private void switchTo(ActionEvent event, String fxmlName) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource(fxmlName));
        // Keep the same stage and scene size; just replace the root.
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = stage.getScene();
        if (scene == null) {
            // Fallback: create a new scene if missing (shouldn't happen in normal flow)
            scene = new Scene(root, 760, 495);
            stage.setScene(scene);
        } else {
            scene.setRoot(root);
        }
    }
}

