/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import java.io.Serializable;

public class Inmueble implements Serializable{

    private String id;
    private String tipoInmueble;
    private int estrato;
    private boolean amoblado;
    private String caracteristicasAdicionales;

    public Inmueble() {
    }

    public Inmueble(String id, String tipoInmueble, int estrato, boolean amoblado, String caracteristicasAdicionales) {
        this.id = id;
        this.tipoInmueble = tipoInmueble;
        this.estrato = estrato;
        this.amoblado = amoblado;
        this.caracteristicasAdicionales = caracteristicasAdicionales;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTipoInmueble() {
        return tipoInmueble;
    }

    public void setTipoInmueble(String tipoInmueble) {
        this.tipoInmueble = tipoInmueble;
    }

    public String getCaracteristicasAdicionales() {
        return caracteristicasAdicionales;
    }

    public void setCaracteristicasAdicionales(String caracteristicasAdicionales) {
        this.caracteristicasAdicionales = caracteristicasAdicionales;
    }

    public int getEstrato() {
        return estrato;
    }

    public void setEstrato(int estrato) {
        this.estrato = estrato;
    }

}
