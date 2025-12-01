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
        // Set challenge mode and go to name entry (new flow).
        GameData.getInstance().setMode("Challenge Mode");
        try {
            System.out.println("Challenge Mode selected - navigating to enter-name.fxml");
            NavigationHelper.navigateTo(event, "select-mode.fxml", "enter-name.fxml");
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error navigating to enter name: " + e.getMessage());
        }
    }

    @FXML
    private void onEndlessModeClick(ActionEvent event) {
        // Practice mode skips name & time selection; goes straight to difficulty.
        GameData gameData = GameData.getInstance();
        gameData.setMode("Practice Mode");
        // Clear player name for Practice Mode since it doesn't use names
        gameData.setPlayerName(null);
        try {
            System.out.println("Practice Mode selected - navigating to select-difficulty.fxml");
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
