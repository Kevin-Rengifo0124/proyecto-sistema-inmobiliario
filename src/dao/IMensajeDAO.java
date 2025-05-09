/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package dao;

import modelo.Mensaje;
import modelo.Propiedad;
import util.ListaEnlazada;

public interface IMensajeDAO extends IGenericaDAO<Mensaje> {

    /**
     * Busca mensajes por propiedad
     *
     * @param propiedad La propiedad de los mensajes a buscar
     * @return Una lista enlazada con los mensajes para esa propiedad
     */
    ListaEnlazada<Mensaje> buscarPorPropiedad(Propiedad propiedad);

    /**
     * Busca mensajes por remitente
     *
     * @param remitente El remitente de los mensajes a buscar
     * @return Una lista enlazada con los mensajes de ese remitente
     */
    ListaEnlazada<Mensaje> buscarPorRemitente(String remitente);

    /**
     * Busca mensajes por destinatario
     *
     * @param destinatario El destinatario de los mensajes a buscar
     * @return Una lista enlazada con los mensajes para ese destinatario
     */
    ListaEnlazada<Mensaje> buscarPorDestinatario(String destinatario);

    /**
     * Busca mensajes no respondidos
     *
     * @return Una lista enlazada con los mensajes no respondidos
     */
    ListaEnlazada<Mensaje> buscarNoRespondidos();
}
