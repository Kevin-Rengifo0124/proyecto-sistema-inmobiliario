package controlador;

import dao.EmpleadoDAO;
import dao.PropiedadDAO;
import dao.SedeDAO;
import excepciones.CapacidadExcedidaException;
import excepciones.EmpleadoIncompatibleException;
import excepciones.UsuarioExistenteException;
import excepciones.UsuarioNoEncontradoException;
import modelo.Agenda;
import modelo.Empleado;
import modelo.Propiedad;
import modelo.Sede;
import util.implementacion.ListaEnlazada;

import java.util.Date;

public class ControladorEmpleado {

    private EmpleadoDAO empleadoDAO;
    private SedeDAO sedeDAO;
    private PropiedadDAO propiedadDAO;

    public ControladorEmpleado(EmpleadoDAO empleadoDAO, SedeDAO sedeDAO, PropiedadDAO propiedadDAO) {
        this.empleadoDAO = empleadoDAO;
        this.sedeDAO = sedeDAO;
        this.propiedadDAO = propiedadDAO;
    }

    /**
     * Obtiene las propiedades asignadas al empleado actual
     */
    public ListaEnlazada<Propiedad> obtenerPropiedadesAsignadas(String emailEmpleado)
            throws UsuarioNoEncontradoException {
        return empleadoDAO.listarPropiedadesAsignadas(emailEmpleado);
    }

    /**
     * Habilita o deshabilita la opción de agendar visitas para una propiedad
     */
    public void habilitarAgendarVisitas(String idPropiedad, boolean habilitar) {
        Propiedad propiedad = propiedadDAO.buscarPorId(idPropiedad);
        if (propiedad != null) {
            propiedad.setPermiteAgendarVisita(habilitar);
            try {
                propiedadDAO.actualizar(propiedad);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Obtiene las visitas agendadas para una propiedad
     */
    public ListaEnlazada<Agenda> obtenerVisitasPropiedad(String idPropiedad) {
        Propiedad propiedad = propiedadDAO.buscarPorId(idPropiedad);
        if (propiedad != null) {
            return propiedad.getVisitas();
        }
        return new ListaEnlazada<>();
    }

    /**
     * Filtra las visitas por fecha
     */
    public ListaEnlazada<Agenda> filtrarVisitasPorFecha(ListaEnlazada<Agenda> visitas, Date fecha) {
        ListaEnlazada<Agenda> resultado = new ListaEnlazada<>();
        for (int i = 0; i < visitas.size(); i++) {
            Agenda visita = visitas.get(i);
            if (visita.getFecha().equals(fecha)) {
                resultado.add(visita);
            }
        }
        return resultado;
    }

    /**
     * Marca una propiedad como vendida o arrendada
     */
    public void cerrarPropiedad(String idPropiedad, String nuevoEstado) {
        Propiedad propiedad = propiedadDAO.buscarPorId(idPropiedad);
        if (propiedad != null) {
            propiedadDAO.cambiarEstado(propiedad, nuevoEstado);
        }
    }

    /**
     * Obtiene propiedades activas del empleado
     */
    public ListaEnlazada<Propiedad> obtenerPropiedadesActivas(String emailEmpleado)
            throws UsuarioNoEncontradoException {
        ListaEnlazada<Propiedad> propiedades = empleadoDAO.listarPropiedadesAsignadas(emailEmpleado);
        ListaEnlazada<Propiedad> activas = new ListaEnlazada<>();

        for (int i = 0; i < propiedades.size(); i++) {
            Propiedad p = propiedades.get(i);
            if (p.getEstado().equals(Propiedad.ESTADO_DISPONIBLE)) {
                activas.add(p);
            }
        }

        return activas;
    }

    /**
     * Obtiene propiedades cerradas (vendidas/arrendadas) del empleado
     */
    public ListaEnlazada<Propiedad> obtenerPropiedadesCerradas(String emailEmpleado)
            throws UsuarioNoEncontradoException {
        ListaEnlazada<Propiedad> propiedades = empleadoDAO.listarPropiedadesAsignadas(emailEmpleado);
        ListaEnlazada<Propiedad> cerradas = new ListaEnlazada<>();

        for (int i = 0; i < propiedades.size(); i++) {
            Propiedad p = propiedades.get(i);
            if (p.getEstado().equals(Propiedad.ESTADO_VENDIDA) || p.getEstado().equals(Propiedad.ESTADO_ARRENDADA)) {
                cerradas.add(p);
            }
        }

        return cerradas;
    }
}
