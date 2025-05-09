/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package excepciones;

public class CredencialesInvalidasException extends InmobiliariaException {

    /**
     * Constructor por defecto
     */
    public CredencialesInvalidasException() {
        super("Credenciales inválidas. Verifique su email y contraseña");
    }
}
