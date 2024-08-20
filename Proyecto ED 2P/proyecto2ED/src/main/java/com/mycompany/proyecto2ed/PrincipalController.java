package com.mycompany.proyecto2ed;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
import javafx.stage.Stage;


public class PrincipalController implements Initializable {

    @FXML
    private Button BTNAgregar;

    @FXML
    private Label BTNEmpezar;

    @FXML
    private TextField txtPreguntas;

    @FXML
    private Label LBL1;

    @FXML
    private Label LBL2;

    @FXML
    private Label LBL3;

    @FXML
    private ImageView imgAnimales;

    private int cantidadPreguntas;
    private List<File> imagenesAnimales;
    private int indiceImagenActual;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarBotones();
        cargarImagenes();
        mostrarSiguienteImagen();
    }

    private void configurarBotones() {
        BTNEmpezar.setOnMouseClicked(e -> iniciarJuego());
        BTNAgregar.setOnMouseClicked(e -> abrirVentanaAgregarJuego());
    }

    private void cargarImagenes() {
        File carpeta = new File("src/main/resources/images/");
        File[] archivos = carpeta.listFiles((dir, name) -> name.toLowerCase().endsWith(".png") || name.toLowerCase().endsWith(".jpg") || name.toLowerCase().endsWith(".jpeg"));

        imagenesAnimales = new ArrayList<>();
        if (archivos != null) {
            for (File archivo : archivos) {
                imagenesAnimales.add(archivo);
            }
        }
        indiceImagenActual = 0;
    }

    private void mostrarSiguienteImagen() {
        if (imagenesAnimales != null && !imagenesAnimales.isEmpty()) {
            File archivoImagen = imagenesAnimales.get(indiceImagenActual);
            try (FileInputStream fis = new FileInputStream(archivoImagen)) {
                Image imagen = new Image(fis);
                imgAnimales.setImage(imagen);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            indiceImagenActual = (indiceImagenActual + 1) % imagenesAnimales.size();
        }
    }

    private void abrirVentanaAgregarJuego() {
        AgregarJuegoController.mapa = InicioController.formarMapaRespuestas("agregarAnimal.txt");
        try {
            App.abrirNuevaVentana("agregarJuego", 424, 448);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void iniciarJuego() {
        String textoPreguntas = txtPreguntas.getText();
        boolean esNumeroValido = esNumeroEntero(textoPreguntas);

        if (!esNumeroValido) {
            mostrarAlerta("Número de preguntas", "Por favor, ingrese un número entero válido en el campo de número de preguntas");
        } else {
            cantidadPreguntas = Integer.parseInt(textoPreguntas);
            int maximoPreguntas = InicioController.preguntasAnimal.size();

            if (cantidadPreguntas < 1 || cantidadPreguntas > maximoPreguntas) {
                mostrarAlerta("Número de preguntas", "Por favor, ingrese un número de preguntas adecuado. Recuerde que el máximo de preguntas es " + maximoPreguntas);
            } else {
                SessionManager session = SessionManager.getInstance();
                session.setNumeroDePreguntas(cantidadPreguntas);
                cerrarVentanaYIniciarEspera();
            }
        }
    }

    private void cerrarVentanaYIniciarEspera() {
        Stage stage = (Stage) txtPreguntas.getScene().getWindow();
        stage.close();
        try {
            App.abrirNuevaVentana("ventanaEspera", 260, 163);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private boolean esNumeroEntero(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static void mostrarAlerta(String titulo, String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.WARNING);
        alerta.setTitle(titulo);
        alerta.setContentText(mensaje);
        alerta.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alerta.showAndWait();
    }
}
