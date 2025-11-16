package com.example.typejam;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("main-menu.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 760, 495);
        // Disable maximize/minimize: use UTILITY style (close only) and prevent resizing
        stage.setResizable(false);
        stage.setTitle("Typing Test Application");
        stage.setScene(scene);
        stage.show();
    }
}
