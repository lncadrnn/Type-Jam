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
            System.out.println("Back button clicked - navigating to main-menu.fxml");
            switchTo(event, "main-menu.fxml");
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error navigating to main menu: " + e.getMessage());
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

        try {
            System.out.println("Next button clicked - navigating to select-mode.fxml");
            switchTo(event, "select-mode.fxml");
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error navigating to select mode: " + e.getMessage());
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

