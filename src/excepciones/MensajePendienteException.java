/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package excepciones;

public class MensajePendienteException extends InmobiliariaException {

    /**
     * Constructor por defecto
     */
    public MensajePendienteException() {
        super("Ya tiene un mensaje pendiente de respuesta para esta propiedad");
    }
}
