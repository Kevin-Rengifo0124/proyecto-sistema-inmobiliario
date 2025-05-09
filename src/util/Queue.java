package util;

import java.io.Serializable;

public class Queue<T> implements IQueue<T>, Serializable {

    Nodo<T> primero;

    public Queue() {
        this.primero = null;
    }

    @Override
    public void enQueue(T dato) {
        Nodo<T> nuevo = new Nodo(dato);
        if (isEmpty()) {
            primero = nuevo;
        } else {
            Nodo<T> observador = primero;
            while (observador.nodoSiguiente != null) {
                observador = observador.nodoSiguiente;
            }
            observador.nodoSiguiente = nuevo;
        }
    }

    @Override
    public T deQueue() {
        if (!isEmpty()) {
            T retorno = primero.dato;
            primero = primero.nodoSiguiente;
            return retorno;
        }
        throw new RuntimeException("La cola está vacía");
    }

    @Override
    public T peek() {
        if (!isEmpty()) {
            return primero.dato;
        }
        throw new RuntimeException("La cola está vacía");
    }

    @Override
    public boolean isEmpty() {
        return primero == null;
    }

}
