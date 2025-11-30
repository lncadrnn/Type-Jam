package com.example.typejam;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.io.IOException;

public class SelectDifficultyController {

    @FXML
    private Button back_btn;

    @FXML
    private Button easyBtn;

    @FXML
    private Button averageBtn;

    @FXML
    private Button difficultBtn;

    @FXML
    private void onEasyClick(ActionEvent event) {
        // Save the difficulty and navigate to ready-scene.fxml
        GameData.getInstance().setDifficulty("Easy");

        try {
            System.out.println("Easy button clicked - navigating to ready-scene.fxml");
            NavigationHelper.navigateTo(event, "select-difficulty.fxml", "ready-scene.fxml");
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error navigating to ready scene: " + e.getMessage());
        }
    }

    @FXML
    private void onAverageClick(ActionEvent event) {
        // Save the difficulty and navigate to ready-scene.fxml
        GameData.getInstance().setDifficulty("Medium");

        try {
            System.out.println("Medium button clicked - navigating to ready-scene.fxml");
            NavigationHelper.navigateTo(event, "select-difficulty.fxml", "ready-scene.fxml");
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error navigating to ready scene: " + e.getMessage());
        }
    }

    @FXML
    private void onDifficultClick(ActionEvent event) {
        // Save the difficulty and navigate to ready-scene.fxml
        GameData.getInstance().setDifficulty("Hard");

        try {
            System.out.println("Hard button clicked - navigating to ready-scene.fxml");
            NavigationHelper.navigateTo(event, "select-difficulty.fxml", "ready-scene.fxml");
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error navigating to ready scene: " + e.getMessage());
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
