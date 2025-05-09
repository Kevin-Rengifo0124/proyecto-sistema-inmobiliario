/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package excepciones;

public class InmobiliariaException extends Exception {

    /**
     * Constructor con mensaje de error
     *
     * @param mensaje Mensaje descriptivo del error
     */
    public InmobiliariaException(String mensaje) {
        super(mensaje);
    }

    /**
     * Constructor con mensaje y causa de error
     *
     * @param mensaje Mensaje descriptivo del error
     * @param causa Causa original del error
     */
    public InmobiliariaException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
