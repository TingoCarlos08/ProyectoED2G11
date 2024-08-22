/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyecto2ed;

/**
 *
 * @author nicol
 */
public class SessionManager {
    private static SessionManager instance;
    private int numeroDePreguntas;

    // Constructor privado para evitar instanciación externa
    private SessionManager() {
        // Inicializa con valor por defecto si es necesario
        this.numeroDePreguntas = 20;
    }

    // Método para obtener la instancia única
    public static synchronized SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    // Método para obtener el valor de numeroDePreguntas
    public int getNumeroDePreguntas() {
        return numeroDePreguntas;
    }

    // Método para establecer el valor de numeroDePreguntas
    public void setNumeroDePreguntas(int numeroDePreguntas) {
        this.numeroDePreguntas = numeroDePreguntas;
    }

    public void cerrarSesion() {
        this.numeroDePreguntas = 20;
    }
}

