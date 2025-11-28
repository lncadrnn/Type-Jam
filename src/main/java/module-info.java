module com.example.typejam {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;

    opens com.example.typejam to javafx.fxml;
    exports com.example.typejam;
}
