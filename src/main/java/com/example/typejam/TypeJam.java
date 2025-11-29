package com.example.typejam;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;

public class TypeJam extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        // Load custom font
        Font.loadFont(getClass().getResourceAsStream("/fonts/LilitaOne-Regular.ttf"), 12);

        FXMLLoader fxmlLoader = new FXMLLoader(TypeJam.class.getResource("main-menu.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 760, 495);
        // Attach global stylesheet
        String css = TypeJam.class.getResource("/styles/styles.css").toExternalForm();
        scene.getStylesheets().add(css);
        // Disable maximize/minimize: use UTILITY style (close only) and prevent resizing
        stage.setResizable(false);
        stage.setTitle("Typing Test Application");
        stage.setScene(scene);
        stage.show();
    }
}
