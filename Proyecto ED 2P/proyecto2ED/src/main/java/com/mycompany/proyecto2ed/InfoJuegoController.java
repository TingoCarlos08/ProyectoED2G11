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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import modelo.Juego;
import modelo.ListaCircularDoble;

public class InfoJuegoController implements Initializable {

    @FXML
    private ImageView imagenJuegoActual;
    @FXML
    private Label nombreJuegoActual;
    @FXML
    private Label descripcionJuego;
    @FXML
    private Button botonAnterior;
    @FXML
    private Button botonSiguiente;

    public static ListaCircularDoble juegosLista;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        actualizarPantalla();

        botonAnterior.setOnMouseClicked(e -> {
            juegosLista.getAnterior();
            actualizarPantalla();
        });

        botonSiguiente.setOnMouseClicked(e -> {
            juegosLista.getSiguiente();
            actualizarPantalla();
        });
    }

    private void actualizarPantalla() {
        Juego juego = juegosLista.getActual();
        cargarImagenJuego(juego.getNombreImagen());
        nombreJuegoActual.setText(juego.getNombre());
        descripcionJuego.setWrapText(true);
        descripcionJuego.setText(obtenerDescripcion(juego.getNombre()));
    }

    private void cargarImagenJuego(String nombreImagen) {
        try (FileInputStream fis = new FileInputStream(App.pathImages + nombreImagen)) {
            Image imagen = new Image(fis, 250, 250, true, true);
            imagenJuegoActual.setImage(imagen);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String obtenerDescripcion(String nombreJuego) {
        String archivoDescripcion = InicioController.idioma.equals("es") ? "descripcionAnimal.txt" : "descripcionAnimalTraducido.txt";
        ArrayList<String> lineasArchivo = ManejoArchivos.leerArchivo(archivoDescripcion);

        for (String linea : lineasArchivo) {
            String[] partes = linea.split(",");
            if (partes[0].trim().equals(nombreJuego)) {
                return partes[1].trim();
            }
        }
        return "";
    }
}
