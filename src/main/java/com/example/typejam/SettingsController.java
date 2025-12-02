package com.example.typejam;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Slider;
import javafx.scene.effect.BoxBlur;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

import java.io.IOException;

public class SettingsController {

    @FXML private StackPane rootStackPane;
    @FXML private AnchorPane contentAnchor; // new for targeted blur
    @FXML private Button back_btn;
    @FXML private Button resetDataBtn;
    @FXML private CheckBox soundEffectsCheckBox;
    @FXML private CheckBox backgroundMusicCheckBox;
    @FXML private Slider sfxVolumeSlider;
    @FXML private Slider musicVolumeSlider;
    @FXML private Text sfxVolumeLabel;
    @FXML private Text musicVolumeLabel;

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
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("reset-confirmation-modal.fxml"));
            StackPane overlay = loader.load();
            ResetConfirmationController modalController = loader.getController();

            // Blur only background content, not the overlay
            BoxBlur blur = new BoxBlur(8, 8, 3);
            contentAnchor.setEffect(blur);

            overlay.prefWidthProperty().bind(rootStackPane.widthProperty());
            overlay.prefHeightProperty().bind(rootStackPane.heightProperty());

            // Simple entrance animation (fade + scale)
            overlay.setOpacity(0);
            overlay.setScaleX(0.96);
            overlay.setScaleY(0.96);
            javafx.animation.FadeTransition fade = new javafx.animation.FadeTransition(javafx.util.Duration.millis(180), overlay);
            fade.setFromValue(0); fade.setToValue(1);
            javafx.animation.ScaleTransition scale = new javafx.animation.ScaleTransition(javafx.util.Duration.millis(220), overlay);
            scale.setFromX(0.96); scale.setFromY(0.96); scale.setToX(1); scale.setToY(1);
            javafx.animation.ParallelTransition pt = new javafx.animation.ParallelTransition(fade, scale);

            modalController.setCloseHandler(confirmed -> {
                rootStackPane.getChildren().remove(overlay);
                contentAnchor.setEffect(null);
                if (confirmed) {
                    boolean success = LeaderboardStorage.clearAllData();
                    ToastUtil.showToast(rootStackPane,
                            success ? "Leaderboards has been successfully reset!" : "Failed to reset Leaderboards. Please try again.",
                            success ? ToastUtil.ToastType.SUCCESS : ToastUtil.ToastType.ERROR);
                }
            });

            rootStackPane.getChildren().add(overlay);
            pt.play();
        } catch (IOException e) {
            System.err.println("Error showing reset confirmation overlay: " + e.getMessage());
            ToastUtil.showToast(rootStackPane, "Error showing confirmation dialog", ToastUtil.ToastType.ERROR);
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
            if (removed > 0 && rootStackPane != null) {
                ToastUtil.showToast(rootStackPane, removed + " old entries (>= 1 year) were purged.", ToastUtil.ToastType.INFO);
            }
        } catch (Exception e) {
            System.err.println("Purge check failed: " + e.getMessage());
        }
    }
}
