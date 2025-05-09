/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package excepciones;

public class CapacidadExcedidaException extends InmobiliariaException {

    /**
     * Constructor con la ciudad de la sede
     *
     * @param ciudad Ciudad de la sede
     */
    public CapacidadExcedidaException(String ciudad) {
        super("Se ha alcanzado la capacidad máxima de inmuebles para la sede en: " + ciudad);
    }
}
