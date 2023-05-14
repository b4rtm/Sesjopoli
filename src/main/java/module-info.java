module com.example.project_sesjopoli {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.project_sesjopoli to javafx.fxml;
    exports com.example.project_sesjopoli;
}