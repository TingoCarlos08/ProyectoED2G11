/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.proyecto2ed;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class VentanaEsperaController implements Initializable {

    @FXML
    private Label secuencia;

    @FXML
    private Label lblAnimalObjeto;
    @FXML
    private ImageView imgAnimalCosa;
    @FXML
    private Label LBL1;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        lblAnimalObjeto.setText("Animal");  // Asumiendo que solo hay un modo de juego que es "animal"
        iniciarCuentaRegresiva();
    }

    private void iniciarCuentaRegresiva() {
        Thread hilo = new Thread(() -> {
            for (int i = 5; i >= 1; i--) {
                final int count = i;
                Platform.runLater(() -> secuencia.setText(String.valueOf(count)));
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            cerrarVentanaYContinuar();
        });
        hilo.start();
    }

    private void cerrarVentanaYContinuar() {
        Platform.runLater(() -> {
            Stage stage = (Stage) secuencia.getScene().getWindow();
            stage.close();
            try {
                App.abrirNuevaVentana("paginaPrincipalJuego", 806, 439);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
    }
}
