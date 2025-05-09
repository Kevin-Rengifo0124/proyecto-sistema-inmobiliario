/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package dao;

import modelo.Cliente;
import modelo.Empleado;
import modelo.Propiedad;
import modelo.Sede;
import util.ListaEnlazada;

public interface IPropiedadDAO extends IGenericaDAO<Propiedad> {

    /**
     * Busca propiedades por su tipo (venta/arriendo)
     *
     * @param tipo El tipo de las propiedades a buscar
     * @return Una lista enlazada con las propiedades de ese tipo
     */
    ListaEnlazada<Propiedad> buscarPorTipo(String tipo);

    /**
     * Busca propiedades por su estado
     *
     * @param estado El estado de las propiedades a buscar
     * @return Una lista enlazada con las propiedades en ese estado
     */
    ListaEnlazada<Propiedad> buscarPorEstado(String estado);

    /**
     * Busca propiedades por ciudad
     *
     * @param ciudad La ciudad de las propiedades a buscar
     * @return Una lista enlazada con las propiedades en esa ciudad
     */
    ListaEnlazada<Propiedad> buscarPorCiudad(String ciudad);

    /**
     * Busca propiedades por sede
     *
     * @param sede La sede de las propiedades a buscar
     * @return Una lista enlazada con las propiedades de esa sede
     */
    ListaEnlazada<Propiedad> buscarPorSede(Sede sede);

    /**
     * Busca propiedades por empleado encargado
     *
     * @param empleado El empleado encargado de las propiedades a buscar
     * @return Una lista enlazada con las propiedades a cargo de ese empleado
     */
    ListaEnlazada<Propiedad> buscarPorEncargado(Empleado empleado);

    /**
     * Busca propiedades por propietario
     *
     * @param propietario El propietario de las propiedades a buscar
     * @return Una lista enlazada con las propiedades de ese propietario
     */
    ListaEnlazada<Propiedad> buscarPorPropietario(Cliente propietario);

    /**
     * Busca propiedades por rango de precio
     *
     * @param minimo El precio mínimo
     * @param maximo El precio máximo
     * @return Una lista enlazada con las propiedades en ese rango de precio
     */
    ListaEnlazada<Propiedad> buscarPorRangoPrecio(double minimo, double maximo);

    /**
     * Busca propiedades que permiten agendar visitas
     *
     * @return Una lista enlazada con las propiedades que permiten agendar
     * visitas
     */
    ListaEnlazada<Propiedad> buscarConAgendamiento();
}
