package com.example.typejam;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;

public class TypingGameController {

    @FXML
    private TextField typingField;

    @FXML
    private Text playerNameText;

    @FXML
    private Text difficultyText;

    @FXML
    private javafx.scene.control.Button backButton;

    @FXML
    public void initialize() {
        // Get data from GameData singleton
        GameData gameData = GameData.getInstance();
        String playerName = gameData.getPlayerName() != null ? gameData.getPlayerName() : "Player";
        String difficulty = gameData.getDifficulty() != null ? gameData.getDifficulty() : "Difficulty";

        // Truncate player name to 30 characters maximum with ellipsis
        if (playerName.length() > 30) {
            playerName = playerName.substring(0, 30) + "...";
        }

        // Set the text values
        playerNameText.setText(playerName);
        difficultyText.setText(difficulty + " Level");
    }

    @FXML
    public void onBack(ActionEvent event) {
        System.out.println("Back button clicked!");
        try {
            switchTo(event, "ready-scene.fxml");
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error loading ready-scene.fxml: " + e.getMessage());
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

