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

public class SelectTimeController {

    @FXML
    private Button back_btn;

    @FXML
    private Button oneMinuteBtn;

    @FXML
    private Button threeMinuteBtn;

    @FXML
    private Button fiveMinuteBtn;

    @FXML
    private Button next_btn;

    @FXML
    private javafx.scene.text.Text errorText;

    private Button selectedTimeButton = null;
    private String selectedTime = null;

    @FXML
    private void onOneMinuteClick(ActionEvent event) {
        selectTime(oneMinuteBtn, "1 Minute");
    }

    @FXML
    private void onThreeMinuteClick(ActionEvent event) {
        selectTime(threeMinuteBtn, "3 Minutes");
    }

    @FXML
    private void onFiveMinuteClick(ActionEvent event) {
        selectTime(fiveMinuteBtn, "5 Minutes");
    }

    private void selectTime(Button selectedButton, String time) {
        // Reset previously selected button
        if (selectedTimeButton != null) {
            selectedTimeButton.setStyle("-fx-background-color: #f0f0f0; -fx-border-color: #cccccc; -fx-border-width: 2; -fx-background-radius: 10; -fx-border-radius: 10;");
        }

        // Apply selected style to the new button
        selectedTimeButton = selectedButton;
        selectedTime = time;
        selectedTimeButton.setStyle("-fx-background-color: rgba(43, 82, 55, 0.37); -fx-border-color: #2B5237; -fx-border-width: 2; -fx-background-radius: 10; -fx-border-radius: 10;");

        // Hide error message if it was showing
        errorText.setVisible(false);
    }

    @FXML
    private void onBack(ActionEvent event) {
        try {
            System.out.println("Back button clicked - navigating to select-mode.fxml");
            switchTo(event, "select-mode.fxml");
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error navigating back to select mode: " + e.getMessage());
        }
    }

    @FXML
    private void onNext(ActionEvent event) {
        // Check if a time is selected
        if (selectedTimeButton == null) {
            // Show error message
            errorText.setVisible(true);
            return;
        }

        // Hide error message if it was showing
        errorText.setVisible(false);

        // Save the selected time to GameData
        GameData.getInstance().setTime(selectedTime);

        try {
            System.out.println("Next button clicked - navigating to select-difficulty.fxml");
            switchTo(event, "select-difficulty.fxml");
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error navigating to select difficulty: " + e.getMessage());
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

