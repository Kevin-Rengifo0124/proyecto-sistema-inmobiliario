/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package excepciones;

public class SedeExistenteException extends InmobiliariaException {

    /**
     * Constructor con la ciudad de la sede existente
     *
     * @param ciudad Ciudad de la sede
     */
    public SedeExistenteException(String ciudad) {
        super("Ya existe una sede registrada en la ciudad: " + ciudad);
    }
}
