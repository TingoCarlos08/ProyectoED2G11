package com.mycompany.proyecto2ed;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import modelo.BinaryTree;
import modelo.Juego;
import modelo.ListaCircularDoble;
import modelo.NodeBinaryTree;

public class PaginaPrincipalJuegoController implements Initializable {

    public static Integer numeroDePreguntas;
    public static BinaryTree<Object> arbolDeDecisiones = null;
    private int contadorRespuestas = 0;
    private int ultimoIndiceImagen = -1;
    private NodeBinaryTree<Object> nodoEnProceso;

    @FXML
    private Label etiquetaPregunta;
    @FXML
    private Button botonResponderSi;
    @FXML
    private Button botonResponderNo;
    @FXML
    private ImageView imagenInicio;
    @FXML
    private ImageView imagenPensando;
    @FXML
    private VBox panelPrincipal;

    public static String[] imagenesPensando = {"preocupado.gif", "secreto.gif", "neutral.gif", "cinico.gif"};
    @FXML
    private Label LBL2;
    @FXML
    private ImageView imagenMago;
    @FXML
    private Label LBLPreguntas;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        SessionManager session = SessionManager.getInstance();
        numeroDePreguntas = session.getNumeroDePreguntas();
        if (numeroDePreguntas == null) {
            numeroDePreguntas = 5; // Asigna un valor predeterminado o maneja la excepción como prefieras
        }

        configurarInterfaz();
        construirArbolDeDecisiones();
        nodoEnProceso = arbolDeDecisiones.getRoot();
        mostrarPregunta();
        configurarBotonInicio();
    }

    private void configurarInterfaz() {
        botonResponderSi.setCursor(Cursor.HAND);
        botonResponderNo.setCursor(Cursor.HAND);
        botonResponderSi.setOnAction(e -> manejarRespuesta("si"));
        botonResponderNo.setOnAction(e -> manejarRespuesta("no"));
    }

    private void construirArbolDeDecisiones() {
        actualizarImagen();
        arbolDeDecisiones = crearArbolDePreguntas(numeroDePreguntas, InicioController.preguntasAnimal, InicioController.respuestasAnimal);
    }

    private void mostrarPregunta() {
        etiquetaPregunta.setText((String) nodoEnProceso.getContent());
    }

    private void actualizarImagen() {
        int intervaloImagen = numeroDePreguntas / imagenesPensando.length;
        int indiceActual = intervaloImagen > 0 ? (contadorRespuestas / intervaloImagen) % imagenesPensando.length : 1;
        if (indiceActual == ultimoIndiceImagen) {
            return;
        }
        ultimoIndiceImagen = indiceActual;
        cargarImagenPensando(imagenesPensando[indiceActual]);
    }

    private void cargarImagenPensando(String nombreImagen) {
        try (FileInputStream streamImagen = new FileInputStream(App.pathImages + nombreImagen)) {
            Image imagen = new Image(streamImagen);
            imagenPensando.setImage(imagen);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void manejarRespuesta(String respuesta) {
        contadorRespuestas++;
        actualizarImagen();
        nodoEnProceso = respuesta.equals("si") ? nodoEnProceso.getLeft().getRoot() : nodoEnProceso.getRight().getRoot();

        if (esNodoFinal(nodoEnProceso)) {
            procesarResultado();
        } else {
            mostrarPregunta();
        }
    }

    private boolean esNodoFinal(NodeBinaryTree<Object> nodo) {
        return nodo.getLeft() == null && nodo.getRight() == null;
    }

    private void procesarResultado() {
        ArrayList<Juego> juegos = (ArrayList<Juego>) nodoEnProceso.getContent();

        if (!juegos.isEmpty()) {
            mostrarResultado(juegos);
        } else {
            mostrarDerrota();
        }
    }

    private void mostrarResultado(ArrayList<Juego> juegos) {
        if (juegos.size() == 1) {
            Juego juego = juegos.get(0);
            InfoJuegoController.juegosLista = new ListaCircularDoble(new ArrayList<>(List.of(juego)));
            String nombreImagen = juego.getNombre().toLowerCase() + ".png";
            Image imagenAnimal = cargarImagen(nombreImagen);
            imagenMago.setImage(imagenAnimal);
            mostrarImagenYVentana("feliz.gif", "infoJuego", 466, 494);
            etiquetaPregunta.setText("El animal es: " + juego.getNombre());
        } else {
            PosiblesController.lista = juegos;
            mostrarImagenYVentana("feliz.gif", "posibles", 424, 448);
            etiquetaPregunta.setText("Varios animales encontrados");
        }
    }
    
    private Image cargarImagen(String nombreImagen) {
        try (FileInputStream streamImagen = new FileInputStream(App.pathImages + nombreImagen)) {
            return new Image(streamImagen);
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    private void mostrarDerrota() {
        mostrarImagenYVentana("triste.gif", null, 0, 0);
        etiquetaPregunta.setText("No se encontró un animal con esas características.");
    }

    private void mostrarImagenYVentana(String nombreImagen, String nombreVentana, int ancho, int alto) {
        try {
            panelPrincipal.getChildren().clear();
            FileInputStream fis = new FileInputStream(App.pathImages + nombreImagen);
            Image image = new Image(fis, 250, 250, true, true);
            ImageView imageView = new ImageView(image);
            panelPrincipal.getChildren().add(imageView);
            if (nombreVentana != null) {
                App.abrirNuevaVentana(nombreVentana, ancho, alto);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static BinaryTree<Object> crearArbol(int nivelActual, int maxPreguntas, List<String> listaPreguntas) {
        if (nivelActual >= maxPreguntas) {
            return new BinaryTree<>(new NodeBinaryTree<>(new ArrayList<Juego>()));
        } else {
            NodeBinaryTree<Object> nodo = new NodeBinaryTree<>(listaPreguntas.get(nivelActual));
            nodo.setLeft(crearArbol(nivelActual + 1, maxPreguntas, listaPreguntas));
            nodo.setRight(crearArbol(nivelActual + 1, maxPreguntas, listaPreguntas));
            return new BinaryTree<>(nodo);
        }
    }

    public static BinaryTree<Object> crearArbolDePreguntas(int maxPreguntas, ArrayList<String> preguntas, Map<Juego, ArrayList<String>> respuestas) {
        BinaryTree<Object> arbol = crearArbol(0, maxPreguntas, preguntas);

        for (Map.Entry<Juego, ArrayList<String>> entry : respuestas.entrySet()) {
            Juego juego = entry.getKey();
            ArrayList<String> respuestasJuego = entry.getValue();
            NodeBinaryTree<Object> nodo = arbol.getRoot();

            for (int i = 0; i < maxPreguntas; i++) {
                nodo = respuestasJuego.get(i).equals("si") ? nodo.getLeft().getRoot() : nodo.getRight().getRoot();
            }

            ((ArrayList<Juego>) nodo.getContent()).add(juego);
        }

        return arbol;
    }

    private void configurarBotonInicio() {
        imagenInicio.setOnMouseClicked(e -> cerrarVentanaInicio());
    }

    private void cerrarVentanaInicio() {
        Stage s = (Stage) imagenInicio.getScene().getWindow();
        s.close();
        try {
            App.abrirNuevaVentana("principal", 416, 520);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
