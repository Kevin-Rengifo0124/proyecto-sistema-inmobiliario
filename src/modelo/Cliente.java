/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import java.io.Serializable;
import util.ListaEnlazada;

public class Cliente extends Persona implements Serializable {

    private String ciudadResidencia;
    private ListaEnlazada<Propiedad> propiedades;
    private ListaEnlazada<Agenda> visitasAgendadas;

    public Cliente(String nombreCompleto, String cedula, String telefono, String direccion,
            String email, String clave, String ciudadResidencia) {
        super(nombreCompleto, cedula, telefono, direccion, email, clave, Usuario.CLIENTE);
        this.ciudadResidencia = ciudadResidencia;
        this.propiedades = new ListaEnlazada<>();
        this.visitasAgendadas = new ListaEnlazada<>();
    }

    public Cliente() {
        super("", "", "", "", "", "", Usuario.CLIENTE);
        this.propiedades = new ListaEnlazada<>();
        this.visitasAgendadas = new ListaEnlazada<>();
    }

    public String getCiudadResidencia() {
        return ciudadResidencia;
    }

    public void setCiudadResidencia(String ciudadResidencia) {
        this.ciudadResidencia = ciudadResidencia;
    }

    public ListaEnlazada<Propiedad> getPropiedades() {
        return propiedades;
    }

    public void setPropiedades(ListaEnlazada<Propiedad> propiedades) {
        this.propiedades = propiedades;
    }

    public ListaEnlazada<Agenda> getVisitasAgendadas() {
        return visitasAgendadas;
    }

    public void setVisitasAgendadas(ListaEnlazada<Agenda> visitasAgendadas) {
        this.visitasAgendadas = visitasAgendadas;
    }

    public void agregarPropiedad(Propiedad propiedad) {
        propiedades.add(propiedad);
    }

    public void agregarVisita(Agenda visita) {
        visitasAgendadas.add(visita);
    }

    public boolean puedeAgendarMasVisitas() {
        // Máximo 2 visitas agendadas activas
        int visitasActivas = 0;
        for (int i = 0; i < visitasAgendadas.size(); i++) {
            if (!visitasAgendadas.get(i).isCompletada()) {
                visitasActivas++;
            }
        }
        return visitasActivas < 2;
    }
}
