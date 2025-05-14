/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import java.io.Serializable;
import java.util.Date;

public class Agenda implements Serializable {

    private String id;
    private Propiedad propiedad;
    private Cliente cliente;
    private Date fecha;
    private int horaInicio; 
    private int duracion; 
    private boolean completada;
    private boolean cancelada;

    public Agenda(String id, Propiedad propiedad, Cliente cliente, Date fecha, int horaInicio, int duracion) {
        this.id = id;
        this.propiedad = propiedad;
        this.cliente = cliente;
        this.fecha = fecha;
        this.horaInicio = horaInicio;
        this.duracion = duracion;
        this.completada = false;
        this.cancelada = false;
    }

    // Constructor vacío para serialización
    public Agenda() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Propiedad getPropiedad() {
        return propiedad;
    }

    public void setPropiedad(Propiedad propiedad) {
        this.propiedad = propiedad;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public int getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(int horaInicio) {
        this.horaInicio = horaInicio;
    }

    public int getDuracion() {
        return duracion;
    }

    public void setDuracion(int duracion) {
        this.duracion = duracion;
    }

    public boolean isCompletada() {
        return completada;
    }

    public void setCompletada(boolean completada) {
        this.completada = completada;
    }

    public boolean isCancelada() {
        return cancelada;
    }

    public void setCancelada(boolean cancelada) {
        this.cancelada = cancelada;
    }

    public int getHoraFin() {
        return horaInicio + duracion;
    }
}
