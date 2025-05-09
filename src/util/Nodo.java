/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util;

import java.io.Serializable;

public class Nodo<S> implements Serializable {

    S dato;
    Nodo<S> nodoSiguiente;

    public Nodo(S dato) {
        this.dato = dato;
        this.nodoSiguiente = null;
    }
}
