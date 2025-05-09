/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package util;

public interface IListaEnlazada<T> {

    public void add(T dato);

    public T get(int index);

    public void add(T dato, int index);

    public void remove(int index);

    public void remove(T dato);

    public int size();

    public boolean isEmpty();

    public int indexOf(T dato);
}
