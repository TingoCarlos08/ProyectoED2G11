/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;



import java.util.ArrayList;

public class ListaCircularDoble <E> {

    private Nodo<E> cabeza;
    private Nodo<E> actual;

    private class Nodo<E> {
        E juego;
        Nodo<E> siguiente;
        Nodo<E> anterior;

        public Nodo(E juego) {
            this.juego = juego;
        }

        public E getJuego() {
            return juego;
        }

        public void setJuego(E juego) {
            this.juego = juego;
        }

        public Nodo<E> getSiguiente() {
            return siguiente;
        }

        public void setSiguiente(Nodo<E> siguiente) {
            this.siguiente = siguiente;
        }

        public Nodo<E> getAnterior() {
            return anterior;
        }

        public void setAnterior(Nodo<E> anterior) {
            this.anterior = anterior;
        }
        
    }

    public ListaCircularDoble(ArrayList<E> juegos) {
        if (juegos == null || juegos.isEmpty()) {
            throw new IllegalArgumentException("La lista de juegos no puede estar vacía.");
        }

        for (E juego : juegos) {
            agregarJuego(juego);
        }

        // Hacer la lista circular
        cabeza.setAnterior(actual);
        actual.setSiguiente(cabeza);

        // Establecer el nodo actual en la cabeza
        actual = cabeza;
    }

    private void agregarJuego(E juego) {
        Nodo<E> nuevoNodo = new Nodo<>(juego);
        if (cabeza == null) {
            cabeza = nuevoNodo;
            actual = nuevoNodo;
        } else {
            Nodo<E> ultimo = actual;
            ultimo.siguiente = nuevoNodo;
            nuevoNodo.anterior = ultimo;
            actual = nuevoNodo;
        }
    }

    public E getActual() {
        return actual.juego;
    }

    public void setActual(E juego) {
        Nodo<E> temp = cabeza;
        do {
            if (temp.juego.equals(juego)) {
                actual = temp;
                break;
            }
            temp = temp.siguiente;
        } while (temp != cabeza);
    }

    public E getSiguiente() {
        actual = actual.siguiente;
        return actual.juego;
    }

    public E getAnterior() {
        actual = actual.anterior;
        return actual.juego;
    }
}

