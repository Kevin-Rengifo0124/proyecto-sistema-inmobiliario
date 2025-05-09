/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

import excepciones.CapacidadExcedidaException;
import excepciones.EmpleadoIncompatibleException;
import modelo.Cliente;
import modelo.Empleado;
import modelo.Propiedad;
import modelo.Sede;
import singleton.Singleton;
import util.ListaEnlazada;

/**
 * Controlador para gestionar las propiedades del sistema
 */
public class ControladorPropiedad {

    private final Singleton singleton;

    public ControladorPropiedad() {
        this.singleton = Singleton.getInstancia();
    }

    /**
     * Obtiene todas las propiedades
     *
     * @return Lista de todas las propiedades
     */
    public ListaEnlazada<Propiedad> getListaPropiedades() {
        return singleton.getListaPropiedades();
    }

    /**
     * Busca una propiedad por su ID
     *
     * @param id ID de la propiedad
     * @return Propiedad encontrada o null si no existe
     */
    public Propiedad buscarPorId(String id) {
        ListaEnlazada<Propiedad> propiedades = singleton.getListaPropiedades();

        for (int i = 0; i < propiedades.size(); i++) {
            Propiedad propiedad = propiedades.get(i);
            if (propiedad.getId().equals(id)) {
                return propiedad;
            }
        }

        return null;
    }

    /**
     * Busca propiedades por su tipo (venta/arriendo)
     *
     * @param tipo Tipo de las propiedades (venta/arriendo)
     * @return Lista de propiedades de ese tipo
     */
    public ListaEnlazada<Propiedad> buscarPorTipo(String tipo) {
        ListaEnlazada<Propiedad> propiedades = singleton.getListaPropiedades();
        ListaEnlazada<Propiedad> resultado = new ListaEnlazada<>();

        for (int i = 0; i < propiedades.size(); i++) {
            Propiedad propiedad = propiedades.get(i);
            if (propiedad.getTipo().equals(tipo)) {
                resultado.add(propiedad);
            }
        }

        return resultado;
    }

    /**
     * Busca propiedades por su estado
     *
     * @param estado Estado de las propiedades
     * @return Lista de propiedades en ese estado
     */
    public ListaEnlazada<Propiedad> buscarPorEstado(String estado) {
        return singleton.buscarPropiedadesPorEstado(estado);
    }

    /**
     * Busca propiedades por ciudad
     *
     * @param ciudad Ciudad de las propiedades
     * @return Lista de propiedades en esa ciudad
     */
    public ListaEnlazada<Propiedad> buscarPorCiudad(String ciudad) {
        ListaEnlazada<Propiedad> propiedades = singleton.getListaPropiedades();
        ListaEnlazada<Propiedad> resultado = new ListaEnlazada<>();

        for (int i = 0; i < propiedades.size(); i++) {
            Propiedad propiedad = propiedades.get(i);
            if (propiedad.getCiudad().equals(ciudad)) {
                resultado.add(propiedad);
            }
        }

        return resultado;
    }

    /**
     * Busca propiedades por sede
     *
     * @param sede Sede de las propiedades
     * @return Lista de propiedades de esa sede
     */
    public ListaEnlazada<Propiedad> buscarPorSede(Sede sede) {
        ListaEnlazada<Propiedad> propiedades = singleton.getListaPropiedades();
        ListaEnlazada<Propiedad> resultado = new ListaEnlazada<>();

        for (int i = 0; i < propiedades.size(); i++) {
            Propiedad propiedad = propiedades.get(i);
            if (propiedad.getSede() != null && propiedad.getSede().getCiudad().equals(sede.getCiudad())) {
                resultado.add(propiedad);
            }
        }

        return resultado;
    }

    /**
     * Busca propiedades por empleado encargado
     *
     * @param empleado Empleado encargado
     * @return Lista de propiedades a cargo de ese empleado
     */
    public ListaEnlazada<Propiedad> buscarPorEncargado(Empleado empleado) {
        return singleton.buscarPropiedadesPorEncargado(empleado);
    }

    /**
     * Busca propiedades por propietario
     *
     * @param propietario Propietario
     * @return Lista de propiedades de ese propietario
     */
    public ListaEnlazada<Propiedad> buscarPorPropietario(Cliente propietario) {
        ListaEnlazada<Propiedad> propiedades = singleton.getListaPropiedades();
        ListaEnlazada<Propiedad> resultado = new ListaEnlazada<>();

        for (int i = 0; i < propiedades.size(); i++) {
            Propiedad propiedad = propiedades.get(i);
            if (propiedad.getPropietario() != null
                    && propiedad.getPropietario().getEmail().equals(propietario.getEmail())) {
                resultado.add(propiedad);
            }
        }

        return resultado;
    }

    /**
     * Busca propiedades por rango de precio
     *
     * @param minimo Precio mínimo
     * @param maximo Precio máximo
     * @return Lista de propiedades en ese rango de precio
     */
    public ListaEnlazada<Propiedad> buscarPorRangoPrecio(double minimo, double maximo) {
        ListaEnlazada<Propiedad> propiedades = singleton.getListaPropiedades();
        ListaEnlazada<Propiedad> resultado = new ListaEnlazada<>();

        for (int i = 0; i < propiedades.size(); i++) {
            Propiedad propiedad = propiedades.get(i);
            double precio = propiedad.getPrecio();
            if (precio >= minimo && precio <= maximo) {
                resultado.add(propiedad);
            }
        }

        return resultado;
    }

    /**
     * Busca propiedades que permiten agendar visitas
     *
     * @return Lista de propiedades que permiten agendar visitas
     */
    public ListaEnlazada<Propiedad> buscarConAgendamiento() {
        ListaEnlazada<Propiedad> propiedades = singleton.getListaPropiedades();
        ListaEnlazada<Propiedad> resultado = new ListaEnlazada<>();

        for (int i = 0; i < propiedades.size(); i++) {
            Propiedad propiedad = propiedades.get(i);
            if (propiedad.isPermiteAgendarVisita()) {
                resultado.add(propiedad);
            }
        }

        return resultado;
    }

    /**
     * Crea una nueva propiedad
     *
     * @param direccion Dirección de la propiedad
     * @param ciudad Ciudad de la propiedad
     * @param tipo Tipo de propiedad (venta/arriendo)
     * @param precio Precio de la propiedad
     * @param descripcion Descripción de la propiedad
     * @param propietario Propietario de la propiedad
     * @return Propiedad creada
     */
    public Propiedad crearPropiedad(String direccion, String ciudad, String tipo,
            double precio, String descripcion, Cliente propietario) {

        // Verificar que el tipo sea válido
        if (!tipo.equals(Propiedad.TIPO_VENTA) && !tipo.equals(Propiedad.TIPO_ARRIENDO)) {
            throw new IllegalArgumentException("Tipo de propiedad no válido");
        }

        // Crear la propiedad
        Propiedad propiedad = new Propiedad(
                singleton.generarId(),
                direccion,
                ciudad,
                tipo,
                precio,
                descripcion,
                propietario
        );

        // Guardar la propiedad
        singleton.guardarPropiedad(propiedad);

        // Actualizar relaciones
        propietario.agregarPropiedad(propiedad);
        singleton.escribirClientes();

        return propiedad;
    }

    /**
     * Asigna una propiedad a un empleado y sede
     *
     * @param propiedad Propiedad a asignar
     * @param empleado Empleado encargado
     * @param sede Sede de la propiedad
     * @throws EmpleadoIncompatibleException Si el empleado no puede gestionar
     * ese tipo de propiedad
     * @throws CapacidadExcedidaException Si la sede ha alcanzado su capacidad
     * máxima
     */
    public void asignarPropiedad(Propiedad propiedad, Empleado empleado, Sede sede)
            throws EmpleadoIncompatibleException, CapacidadExcedidaException {

        // Verificar que el empleado pueda gestionar ese tipo de propiedad
        if (!empleado.validarTipoPropiedad(propiedad)) {
            throw new EmpleadoIncompatibleException(propiedad.getTipo());
        }

        // Verificar que la sede tenga capacidad
        if (!sede.tieneCupo()) {
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
     * Actualiza una propiedad existente
     *
     * @param propiedad Propiedad a actualizar
     * @return true si se actualizó exitosamente
     */
    public boolean actualizarPropiedad(Propiedad propiedad) {
        return singleton.actualizarPropiedad(propiedad);
    }

    /**
     * Marca una propiedad como vendida o arrendada
     *
     * @param propiedad Propiedad a marcar
     * @param estado Nuevo estado (vendida o arrendada)
     */
    public void marcarPropiedad(Propiedad propiedad, String estado) {
        // Verificar que el nuevo estado sea válido
        if (!estado.equals(Propiedad.ESTADO_VENDIDA) && !estado.equals(Propiedad.ESTADO_ARRENDADA)) {
            throw new IllegalArgumentException("Estado no válido");
        }

        // Actualizar la propiedad
        propiedad.setEstado(estado);
        singleton.actualizarPropiedad(propiedad);
    }

    /**
     * Habilita o deshabilita el agendamiento de visitas para una propiedad
     *
     * @param propiedad Propiedad a modificar
     * @param permiteAgendarVisita true para permitir, false para no permitir
     */
    public void habilitarAgendamiento(Propiedad propiedad, boolean permiteAgendarVisita) {
        propiedad.setPermiteAgendarVisita(permiteAgendarVisita);
        singleton.actualizarPropiedad(propiedad);
    }
}
