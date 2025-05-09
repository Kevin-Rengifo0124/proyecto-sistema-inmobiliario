/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import java.io.Serializable;
import util.ListaEnlazada;

public class Sede implements Serializable {

    private String ciudad;
    private int capacidadInmuebles;
    private int cantidadInmueblesActual;
    private ListaEnlazada<Empleado> empleados;
    private ListaEnlazada<Propiedad> propiedades;

    public Sede(String ciudad, int capacidadInmuebles) {
        this.ciudad = ciudad;
        this.capacidadInmuebles = capacidadInmuebles;
        this.cantidadInmueblesActual = 0;
        this.empleados = new ListaEnlazada<>();
        this.propiedades = new ListaEnlazada<>();
    }

    public Sede() {
        this.empleados = new ListaEnlazada<>();
        this.propiedades = new ListaEnlazada<>();
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public int getCapacidadInmuebles() {
        return capacidadInmuebles;
    }

    public void setCapacidadInmuebles(int capacidadInmuebles) {
        this.capacidadInmuebles = capacidadInmuebles;
    }

    public int getCantidadInmueblesActual() {
        return cantidadInmueblesActual;
    }

    public void setCantidadInmueblesActual(int cantidadInmueblesActual) {
        this.cantidadInmueblesActual = cantidadInmueblesActual;
    }

    public ListaEnlazada<Empleado> getEmpleados() {
        return empleados;
    }

    public void setEmpleados(ListaEnlazada<Empleado> empleados) {
        this.empleados = empleados;
    }

    public ListaEnlazada<Propiedad> getPropiedades() {
        return propiedades;
    }

    public void setPropiedades(ListaEnlazada<Propiedad> propiedades) {
        this.propiedades = propiedades;
    }

    public void agregarEmpleado(Empleado empleado) {
        empleados.add(empleado);
    }

    public void agregarPropiedad(Propiedad propiedad) {
        if (cantidadInmueblesActual < capacidadInmuebles) {
            propiedades.add(propiedad);
            cantidadInmueblesActual++;
        } else {
            throw new RuntimeException("No se pueden agregar más propiedades a esta sede. Capacidad máxima alcanzada.");
        }
    }

    public boolean tieneCupo() {
        return cantidadInmueblesActual < capacidadInmuebles;
    }

    public void removerPropiedad(Propiedad propiedad) {
        propiedades.remove(propiedad);
        cantidadInmueblesActual--;
    }
}
