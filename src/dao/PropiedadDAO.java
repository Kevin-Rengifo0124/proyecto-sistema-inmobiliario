/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package dao;

import excepciones.EmpleadoIncompatibleException;
import excepciones.PropiedadNoDisponibleException;
import excepciones.UsuarioNoEncontradoException;
import modelo.Empleado;
import modelo.Propiedad;
import modelo.Sede;
import util.implementacion.ListaEnlazada;

public interface PropiedadDAO {

    void guardar(Propiedad propiedad);

    void actualizar(Propiedad propiedad) throws PropiedadNoDisponibleException;

    void eliminar(String id);

    Propiedad buscarPorId(String id);

    ListaEnlazada<Propiedad> listarTodas();

    ListaEnlazada<Propiedad> listarPorEstado(String estado);

    ListaEnlazada<Propiedad> listarPorTipo(String tipo);

    ListaEnlazada<Propiedad> listarPorCiudad(String ciudad);

    ListaEnlazada<Propiedad> listarPorRangoPrecio(double precioMin, double precioMax);

    ListaEnlazada<Propiedad> listarPorEmpleado(String emailEmpleado) throws UsuarioNoEncontradoException;

    ListaEnlazada<Propiedad> listarPorPropietario(String emailCliente) throws UsuarioNoEncontradoException;

    void asignarEmpleado(Propiedad propiedad, Empleado empleado) throws EmpleadoIncompatibleException;

    void asignarSede(Propiedad propiedad, Sede sede);

    void cambiarEstado(Propiedad propiedad, String nuevoEstado);
}
