package com.example.typejam;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class EnterNameController {

    @FXML
    private void onBack(ActionEvent event) throws IOException {
        switchTo(event, "main-menu.fxml");
    }

    @FXML
    private void onNext(ActionEvent event) throws IOException {
        switchTo(event, "select-mode.fxml");
    }

    private void switchTo(ActionEvent event, String fxmlName) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource(fxmlName));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = stage.getScene();
        if (scene == null) {
            scene = new Scene(root, 760, 495);
            stage.setScene(scene);
        } else {
            scene.setRoot(root);
        }
    }
}

