/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import java.io.Serializable;

/**
 *
 * @author kevinrengifo
 */
public class MatrizSedes implements Serializable {

    private static final int FILAS = 4;
    private static final int COLUMNAS = 5;
    private static final int MAX_SEDES = FILAS * COLUMNAS;

    private Sede[][] sedes;

    public MatrizSedes() {
        sedes = new Sede[FILAS][COLUMNAS];
    }

    public boolean agregarSede(Sede sede, int fila, int columna) {
        if (fila >= 0 && fila < FILAS && columna >= 0 && columna < COLUMNAS) {
            if (sedes[fila][columna] == null) {
                // Verificar que no exista ya una sede en la misma ciudad
                if (buscarSedePorCiudad(sede.getCiudad()) == null) {
                    sedes[fila][columna] = sede;
                    return true;
                } else {
                    return false; // Ya existe una sede en esa ciudad
                }
            } else {
                return false; // Ya hay una sede en esa posición
            }
        } else {
            return false; // Índices fuera de rango
        }
    }

    public Sede obtenerSede(int fila, int columna) {
        if (fila >= 0 && fila < FILAS && columna >= 0 && columna < COLUMNAS) {
            return sedes[fila][columna];
        } else {
            return null;
        }
    }

    public boolean removerSede(int fila, int columna) {
        if (fila >= 0 && fila < FILAS && columna >= 0 && columna < COLUMNAS) {
            if (sedes[fila][columna] != null) {
                sedes[fila][columna] = null;
                return true;
            } else {
                return false; // No hay sede en esa posición
            }
        } else {
            return false; // Índices fuera de rango
        }
    }

    public Sede buscarSedePorCiudad(String ciudad) {
        for (int i = 0; i < FILAS; i++) {
            for (int j = 0; j < COLUMNAS; j++) {
                if (sedes[i][j] != null && sedes[i][j].getCiudad().equals(ciudad)) {
                    return sedes[i][j];
                }
            }
        }
        return null;
    }

    public int[] getPosicionSede(String ciudad) {
        for (int i = 0; i < FILAS; i++) {
            for (int j = 0; j < COLUMNAS; j++) {
                if (sedes[i][j] != null && sedes[i][j].getCiudad().equals(ciudad)) {
                    return new int[]{i, j};
                }
            }
        }
        return null;
    }

    public int getEstadoCasilla(int fila, int columna) {
        if (fila >= 0 && fila < FILAS && columna >= 0 && columna < COLUMNAS) {
            if (sedes[fila][columna] == null) {
                return 0; // Blanco - No asignada
            } else if (sedes[fila][columna].tieneCupo()) {
                return 1; // Gris - Asignada con cupo
            } else {
                return 2; // Amarillo - Asignada sin cupo
            }
        } else {
            return -1; // Error
        }
    }

    public int getFilas() {
        return FILAS;
    }

    public int getColumnas() {
        return COLUMNAS;
    }

    public int getMaxSedes() {
        return MAX_SEDES;
    }
}
