package com.example.typejam;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

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
    private void onOneMinuteClick(ActionEvent event) {
        // Save the time and navigate to select-difficulty.fxml
        GameData.getInstance().setTime("1 Minute");

        try {
            System.out.println("1 Minute button clicked - navigating to select-difficulty.fxml");
            NavigationHelper.navigateTo(event, "select-time.fxml", "select-difficulty.fxml");
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error navigating to select difficulty: " + e.getMessage());
        }
    }

    @FXML
    private void onThreeMinuteClick(ActionEvent event) {
        // Save the time and navigate to select-difficulty.fxml
        GameData.getInstance().setTime("3 Minutes");

        try {
            System.out.println("3 Minutes button clicked - navigating to select-difficulty.fxml");
            NavigationHelper.navigateTo(event, "select-time.fxml", "select-difficulty.fxml");
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error navigating to select difficulty: " + e.getMessage());
        }
    }

    @FXML
    private void onFiveMinuteClick(ActionEvent event) {
        // Save the time and navigate to select-difficulty.fxml
        GameData.getInstance().setTime("5 Minutes");

        try {
            System.out.println("5 Minutes button clicked - navigating to select-difficulty.fxml");
            NavigationHelper.navigateTo(event, "select-time.fxml", "select-difficulty.fxml");
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error navigating to select difficulty: " + e.getMessage());
        }
    }

    // Ensure the back button is front-most at runtime so it can receive mouse events
    @FXML
    public void initialize() {
        if (back_btn != null) {
            back_btn.toFront();
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
