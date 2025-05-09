/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package dao;

import modelo.Sede;

public interface ISedeDAO extends IGenericaDAO<Sede> {

    /**
     * Busca una sede por su ciudad
     *
     * @param ciudad La ciudad de la sede a buscar
     * @return La sede encontrada o null si no existe
     */
    Sede buscarPorCiudad(String ciudad);
}
