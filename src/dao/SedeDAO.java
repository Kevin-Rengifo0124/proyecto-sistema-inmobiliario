/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package dao;

import excepciones.SedeExistenteException;
import modelo.Empleado;
import modelo.Propiedad;
import modelo.Sede;
import util.implementacion.ListaEnlazada;

public interface SedeDAO {

    void guardar(Sede sede) throws SedeExistenteException;

    void actualizar(Sede sede);

    void eliminar(String ciudad);

    Sede buscarPorCiudad(String ciudad);

    ListaEnlazada<Sede> listarTodas();

    void asignarEmpleado(Sede sede, Empleado empleado);

    void asignarPropiedad(Sede sede, Propiedad propiedad);

    ListaEnlazada<Empleado> listarEmpleadosPorSede(String ciudad);

    ListaEnlazada<Propiedad> listarPropiedadesPorSede(String ciudad);

    boolean existeSede(String ciudad);
}
