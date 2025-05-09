/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package excepciones;

public class UsuarioNoEncontradoException extends InmobiliariaException {

    /**
     * Constructor con identificador del usuario no encontrado
     *
     * @param identificador Email o cédula del usuario
     */
    public UsuarioNoEncontradoException(String identificador) {
        super("No se encontró ningún usuario con identificador: " + identificador);
    }
}
