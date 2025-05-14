/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package dao;

import excepciones.CapacidadExcedidaException;
import excepciones.EmpleadoIncompatibleException;
import excepciones.UsuarioExistenteException;
import excepciones.UsuarioNoEncontradoException;
import modelo.Empleado;
import modelo.Propiedad;
import modelo.Sede;
import util.implementacion.ListaEnlazada;

public interface EmpleadoDAO {

    void guardar(Empleado empleado) throws UsuarioExistenteException;

    void actualizar(Empleado empleado) throws UsuarioNoEncontradoException;

    void eliminar(String email) throws UsuarioNoEncontradoException;

    Empleado buscarPorEmail(String email) throws UsuarioNoEncontradoException;

    Empleado buscarPorCedula(String cedula) throws UsuarioNoEncontradoException;

    ListaEnlazada<Empleado> listarTodos();

    ListaEnlazada<Empleado> listarPorSede(Sede sede);

    void asignarSede(Empleado empleado, Sede sede);

    void asignarPropiedad(Empleado empleado, Propiedad propiedad) throws EmpleadoIncompatibleException, CapacidadExcedidaException;

    ListaEnlazada<Propiedad> listarPropiedadesAsignadas(String emailEmpleado) throws UsuarioNoEncontradoException;
}
