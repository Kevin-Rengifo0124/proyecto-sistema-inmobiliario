/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package excepciones;

public class VisitasExcedidasException extends InmobiliariaException {

    /**
     * Constructor por defecto
     */
    public VisitasExcedidasException() {
        super("Ha alcanzado el límite máximo de visitas agendadas (2)");
    }
}
