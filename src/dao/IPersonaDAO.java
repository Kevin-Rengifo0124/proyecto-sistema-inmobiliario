/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package dao;

import modelo.Persona;
import util.ListaEnlazada;

public interface IPersonaDAO extends IGenericaDAO<Persona> {

    /**
     * Busca una persona por su cédula
     *
     * @param cedula La cédula de la persona a buscar
     * @return La persona encontrada o null si no existe
     */
    Persona buscarPorCedula(String cedula);

    /**
     * Busca personas por su rol
     *
     * @param rol El rol de las personas a buscar
     * @return Una lista enlazada con las personas que tienen ese rol
     */
    ListaEnlazada<Persona> listarPorRol(String rol);

}
