package com.example.typejam;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
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
    private TextFlow titleTextFlow;

    @FXML
    private TextFlow descriptionTextFlow;

    @FXML
    public void initialize() {
        // Get data from GameData singleton
        GameData gameData = GameData.getInstance();
        String playerName = gameData.getPlayerName();
        String mode = gameData.getMode() != null ? gameData.getMode().toUpperCase() : "MODE";
        String difficulty = gameData.getDifficulty() != null ? gameData.getDifficulty().toUpperCase() : "DIFFICULTY";

        boolean isPracticeMode = gameData.getMode() != null && gameData.getMode().equalsIgnoreCase("Practice Mode");

        // Different title for Practice Mode vs Challenge Mode
        if (isPracticeMode) {
            // For Practice Mode, just show "PRACTICE MODE" as the title
            Text titleText = new Text("PRACTICE MODE");
            titleText.setFill(Color.web("#2b5237"));
            titleText.setFont(Font.font("Arial", FontWeight.BOLD, 28));
            titleTextFlow.getChildren().add(titleText);
        } else {
            // For Challenge Mode, show "Are you ready, [NAME]?"
            if (playerName == null) {
                playerName = "PLAYER";
            }

            // Truncate player name to 30 characters maximum with ellipsis
            if (playerName.length() > 30) {
                playerName = playerName.substring(0, 30) + "...";
            }

            Text titlePart1 = new Text("Are you ready, ");
            titlePart1.setFill(Color.web("#2b5237"));
            titlePart1.setFont(Font.font("Arial", FontWeight.BOLD, 28));

            Text titleName = new Text(playerName);
            titleName.setFill(Color.web("#2b5237"));
            titleName.setFont(Font.font("Arial", FontWeight.BOLD, 28));

            Text titlePart2 = new Text("?");
            titlePart2.setFill(Color.web("#2b5237"));
            titlePart2.setFont(Font.font("Arial", FontWeight.BOLD, 28));

            titleTextFlow.getChildren().addAll(titlePart1, titleName, titlePart2);
        }

        // Create description text with colored mode and difficulty
        Text descPart1 = new Text("You have selected ");
        descPart1.setFill(Color.web("#2b5237"));
        descPart1.setFont(Font.font("Arial", FontWeight.BOLD, 25));

        Text descMode = new Text(mode);
        descMode.setFill(Color.web("#2b5237"));
        descMode.setFont(Font.font("Arial", FontWeight.BOLD, 25));

        Text descPart2 = new Text(" in ");
        descPart2.setFill(Color.web("#2b5237"));
        descPart2.setFont(Font.font("Arial", FontWeight.BOLD, 25));

        Text descDifficulty = new Text(difficulty);
        descDifficulty.setFill(Color.web("#2b5237"));
        descDifficulty.setFont(Font.font("Arial", FontWeight.BOLD, 25));

        Text descPart3 = new Text(" LEVEL.");
        descPart3.setFill(Color.web("#2b5237"));
        descPart3.setFont(Font.font("Arial", FontWeight.BOLD, 25));

        descriptionTextFlow.getChildren().addAll(descPart1, descMode, descPart2, descDifficulty, descPart3);
    }

    @FXML
    private void onMainMenu(ActionEvent event) throws IOException {
        // Reset game data when returning to main menu
        GameData.getInstance().reset();
        GameData.getInstance().clearNavigationHistory();
        NavigationHelper.switchToScene(event, "main-menu.fxml");
    }

    @FXML
    private void onLeaderboard(ActionEvent event) throws IOException {
        NavigationHelper.navigateTo(event, "ready-scene.fxml", "leaderboards.fxml");
    }

    @FXML
    private void onSettings(ActionEvent event) throws IOException {
        NavigationHelper.navigateTo(event, "ready-scene.fxml", "settings.fxml");
    }

    @FXML
    private void onAbout(ActionEvent event) throws IOException {
        NavigationHelper.navigateTo(event, "ready-scene.fxml", "about-us.fxml");
    }

    @FXML
    private void onLetsType(ActionEvent event) throws IOException {
        NavigationHelper.navigateTo(event, "ready-scene.fxml", "typing-game.fxml");
    }
}

