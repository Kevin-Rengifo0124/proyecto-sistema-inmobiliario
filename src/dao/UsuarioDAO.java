/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package dao;

import excepciones.CredencialesInvalidasException;
import excepciones.UsuarioExistenteException;
import excepciones.UsuarioNoEncontradoException;
import modelo.Usuario;
import util.implementacion.ListaEnlazada;

public interface UsuarioDAO {

    Usuario autenticar(String email, String clave) throws CredencialesInvalidasException;

    void guardar(Usuario usuario) throws UsuarioExistenteException;

    void actualizar(Usuario usuario) throws UsuarioNoEncontradoException;

    void eliminar(String email) throws UsuarioNoEncontradoException;

    Usuario buscarPorEmail(String email) throws UsuarioNoEncontradoException;

    Usuario buscarPorCedula(String cedula) throws UsuarioNoEncontradoException;

    ListaEnlazada<Usuario> listarTodos();

    boolean existeUsuario(String email);
}
