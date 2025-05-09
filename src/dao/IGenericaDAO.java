/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package dao;

import util.ListaEnlazada;

public interface IGenericaDAO<T> {

    /**
     * Guarda un objeto en el almacenamiento de datos
     *
     * @param objeto El objeto a guardar
     * @return true si se guardó exitosamente, false si no
     */
    boolean guardar(T objeto);

    /**
     * Actualiza un objeto en el almacenamiento de datos
     *
     * @param objeto El objeto a actualizar
     * @return true si se actualizó exitosamente, false si no
     */
    boolean actualizar(T objeto);

    /**
     * Elimina un objeto del almacenamiento de datos
     *
     * @param objeto El objeto a eliminar
     * @return true si se eliminó exitosamente, false si no
     */
    boolean eliminar(T objeto);

    /**
     * Busca un objeto por su ID en el almacenamiento de datos
     *
     * @param id El ID del objeto a buscar
     * @return El objeto encontrado o null si no existe
     */
    T buscarPorId(String id);

    /**
     * Obtiene todos los objetos del almacenamiento de datos
     *
     * @return Una lista enlazada con todos los objetos
     */
    ListaEnlazada<T> listarTodos();
}
