/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package dao;

import excepciones.MensajePendienteException;
import modelo.Cliente;
import modelo.Mensaje;
import modelo.Propiedad;
import util.implementacion.ListaEnlazada;

public interface MensajeDAO {

    void guardar(Mensaje mensaje) throws MensajePendienteException;

    void actualizar(Mensaje mensaje);

    void eliminar(String id);

    Mensaje buscarPorId(String id);

    ListaEnlazada<Mensaje> listarTodos();

    ListaEnlazada<Mensaje> listarPorPropiedad(Propiedad propiedad);

    ListaEnlazada<Mensaje> listarPorRemitente(String emailRemitente);

    ListaEnlazada<Mensaje> listarPorDestinatario(String emailDestinatario);

    void marcarRespondido(Mensaje mensaje);

    boolean tieneMensajePendiente(Cliente cliente, Propiedad propiedad);
}
