/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import java.io.Serializable;
import java.util.Date;

public class Mensaje implements Serializable {

    private String id;
    private Propiedad propiedad;
    private String remitente; // Email del remitente
    private String destinatario; // Email del destinatario
    private String contenido;
    private Date fecha;
    private boolean respondido;

    public Mensaje(String id, Propiedad propiedad, String remitente, String destinatario, String contenido) {
        this.id = id;
        this.propiedad = propiedad;
        this.remitente = remitente;
        this.destinatario = destinatario;
        this.contenido = contenido;
        this.fecha = new Date();
        this.respondido = false;
    }

    // Constructor vacío para serialización
    public Mensaje() {
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

    public String getRemitente() {
        return remitente;
    }

    public void setRemitente(String remitente) {
        this.remitente = remitente;
    }

    public String getDestinatario() {
        return destinatario;
    }

    public void setDestinatario(String destinatario) {
        this.destinatario = destinatario;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public boolean isRespondido() {
        return respondido;
    }

    public void setRespondido(boolean respondido) {
        this.respondido = respondido;
    }
}
