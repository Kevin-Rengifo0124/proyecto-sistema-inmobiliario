/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

import excepciones.ConflictoHorarioException;
import excepciones.MensajePendienteException;
import excepciones.PropiedadNoDisponibleException;
import excepciones.UsuarioNoEncontradoException;
import excepciones.VisitasExcedidasException;
import java.util.Calendar;
import java.util.Date;
import modelo.Agenda;
import modelo.Cliente;
import modelo.Mensaje;
import modelo.Propiedad;
import singleton.Singleton;
import util.ListaEnlazada;

/**
 * Controlador para gestionar las funcionalidades del cliente
 */
public class ControladorCliente {

    private final Singleton singleton;

    public ControladorCliente() {
        this.singleton = Singleton.getInstancia();
    }

    /**
     * Obtiene el cliente actual con sesión iniciada
     *
     * @return Cliente actual
     * @throws UsuarioNoEncontradoException Si no hay sesión iniciada o el
     * usuario no es un cliente
     */
    public Cliente getClienteActual() throws UsuarioNoEncontradoException {
        if (singleton.getUsuarioActual() == null) {
            throw new UsuarioNoEncontradoException("No hay sesión iniciada");
        }

        if (!singleton.getUsuarioActual().getRol().equals(modelo.Usuario.CLIENTE)) {
            throw new UsuarioNoEncontradoException("El usuario actual no es un cliente");
        }

        String email = singleton.getUsuarioActual().getEmail();

        for (int i = 0; i < singleton.getListaClientes().size(); i++) {
            Cliente cliente = singleton.getListaClientes().get(i);
            if (cliente.getEmail().equals(email)) {
                return cliente;
            }
        }

        throw new UsuarioNoEncontradoException(email);
    }

    /**
     * Obtiene todas las propiedades disponibles
     *
     * @return Lista de propiedades disponibles
     */
    public ListaEnlazada<Propiedad> getPropiedadesDisponibles() {
        ListaEnlazada<Propiedad> disponibles = new ListaEnlazada<>();
        ListaEnlazada<Propiedad> propiedades = singleton.getListaPropiedades();

        for (int i = 0; i < propiedades.size(); i++) {
            Propiedad propiedad = propiedades.get(i);
            if (propiedad.getEstado().equals(Propiedad.ESTADO_DISPONIBLE)) {
                disponibles.add(propiedad);
            }
        }

        return disponibles;
    }

    /**
     * Filtra las propiedades disponibles según diferentes criterios
     *
     * @param tipo Tipo de propiedad (venta/arriendo) o null para no filtrar
     * @param precioMinimo Precio mínimo o -1 para no filtrar
     * @param precioMaximo Precio máximo o -1 para no filtrar
     * @param ciudad Ciudad o null para no filtrar
     * @return Lista de propiedades filtradas
     */
    public ListaEnlazada<Propiedad> filtrarPropiedades(String tipo, double precioMinimo,
            double precioMaximo, String ciudad) {

        ListaEnlazada<Propiedad> disponibles = getPropiedadesDisponibles();
        ListaEnlazada<Propiedad> filtradas = new ListaEnlazada<>();

        for (int i = 0; i < disponibles.size(); i++) {
            Propiedad propiedad = disponibles.get(i);
            boolean incluir = true;

            // Filtrar por tipo
            if (tipo != null && !tipo.isEmpty()) {
                if (!propiedad.getTipo().equals(tipo)) {
                    incluir = false;
                }
            }

            // Filtrar por precio mínimo
            if (precioMinimo >= 0) {
                if (propiedad.getPrecio() < precioMinimo) {
                    incluir = false;
                }
            }

            // Filtrar por precio máximo
            if (precioMaximo >= 0) {
                if (propiedad.getPrecio() > precioMaximo) {
                    incluir = false;
                }
            }

            // Filtrar por ciudad
            if (ciudad != null && !ciudad.isEmpty()) {
                if (!propiedad.getCiudad().equals(ciudad)) {
                    incluir = false;
                }
            }

            if (incluir) {
                filtradas.add(propiedad);
            }
        }

        return filtradas;
    }

    /**
     * Agenda una visita a una propiedad
     *
     * @param propiedad Propiedad a visitar
     * @param fecha Fecha de la visita
     * @param horaInicio Hora de inicio (0-23)
     * @param duracion Duración en horas (1-3)
     * @return Agenda creada
     * @throws PropiedadNoDisponibleException Si la propiedad no permite agendar
     * visitas
     * @throws VisitasExcedidasException Si el cliente ya tiene el máximo de
     * visitas agendadas
     * @throws ConflictoHorarioException Si hay un conflicto de horario con otra
     * visita
     * @throws UsuarioNoEncontradoException Si no hay sesión iniciada o el
     * usuario no es un cliente
     */
    public Agenda agendarVisita(Propiedad propiedad, Date fecha, int horaInicio, int duracion)
            throws PropiedadNoDisponibleException, VisitasExcedidasException,
            ConflictoHorarioException, UsuarioNoEncontradoException {

        Cliente cliente = getClienteActual();

        // Verificar que la propiedad permita agendar visitas
        if (!propiedad.isPermiteAgendarVisita()) {
            throw new PropiedadNoDisponibleException("La propiedad no permite agendar visitas");
        }

        // Verificar que el cliente no haya excedido el límite de visitas
        if (!cliente.puedeAgendarMasVisitas()) {
            throw new VisitasExcedidasException();
        }

        // Verificar que la duración sea válida (1-3 horas)
        if (duracion < 1 || duracion > 3) {
            throw new IllegalArgumentException("La duración debe ser entre 1 y 3 horas");
        }

        // Verificar que la hora de inicio sea válida (dentro de horario laboral)
        if (horaInicio < 8 || horaInicio > 16 || (horaInicio + duracion) > 17) {
            throw new IllegalArgumentException("La hora de inicio debe estar dentro del horario laboral (8-17)");
        }

        // Verificar que la fecha sea válida (no en el pasado)
        Date hoy = new Date();
        if (fecha.before(hoy)) {
            throw new IllegalArgumentException("No se puede agendar una visita en el pasado");
        }

        // Si el cliente vive en otra ciudad, la visita debe ser al menos 5 días después
        if (!cliente.getCiudadResidencia().equals(propiedad.getCiudad())) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(hoy);
            calendar.add(Calendar.DAY_OF_YEAR, 5);
            Date minDate = calendar.getTime();

            if (fecha.before(minDate)) {
                throw new IllegalArgumentException("Para propiedades en otra ciudad, la visita debe ser al menos 5 días después");
            }
        }

        // Verificar que no haya conflicto de horario
        if (propiedad.hayConflictoHorario(fecha, horaInicio, duracion)) {
            throw new ConflictoHorarioException();
        }

        // Crear y guardar la agenda
        Agenda agenda = new Agenda(
                singleton.generarId(),
                propiedad,
                cliente,
                fecha,
                horaInicio,
                duracion
        );

        singleton.guardarAgenda(agenda);

        // Actualizar las relaciones
        cliente.agregarVisita(agenda);
        propiedad.agregarVisita(agenda);

        // Guardar los cambios
        singleton.escribirClientes();
        singleton.actualizarPropiedad(propiedad);

        return agenda;
    }

    /**
     * Registra una propiedad para que la inmobiliaria la gestione
     *
     * @param direccion Dirección de la propiedad
     * @param ciudad Ciudad de la propiedad
     * @param tipo Tipo de propiedad (venta o arriendo)
     * @param precio Precio de la propiedad
     * @param descripcion Descripción de la propiedad
     * @return Propiedad registrada
     * @throws UsuarioNoEncontradoException Si no hay sesión iniciada o el
     * usuario no es un cliente
     */
    public Propiedad registrarPropiedad(String direccion, String ciudad, String tipo,
            double precio, String descripcion) throws UsuarioNoEncontradoException {

        Cliente cliente = getClienteActual();

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
                cliente
        );

        // Guardar la propiedad
        singleton.guardarPropiedad(propiedad);

        // Actualizar relaciones
        cliente.agregarPropiedad(propiedad);
        singleton.escribirClientes();

        return propiedad;
    }

    /**
     * Envía un mensaje sobre una propiedad
     *
     * @param propiedad Propiedad sobre la que se consulta
     * @param contenido Contenido del mensaje
     * @return Mensaje enviado
     * @throws MensajePendienteException Si ya hay un mensaje pendiente de
     * respuesta
     * @throws UsuarioNoEncontradoException Si no hay sesión iniciada o el
     * usuario no es un cliente
     */
    public Mensaje enviarMensaje(Propiedad propiedad, String contenido)
            throws MensajePendienteException, UsuarioNoEncontradoException {

        Cliente cliente = getClienteActual();

        // Verificar que la propiedad tenga un encargado
        if (propiedad.getEncargado() == null) {
            throw new IllegalArgumentException("La propiedad no tiene un encargado asignado");
        }

        // Verificar que no haya un mensaje pendiente de respuesta
        ListaEnlazada<Mensaje> mensajes = propiedad.getMensajes();
        for (int i = 0; i < mensajes.size(); i++) {
            Mensaje mensaje = mensajes.get(i);
            if (mensaje.getRemitente().equals(cliente.getEmail()) && !mensaje.isRespondido()) {
                throw new MensajePendienteException();
            }
        }

        // Crear y guardar el mensaje
        Mensaje mensaje = new Mensaje(
                singleton.generarId(),
                propiedad,
                cliente.getEmail(),
                propiedad.getEncargado().getEmail(),
                contenido
        );

        singleton.guardarMensaje(mensaje);

        // Actualizar relaciones
        propiedad.agregarMensaje(mensaje);
        singleton.actualizarPropiedad(propiedad);

        return mensaje;
    }

    /**
     * Compra o arrienda una propiedad
     *
     * @param propiedad Propiedad a comprar o arrendar
     * @throws PropiedadNoDisponibleException Si la propiedad no está disponible
     * @throws UsuarioNoEncontradoException Si no hay sesión iniciada o el
     * usuario no es un cliente
     */
    public void comprarArrendar(Propiedad propiedad)
            throws PropiedadNoDisponibleException, UsuarioNoEncontradoException {

        Cliente cliente = getClienteActual();

        // Verificar que la propiedad esté disponible
        if (!propiedad.getEstado().equals(Propiedad.ESTADO_DISPONIBLE)) {
            throw new PropiedadNoDisponibleException();
        }

        // Actualizar estado según el tipo de propiedad
        if (propiedad.getTipo().equals(Propiedad.TIPO_VENTA)) {
            propiedad.setEstado(Propiedad.ESTADO_VENDIDA);
        } else {
            propiedad.setEstado(Propiedad.ESTADO_ARRENDADA);
        }

        // Guardar los cambios
        singleton.actualizarPropiedad(propiedad);
    }

    /**
     * Obtiene las propiedades propias del cliente actual
     *
     * @return Lista de propiedades propias
     * @throws UsuarioNoEncontradoException Si no hay sesión iniciada o el
     * usuario no es un cliente
     */
    public ListaEnlazada<Propiedad> getPropiedadesPropias() throws UsuarioNoEncontradoException {
        Cliente cliente = getClienteActual();

        // Buscar propiedades del cliente entre todas las propiedades
        ListaEnlazada<Propiedad> propiedades = singleton.getListaPropiedades();
        ListaEnlazada<Propiedad> propias = new ListaEnlazada<>();

        for (int i = 0; i < propiedades.size(); i++) {
            Propiedad propiedad = propiedades.get(i);
            if (propiedad.getPropietario() != null
                    && propiedad.getPropietario().getEmail().equals(cliente.getEmail())) {
                propias.add(propiedad);
            }
        }

        return propias;
    }

    /**
     * Obtiene las visitas agendadas por el cliente actual
     *
     * @return Lista de visitas agendadas
     * @throws UsuarioNoEncontradoException Si no hay sesión iniciada o el
     * usuario no es un cliente
     */
    public ListaEnlazada<Agenda> getVisitasAgendadas() throws UsuarioNoEncontradoException {
        Cliente cliente = getClienteActual();
        return singleton.buscarAgendasPorCliente(cliente);
    }

    /**
     * Obtiene los mensajes enviados por el cliente actual y sus respuestas
     *
     * @param propiedad Propiedad sobre la que se consultaron los mensajes
     * @return Lista de mensajes
     * @throws UsuarioNoEncontradoException Si no hay sesión iniciada o el
     * usuario no es un cliente
     */
    public ListaEnlazada<Mensaje> getConversacion(Propiedad propiedad) throws UsuarioNoEncontradoException {
        Cliente cliente = getClienteActual();
        ListaEnlazada<Mensaje> mensajes = propiedad.getMensajes();
        ListaEnlazada<Mensaje> conversacion = new ListaEnlazada<>();

        for (int i = 0; i < mensajes.size(); i++) {
            Mensaje mensaje = mensajes.get(i);
            if (mensaje.getRemitente().equals(cliente.getEmail())
                    || mensaje.getDestinatario().equals(cliente.getEmail())) {
                conversacion.add(mensaje);
            }
        }

        return conversacion;
    }
}
