/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

import excepciones.ConflictoHorarioException;
import java.util.Date;
import modelo.Agenda;
import modelo.Cliente;
import modelo.Propiedad;
import singleton.Singleton;
import util.ListaEnlazada;

/**
 * Controlador para gestionar las agendas de visitas
 */
public class ControladorAgenda {

    private final Singleton singleton;

    public ControladorAgenda() {
        this.singleton = Singleton.getInstancia();
    }

    /**
     * Obtiene todas las agendas
     *
     * @return Lista de todas las agendas
     */
    public ListaEnlazada<Agenda> getListaAgendas() {
        return singleton.getListaAgendas();
    }

    /**
     * Busca agendas por propiedad
     *
     * @param propiedad Propiedad de las agendas
     * @return Lista de agendas para esa propiedad
     */
    public ListaEnlazada<Agenda> buscarPorPropiedad(Propiedad propiedad) {
        ListaEnlazada<Agenda> agendas = singleton.getListaAgendas();
        ListaEnlazada<Agenda> resultado = new ListaEnlazada<>();

        for (int i = 0; i < agendas.size(); i++) {
            Agenda agenda = agendas.get(i);
            if (agenda.getPropiedad().getId().equals(propiedad.getId())) {
                resultado.add(agenda);
            }
        }

        return resultado;
    }

    /**
     * Busca agendas por cliente
     *
     * @param cliente Cliente de las agendas
     * @return Lista de agendas de ese cliente
     */
    public ListaEnlazada<Agenda> buscarPorCliente(Cliente cliente) {
        return singleton.buscarAgendasPorCliente(cliente);
    }

    /**
     * Busca agendas por fecha
     *
     * @param fecha Fecha de las agendas
     * @return Lista de agendas para esa fecha
     */
    public ListaEnlazada<Agenda> buscarPorFecha(Date fecha) {
        ListaEnlazada<Agenda> agendas = singleton.getListaAgendas();
        ListaEnlazada<Agenda> resultado = new ListaEnlazada<>();

        for (int i = 0; i < agendas.size(); i++) {
            Agenda agenda = agendas.get(i);
            if (agenda.getFecha().equals(fecha)) {
                resultado.add(agenda);
            }
        }

        return resultado;
    }

    /**
     * Busca agendas activas (no completadas ni canceladas)
     *
     * @return Lista de agendas activas
     */
    public ListaEnlazada<Agenda> buscarAgendaActivas() {
        ListaEnlazada<Agenda> agendas = singleton.getListaAgendas();
        ListaEnlazada<Agenda> resultado = new ListaEnlazada<>();

        for (int i = 0; i < agendas.size(); i++) {
            Agenda agenda = agendas.get(i);
            if (!agenda.isCompletada() && !agenda.isCancelada()) {
                resultado.add(agenda);
            }
        }

        return resultado;
    }

    /**
     * Crea una nueva agenda de visita
     *
     * @param propiedad Propiedad a visitar
     * @param cliente Cliente que visita
     * @param fecha Fecha de la visita
     * @param horaInicio Hora de inicio (0-23)
     * @param duracion Duración en horas
     * @return Agenda creada
     * @throws ConflictoHorarioException Si hay un conflicto de horario con otra
     * visita
     */
    public Agenda crearAgenda(Propiedad propiedad, Cliente cliente, Date fecha,
            int horaInicio, int duracion) throws ConflictoHorarioException {

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
     * Marca una agenda como completada
     *
     * @param agenda Agenda a marcar
     */
    public void marcarCompletada(Agenda agenda) {
        agenda.setCompletada(true);
        singleton.escribirAgendas();
    }

    /**
     * Cancela una agenda
     *
     * @param agenda Agenda a cancelar
     */
    public void cancelarAgenda(Agenda agenda) {
        agenda.setCancelada(true);
        singleton.escribirAgendas();
    }

    /**
     * Verifica si una hora y duración son válidas para una visita
     *
     * @param horaInicio Hora de inicio (0-23)
     * @param duracion Duración en horas
     * @return true si son válidas, false si no
     */
    public boolean validarHorario(int horaInicio, int duracion) {
        // Verificar que la duración sea válida (1-3 horas)
        if (duracion < 1 || duracion > 3) {
            return false;
        }

        // Verificar que la hora de inicio sea válida (dentro de horario laboral)
        if (horaInicio < 8 || horaInicio > 16 || (horaInicio + duracion) > 17) {
            return false;
        }

        return true;
    }

    /**
     * Verifica si un cliente puede agendar más visitas
     *
     * @param cliente Cliente a verificar
     * @return true si puede agendar más visitas, false si no
     */
    public boolean puedeAgendarMasVisitas(Cliente cliente) {
        return cliente.puedeAgendarMasVisitas();
    }

    /**
     * Cancela todas las agendas de una propiedad
     *
     * @param propiedad Propiedad cuyas agendas serán canceladas
     */
    public void cancelarAgendasPropiedad(Propiedad propiedad) {
        ListaEnlazada<Agenda> agendas = buscarPorPropiedad(propiedad);

        for (int i = 0; i < agendas.size(); i++) {
            Agenda agenda = agendas.get(i);
            if (!agenda.isCompletada() && !agenda.isCancelada()) {
                agenda.setCancelada(true);
            }
        }

        singleton.escribirAgendas();
    }
}
