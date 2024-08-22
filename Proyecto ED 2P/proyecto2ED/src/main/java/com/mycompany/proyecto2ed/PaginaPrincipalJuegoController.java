package com.mycompany.proyecto2ed;

import static com.mycompany.proyecto2ed.PrincipalController.mostrarAlerta;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
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
    private String respuestasUsuario = "";

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
    @FXML
    private TextField nombreNuevoAnimal;
    @FXML
    private Label nombreArchivoImagen;
    private File archivoImagenNuevoAnimal;
    
    public static String[] imagenesPensando = {"preocupado.gif", "secreto.gif", "neutral.gif", "cinico.gif"};
    @FXML
    private Label LBL2;
    @FXML
    private ImageView imagenMago;
    @FXML
    private Label LBLPreguntas;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        numeroDePreguntas = SessionManager.getInstance().getNumeroDePreguntas();
        if (numeroDePreguntas == null) {
            numeroDePreguntas = 20; // Asigna un valor predeterminado o maneja la excepción como prefieras
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
    private void cargarImagenNuevoAnimal() {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Imágenes", "*.png", "*.jpg", "*.jpeg")
            );
            archivoImagenNuevoAnimal = fileChooser.showOpenDialog(panelPrincipal.getScene().getWindow());

            if (archivoImagenNuevoAnimal != null) {
                nombreArchivoImagen.setText(archivoImagenNuevoAnimal.getName());
            }
        }

    private void agregarNuevoAnimal() {
            String nombreAnimal = nombreNuevoAnimal.getText().trim();

            if (nombreAnimal.isEmpty() || archivoImagenNuevoAnimal == null) {
                mostrarAlerta("Faltan datos", "Por favor, ingrese el nombre y la imagen del nuevo animal.");
                return;
            }

            // Construir la entrada de respuestas del nuevo animal
            StringBuilder entrada = new StringBuilder();
            entrada.append(nombreAnimal).append(",")
                   .append(nombreAnimal.toLowerCase()).append(".png");

            // Suponiendo que las respuestas dadas hasta ahora están en el nodo actual
            // Recorremos desde la raíz hasta el nodo actual para obtener las respuestas
            NodeBinaryTree<Object> nodoActual = arbolDeDecisiones.getRoot();
            while (!nodoActual.equals(nodoEnProceso)) {
                if (nodoEnProceso.equals(nodoActual.getLeft().getRoot())) {
                    entrada.append(";si");
                    nodoActual = nodoActual.getLeft().getRoot();
                } else if (nodoEnProceso.equals(nodoActual.getRight().getRoot())) {
                    entrada.append(";no");
                    nodoActual = nodoActual.getRight().getRoot();
                }
            }

            // Añadir respuestas 'no' para completar el número máximo de preguntas
            while (entrada.toString().split(";").length - 1 < numeroDePreguntas) {
                entrada.append(";no");
            }

            // Escribir la nueva entrada en el archivo
            ManejoArchivos.escribirEnArchivo("respuestaAnimal.txt", entrada.toString());

            // Guardar la imagen en el lugar adecuado
            try {
                Files.copy(archivoImagenNuevoAnimal.toPath(), Paths.get(App.pathImages + nombreAnimal.toLowerCase() + ".png"), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException ex) {
                ex.printStackTrace();
                mostrarAlerta("Error", "No se pudo guardar la imagen.");
                return;
            }

            mostrarAlerta("Éxito", "¡El nuevo animal ha sido agregado exitosamente!");

            // Reiniciar el juego o volver al menú principal
            cerrarVentanaInicio();
        }
    public static BinaryTree<Object> crearArbol(int nivelActual, int maxPreguntas, ArrayList<String> listaPreguntas) {
        System.out.println("Creando nivel " + nivelActual + " de " + maxPreguntas);

        if (nivelActual >= maxPreguntas) {
            System.out.println("Nivel máximo alcanzado. Creando nodo hoja.");
            return new BinaryTree<>(new NodeBinaryTree<>(new ArrayList<Juego>()));
        } else {
            NodeBinaryTree<Object> nodo = new NodeBinaryTree<>(listaPreguntas.get(nivelActual));
            System.out.println("Pregunta en nivel " + nivelActual + ": " + listaPreguntas.get(nivelActual));

            nodo.setLeft(crearArbol(nivelActual + 1, maxPreguntas, listaPreguntas));
            nodo.setRight(crearArbol(nivelActual + 1, maxPreguntas, listaPreguntas));

            return new BinaryTree<>(nodo);
        }
    }

    public static BinaryTree<Object> crearArbolDePreguntas(int maxPreguntas, ArrayList<String> preguntas, Map<Juego, ArrayList<String>> respuestas) {
        System.out.println("Iniciando creación del árbol de preguntas.");
        System.out.println(preguntas);
        System.out.println("mac preguntas "+maxPreguntas);
        BinaryTree<Object> arbol = crearArbol(0, maxPreguntas, preguntas);
        System.out.println(preguntas);
        for (Map.Entry<Juego, ArrayList<String>> entry : respuestas.entrySet()) {
            Juego juego = entry.getKey();
            ArrayList<String> respuestasJuego = entry.getValue();
            NodeBinaryTree<Object> nodo = arbol.getRoot();

            System.out.println("Ubicando el Animal: " + juego.getNombre());

            for (int i = 0; i < maxPreguntas; i++) {
                System.out.println("Respuesta en nivel " + i + ": " + respuestasJuego.get(i));
                if (respuestasJuego.get(i).equals("si")) {
                    nodo = nodo.getLeft().getRoot();
                } else {
                    nodo = nodo.getRight().getRoot();
                }
            }

            System.out.println("Añadiendo juego al nodo hoja.");
            ((ArrayList<Juego>) nodo.getContent()).add(juego);
        }

        System.out.println("Finalizada la creación del árbol de preguntas.");
        return arbol;
    }


    private void construirArbolDeDecisiones() {
        actualizarImagen();
        arbolDeDecisiones = crearArbolDePreguntas(numeroDePreguntas, InicioController.preguntasAnimal, InicioController.respuestasAnimal);
    }

    private void mostrarPregunta() {
        System.out.println(nodoEnProceso.getContent());
        if(!esNodoFinal(nodoEnProceso)){
        etiquetaPregunta.setText((String) nodoEnProceso.getContent());}
        
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
        try (FileInputStream streamImagen = new FileInputStream(App.pathCaritas + nombreImagen)) {
            Image imagen = new Image(streamImagen);
            imagenPensando.setImage(imagen);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void manejarRespuesta(String respuesta) {
        contadorRespuestas++;
        actualizarImagen();
        respuestasUsuario += ";" + respuesta;
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
    // Cambiar el texto de la etiqueta para indicar que no se encontró un animal
    etiquetaPregunta.setText("No se encontró un animal con esas características. Puedes agregar un nuevo animal:");
    // Limpiar el panel principal para agregar los nuevos controles
    int numeroMaximoDePreguntas = InicioController.preguntasAnimal.size();
    panelPrincipal.getChildren().clear();
    if (numeroDePreguntas == numeroMaximoDePreguntas) {
        
        panelPrincipal.getChildren().clear();
        // Crear los controles de entrada dinámicamente
        Label labelNombre = new Label("Nombre del nuevo animal:");
        TextField campoNombre = new TextField();
        Button botonCargarImagen = new Button("Cargar Imagen");
        Label labelNombreArchivo = new Label();
        Button botonAgregarAnimal = new Button("Agregar Animal");

        // Configurar el botón para cargar la imagen
        botonCargarImagen.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Imágenes", "*.png", "*.jpg", "*.jpeg")
            );
            archivoImagenNuevoAnimal = fileChooser.showOpenDialog(panelPrincipal.getScene().getWindow());

            if (archivoImagenNuevoAnimal != null) {
                labelNombreArchivo.setText(archivoImagenNuevoAnimal.getName());
            }
            });

            botonAgregarAnimal.setOnAction(e -> {
                String nombreAnimal = campoNombre.getText().trim();

                if (nombreAnimal.isEmpty() || archivoImagenNuevoAnimal == null) {
                    mostrarAlerta("Faltan datos", "Por favor, ingrese el nombre y la imagen del nuevo animal.");
                    return;
                }

                // Construir la entrada de respuestas del nuevo animal
                StringBuilder entrada = new StringBuilder();
                entrada.append(nombreAnimal).append(",")
                       .append(nombreAnimal.toLowerCase()).append(".png")
                       .append(respuestasUsuario);

                // Añadir respuestas 'no' para completar el número máximo de preguntas
                while (entrada.toString().split(";").length - 1 < numeroDePreguntas) {
                    entrada.append(";no");
                }

                // Escribir la nueva entrada en el archivo
                ManejoArchivos.escribirEnArchivo("respuestasAnimal.txt", entrada.toString());

                // Guardar la imagen en el lugar adecuado
                try {
                    Files.copy(archivoImagenNuevoAnimal.toPath(), Paths.get(App.pathImages + nombreAnimal.toLowerCase() + ".png"), StandardCopyOption.REPLACE_EXISTING);
                } catch (IOException ex) {
                    ex.printStackTrace();
                    mostrarAlerta("Error", "No se pudo guardar la imagen.");
                    return;
                }

                mostrarAlerta("Éxito", "¡El nuevo animal ha sido agregado exitosamente!");

                // Reiniciar el juego o volver al menú principal
                cerrarVentanaInicio();
            });

            // Añadir los controles al panel principal
            panelPrincipal.getChildren().addAll(
                labelNombre,
                campoNombre,
                botonCargarImagen,
                labelNombreArchivo,
                botonAgregarAnimal
            );
        }
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
