/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util.implementacion;

import java.io.Serializable;
import util.IListaEnlazada;

public class ListaEnlazada<T> implements IListaEnlazada<T>,Serializable {

    Nodo<T> primero;
    int size;

    public ListaEnlazada() {
        this.size = 0;
        this.primero = null;
    }

    @Override
    public void add(T dato) {
        Nodo<T> nuevo = new Nodo<>(dato);
        if (isEmpty()) {
            this.primero = nuevo;
        } else {
            Nodo<T> aux = primero;
            while (aux.nodoSiguiente != null) {
                aux = aux.nodoSiguiente;
            }
            aux.nodoSiguiente = nuevo;
        }
        size++;
    }

    @Override
    public T get(int index) {
        if (index < 0 || index >= size) {
            throw new ArrayIndexOutOfBoundsException(index);
        } else {
            if (index == 0) {
                return primero.dato;
            } else {
                Nodo<T> observador = primero;
                int contador = 0;
                while (contador < index) {
                    contador++;
                    observador = observador.nodoSiguiente;
                }
                return observador.dato;
            }
        }

    }

    @Override
    public void add(T dato, int index) {

        if (index < 0 || index > size) {
            throw new ArrayIndexOutOfBoundsException(index);
        } else {
            if (index == 0) {
                Nodo<T> nuevo = new Nodo<>(dato);
                Nodo<T> aux = primero;
                primero = nuevo;
                nuevo.nodoSiguiente = aux;

            } else {
                Nodo<T> observador = primero;
                int contador = 0;
                while (contador < index - 1) {
                    observador = observador.nodoSiguiente;
                    contador++;
                }
                Nodo<T> nuevo = new Nodo<>(dato);
                Nodo<T> aux = observador.nodoSiguiente;
                observador.nodoSiguiente = nuevo;
                nuevo.nodoSiguiente = aux;

            }
        }
        size++;

    }

    @Override
    public void remove(int index) {
        if (index < 0 || index > size) {
            throw new ArrayIndexOutOfBoundsException(index);
        }
        if (index == 0) {
            primero = primero.nodoSiguiente;
        } else {
            Nodo<T> observador = primero;
            int contador = 0;
            while (contador < index - 1) {
                observador = observador.nodoSiguiente;
                contador++;
            }
            Nodo<T> aux = observador.nodoSiguiente.nodoSiguiente;
            observador.nodoSiguiente = aux;
        }
        size--;

    }

    @Override
    public void remove(T dato) {
        if (primero.dato == dato) {
            primero = primero.nodoSiguiente;
        } else {
            Nodo<T> observador = primero;
            while (observador.nodoSiguiente != null) {
                if (observador.nodoSiguiente.dato == dato) {
                    observador.nodoSiguiente = observador.nodoSiguiente.nodoSiguiente;
                    break;
                }
                observador = observador.nodoSiguiente;
            }
        }
        size--;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return primero == null;
    }

    @Override
    public int indexOf(T dato) {
        for (int i = 0; i < size(); i++) {
            if (dato.equals(get(i))) {
                return i;
            }
        }
        throw new ArrayIndexOutOfBoundsException("El dato no se encuentra en la lista.");
    }
}
