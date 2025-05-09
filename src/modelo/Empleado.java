/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import java.io.Serializable;
import util.ListaEnlazada;

public class Empleado extends Persona implements Serializable {

    private Sede sede;
    private boolean puedeVender;
    private boolean puedeArrendar;
    private ListaEnlazada<Propiedad> propiedadesAsignadas;

    public Empleado(String nombreCompleto, String cedula, String telefono, String direccion,
            String email, String clave, Sede sede, boolean puedeVender, boolean puedeArrendar) {
        super(nombreCompleto, cedula, telefono, direccion, email, clave, Usuario.EMPLEADO);
        this.sede = sede;
        this.puedeVender = puedeVender;
        this.puedeArrendar = puedeArrendar;
        this.propiedadesAsignadas = new ListaEnlazada<>();
    }

    public Empleado() {
        super("", "", "", "", "", "", Usuario.EMPLEADO);
        this.propiedadesAsignadas = new ListaEnlazada<>();
    }

    public Sede getSede() {
        return sede;
    }

    public void setSede(Sede sede) {
        this.sede = sede;
    }

    public boolean isPuedeVender() {
        return puedeVender;
    }

    public void setPuedeVender(boolean puedeVender) {
        this.puedeVender = puedeVender;
    }

    public boolean isPuedeArrendar() {
        return puedeArrendar;
    }

    public void setPuedeArrendar(boolean puedeArrendar) {
        this.puedeArrendar = puedeArrendar;
    }

    public ListaEnlazada<Propiedad> getPropiedadesAsignadas() {
        return propiedadesAsignadas;
    }

    public void setPropiedadesAsignadas(ListaEnlazada<Propiedad> propiedadesAsignadas) {
        this.propiedadesAsignadas = propiedadesAsignadas;
    }

    public void asignarPropiedad(Propiedad propiedad) {
        propiedadesAsignadas.add(propiedad);
    }

    public boolean validarTipoPropiedad(Propiedad propiedad) {
        if (propiedad.getTipo().equals(Propiedad.TIPO_VENTA) && !puedeVender) {
            return false;
        }
        if (propiedad.getTipo().equals(Propiedad.TIPO_ARRIENDO) && !puedeArrendar) {
            return false;
        }
        return true;
    }
}
