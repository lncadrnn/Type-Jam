package com.example.typejam;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Slider;
import javafx.scene.text.Text;

import java.io.IOException;
import java.util.Optional;

public class SettingsController {

    @FXML
    private Button back_btn;

    @FXML
    private Button resetDataBtn;

    @FXML
    private CheckBox soundEffectsCheckBox;

    @FXML
    private CheckBox backgroundMusicCheckBox;

    @FXML
    private Slider sfxVolumeSlider;

    @FXML
    private Slider musicVolumeSlider;

    @FXML
    private Text sfxVolumeLabel;

    @FXML
    private Text musicVolumeLabel;


    @FXML
    private void onBack(ActionEvent event) {
        try {
            NavigationHelper.navigateBack(event);
        } catch (IOException e) {
            System.err.println("Error navigating back: " + e.getMessage());
        }
    }

    @FXML
    private void onResetData(ActionEvent event) {

        // Show confirmation dialog
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Reset Data");
        alert.setHeaderText("Are you sure you want to reset all data?");
        alert.setContentText("This will permanently delete all leaderboard entries and cannot be undone.");
        alert.getDialogPane().setStyle("-fx-background-color: #ffffff;");

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            // User confirmed, proceed with reset
            boolean success = LeaderboardStorage.clearAllData();

            // Show result
            Alert resultAlert = new Alert(
                success ? Alert.AlertType.INFORMATION : Alert.AlertType.ERROR
            );
            resultAlert.setTitle("Reset Data");
            resultAlert.setHeaderText(null);
            resultAlert.setContentText(
                success ? "All data has been successfully reset!" : "Failed to reset data. Please try again."
            );
            resultAlert.getDialogPane().setStyle("-fx-background-color: #ffffff;");
            resultAlert.showAndWait();
        }
    }

    @FXML
    private void initialize() {
        // Load settings from SettingsManager
        SettingsManager settings = SettingsManager.getInstance();
        SoundManager soundManager = SoundManager.getInstance();

        // Initialize checkboxes with current settings
        if (soundEffectsCheckBox != null) {
            soundEffectsCheckBox.setSelected(settings.isSoundEffectsEnabled());
            soundEffectsCheckBox.selectedProperty().addListener((obs, oldVal, newVal) -> {
                settings.setSoundEffectsEnabled(newVal);
            });
        }

        if (backgroundMusicCheckBox != null) {
            backgroundMusicCheckBox.setSelected(settings.isMusicEnabled());
            backgroundMusicCheckBox.selectedProperty().addListener((obs, oldVal, newVal) -> {
                settings.setMusicEnabled(newVal);
                if (newVal) {
                    soundManager.startBackgroundMusic();
                } else {
                    soundManager.stopBackgroundMusic();
                }
            });
        }

        // Initialize volume sliders
        if (sfxVolumeSlider != null) {
            sfxVolumeSlider.setValue(settings.getSfxVolume() * 100);
            if (sfxVolumeLabel != null) {
                sfxVolumeLabel.setText(String.format("%.0f%%", sfxVolumeSlider.getValue()));
            }

            sfxVolumeSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
                double volume = newVal.doubleValue() / 100.0;
                settings.setSfxVolume(volume);
                soundManager.setSfxVolume(volume);
                if (sfxVolumeLabel != null) {
                    sfxVolumeLabel.setText(String.format("%.0f%%", newVal.doubleValue()));
                }
            });
        }

        if (musicVolumeSlider != null) {
            musicVolumeSlider.setValue(settings.getMusicVolume() * 100);
            if (musicVolumeLabel != null) {
                musicVolumeLabel.setText(String.format("%.0f%%", musicVolumeSlider.getValue()));
            }

            musicVolumeSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
                double volume = newVal.doubleValue() / 100.0;
                settings.setMusicVolume(volume);
                soundManager.setMusicVolume(volume);
                if (musicVolumeLabel != null) {
                    musicVolumeLabel.setText(String.format("%.0f%%", newVal.doubleValue()));
                }
            });
        }

        // Optionally purge old entries upon opening settings
        try {
            int removed = LeaderboardStorage.purgeEntriesOlderThanOneYear();
            if (removed > 0) {
                Alert info = new Alert(Alert.AlertType.INFORMATION);
                info.setTitle("Leaderboard Maintenance");
                info.setHeaderText(null);
                info.setContentText(removed + " old entries (>= 1 year) were purged.");
                info.getDialogPane().setStyle("-fx-background-color: #ffffff;");
                info.showAndWait();
            }
        } catch (Exception e) {
            System.err.println("Purge check failed: " + e.getMessage());
        }
    }
}
