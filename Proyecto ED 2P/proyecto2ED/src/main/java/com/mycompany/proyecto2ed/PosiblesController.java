/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.proyecto2ed;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import modelo.Juego;

public class PosiblesController implements Initializable {

    public static ArrayList<Juego> lista;

    @FXML
    private Label LBLPosibles;
    @FXML
    private FlowPane APLista;
    @FXML
    private Label LBL1;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarEtiquetas();
        inicializarListaDeJuegos();
    }

    private void configurarEtiquetas() {
        LBLPosibles.setText("Animales");
    }

    private void inicializarListaDeJuegos() {
        for (Juego juego : lista) {
            VBox contenedor = crearContenedorParaJuego(juego);
            contenedor.setOnMouseClicked(e -> abrirVentanaInformacion(juego));
            APLista.getChildren().add(contenedor);
        }
    }

    private void abrirVentanaInformacion(Juego juego) {
        try {
            App.abrirNuevaVentana("infoJuego", 466, 494);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static VBox crearContenedorParaJuego(Juego juego) {
        Label nombre = new Label(juego.getNombre());
        nombre.setAlignment(Pos.CENTER);

        ImageView imageView = new ImageView();

        try (FileInputStream imageStream = new FileInputStream(App.pathImages + juego.getNombreImagen())) {
            Image image = new Image(imageStream, 80, 80, true, true);
            imageView.setImage(image);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        VBox contenedor = new VBox();
        VBox contenedorImagen = new VBox();
        VBox contenedorNombre = new VBox();

        contenedorImagen.setPadding(new Insets(15, 15, 15, 15));
        contenedorNombre.setPadding(new Insets(15, 15, 15, 15));
        contenedorNombre.setAlignment(Pos.CENTER);
        contenedorImagen.setAlignment(Pos.CENTER);

        contenedorNombre.getChildren().add(nombre);
        contenedorImagen.getChildren().add(imageView);
        contenedor.setStyle("-fx-background-color: white;"
                + "-fx-background-radius: 15;");
        contenedor.getChildren().addAll(contenedorNombre, contenedorImagen);
        return contenedor;
    }
}
