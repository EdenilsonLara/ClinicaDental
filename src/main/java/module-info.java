module com.example.democlinica {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.democlinica to javafx.fxml;
    exports com.example.democlinica;
}