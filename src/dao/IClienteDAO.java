/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package dao;

import modelo.Cliente;
import util.ListaEnlazada;

public interface IClienteDAO extends IGenericaDAO<Cliente> {

    /**
     * Busca clientes por ciudad de residencia
     *
     * @param ciudad La ciudad de residencia
     * @return Una lista enlazada con los clientes de esa ciudad
     */
    ListaEnlazada<Cliente> buscarPorCiudad(String ciudad);
}
