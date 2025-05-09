/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package excepciones;

public class PropiedadNoDisponibleException extends InmobiliariaException {

    /**
     * Constructor por defecto
     */
    public PropiedadNoDisponibleException() {
        super("La propiedad no está disponible");
    }

    /**
     * Constructor con motivo específico
     *
     * @param motivo Motivo de no disponibilidad
     */
    public PropiedadNoDisponibleException(String motivo) {
        super("La propiedad no está disponible: " + motivo);
    }
}
