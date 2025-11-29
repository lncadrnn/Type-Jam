package com.example.typejam;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextInputDialog;

import java.io.IOException;
import java.util.Optional;

public class SettingsController {

    @FXML
    private Button back_btn;

    @FXML
    private Button resetDataBtn;

    private static final String RESET_PASSWORD = "BESTypeJam"; // simple static password; can be externalized later

    @FXML
    private void onBack(ActionEvent event) {
        try {
            NavigationHelper.navigateBack(event);
        } catch (IOException e) {
            System.err.println("Error navigating back: " + e.getMessage());
        }
    }

    @FXML
    private void onResetData(ActionEvent event) {
        // Ask for password before showing destructive confirmation
        TextInputDialog pwdDialog = new TextInputDialog();
        pwdDialog.setTitle("Reset Data - Password Required");
        pwdDialog.setHeaderText("Enter the admin password to reset all data");
        pwdDialog.setContentText("Password:");
        pwdDialog.getDialogPane().setStyle("-fx-background-color: #ffffff;");

        Optional<String> pwdResult = pwdDialog.showAndWait();
        if (!pwdResult.isPresent() || !RESET_PASSWORD.equals(pwdResult.get())) {
            Alert denied = new Alert(Alert.AlertType.ERROR);
            denied.setTitle("Reset Data");
            denied.setHeaderText(null);
            denied.setContentText("Incorrect password. Reset canceled.");
            denied.getDialogPane().setStyle("-fx-background-color: #ffffff;");
            denied.showAndWait();
            return;
        }

        // Show confirmation dialog
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Reset Data");
        alert.setHeaderText("Are you sure you want to reset all data?");
        alert.setContentText("This will permanently delete all leaderboard entries and cannot be undone.");
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

    @FXML
    private void initialize() {
        // Optionally purge old entries upon opening settings
        try {
            int removed = LeaderboardStorage.purgeEntriesOlderThanOneYear();
            if (removed > 0) {
                Alert info = new Alert(Alert.AlertType.INFORMATION);
                info.setTitle("Leaderboard Maintenance");
                info.setHeaderText(null);
                info.setContentText(removed + " old entries (>= 1 year) were purged.");
                info.getDialogPane().setStyle("-fx-background-color: #ffffff;");
                info.showAndWait();
            }
        } catch (Exception e) {
            System.err.println("Purge check failed: " + e.getMessage());
        }
    }
}
