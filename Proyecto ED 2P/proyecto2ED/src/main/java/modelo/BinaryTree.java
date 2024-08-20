/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 *
 * @author Carlos Tingo
 */
public class BinaryTree<E> {

    private NodeBinaryTree<E> root;

    public BinaryTree() {
        this.root = null;
    }

    public BinaryTree(E e) {
        this.root = new NodeBinaryTree<>(e);
    }

    public BinaryTree(NodeBinaryTree<E> root) {
        this.root = root;
    }

    public boolean isEmpty() {
        return this.root == null;
    }

    public boolean isLeaf() {
        if (!this.isEmpty()) {
            return this.root.getLeft() == null && this.root.getRight() == null;

        }
        return false;
    }

    public void recorrerEnPreOrden() {
        if (!this.isEmpty()) {
            //1.imprimir a la raiz
            System.out.println(this.root.getContent());
            //2. recorrerEnPreorden el hijo izquierdo
            if (this.root.getLeft() != null) {
                this.root.getLeft().recorrerEnPreOrden();
            }
            //3.. recorrerEnPreorden el hijo izquierdo
            if (this.root.getRight() != null) {
                this.root.getRight().recorrerEnPreOrden();
            }
        }
    }

    public void recorrerEnPostOrden() {
        if (!this.isEmpty()) {
            //1. recorrerEnPreorden el hijo izquierdo
            if (this.root.getLeft() != null) {
                this.root.getLeft().recorrerEnPreOrden();
            }
            //2. recorrerEnPreorden el hijo izquierdo
            if (this.root.getRight() != null) {
                this.root.getRight().recorrerEnPreOrden();
            }
            //3.imprimir a la raiz
            System.out.println(this.root.getContent());
        }
    }

    public void recorrerEnOrden() {
        if (!this.isEmpty()) {
            //1. recorrerEnPreorden el hijo izquierdo
            if (this.root.getLeft() != null) {
                this.root.getLeft().recorrerEnPreOrden();
            }
            //2.imprimir a la raiz
            System.out.println(this.root.getContent());
            //3. recorrerEnPreorden el hijo derecho
            if (this.root.getRight() != null) {
                this.root.getRight().recorrerEnPreOrden();
            }
        }
    }

    public NodeBinaryTree<E> getRoot() {
        return root;
    }

    public void setRoot(NodeBinaryTree<E> root) {
        this.root = root;
    }

}
