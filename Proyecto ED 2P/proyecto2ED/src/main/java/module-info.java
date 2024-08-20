module com.mycompany.proyecto2ed {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires org.eclipse.scout.json;

    opens com.mycompany.proyecto2ed to javafx.fxml;
    exports com.mycompany.proyecto2ed;
}
