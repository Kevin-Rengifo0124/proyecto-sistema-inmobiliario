/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import java.io.Serializable;

public class Usuario implements Serializable{

    protected String email;
    protected String clave;
    protected String rol;

    public static final String ADMINISTRATIVO = "Administrativo";
    public static final String EMPLEADO = "Empleado";
    public static final String CLIENTE = "Cliente";

    public Usuario() {
    }

    public Usuario(String email, String clave, String rol) {
        this.email = email;
        this.clave = clave;
        this.rol = rol;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

}
