/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package excepciones;

public class ConflictoHorarioException extends InmobiliariaException {

    /**
     * Constructor por defecto
     */
    public ConflictoHorarioException() {
        super("Existe un conflicto de horario con otra visita programada para esta propiedad");
    }
}
