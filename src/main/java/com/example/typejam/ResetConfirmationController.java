package com.example.typejam;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import java.util.function.Consumer;

/** Controller for the reset confirmation in-scene overlay. */
public class ResetConfirmationController {

    @FXML
    private StackPane modalRoot;

    @FXML
    private Button resetButton;

    @FXML
    private Button cancelButton;

    private boolean confirmed = false;
    private Consumer<Boolean> closeHandler; // Provided by parent to remove overlay

    public void setCloseHandler(Consumer<Boolean> handler) {
        this.closeHandler = handler;
    }

    @FXML
    private void onConfirmReset() {
        confirmed = true;
        notifyAndClose();
    }

    @FXML
    private void onCancel() {
        confirmed = false;
        notifyAndClose();
    }

    @FXML
    private void onResetButtonHover() {
        resetButton.setStyle("-fx-background-color:#c82333; -fx-background-radius:32; -fx-text-fill:white; -fx-font-weight:bold; -fx-scale-x:1.02; -fx-scale-y:1.02;");
    }

    @FXML
    private void onResetButtonExit() {
        resetButton.setStyle("-fx-background-color:#dc3545; -fx-background-radius:32; -fx-text-fill:white; -fx-font-weight:bold;");
    }

    @FXML
    private void onCancelButtonHover() {
        cancelButton.setStyle("-fx-background-color:#5a6268; -fx-background-radius:32; -fx-text-fill:white; -fx-font-weight:bold; -fx-scale-x:1.02; -fx-scale-y:1.02;");
    }

    @FXML
    private void onCancelButtonExit() {
        cancelButton.setStyle("-fx-background-color:#6c757d; -fx-background-radius:32; -fx-text-fill:white; -fx-font-weight:bold;");
    }

    public boolean isConfirmed() { return confirmed; }

    private void notifyAndClose() {
        if (closeHandler != null) {
            closeHandler.accept(confirmed);
        }
    }
}
