/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package excepciones;

public class UsuarioExistenteException extends InmobiliariaException {

    /**
     * Constructor con el email del usuario existente
     *
     * @param email Email del usuario
     */
    public UsuarioExistenteException(String email) {
        super("Ya existe un usuario registrado con el email: " + email);
    }

    /**
     * Constructor con la cédula del usuario existente
     *
     * @param cedula Cédula del usuario
     * @param esEmail Indica si el identificador es un email (para diferenciar
     * constructores)
     */
    public UsuarioExistenteException(String cedula, boolean esEmail) {
        super("Ya existe un usuario registrado con la cédula: " + cedula);
    }
}
