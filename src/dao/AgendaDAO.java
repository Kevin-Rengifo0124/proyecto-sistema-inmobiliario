/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package dao;

import excepciones.ConflictoHorarioException;
import excepciones.VisitasExcedidasException;
import modelo.Agenda;
import modelo.Cliente;
import modelo.Propiedad;
import util.implementacion.ListaEnlazada;

import java.util.Date;

public interface AgendaDAO {

    void guardar(Agenda agenda) throws ConflictoHorarioException, VisitasExcedidasException;

    void actualizar(Agenda agenda);

    void eliminar(String id);

    Agenda buscarPorId(String id);

    ListaEnlazada<Agenda> listarTodas();

    ListaEnlazada<Agenda> listarPorPropiedad(Propiedad propiedad);

    ListaEnlazada<Agenda> listarPorCliente(Cliente cliente);

    ListaEnlazada<Agenda> listarPorFecha(Date fecha);

    void marcarCompletada(Agenda agenda);

    void cancelar(Agenda agenda);
}
