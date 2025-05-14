/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import java.io.Serializable;
import java.util.Date;
import util.implementacion.ListaEnlazada;

public class Propiedad implements Serializable {

    // Constantes para los tipos de propiedad
    public static final String TIPO_VENTA = "Venta";
    public static final String TIPO_ARRIENDO = "Arriendo";

    // Constantes para el estado de la propiedad
    public static final String ESTADO_DISPONIBLE = "Disponible";
    public static final String ESTADO_VENDIDA = "Vendida";
    public static final String ESTADO_ARRENDADA = "Arrendada";
    public static final String ESTADO_PENDIENTE_ASIGNACION = "Pendiente de Asignación";

    private String id;
    private String direccion;
    private String ciudad;
    private String tipo; // Venta o Arriendo
    private double precio;
    private String descripcion;
    private String estado;
    private Cliente propietario;
    private Empleado encargado;
    private Sede sede;
    private boolean permiteAgendarVisita;
    private ListaEnlazada<Agenda> visitas;
    private ListaEnlazada<Mensaje> mensajes;
    private Date fechaRegistro;
    private Date fechaCierre;
    private Inmueble inmueble;

    public Propiedad() {
    }

    public Propiedad(String id, String direccion, String ciudad, String tipo, double precio, String descripcion, String estado, Cliente propietario, Empleado encargado, Sede sede, boolean permiteAgendarVisita, ListaEnlazada<Agenda> visitas, ListaEnlazada<Mensaje> mensajes, Date fechaRegistro, Date fechaCierre, Inmueble inmueble) {
        this.id = id;
        this.direccion = direccion;
        this.ciudad = ciudad;
        this.tipo = tipo;
        this.precio = precio;
        this.descripcion = descripcion;
        this.estado = estado;
        this.propietario = propietario;
        this.encargado = encargado;
        this.sede = sede;
        this.permiteAgendarVisita = permiteAgendarVisita;
        this.visitas = visitas;
        this.mensajes = mensajes;
        this.fechaRegistro = fechaRegistro;
        this.fechaCierre = fechaCierre;
        this.inmueble = inmueble;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
        if (estado.equals(ESTADO_VENDIDA) || estado.equals(ESTADO_ARRENDADA)) {
            this.fechaCierre = new Date();
            this.permiteAgendarVisita = false;

            // Cancelar todas las visitas pendientes
            for (int i = 0; i < visitas.size(); i++) {
                Agenda visita = visitas.get(i);
                if (!visita.isCompletada()) {
                    visita.setCancelada(true);
                }
            }
        }
    }

    public Cliente getPropietario() {
        return propietario;
    }

    public void setPropietario(Cliente propietario) {
        this.propietario = propietario;
    }

    public Empleado getEncargado() {
        return encargado;
    }

    public void setEncargado(Empleado encargado) {
        this.encargado = encargado;
    }

    public Sede getSede() {
        return sede;
    }

    public void setSede(Sede sede) {
        this.sede = sede;
    }

    public boolean isPermiteAgendarVisita() {
        return permiteAgendarVisita;
    }

    public void setPermiteAgendarVisita(boolean permiteAgendarVisita) {
        this.permiteAgendarVisita = permiteAgendarVisita;
    }

    public ListaEnlazada<Agenda> getVisitas() {
        return visitas;
    }

    public void setVisitas(ListaEnlazada<Agenda> visitas) {
        this.visitas = visitas;
    }

    public ListaEnlazada<Mensaje> getMensajes() {
        return mensajes;
    }

    public void setMensajes(ListaEnlazada<Mensaje> mensajes) {
        this.mensajes = mensajes;
    }

    public Date getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(Date fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public Date getFechaCierre() {
        return fechaCierre;
    }

    public void setFechaCierre(Date fechaCierre) {
        this.fechaCierre = fechaCierre;
    }

    public Inmueble getInmueble() {
        return inmueble;
    }

    public void setInmueble(Inmueble inmueble) {
        this.inmueble = inmueble;
    }

    public void agregarVisita(Agenda visita) {
        visitas.add(visita);
    }

    public void agregarMensaje(Mensaje mensaje) {
        mensajes.add(mensaje);
    }

    public boolean hayConflictoHorario(Date fecha, int horaInicio, int duracion) {
        for (int i = 0; i < visitas.size(); i++) {
            Agenda visita = visitas.get(i);
            if (!visita.isCompletada() && !visita.isCancelada()
                    && visita.getFecha().equals(fecha)) {
                int visitaInicio = visita.getHoraInicio();
                int visitaFin = visitaInicio + visita.getDuracion();
                int nuevaVisitaFin = horaInicio + duracion;

                if ((horaInicio >= visitaInicio && horaInicio < visitaFin)
                        || (nuevaVisitaFin > visitaInicio && nuevaVisitaFin <= visitaFin)
                        || (horaInicio <= visitaInicio && nuevaVisitaFin >= visitaFin)) {
                    return true;
                }
            }
        }
        return false;
    }

}
