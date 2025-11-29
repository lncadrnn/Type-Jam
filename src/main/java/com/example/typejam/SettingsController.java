package com.example.typejam;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;

import java.io.IOException;
import java.util.Optional;

public class SettingsController {

    @FXML
    private Button back_btn;

    @FXML
    private Button resetDataBtn;

    @FXML
    private void onBack(ActionEvent event) {
        try {
            NavigationHelper.navigateBack(event);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error navigating back: " + e.getMessage());
        }
    }

    @FXML
    private void onResetData(ActionEvent event) {
        // Show confirmation dialog
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Reset Data");
        alert.setHeaderText("Are you sure you want to reset all data?");
        alert.setContentText("This will permanently delete all leaderboard entries and cannot be undone.");

        // Style the alert to match the app theme
        alert.getDialogPane().setStyle("-fx-background-color: #ffffff;");

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            // User confirmed, proceed with reset
            boolean success = LeaderboardStorage.clearAllData();

            // Show result
            Alert resultAlert = new Alert(
                success ? Alert.AlertType.INFORMATION : Alert.AlertType.ERROR
            );
            resultAlert.setTitle("Reset Data");
            resultAlert.setHeaderText(null);
            resultAlert.setContentText(
                success ? "All data has been successfully reset!" : "Failed to reset data. Please try again."
            );
            resultAlert.getDialogPane().setStyle("-fx-background-color: #ffffff;");
            resultAlert.showAndWait();
        }
    }
}

