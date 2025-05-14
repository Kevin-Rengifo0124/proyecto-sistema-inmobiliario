/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package dao;

import excepciones.UsuarioExistenteException;
import excepciones.UsuarioNoEncontradoException;
import modelo.Cliente;
import modelo.Propiedad;
import util.implementacion.ListaEnlazada;

public interface ClienteDAO {

    void guardar(Cliente cliente) throws UsuarioExistenteException;

    void actualizar(Cliente cliente) throws UsuarioNoEncontradoException;

    void eliminar(String email) throws UsuarioNoEncontradoException;

    Cliente buscarPorEmail(String email) throws UsuarioNoEncontradoException;

    Cliente buscarPorCedula(String cedula) throws UsuarioNoEncontradoException;

    ListaEnlazada<Cliente> listarTodos();

    ListaEnlazada<Propiedad> listarPropiedades(String emailCliente) throws UsuarioNoEncontradoException;

    void agregarPropiedad(Cliente cliente, Propiedad propiedad);
}
