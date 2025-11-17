package com.example.typejam;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class TimeChallengeOverlayController {

    @FXML
    private Button startButton;

    @FXML
    private Button cancelButton;

    @FXML
    public void initialize() {
        // Initialize the overlay
        System.out.println("TimeChallengeOverlayController initialized");
    }

    @FXML
    public void onStart(ActionEvent event) {
        System.out.println("Starting Time Challenge mode!");
        try {
            // Navigate to select difficulty with Time Challenge mode set
            GameData gameData = GameData.getInstance();
            gameData.setMode("Time Challenge");

            switchTo(event, "select-difficulty.fxml");
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error loading select-difficulty.fxml: " + e.getMessage());
        }
    }

    @FXML
    public void onCancel(ActionEvent event) {
        System.out.println("Cancelled Time Challenge overlay");
        // Close the overlay and return to select-mode scene
        try {
            switchTo(event, "select-mode.fxml");
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error loading select-mode.fxml: " + e.getMessage());
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

