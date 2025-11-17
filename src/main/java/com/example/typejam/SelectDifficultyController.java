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

public class SelectDifficultyController {

    @FXML
    private Button back_btn;

    @FXML
    private Button next_btn;

    @FXML
    private Button easyBtn;

    @FXML
    private Button averageBtn;

    @FXML
    private Button difficultBtn;

    @FXML
    private javafx.scene.text.Text errorText;

    private Button selectedDifficultyButton = null;

    @FXML
    private void onEasyClick(ActionEvent event) {
        selectDifficulty(easyBtn);
        // Hide error message if it was showing
        errorText.setVisible(false);
    }

    @FXML
    private void onAverageClick(ActionEvent event) {
        selectDifficulty(averageBtn);
        // Hide error message if it was showing
        errorText.setVisible(false);
    }

    @FXML
    private void onDifficultClick(ActionEvent event) {
        selectDifficulty(difficultBtn);
        // Hide error message if it was showing
        errorText.setVisible(false);
    }

    private void selectDifficulty(Button selectedButton) {
        // Reset previously selected button
        if (selectedDifficultyButton != null) {
            selectedDifficultyButton.setStyle("-fx-background-color: #f0f0f0; -fx-border-color: #cccccc; -fx-border-width: 2; -fx-background-radius: 10; -fx-border-radius: 10;");
        }

        // Apply selected style to the new button
        selectedDifficultyButton = selectedButton;
        selectedDifficultyButton.setStyle("-fx-background-color: rgba(43, 82, 55, 0.37); -fx-border-color: #2B5237; -fx-border-width: 2; -fx-background-radius: 10; -fx-border-radius: 10;");
    }

    @FXML
    private void onBack(ActionEvent event) {
        try {
            System.out.println("Back button clicked - navigating to select-mode.fxml");
            switchTo(event, "select-mode.fxml");
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error navigating to select mode: " + e.getMessage());
        }
    }

    @FXML
    private void onNext(ActionEvent event) {
        // Check if a difficulty is selected
        if (selectedDifficultyButton == null) {
            // Show error message
            errorText.setVisible(true);
            return;
        }

        // Hide error message if it was showing
        errorText.setVisible(false);

        try {
            System.out.println("Next button clicked - navigating to next screen");
            // TODO: Navigate to the next screen (e.g., game screen)
            // switchTo(event, "game-screen.fxml");
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error navigating to next screen: " + e.getMessage());
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

