/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

import excepciones.CapacidadExcedidaException;
import excepciones.EmpleadoIncompatibleException;
import excepciones.SedeExistenteException;
import excepciones.UsuarioExistenteException;
import modelo.Empleado;
import modelo.MatrizSedes;
import modelo.Propiedad;
import modelo.Sede;
import modelo.Usuario;
import singleton.Singleton;
import util.ListaEnlazada;

/**
 * Controlador para gestionar las funcionalidades del administrador
 */
public class ControladorAdministrador {

    private final Singleton singleton;

    public ControladorAdministrador() {
        this.singleton = Singleton.getInstancia();
    }

    /**
     * Registra una nueva sede en la matriz
     *
     * @param ciudad Ciudad de la sede
     * @param capacidadInmuebles Capacidad máxima de inmuebles
     * @param fila Fila en la matriz
     * @param columna Columna en la matriz
     * @return Sede registrada
     * @throws SedeExistenteException Si ya existe una sede en esa ciudad
     */
    public Sede registrarSede(String ciudad, int capacidadInmuebles, int fila, int columna) throws SedeExistenteException {
        // Verificar que no exista una sede en esa ciudad
        if (singleton.buscarSedePorCiudad(ciudad) != null) {
            throw new SedeExistenteException(ciudad);
        }

        // Crear y agregar la sede a la matriz
        Sede sede = new Sede(ciudad, capacidadInmuebles);
        MatrizSedes matriz = singleton.getMatrizSedes();

        boolean resultado = matriz.agregarSede(sede, fila, columna);
        if (!resultado) {
            throw new SedeExistenteException(ciudad);
        }

        // Guardar la sede en la lista de sedes
        singleton.guardarSede(sede);
        singleton.escribirMatrizSedes();

        return sede;
    }

    /**
     * Actualiza la capacidad de inmuebles de una sede
     *
     * @param ciudad Ciudad de la sede
     * @param nuevaCapacidad Nueva capacidad de inmuebles
     * @throws CapacidadExcedidaException Si la nueva capacidad es menor que la
     * cantidad actual de inmuebles
     */
    public void actualizarCapacidadSede(String ciudad, int nuevaCapacidad) throws CapacidadExcedidaException {
        Sede sede = singleton.buscarSedePorCiudad(ciudad);
        if (sede != null) {
            if (nuevaCapacidad < sede.getCantidadInmueblesActual()) {
                throw new CapacidadExcedidaException(ciudad);
            }
            sede.setCapacidadInmuebles(nuevaCapacidad);
            singleton.escribirSedes();
            singleton.escribirMatrizSedes();
        }
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
     * Registra un nuevo empleado
     *
     * @param nombreCompleto Nombre completo del empleado
     * @param cedula Cédula del empleado
     * @param telefono Teléfono del empleado
     * @param direccion Dirección del empleado
     * @param email Email del empleado
     * @param clave Clave del empleado
     * @param sedeCiudad Ciudad de la sede
     * @param puedeVender Puede gestionar propiedades en venta
     * @param puedeArrendar Puede gestionar propiedades en arriendo
     * @return Empleado registrado
     * @throws UsuarioExistenteException Si ya existe un usuario con ese email o
     * cédula
     */
    public Empleado registrarEmpleado(String nombreCompleto, String cedula, String telefono,
            String direccion, String email, String clave, String sedeCiudad,
            boolean puedeVender, boolean puedeArrendar) throws UsuarioExistenteException {

        // Verificar si ya existe un usuario con ese email
        if (singleton.buscarUsuarioPorEmail(email) != null) {
            throw new UsuarioExistenteException(email);
        }

        // Verificar si ya existe una persona con esa cédula
        if (singleton.buscarPersonaPorCedula(cedula) != null) {
            throw new UsuarioExistenteException(cedula, true);
        }

        // Obtener la sede
        Sede sede = singleton.buscarSedePorCiudad(sedeCiudad);

        // Verificar que se haya seleccionado al menos una opción
        if (!puedeVender && !puedeArrendar) {
            throw new IllegalArgumentException("El empleado debe poder gestionar al menos un tipo de propiedad");
        }

        // Crear y guardar el nuevo empleado
        Empleado empleado = new Empleado(nombreCompleto, cedula, telefono, direccion, email, clave, sede, puedeVender, puedeArrendar);
        boolean resultado = singleton.guardarPersona(empleado);

        if (!resultado) {
            throw new UsuarioExistenteException(email);
        }

        // Agregar el empleado a la sede
        sede.agregarEmpleado(empleado);
        singleton.escribirSedes();

        return empleado;
    }

    /**
     * Obtiene la siguiente propiedad pendiente de asignación
     *
     * @return Propiedad pendiente o null si no hay
     */
    public Propiedad getSiguientePropiedadPendiente() {
        return singleton.siguientePropiedadEnCola();
    }

    /**
     * Asigna una propiedad pendiente a un empleado y sede
     *
     * @param empleado Empleado encargado
     * @param sede Sede de la propiedad
     * @throws EmpleadoIncompatibleException Si el empleado no puede gestionar
     * ese tipo de propiedad
     * @throws CapacidadExcedidaException Si la sede ha alcanzado su capacidad
     * máxima
     */
    public void asignarPropiedad(Empleado empleado, Sede sede) throws EmpleadoIncompatibleException, CapacidadExcedidaException {
        // Obtener la siguiente propiedad en la cola
        Propiedad propiedad = singleton.extraerPropiedadDeCola();
        if (propiedad == null) {
            return;
        }

        // Verificar que el empleado pueda gestionar ese tipo de propiedad
        if (!empleado.validarTipoPropiedad(propiedad)) {
            // Volver a agregar la propiedad a la cola
            singleton.agregarPropiedadACola(propiedad);
            throw new EmpleadoIncompatibleException(propiedad.getTipo());
        }

        // Verificar que la sede tenga capacidad
        if (!sede.tieneCupo()) {
            // Volver a agregar la propiedad a la cola
            singleton.agregarPropiedadACola(propiedad);
            throw new CapacidadExcedidaException(sede.getCiudad());
        }

        // Asignar la propiedad
        propiedad.setEncargado(empleado);
        propiedad.setSede(sede);
        propiedad.setEstado(Propiedad.ESTADO_DISPONIBLE);

        // Actualizar las relaciones
        empleado.asignarPropiedad(propiedad);
        sede.agregarPropiedad(propiedad);

        // Guardar los cambios
        singleton.actualizarPropiedad(propiedad);
        singleton.escribirEmpleados();
        singleton.escribirSedes();
    }

    /**
     * Obtiene el historial de propiedades vendidas o arrendadas
     *
     * @return Lista de propiedades vendidas o arrendadas
     */
    public ListaEnlazada<Propiedad> getHistorialPropiedades() {
        ListaEnlazada<Propiedad> historial = new ListaEnlazada<>();
        ListaEnlazada<Propiedad> propiedades = singleton.getListaPropiedades();

        for (int i = 0; i < propiedades.size(); i++) {
            Propiedad propiedad = propiedades.get(i);
            if (propiedad.getEstado().equals(Propiedad.ESTADO_VENDIDA)
                    || propiedad.getEstado().equals(Propiedad.ESTADO_ARRENDADA)) {
                historial.add(propiedad);
            }
        }

        return historial;
    }

    /**
     * Filtra el historial de propiedades por diferentes criterios
     *
     * @param empleadoEmail Email del empleado (null para no filtrar)
     * @param ciudad Ciudad de la sede (null para no filtrar)
     * @param soloVendidas true para mostrar solo vendidas
     * @param soloArrendadas true para mostrar solo arrendadas
     * @return Lista de propiedades filtrada
     */
    public ListaEnlazada<Propiedad> filtrarHistorial(String empleadoEmail, String ciudad,
            boolean soloVendidas, boolean soloArrendadas) {

        ListaEnlazada<Propiedad> historial = getHistorialPropiedades();
        ListaEnlazada<Propiedad> resultado = new ListaEnlazada<>();

        for (int i = 0; i < historial.size(); i++) {
            Propiedad propiedad = historial.get(i);
            boolean incluir = true;

            // Filtrar por empleado
            if (empleadoEmail != null && !empleadoEmail.isEmpty()) {
                if (propiedad.getEncargado() == null
                        || !propiedad.getEncargado().getEmail().equals(empleadoEmail)) {
                    incluir = false;
                }
            }

            // Filtrar por ciudad
            if (ciudad != null && !ciudad.isEmpty()) {
                if (!propiedad.getCiudad().equals(ciudad)) {
                    incluir = false;
                }
            }

            // Filtrar por estado
            if (soloVendidas && !propiedad.getEstado().equals(Propiedad.ESTADO_VENDIDA)) {
                incluir = false;
            }

            if (soloArrendadas && !propiedad.getEstado().equals(Propiedad.ESTADO_ARRENDADA)) {
                incluir = false;
            }

            // Si no se especifica ni vendidas ni arrendadas, mostrar ambas
            if (soloVendidas || soloArrendadas) {
                // Ya filtrado arriba
            } else {
                // Mostrar ambas
            }

            if (incluir) {
                resultado.add(propiedad);
            }
        }

        return resultado;
    }

    /**
     * Obtiene la lista de todos los empleados
     *
     * @return Lista de empleados
     */
    public ListaEnlazada<Empleado> getListaEmpleados() {
        return singleton.getListaEmpleados();
    }

    /**
     * Obtiene la lista de empleados de una sede
     *
     * @param ciudad Ciudad de la sede
     * @return Lista de empleados de esa sede
     */
    public ListaEnlazada<Empleado> getEmpleadosPorSede(String ciudad) {
        Sede sede = singleton.buscarSedePorCiudad(ciudad);
        if (sede == null) {
            return new ListaEnlazada<>();
        }
        return sede.getEmpleados();
    }

    /**
     * Obtiene la lista de todas las sedes
     *
     * @return Lista de sedes
     */
    public ListaEnlazada<Sede> getListaSedes() {
        return singleton.getListaSedes();
    }
}
