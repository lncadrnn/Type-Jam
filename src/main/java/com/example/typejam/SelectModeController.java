package com.example.typejam;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.io.IOException;

public class SelectModeController {

    @FXML
    private Button back_btn;

    @FXML
    private Button timeChallengeBtn;

    @FXML
    private Button endlessModeBtn;

    @FXML
    private void onTimeChallengeClick(ActionEvent event) {
        // Save the mode and navigate to select-time.fxml
        GameData.getInstance().setMode("Challenge Mode");

        try {
            System.out.println("Challenge Mode button clicked - navigating to select-time.fxml");
            NavigationHelper.navigateTo(event, "select-mode.fxml", "select-time.fxml");
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error navigating to select time: " + e.getMessage());
        }
    }

    @FXML
    private void onEndlessModeClick(ActionEvent event) {
        // Save the mode and navigate to select-difficulty.fxml
        GameData.getInstance().setMode("Practice Mode");

        try {
            System.out.println("Practice Mode button clicked - navigating to select-difficulty.fxml");
            NavigationHelper.navigateTo(event, "select-mode.fxml", "select-difficulty.fxml");
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error navigating to select difficulty: " + e.getMessage());
        }
    }

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
}
