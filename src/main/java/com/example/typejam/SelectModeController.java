package com.example.typejam;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;

public class SelectModeController {

    @FXML
    private AnchorPane rootPane;

    @FXML
    private Button back_btn;

    @FXML
    private Button next_btn;

    @FXML
    private Button timeChallengeBtn;

    @FXML
    private Button endlessModeBtn;

    @FXML
    private javafx.scene.text.Text errorText;

    private StackPane overlayPane;

    private Button selectedModeButton = null;
    private String selectedMode = null;

    @FXML
    private void onTimeChallengeClick(ActionEvent event) {
        // Show the Time Challenge overlay on top of current scene
        try {
            System.out.println("Time Challenge button clicked - showing overlay");
            showOverlay();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error showing time challenge overlay: " + e.getMessage());
        }
    }

    private void showOverlay() throws IOException {
        if (overlayPane == null) {
            // Load the overlay FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("select-time.fxml"));
            overlayPane = loader.load();

            // Position it to cover the entire scene
            AnchorPane.setTopAnchor(overlayPane, 0.0);
            AnchorPane.setBottomAnchor(overlayPane, 0.0);
            AnchorPane.setLeftAnchor(overlayPane, 0.0);
            AnchorPane.setRightAnchor(overlayPane, 0.0);

            // Add click handler to close overlay when clicking on background
            overlayPane.setOnMouseClicked(e -> hideOverlay());
        }

        // Add overlay to the root pane
        if (!rootPane.getChildren().contains(overlayPane)) {
            rootPane.getChildren().add(overlayPane);
        }
    }

    private void hideOverlay() {
        if (overlayPane != null && rootPane.getChildren().contains(overlayPane)) {
            rootPane.getChildren().remove(overlayPane);
        }
    }

    @FXML
    private void onEndlessModeClick(ActionEvent event) {
        selectMode(endlessModeBtn, "Endless Mode");
        // Hide error message if it was showing
        errorText.setVisible(false);
    }

    private void selectMode(Button selectedButton, String mode) {
        // Reset previously selected button
        if (selectedModeButton != null) {
            selectedModeButton.setStyle("-fx-background-color: #f0f0f0; -fx-border-color: #cccccc; -fx-border-width: 2; -fx-background-radius: 10; -fx-border-radius: 10;");
        }

        // Apply selected style to the new button
        selectedModeButton = selectedButton;
        selectedMode = mode;
        selectedModeButton.setStyle("-fx-background-color: rgba(43, 82, 55, 0.37); -fx-border-color: #2B5237; -fx-border-width: 2; -fx-background-radius: 10; -fx-border-radius: 10;");
    }

    @FXML
    private void onBack(ActionEvent event) {
        try {
            System.out.println("Back button clicked - navigating to enter-name.fxml");
            switchTo(event, "enter-name.fxml");
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error navigating to enter name: " + e.getMessage());
        }
    }

    @FXML
    private void onNext(ActionEvent event) {
        // Check if a mode is selected
        if (selectedModeButton == null) {
            // Show error message
            errorText.setVisible(true);
            return;
        }

        // Hide error message if it was showing
        errorText.setVisible(false);

        // Save the selected mode to GameData
        GameData.getInstance().setMode(selectedMode);

        try {
            System.out.println("Next button clicked - navigating to select-difficulty.fxml");
            switchTo(event, "select-difficulty.fxml");
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error navigating to select difficulty: " + e.getMessage());
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

