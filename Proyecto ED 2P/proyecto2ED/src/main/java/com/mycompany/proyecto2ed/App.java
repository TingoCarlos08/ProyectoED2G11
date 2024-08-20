package com.mycompany.proyecto2ed;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {

    private static Scene scene;
    public static String pathFiles = "src/main/resources/files/";
    public static String pathImages = "src/main/resources/images/";
    public static String pathMusic = "src/main/resources/sonidos/";

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("inicio"), 394, 486);
        stage.setScene(scene);
        stage.show();
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    public static Parent loadFXML(String fxml) throws IOException {
        System.out.println("Cargando FXML: " + fxml + ".fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        if (fxmlLoader.getLocation() == null) {
            throw new IOException("No se pudo encontrar el archivo FXML: " + fxml + ".fxml");
        }
        return fxmlLoader.load();
    }

    public static Stage abrirNuevaVentana(String fxml, double width, double height) throws IOException {
        Stage nuevaVentana = new Stage();
        Parent root = loadFXML(fxml);
        Scene nuevaScene = new Scene(root, width, height);
        nuevaVentana.setScene(nuevaScene);
        nuevaVentana.setResizable(false);
        nuevaVentana.show();
        return nuevaVentana;
    }

    public static void main(String[] args) {
        launch();
    }

    public static Scene getScene() {
        return scene;
    }

    public static void setScene(Scene scene) {
        App.scene = scene;
    }
}
