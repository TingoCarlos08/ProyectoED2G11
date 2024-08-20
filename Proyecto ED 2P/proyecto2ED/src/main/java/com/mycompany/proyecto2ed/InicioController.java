package com.mycompany.proyecto2ed;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import modelo.Juego;

public class InicioController implements Initializable {

    public static ArrayList<String> preguntasAnimal;
    public static Map<Juego, ArrayList<String>> respuestasAnimal;
    public static MediaPlayer musicaInicio;
    public static String idioma;

    @FXML
    private Button BtnJugar;
    @FXML
    private Label LBL1;
    @FXML
    private Label LBL2;
    @FXML
    private Label LBL11;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        inicializarJuego();
        configurarBotones();
    }

    private void inicializarJuego() {
        ManejoArchivos.borrarContenidoArchivo("juegosAnadidos.txt");
        idioma = "es";
        preguntasAnimal = ManejoArchivos.leerArchivo("preguntasAnimal.txt");
        respuestasAnimal = formarMapaRespuestas("respuestasAnimal.txt");
        musicaInicio = reproducirSonido("menu1.mp3");
    }

    private void configurarBotones() {
        BtnJugar.setOnAction(e -> iniciarJuego());
    }

    private void iniciarJuego() {
        Stage stage = (Stage) BtnJugar.getScene().getWindow();
        preguntasAnimal = ManejoArchivos.leerArchivo("preguntasAnimal.txt");
        respuestasAnimal = formarMapaRespuestas("respuestasAnimal.txt");

        try {
            App.abrirNuevaVentana("principal", 424, 520);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        stage.close();
    }

    public static MediaPlayer reproducirSonido(String nombre) {
        String ubicacion = App.pathMusic + nombre;
        Media media = new Media(new File(ubicacion).toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        mediaPlayer.play();
        return mediaPlayer;
    }

    public static Map<Juego, ArrayList<String>> formarMapaRespuestas(String nombreArchivo) {
        Map<Juego, ArrayList<String>> mapaRespuestas = new HashMap<>();
        ArrayList<String> lineas = ManejoArchivos.leerArchivo(nombreArchivo);

        for (String linea : lineas) {
            String[] partes = linea.split(";");
            String[] infoJuego = partes[0].split(",");
            ArrayList<String> respuestas = new ArrayList<>();
            for (int i = 1; i < partes.length; i++) {
                respuestas.add(partes[i]);
            }
            Juego juego = new Juego(infoJuego[0], infoJuego[1]);
            mapaRespuestas.put(juego, respuestas);
        }
        return mapaRespuestas;
    }
}
