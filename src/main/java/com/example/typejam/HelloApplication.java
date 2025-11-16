package com.example.typejam;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        // Load custom font
        Font.loadFont(getClass().getResourceAsStream("/fonts/LilitaOne-Regular.ttf"), 12);

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("main-menu.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 760, 495);
        // Disable maximize/minimize: use UTILITY style (close only) and prevent resizing
        stage.setResizable(false);
        stage.setTitle("Typing Test Application");
        stage.setScene(scene);
        stage.show();
    }
}
