/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package dao;

import excepciones.SedeExistenteException;
import modelo.MatrizSedes;
import modelo.Sede;

public interface MatrizSedesDAO {

    void agregarSede(Sede sede, int fila, int columna) throws SedeExistenteException;

    Sede obtenerSede(int fila, int columna);

    boolean removerSede(int fila, int columna);

    Sede buscarSedePorCiudad(String ciudad);

    int[] getPosicionSede(String ciudad);

    int getEstadoCasilla(int fila, int columna);

    MatrizSedes obtenerMatriz();

    void guardarMatriz(MatrizSedes matriz);
}
