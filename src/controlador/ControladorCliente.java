package controlador;

import dao.AgendaDAO;
import dao.ClienteDAO;
import dao.MensajeDAO;
import dao.PropiedadDAO;
import excepciones.ConflictoHorarioException;
import excepciones.MensajePendienteException;
import excepciones.UsuarioExistenteException;
import excepciones.UsuarioNoEncontradoException;
import excepciones.VisitasExcedidasException;
import modelo.Agenda;
import modelo.Cliente;
import modelo.Mensaje;
import modelo.Propiedad;
import singleton.Singleton;
import util.implementacion.ListaEnlazada;

import java.util.Date;

public class ControladorCliente {
    
    private ClienteDAO clienteDAO;
    private PropiedadDAO propiedadDAO;
    private AgendaDAO agendaDAO;
    private MensajeDAO mensajeDAO;
    
    public ControladorCliente(ClienteDAO clienteDAO, PropiedadDAO propiedadDAO, 
                             AgendaDAO agendaDAO, MensajeDAO mensajeDAO) {
        this.clienteDAO = clienteDAO;
        this.propiedadDAO = propiedadDAO;
        this.agendaDAO = agendaDAO;
        this.mensajeDAO = mensajeDAO;
    }
    
    /**
     * Busca propiedades disponibles con filtros opcionales
     */
    public ListaEnlazada<Propiedad> buscarPropiedades(String tipo, double precioMin, double precioMax, String ciudad) {
        ListaEnlazada<Propiedad> propiedades = propiedadDAO.listarTodas();
        ListaEnlazada<Propiedad> resultado = new ListaEnlazada<>();
        
        for (int i = 0; i < propiedades.size(); i++) {
            Propiedad p = propiedades.get(i);
            boolean cumpleTipo = tipo == null || tipo.isEmpty() || p.getTipo().equals(tipo);
            boolean cumplePrecio = p.getPrecio() >= precioMin && (precioMax <= 0 || p.getPrecio() <= precioMax);
            boolean cumpleCiudad = ciudad == null || ciudad.isEmpty() || p.getCiudad().equals(ciudad);
            
            if (cumpleTipo && cumplePrecio && cumpleCiudad && p.getEstado().equals(Propiedad.ESTADO_DISPONIBLE)) {
                resultado.add(p);
            }
        }
        
        return resultado;
    }
    
    /**
     * Agenda una visita a una propiedad
     */
    public void agendarVisita(String idPropiedad, String emailCliente, Date fecha, int horaInicio, int duracion) 
            throws UsuarioNoEncontradoException, ConflictoHorarioException, VisitasExcedidasException {
        
        Propiedad propiedad = propiedadDAO.buscarPorId(idPropiedad);
        Cliente cliente = clienteDAO.buscarPorEmail(emailCliente);
        
        if (propiedad == null) {
            throw new RuntimeException("La propiedad no existe");
        }
        
        if (!propiedad.isPermiteAgendarVisita()) {
            throw new RuntimeException("La propiedad no permite agendamiento de visitas");
        }
        
        // Validar si el inmueble está en otra ciudad y la visita es antes de 5 días
        if (!cliente.getCiudadResidencia().equals(propiedad.getCiudad())) {
            Date hoy = new Date();
            long diffTime = fecha.getTime() - hoy.getTime();
            long diffDays = diffTime / (1000 * 60 * 60 * 24);
            
            if (diffDays < 5) {
                throw new RuntimeException("Para propiedades en otra ciudad, la visita debe ser al menos 5 días después");
            }
        }
        
        // Crear y guardar la agenda
        String id = "AGD" + System.currentTimeMillis();
        Agenda agenda = new Agenda(id, propiedad, cliente, fecha, horaInicio, duracion);
        agendaDAO.guardar(agenda);
    }
    
    /**
     * Envía un mensaje sobre una propiedad
     */
    public void enviarMensaje(String idPropiedad, String emailCliente, String contenido) 
            throws UsuarioNoEncontradoException, MensajePendienteException {
        
        Propiedad propiedad = propiedadDAO.buscarPorId(idPropiedad);
        Cliente cliente = clienteDAO.buscarPorEmail(emailCliente);
        
        if (propiedad == null) {
            throw new RuntimeException("La propiedad no existe");
        }
        
        if (propiedad.getEncargado() == null) {
            throw new RuntimeException("La propiedad no tiene un encargado asignado");
        }
        
        // Verificar si ya tiene un mensaje pendiente
        if (mensajeDAO.tieneMensajePendiente(cliente, propiedad)) {
            throw new MensajePendienteException();
        }
        
        // Crear y guardar el mensaje
        String id = "MSG" + System.currentTimeMillis();
        Mensaje mensaje = new Mensaje(id, propiedad, cliente.getEmail(), propiedad.getEncargado().getEmail(), contenido);
        mensajeDAO.guardar(mensaje);
    }
    
    /**
     * Registra una propiedad para ser ofrecida por la inmobiliaria
     */
    public void registrarPropiedadParaVenta(Propiedad propiedad, String emailCliente) 
            throws UsuarioNoEncontradoException {
        
        Cliente cliente = clienteDAO.buscarPorEmail(emailCliente);
        
        // Establecer el estado como pendiente de asignación
        propiedad.setEstado(Propiedad.ESTADO_PENDIENTE_ASIGNACION);
        propiedad.setPropietario(cliente);
        
        // Guardar la propiedad
        propiedadDAO.guardar(propiedad);
        
        // Agregar la propiedad al cliente
        clienteDAO.agregarPropiedad(cliente, propiedad);
        
        // Agregar a la cola de propiedades pendientes
        Singleton.getINSTANCIA().getColaPropiedades().enQueue(propiedad);
    }
    
    /**
     * Realiza la compra o arrendamiento de una propiedad
     */
    public void comprarArrendar(String idPropiedad, String emailCliente, String tipo) 
            throws UsuarioNoEncontradoException {
        
        Propiedad propiedad = propiedadDAO.buscarPorId(idPropiedad);
        Cliente cliente = clienteDAO.buscarPorEmail(emailCliente);
        
        if (propiedad == null) {
            throw new RuntimeException("La propiedad no existe");
        }
        
        if (!propiedad.getEstado().equals(Propiedad.ESTADO_DISPONIBLE)) {
            throw new RuntimeException("La propiedad no está disponible");
        }
        
        if (tipo.equals(Propiedad.TIPO_VENTA)) {
            propiedadDAO.cambiarEstado(propiedad, Propiedad.ESTADO_VENDIDA);
        } else {
            propiedadDAO.cambiarEstado(propiedad, Propiedad.ESTADO_ARRENDADA);
        }
        
        // Cancelar todas las agendas pendientes
        ListaEnlazada<Agenda> visitas = propiedad.getVisitas();
        for (int i = 0; i < visitas.size(); i++) {
            Agenda visita = visitas.get(i);
            if (!visita.isCompletada() && !visita.isCancelada()) {
                agendaDAO.cancelar(visita);
            }
        }
    }
    
    /**
     * Obtiene las propiedades del cliente
     */
    public ListaEnlazada<Propiedad> obtenerPropiedadesCliente(String emailCliente) 
            throws UsuarioNoEncontradoException {
        return clienteDAO.listarPropiedades(emailCliente);
    }
}