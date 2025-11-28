package com.example.typejam;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;

public class EnterNameController {

    @FXML
    private Button back_btn;

    @FXML
    private TextField nameField;

    @FXML
    private Text errorText;

    @FXML
    private void onBack(ActionEvent event) {
        try {
            System.out.println("Back button clicked - navigating back");
            NavigationHelper.navigateBack(event);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error navigating back: " + e.getMessage());
        }
    }

    @FXML
    private void onNext(ActionEvent event) {
        String name = nameField.getText().trim();

        if (name.isEmpty()) {
            // Show error message
            errorText.setVisible(true);
            return;
        }

        // Hide error message if it was showing
        errorText.setVisible(false);

        // Save the player name to GameData
        GameData.getInstance().setPlayerName(name);

        try {
            System.out.println("Next button clicked - navigating to select-mode.fxml");
            NavigationHelper.navigateTo(event, "enter-name.fxml", "select-mode.fxml");
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error navigating to select mode: " + e.getMessage());
        }
    }
}

