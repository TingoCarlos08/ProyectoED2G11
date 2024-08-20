/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.proyecto2ed;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import modelo.Juego;

public class AgregarAnimalController implements Initializable {

    @FXML
    private Label LBL1;
    @FXML
    private Label LBLPosibles;
    @FXML
    private FlowPane APLista;
    @FXML
    private Button BTNAgregar;

    public static Map<Juego, ArrayList<String>> mapa;
    private VBox contenedorSeleccionado;
    private Juego juegoSeleccionado;
    private ArrayList<String> respuestasDelJuego;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        contenedorSeleccionado = null;
        inicializarListaDeJuegos(mapa, APLista);
        configurarBotonAgregar();
    }

    private void inicializarListaDeJuegos(Map<Juego, ArrayList<String>> mapa, FlowPane panelLista) {
        for (Juego juego : mapa.keySet()) {
            VBox contenedorJuego = PosiblesController.crearContenedorParaJuego(juego);
            contenedorJuego.setOnMouseClicked(e -> seleccionarJuego(contenedorJuego, juego, mapa.get(juego)));
            panelLista.getChildren().add(contenedorJuego);
        }
    }

    private void seleccionarJuego(VBox contenedor, Juego juego, ArrayList<String> respuestas) {
        if (contenedorSeleccionado != null) {
            contenedorSeleccionado.setStyle("-fx-background-color: White;");
        }
        contenedor.setStyle("-fx-background-color: #ADD8E6;");
        contenedorSeleccionado = contenedor;
        juegoSeleccionado = juego;
        respuestasDelJuego = respuestas;
    }

    private void configurarBotonAgregar() {
        BTNAgregar.setOnMouseClicked(e -> {
            if (juegoSeleccionado == null) {
                mostrarAlerta("Seleccionar contenedor", "Por favor, seleccione un juego para añadir.");
            } else if (verificarSiJuegoYaEstaAñadido(juegoSeleccionado)) {
                mostrarAlerta("Juego encontrado", "El juego ya está en la lista de juegos del sistema. Escoja otro juego.");
            } else {
                agregarJuego();
                cerrarVentana();
            }
        });
    }

    private boolean verificarSiJuegoYaEstaAñadido(Juego juego) {
        ArrayList<String> juegosAñadidos = ManejoArchivos.leerArchivo("juegosAnadidos.txt");
        return juegosAñadidos.contains(juego.getNombre());
    }

    private void agregarJuego() {
        InicioController.respuestasAnimal.put(juegoSeleccionado, respuestasDelJuego);
        
        ManejoArchivos.escribirEnArchivo("juegosAnadidos.txt", juegoSeleccionado.getNombre());
    }

    private void cerrarVentana() {
        Stage stage = (Stage) BTNAgregar.getScene().getWindow();
        stage.close();
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        PrincipalController.mostrarAlerta(titulo, mensaje);
    }
}
