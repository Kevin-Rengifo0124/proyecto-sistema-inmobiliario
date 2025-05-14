package dao.implementacion;

import dao.AgendaDAO;
import excepciones.ConflictoHorarioException;
import excepciones.VisitasExcedidasException;
import modelo.Agenda;
import modelo.Cliente;
import modelo.Propiedad;
import singleton.Singleton;
import util.implementacion.ListaEnlazada;

import java.util.Date;

public class AgendaDAOImpl implements AgendaDAO {

    private ListaEnlazada<Agenda> listaAgendas;

    public AgendaDAOImpl() {
        this.listaAgendas = Singleton.getINSTANCIA().getListaAgendas();
    }

    @Override
    public void guardar(Agenda agenda) throws ConflictoHorarioException, VisitasExcedidasException {
        // Verificar si hay conflicto de horario en la propiedad
        if (agenda.getPropiedad().hayConflictoHorario(agenda.getFecha(), agenda.getHoraInicio(), agenda.getDuracion())) {
            throw new ConflictoHorarioException();
        }

        // Verificar si el cliente ya tiene 2 visitas agendadas
        if (!agenda.getCliente().puedeAgendarMasVisitas()) {
            throw new VisitasExcedidasException();
        }

        listaAgendas.add(agenda);
        agenda.getPropiedad().agregarVisita(agenda);
        agenda.getCliente().agregarVisita(agenda);

        Singleton.getINSTANCIA().escribirAgendas();
        Singleton.getINSTANCIA().escribirPropiedades();
        Singleton.getINSTANCIA().escribirClientes();
    }

    @Override
    public void actualizar(Agenda agenda) {
        for (int i = 0; i < listaAgendas.size(); i++) {
            if (listaAgendas.get(i).getId().equals(agenda.getId())) {
                listaAgendas.remove(i);
                listaAgendas.add(agenda, i);
                Singleton.getINSTANCIA().escribirAgendas();
                return;
            }
        }
    }

    @Override
    public void eliminar(String id) {
        for (int i = 0; i < listaAgendas.size(); i++) {
            if (listaAgendas.get(i).getId().equals(id)) {
                listaAgendas.remove(i);
                Singleton.getINSTANCIA().escribirAgendas();
                return;
            }
        }
    }

    @Override
    public Agenda buscarPorId(String id) {
        for (int i = 0; i < listaAgendas.size(); i++) {
            if (listaAgendas.get(i).getId().equals(id)) {
                return listaAgendas.get(i);
            }
        }
        return null;
    }

    @Override
    public ListaEnlazada<Agenda> listarTodas() {
        return listaAgendas;
    }

    @Override
    public ListaEnlazada<Agenda> listarPorPropiedad(Propiedad propiedad) {
        ListaEnlazada<Agenda> resultado = new ListaEnlazada<>();
        for (int i = 0; i < listaAgendas.size(); i++) {
            Agenda a = listaAgendas.get(i);
            if (a.getPropiedad().getId().equals(propiedad.getId())) {
                resultado.add(a);
            }
        }
        return resultado;
    }

    @Override
    public ListaEnlazada<Agenda> listarPorCliente(Cliente cliente) {
        ListaEnlazada<Agenda> resultado = new ListaEnlazada<>();
        for (int i = 0; i < listaAgendas.size(); i++) {
            Agenda a = listaAgendas.get(i);
            if (a.getCliente().getEmail().equals(cliente.getEmail())) {
                resultado.add(a);
            }
        }
        return resultado;
    }

    @Override
    public ListaEnlazada<Agenda> listarPorFecha(Date fecha) {
        ListaEnlazada<Agenda> resultado = new ListaEnlazada<>();
        for (int i = 0; i < listaAgendas.size(); i++) {
            Agenda a = listaAgendas.get(i);
            if (a.getFecha().equals(fecha)) {
                resultado.add(a);
            }
        }
        return resultado;
    }

    @Override
    public void marcarCompletada(Agenda agenda) {
        agenda.setCompletada(true);
        actualizar(agenda);
    }

    @Override
    public void cancelar(Agenda agenda) {
        agenda.setCancelada(true);
        actualizar(agenda);
    }
}
