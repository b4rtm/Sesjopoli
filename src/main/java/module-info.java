module com.example.project_sesjopoli {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;
    requires java.net.http;
    requires spring.web;
    requires javafx.media;


    opens com.example.project_sesjopoli to javafx.fxml;
    exports com.example.project_sesjopoli;
    exports com.example.project_sesjopoli.post_objects;
    opens com.example.project_sesjopoli.post_objects to javafx.fxml;
}