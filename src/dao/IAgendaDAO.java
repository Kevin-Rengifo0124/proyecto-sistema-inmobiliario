/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package dao;

import java.util.Date;
import modelo.Agenda;
import modelo.Cliente;
import modelo.Propiedad;
import util.ListaEnlazada;

public interface IAgendaDAO extends IGenericaDAO<Agenda> {

    /**
     * Busca agendas por propiedad
     *
     * @param propiedad La propiedad de las agendas a buscar
     * @return Una lista enlazada con las agendas para esa propiedad
     */
    ListaEnlazada<Agenda> buscarPorPropiedad(Propiedad propiedad);

    /**
     * Busca agendas por cliente
     *
     * @param cliente El cliente de las agendas a buscar
     * @return Una lista enlazada con las agendas de ese cliente
     */
    ListaEnlazada<Agenda> buscarPorCliente(Cliente cliente);

    /**
     * Busca agendas por fecha
     *
     * @param fecha La fecha de las agendas a buscar
     * @return Una lista enlazada con las agendas para esa fecha
     */
    ListaEnlazada<Agenda> buscarPorFecha(Date fecha);

    /**
     * Busca agendas activas (no completadas ni canceladas)
     *
     * @return Una lista enlazada con las agendas activas
     */
    ListaEnlazada<Agenda> buscarAgendaActivas();

}
