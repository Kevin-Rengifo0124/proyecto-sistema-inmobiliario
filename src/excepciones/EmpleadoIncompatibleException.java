/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package excepciones;

public class EmpleadoIncompatibleException extends InmobiliariaException {

    /**
     * Constructor con el tipo de propiedad incompatible
     *
     * @param tipo Tipo de propiedad (venta o arriendo)
     */
    public EmpleadoIncompatibleException(String tipo) {
        super("El empleado no está autorizado para gestionar propiedades de tipo: " + tipo);
    }
}
