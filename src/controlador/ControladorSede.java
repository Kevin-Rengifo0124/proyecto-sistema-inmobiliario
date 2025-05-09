/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

import excepciones.CapacidadExcedidaException;
import excepciones.SedeExistenteException;
import modelo.Empleado;
import modelo.MatrizSedes;
import modelo.Propiedad;
import modelo.Sede;
import singleton.Singleton;
import util.ListaEnlazada;

/**
 * Controlador para gestionar las sedes del sistema
 */
public class ControladorSede {

    private final Singleton singleton;

    public ControladorSede() {
        this.singleton = Singleton.getInstancia();
    }

    /**
     * Obtiene todas las sedes
     *
     * @return Lista de todas las sedes
     */
    public ListaEnlazada<Sede> getListaSedes() {
        return singleton.getListaSedes();
    }

    /**
     * Busca una sede por su ciudad
     *
     * @param ciudad Ciudad de la sede
     * @return Sede encontrada o null si no existe
     */
    public Sede buscarPorCiudad(String ciudad) {
        return singleton.buscarSedePorCiudad(ciudad);
    }

    /**
     * Crea una nueva sede
     *
     * @param ciudad Ciudad de la sede
     * @param capacidadInmuebles Capacidad máxima de inmuebles
     * @return Sede creada
     * @throws SedeExistenteException Si ya existe una sede en esa ciudad
     */
    public Sede crearSede(String ciudad, int capacidadInmuebles) throws SedeExistenteException {
        // Verificar que no exista ya una sede en esa ciudad
        if (singleton.buscarSedePorCiudad(ciudad) != null) {
            throw new SedeExistenteException(ciudad);
        }

        // Crear y guardar la sede
        Sede sede = new Sede(ciudad, capacidadInmuebles);
        boolean resultado = singleton.guardarSede(sede);

        if (!resultado) {
            throw new SedeExistenteException(ciudad);
        }

        return sede;
    }

    /**
     * Agrega una sede a la matriz de sedes
     *
     * @param sede Sede a agregar
     * @param fila Fila en la matriz
     * @param columna Columna en la matriz
     * @return true si se agregó exitosamente
     */
    public boolean agregarSedeAMatriz(Sede sede, int fila, int columna) {
        MatrizSedes matriz = singleton.getMatrizSedes();
        boolean resultado = matriz.agregarSede(sede, fila, columna);

        if (resultado) {
            singleton.escribirMatrizSedes();
        }

        return resultado;
    }

    /**
     * Obtiene la matriz de sedes
     *
     * @return Matriz de sedes
     */
    public MatrizSedes getMatrizSedes() {
        return singleton.getMatrizSedes();
    }

    /**
     * Actualiza la capacidad de inmuebles de una sede
     *
     * @param sede Sede a actualizar
     * @param nuevaCapacidad Nueva capacidad de inmuebles
     * @throws CapacidadExcedidaException Si la nueva capacidad es menor que la
     * cantidad actual de inmuebles
     */
    public void actualizarCapacidad(Sede sede, int nuevaCapacidad) throws CapacidadExcedidaException {
        if (nuevaCapacidad < sede.getCantidadInmueblesActual()) {
            throw new CapacidadExcedidaException(sede.getCiudad());
        }

        sede.setCapacidadInmuebles(nuevaCapacidad);
        singleton.escribirSedes();
        singleton.escribirMatrizSedes();
    }

    /**
     * Obtiene el estado de una casilla en la matriz de sedes
     *
     * @param fila Fila en la matriz
     * @param columna Columna en la matriz
     * @return Estado de la casilla: 0 - Blanco (no asignada) 1 - Gris (asignada
     * con cupo) 2 - Amarillo (asignada sin cupo) -1 - Error
     */
    public int getEstadoCasilla(int fila, int columna) {
        return singleton.getMatrizSedes().getEstadoCasilla(fila, columna);
    }

    /**
     * Obtiene los empleados de una sede
     *
     * @param sede Sede a consultar
     * @return Lista de empleados de esa sede
     */
    public ListaEnlazada<Empleado> getEmpleadosPorSede(Sede sede) {
        return sede.getEmpleados();
    }

    /**
     * Obtiene las propiedades de una sede
     *
     * @param sede Sede a consultar
     * @return Lista de propiedades de esa sede
     */
    public ListaEnlazada<Propiedad> getPropiedadesPorSede(Sede sede) {
        return sede.getPropiedades();
    }

    /**
     * Verifica si una sede tiene cupo para más propiedades
     *
     * @param sede Sede a verificar
     * @return true si tiene cupo, false si no
     */
    public boolean tieneCupo(Sede sede) {
        return sede.tieneCupo();
    }

    /**
     * Obtiene la cantidad de inmuebles actual de una sede
     *
     * @param sede Sede a consultar
     * @return Cantidad de inmuebles actual
     */
    public int getCantidadInmueblesActual(Sede sede) {
        return sede.getCantidadInmueblesActual();
    }

    /**
     * Obtiene la capacidad máxima de inmuebles de una sede
     *
     * @param sede Sede a consultar
     * @return Capacidad máxima de inmuebles
     */
    public int getCapacidadInmuebles(Sede sede) {
        return sede.getCapacidadInmuebles();
    }

    /**
     * Obtiene la posición de una sede en la matriz
     *
     * @param ciudad Ciudad de la sede
     * @return Array con la posición [fila, columna] o null si no se encuentra
     */
    public int[] getPosicionSede(String ciudad) {
        return singleton.getMatrizSedes().getPosicionSede(ciudad);
    }

}
