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

public class ResultSceneController {

    @FXML
    private Button backButton;

    @FXML
    public void onBack(ActionEvent event) {
        System.out.println("Back button clicked from result scene!");
        try {
            switchTo(event, "main-menu.fxml");
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error loading main-menu.fxml: " + e.getMessage());
        }
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

