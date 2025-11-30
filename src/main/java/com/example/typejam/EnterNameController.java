package com.example.typejam;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

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
            errorText.setVisible(true);
            return;
        }
        errorText.setVisible(false);
        GameData.getInstance().setPlayerName(name);
        try {
            // Updated: after name entry we now go to select-time.fxml (for challenge flow)
            System.out.println("Name entered - navigating to select-time.fxml");
            NavigationHelper.navigateTo(event, "enter-name.fxml", "select-time.fxml");
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error navigating to select time: " + e.getMessage());
        }
    }
}
