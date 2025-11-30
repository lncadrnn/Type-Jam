package com.example.typejam;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class TypeJam extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        // Load custom font
        Font.loadFont(getClass().getResourceAsStream("/fonts/LilitaOne-Regular.ttf"), 12);

        // Initialize sound system
        SoundManager.getInstance();

        // Ensure defaults enabled on first launch
        SettingsManager settings = SettingsManager.getInstance();
        if (settings.isFirstLaunch()) {
            settings.setSoundEffectsEnabled(true);
            settings.setMusicEnabled(true);
            // Set default volumes to 75%
            settings.setSfxVolume(0.75);
            settings.setMusicVolume(0.75);
        }

        FXMLLoader fxmlLoader = new FXMLLoader(TypeJam.class.getResource("main-menu.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 760, 495);
            // Attach global stylesheet with null check
        URL cssUrl = TypeJam.class.getResource("/styles/styles.css");
        if (cssUrl != null) {
            scene.getStylesheets().add(cssUrl.toExternalForm());
        } else {
            System.err.println("Warning: styles.css not found in resources.");
        }
        // Disable maximize/minimize: use UTILITY style (close only) and prevent resizing
        stage.setResizable(false);
        stage.setTitle("Typing Test Application");
        stage.setScene(scene);
        stage.show();

        // Start background music after stage is shown
        SoundManager.getInstance().startBackgroundMusic();

        // Mark first launch complete
        settings.setFirstLaunch(false);
    }
}
