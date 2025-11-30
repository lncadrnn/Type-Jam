package com.example.typejam;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.io.IOException;

public class MainMenuController {

    @FXML
    private Button play_btn;

    @FXML
    private Button leaderboard_btn;

    @FXML
    private Button about_btn;

    @FXML
    private Button settings_btn;

    @FXML
    private void onPlay(ActionEvent event) throws IOException {
        NavigationHelper.navigateTo(event, "main-menu.fxml", "select-mode.fxml");
    }

    @FXML
    private void onLeaderboard(ActionEvent event) throws IOException {
        NavigationHelper.navigateTo(event, "main-menu.fxml", "leaderboards.fxml");
    }

    @FXML
    private void onAbout(ActionEvent event) throws IOException {
        NavigationHelper.navigateTo(event, "main-menu.fxml", "about-us.fxml");
    }

    @FXML
    private void onSettings(ActionEvent event) throws IOException {
        NavigationHelper.navigateTo(event, "main-menu.fxml", "settings.fxml");
    }
}
