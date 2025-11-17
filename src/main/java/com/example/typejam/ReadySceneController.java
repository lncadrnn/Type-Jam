package com.example.typejam;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class ReadySceneController {

    @FXML
    private Button mainMenuButton;

    @FXML
    private Button leaderboardButton;

    @FXML
    private Button settingsButton;

    @FXML
    private Button aboutButton;

    @FXML
    private Button letsTypeButton;

    @FXML
    private void onMainMenu(ActionEvent event) throws IOException {
        switchTo(event, "main-menu.fxml");
    }

    @FXML
    private void onLeaderboard(ActionEvent event) throws IOException {
        switchTo(event, "leaderboards.fxml");
    }

    @FXML
    private void onSettings(ActionEvent event) throws IOException {
        switchTo(event, "settings.fxml");
    }

    @FXML
    private void onAbout(ActionEvent event) throws IOException {
        switchTo(event, "about-us.fxml");
    }

    @FXML
    private void onLetsType(ActionEvent event) throws IOException {
        switchTo(event, "typing-game.fxml");
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

