/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

import excepciones.MensajePendienteException;
import modelo.Mensaje;
import modelo.Propiedad;
import singleton.Singleton;
import util.ListaEnlazada;

/**
 * Controlador para gestionar los mensajes del sistema
 */
public class ControladorMensaje {

    private final Singleton singleton;

    public ControladorMensaje() {
        this.singleton = Singleton.getInstancia();
    }

    /**
     * Obtiene todos los mensajes
     *
     * @return Lista de todos los mensajes
     */
    public ListaEnlazada<Mensaje> getListaMensajes() {
        return singleton.getListaMensajes();
    }

    /**
     * Busca mensajes por propiedad
     *
     * @param propiedad Propiedad de los mensajes
     * @return Lista de mensajes para esa propiedad
     */
    public ListaEnlazada<Mensaje> buscarPorPropiedad(Propiedad propiedad) {
        ListaEnlazada<Mensaje> mensajes = singleton.getListaMensajes();
        ListaEnlazada<Mensaje> resultado = new ListaEnlazada<>();

        for (int i = 0; i < mensajes.size(); i++) {
            Mensaje mensaje = mensajes.get(i);
            if (mensaje.getPropiedad().getId().equals(propiedad.getId())) {
                resultado.add(mensaje);
            }
        }

        return resultado;
    }

    /**
     * Busca mensajes por remitente
     *
     * @param remitente Email del remitente
     * @return Lista de mensajes de ese remitente
     */
    public ListaEnlazada<Mensaje> buscarPorRemitente(String remitente) {
        ListaEnlazada<Mensaje> mensajes = singleton.getListaMensajes();
        ListaEnlazada<Mensaje> resultado = new ListaEnlazada<>();

        for (int i = 0; i < mensajes.size(); i++) {
            Mensaje mensaje = mensajes.get(i);
            if (mensaje.getRemitente().equals(remitente)) {
                resultado.add(mensaje);
            }
        }

        return resultado;
    }

    /**
     * Busca mensajes por destinatario
     *
     * @param destinatario Email del destinatario
     * @return Lista de mensajes para ese destinatario
     */
    public ListaEnlazada<Mensaje> buscarPorDestinatario(String destinatario) {
        ListaEnlazada<Mensaje> mensajes = singleton.getListaMensajes();
        ListaEnlazada<Mensaje> resultado = new ListaEnlazada<>();

        for (int i = 0; i < mensajes.size(); i++) {
            Mensaje mensaje = mensajes.get(i);
            if (mensaje.getDestinatario().equals(destinatario)) {
                resultado.add(mensaje);
            }
        }

        return resultado;
    }

    /**
     * Busca mensajes no respondidos
     *
     * @return Lista de mensajes no respondidos
     */
    public ListaEnlazada<Mensaje> buscarNoRespondidos() {
        ListaEnlazada<Mensaje> mensajes = singleton.getListaMensajes();
        ListaEnlazada<Mensaje> resultado = new ListaEnlazada<>();

        for (int i = 0; i < mensajes.size(); i++) {
            Mensaje mensaje = mensajes.get(i);
            if (!mensaje.isRespondido()) {
                resultado.add(mensaje);
            }
        }

        return resultado;
    }

    /**
     * Crea un nuevo mensaje
     *
     * @param propiedad Propiedad sobre la que se consulta
     * @param remitente Email del remitente
     * @param destinatario Email del destinatario
     * @param contenido Contenido del mensaje
     * @return Mensaje creado
     */
    public Mensaje crearMensaje(Propiedad propiedad, String remitente, String destinatario, String contenido) {
        // Crear y guardar el mensaje
        Mensaje mensaje = new Mensaje(
                singleton.generarId(),
                propiedad,
                remitente,
                destinatario,
                contenido
        );

        singleton.guardarMensaje(mensaje);

        // Actualizar relaciones
        propiedad.agregarMensaje(mensaje);
        singleton.actualizarPropiedad(propiedad);

        return mensaje;
    }

    /**
     * Verifica si un cliente ya tiene un mensaje pendiente de respuesta para
     * una propiedad
     *
     * @param propiedad Propiedad a verificar
     * @param clienteEmail Email del cliente
     * @return true si ya tiene un mensaje pendiente, false si no
     */
    public boolean tieneMensajePendiente(Propiedad propiedad, String clienteEmail) throws MensajePendienteException {
        ListaEnlazada<Mensaje> mensajes = propiedad.getMensajes();

        for (int i = 0; i < mensajes.size(); i++) {
            Mensaje mensaje = mensajes.get(i);
            if (mensaje.getRemitente().equals(clienteEmail) && !mensaje.isRespondido()) {
                return true;
            }
        }

        return false;
    }

    /**
     * Marca un mensaje como respondido
     *
     * @param mensaje Mensaje a marcar
     */
    public void marcarRespondido(Mensaje mensaje) {
        mensaje.setRespondido(true);
        singleton.escribirMensajes();
    }

    /**
     * Responde a un mensaje
     *
     * @param mensajeOriginal Mensaje original
     * @param contenido Contenido de la respuesta
     * @return Mensaje de respuesta
     */
    public Mensaje responderMensaje(Mensaje mensajeOriginal, String contenido) {
        // Marcar el mensaje original como respondido
        mensajeOriginal.setRespondido(true);
        singleton.escribirMensajes();

        // Crear y guardar la respuesta
        Mensaje respuesta = new Mensaje(
                singleton.generarId(),
                mensajeOriginal.getPropiedad(),
                mensajeOriginal.getDestinatario(),
                mensajeOriginal.getRemitente(),
                contenido
        );

        singleton.guardarMensaje(respuesta);

        // Actualizar relaciones
        mensajeOriginal.getPropiedad().agregarMensaje(respuesta);
        singleton.actualizarPropiedad(mensajeOriginal.getPropiedad());

        return respuesta;
    }

    /**
     * Obtiene la conversación entre dos usuarios sobre una propiedad
     *
     * @param propiedad Propiedad de la conversación
     * @param usuario1 Email del primer usuario
     * @param usuario2 Email del segundo usuario
     * @return Lista de mensajes ordenados cronológicamente
     */
    public ListaEnlazada<Mensaje> getConversacion(Propiedad propiedad, String usuario1, String usuario2) {
        ListaEnlazada<Mensaje> mensajes = propiedad.getMensajes();
        ListaEnlazada<Mensaje> conversacion = new ListaEnlazada<>();

        for (int i = 0; i < mensajes.size(); i++) {
            Mensaje mensaje = mensajes.get(i);
            // Incluir mensajes entre los dos usuarios
            if ((mensaje.getRemitente().equals(usuario1) && mensaje.getDestinatario().equals(usuario2))
                    || (mensaje.getRemitente().equals(usuario2) && mensaje.getDestinatario().equals(usuario1))) {
                conversacion.add(mensaje);
            }
        }

        return conversacion;
    }
}
