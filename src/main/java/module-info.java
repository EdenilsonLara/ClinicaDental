module com.example.democlinica {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.democlinica to javafx.fxml;
    exports com.example.democlinica;
}