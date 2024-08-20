/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;



import java.util.ArrayList;

public class ListaCircularDoble {

    private Nodo cabeza;
    private Nodo actual;

    private class Nodo {
        Juego juego;
        Nodo siguiente;
        Nodo anterior;

        public Nodo(Juego juego) {
            this.juego = juego;
        }
    }

    public ListaCircularDoble(ArrayList<Juego> juegos) {
        if (juegos == null || juegos.isEmpty()) {
            throw new IllegalArgumentException("La lista de juegos no puede estar vacía.");
        }

        for (Juego juego : juegos) {
            agregarJuego(juego);
        }

        // Hacer la lista circular
        cabeza.anterior = actual;
        actual.siguiente = cabeza;

        // Establecer el nodo actual en la cabeza
        actual = cabeza;
    }

    private void agregarJuego(Juego juego) {
        Nodo nuevoNodo = new Nodo(juego);
        if (cabeza == null) {
            cabeza = nuevoNodo;
            actual = nuevoNodo;
        } else {
            Nodo ultimo = actual;
            ultimo.siguiente = nuevoNodo;
            nuevoNodo.anterior = ultimo;
            actual = nuevoNodo;
        }
    }

    public Juego getActual() {
        return actual.juego;
    }

    public void setActual(Juego juego) {
        Nodo temp = cabeza;
        do {
            if (temp.juego.equals(juego)) {
                actual = temp;
                break;
            }
            temp = temp.siguiente;
        } while (temp != cabeza);
    }

    public Juego getSiguiente() {
        actual = actual.siguiente;
        return actual.juego;
    }

    public Juego getAnterior() {
        actual = actual.anterior;
        return actual.juego;
    }
}

