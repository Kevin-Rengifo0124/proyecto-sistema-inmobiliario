/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

import excepciones.EmpleadoIncompatibleException;
import excepciones.PropiedadNoDisponibleException;
import excepciones.UsuarioNoEncontradoException;
import java.util.Date;
import modelo.Agenda;
import modelo.Empleado;
import modelo.Mensaje;
import modelo.Propiedad;
import singleton.Singleton;
import util.ListaEnlazada;

/**
 * Controlador para gestionar las funcionalidades del empleado
 */
public class ControladorEmpleado {

    private final Singleton singleton;

    public ControladorEmpleado() {
        this.singleton = Singleton.getInstancia();
    }

    /**
     * Obtiene el empleado actual con sesión iniciada
     *
     * @return Empleado actual
     * @throws UsuarioNoEncontradoException Si no hay sesión iniciada o el
     * usuario no es un empleado
     */
    public Empleado getEmpleadoActual() throws UsuarioNoEncontradoException {
        if (singleton.getUsuarioActual() == null) {
            throw new UsuarioNoEncontradoException("No hay sesión iniciada");
        }

        if (!singleton.getUsuarioActual().getRol().equals(modelo.Usuario.EMPLEADO)) {
            throw new UsuarioNoEncontradoException("El usuario actual no es un empleado");
        }

        String email = singleton.getUsuarioActual().getEmail();

        for (int i = 0; i < singleton.getListaEmpleados().size(); i++) {
            Empleado empleado = singleton.getListaEmpleados().get(i);
            if (empleado.getEmail().equals(email)) {
                return empleado;
            }
        }

        throw new UsuarioNoEncontradoException(email);
    }

    /**
     * Obtiene las propiedades asignadas al empleado actual
     *
     * @return Lista de propiedades asignadas
     * @throws UsuarioNoEncontradoException Si no hay sesión iniciada o el
     * usuario no es un empleado
     */
    public ListaEnlazada<Propiedad> getPropiedadesAsignadas() throws UsuarioNoEncontradoException {
        Empleado empleado = getEmpleadoActual();
        return singleton.buscarPropiedadesPorEncargado(empleado);
    }

    /**
     * Obtiene las propiedades activas (disponibles) asignadas al empleado
     * actual
     *
     * @return Lista de propiedades activas
     * @throws UsuarioNoEncontradoException Si no hay sesión iniciada o el
     * usuario no es un empleado
     */
    public ListaEnlazada<Propiedad> getPropiedadesActivas() throws UsuarioNoEncontradoException {
        ListaEnlazada<Propiedad> asignadas = getPropiedadesAsignadas();
        ListaEnlazada<Propiedad> activas = new ListaEnlazada<>();

        for (int i = 0; i < asignadas.size(); i++) {
            Propiedad propiedad = asignadas.get(i);
            if (propiedad.getEstado().equals(Propiedad.ESTADO_DISPONIBLE)) {
                activas.add(propiedad);
            }
        }

        return activas;
    }

    /**
     * Obtiene el historial de propiedades cerradas (vendidas/arrendadas) del
     * empleado actual
     *
     * @return Lista de propiedades cerradas
     * @throws UsuarioNoEncontradoException Si no hay sesión iniciada o el
     * usuario no es un empleado
     */
    public ListaEnlazada<Propiedad> getPropiedadesCerradas() throws UsuarioNoEncontradoException {
        ListaEnlazada<Propiedad> asignadas = getPropiedadesAsignadas();
        ListaEnlazada<Propiedad> cerradas = new ListaEnlazada<>();

        for (int i = 0; i < asignadas.size(); i++) {
            Propiedad propiedad = asignadas.get(i);
            if (propiedad.getEstado().equals(Propiedad.ESTADO_VENDIDA)
                    || propiedad.getEstado().equals(Propiedad.ESTADO_ARRENDADA)) {
                cerradas.add(propiedad);
            }
        }

        return cerradas;
    }

    /**
     * Habilita o deshabilita el agendamiento de visitas para una propiedad
     *
     * @param propiedad Propiedad a modificar
     * @param permiteAgendarVisita true para permitir, false para no permitir
     * @throws EmpleadoIncompatibleException Si el empleado actual no es el
     * encargado de la propiedad
     * @throws PropiedadNoDisponibleException Si la propiedad no está disponible
     */
    public void habilitarAgendamiento(Propiedad propiedad, boolean permiteAgendarVisita)
            throws EmpleadoIncompatibleException, PropiedadNoDisponibleException, UsuarioNoEncontradoException {

        Empleado empleado = getEmpleadoActual();

        // Verificar que el empleado sea el encargado de la propiedad
        if (propiedad.getEncargado() == null
                || !propiedad.getEncargado().getEmail().equals(empleado.getEmail())) {
            throw new EmpleadoIncompatibleException("No es el encargado de esta propiedad");
        }

        // Verificar que la propiedad esté disponible
        if (!propiedad.getEstado().equals(Propiedad.ESTADO_DISPONIBLE)) {
            throw new PropiedadNoDisponibleException();
        }

        // Actualizar la propiedad
        propiedad.setPermiteAgendarVisita(permiteAgendarVisita);
        singleton.actualizarPropiedad(propiedad);
    }

    /**
     * Obtiene la agenda de visitas para una propiedad
     *
     * @param propiedad Propiedad a consultar
     * @return Lista de agendas para esa propiedad
     * @throws EmpleadoIncompatibleException Si el empleado actual no es el
     * encargado de la propiedad
     */
    public ListaEnlazada<Agenda> getAgendaPropiedad(Propiedad propiedad)
            throws EmpleadoIncompatibleException, UsuarioNoEncontradoException {

        Empleado empleado = getEmpleadoActual();

        // Verificar que el empleado sea el encargado de la propiedad
        if (propiedad.getEncargado() == null
                || !propiedad.getEncargado().getEmail().equals(empleado.getEmail())) {
            throw new EmpleadoIncompatibleException("No es el encargado de esta propiedad");
        }

        return propiedad.getVisitas();
    }

    /**
     * Filtra la agenda de visitas por fecha
     *
     * @param propiedad Propiedad a consultar
     * @param fecha Fecha para filtrar
     * @return Lista de agendas filtrada
     * @throws EmpleadoIncompatibleException Si el empleado actual no es el
     * encargado de la propiedad
     */
    public ListaEnlazada<Agenda> filtrarAgendaPorFecha(Propiedad propiedad, Date fecha)
            throws EmpleadoIncompatibleException, UsuarioNoEncontradoException {

        ListaEnlazada<Agenda> agenda = getAgendaPropiedad(propiedad);
        ListaEnlazada<Agenda> filtrada = new ListaEnlazada<>();

        for (int i = 0; i < agenda.size(); i++) {
            Agenda visita = agenda.get(i);
            if (visita.getFecha().equals(fecha)) {
                filtrada.add(visita);
            }
        }

        return filtrada;
    }

    /**
     * Marca una propiedad como vendida o arrendada
     *
     * @param propiedad Propiedad a marcar
     * @param estado Nuevo estado (vendida o arrendada)
     * @throws EmpleadoIncompatibleException Si el empleado actual no es el
     * encargado de la propiedad
     * @throws PropiedadNoDisponibleException Si la propiedad no está disponible
     */
    public void marcarPropiedad(Propiedad propiedad, String estado)
            throws EmpleadoIncompatibleException, PropiedadNoDisponibleException, UsuarioNoEncontradoException {

        Empleado empleado = getEmpleadoActual();

        // Verificar que el empleado sea el encargado de la propiedad
        if (propiedad.getEncargado() == null
                || !propiedad.getEncargado().getEmail().equals(empleado.getEmail())) {
            throw new EmpleadoIncompatibleException("No es el encargado de esta propiedad");
        }

        // Verificar que la propiedad esté disponible
        if (!propiedad.getEstado().equals(Propiedad.ESTADO_DISPONIBLE)) {
            throw new PropiedadNoDisponibleException();
        }

        // Verificar que el nuevo estado sea válido
        if (!estado.equals(Propiedad.ESTADO_VENDIDA) && !estado.equals(Propiedad.ESTADO_ARRENDADA)) {
            throw new IllegalArgumentException("Estado no válido");
        }

        // Actualizar la propiedad
        propiedad.setEstado(estado);
        singleton.actualizarPropiedad(propiedad);
    }

    /**
     * Responde a un mensaje de un cliente
     *
     * @param mensaje Mensaje original
     * @param contenido Contenido de la respuesta
     * @throws EmpleadoIncompatibleException Si el empleado actual no es el
     * destinatario del mensaje
     */
    public void responderMensaje(Mensaje mensaje, String contenido)
            throws EmpleadoIncompatibleException, UsuarioNoEncontradoException {

        Empleado empleado = getEmpleadoActual();

        // Verificar que el empleado sea el destinatario del mensaje
        if (!mensaje.getDestinatario().equals(empleado.getEmail())) {
            throw new EmpleadoIncompatibleException("No es el destinatario de este mensaje");
        }

        // Marcar el mensaje original como respondido
        mensaje.setRespondido(true);
        singleton.escribirMensajes();

        // Crear y guardar la respuesta
        Mensaje respuesta = new Mensaje(
                singleton.generarId(),
                mensaje.getPropiedad(),
                empleado.getEmail(),
                mensaje.getRemitente(),
                contenido
        );

        singleton.guardarMensaje(respuesta);
        mensaje.getPropiedad().agregarMensaje(respuesta);
        singleton.actualizarPropiedad(mensaje.getPropiedad());
    }

    /**
     * Obtiene los mensajes no respondidos para el empleado actual
     *
     * @return Lista de mensajes no respondidos
     * @throws UsuarioNoEncontradoException Si no hay sesión iniciada o el
     * usuario no es un empleado
     */
    public ListaEnlazada<Mensaje> getMensajesNoRespondidos() throws UsuarioNoEncontradoException {
        Empleado empleado = getEmpleadoActual();
        ListaEnlazada<Mensaje> mensajes = singleton.getListaMensajes();
        ListaEnlazada<Mensaje> noRespondidos = new ListaEnlazada<>();

        for (int i = 0; i < mensajes.size(); i++) {
            Mensaje mensaje = mensajes.get(i);
            if (mensaje.getDestinatario().equals(empleado.getEmail()) && !mensaje.isRespondido()) {
                noRespondidos.add(mensaje);
            }
        }

        return noRespondidos;
    }
}
