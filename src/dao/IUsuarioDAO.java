/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package dao;

import modelo.Usuario;


public interface IUsuarioDAO extends IGenericaDAO<Usuario> {

    /**
     * Busca un usuario por su email
     *
     * @param email El email del usuario a buscar
     * @return El usuario encontrado o null si no existe
     */
    Usuario buscarPorEmail(String email);

    /**
     * Verifica las credenciales de un usuario
     *
     * @param email El email del usuario
     * @param clave La clave del usuario
     * @return El usuario si las credenciales son correctas, null si no
     */
    Usuario validarCredenciales(String email, String clave);

}
