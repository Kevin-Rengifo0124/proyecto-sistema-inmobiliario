/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import java.io.Serializable;

public class Administrador extends Persona implements Serializable {

    public Administrador(String nombreCompleto, String cedula, String telefono, String direccion, String email, String clave, String rol) {
        super(nombreCompleto, cedula, telefono, direccion, email, clave, rol);
    }
    
    public Administrador() {
        super("", "", "", "", "", "", Usuario.ADMINISTRATIVO);
    }

}
