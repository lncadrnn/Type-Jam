package com.example.typejam;

import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ProgressIndicator;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

public class LoadingScreenController {

    @FXML
    private ProgressIndicator loadingSpinner;

    @FXML
    public void initialize() {
        // Create a 2-second delay before transitioning to result scene
        PauseTransition pause = new PauseTransition(Duration.seconds(2));
        pause.setOnFinished(event -> {
            try {
                loadResultScene();
            } catch (IOException e) {
                e.printStackTrace();
                System.err.println("Error loading result-scene.fxml: " + e.getMessage());
            }
        });
        pause.play();
    }

    private void loadResultScene() throws IOException {
        java.net.URL url = getClass().getResource("result-scene.fxml");
        if (url == null) {
            System.err.println("result-scene.fxml not found relative to " + getClass().getName());
            return;
        }
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(url);
        Parent root = loader.load();
        Stage stage = (Stage) loadingSpinner.getScene().getWindow();
        Scene scene = new Scene(root, 760, 495);
        stage.setScene(scene);
        stage.show();
    }
}
