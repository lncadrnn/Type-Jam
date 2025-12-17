package com.example.typejam;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.io.IOException;

public class AboutUsController {

    @FXML
    private Button back_btn;

    // Ensure the back button stays on top so it remains clickable after revisits
    @FXML
    public void initialize() {
        if (back_btn != null) {
            back_btn.toFront();
        }
    }

    @FXML
    private void onBack(ActionEvent event) {
        try {
            NavigationHelper.navigateBack(event);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error navigating back: " + e.getMessage());
        }
    }
}
