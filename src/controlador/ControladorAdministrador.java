package controlador;

import dao.EmpleadoDAO;
import dao.MatrizSedesDAO;
import dao.PropiedadDAO;
import dao.SedeDAO;
import excepciones.SedeExistenteException;
import excepciones.UsuarioExistenteException;
import excepciones.UsuarioNoEncontradoException;
import modelo.Empleado;
import modelo.MatrizSedes;
import modelo.Propiedad;
import modelo.Sede;
import singleton.Singleton;
import util.implementacion.ListaEnlazada;
import util.implementacion.Queue;

public class ControladorAdministrador {

    private SedeDAO sedeDAO;
    private EmpleadoDAO empleadoDAO;
    private MatrizSedesDAO matrizSedesDAO;
    private PropiedadDAO propiedadDAO;

    public ControladorAdministrador(SedeDAO sedeDAO, EmpleadoDAO empleadoDAO,
            MatrizSedesDAO matrizSedesDAO, PropiedadDAO propiedadDAO) {
        this.sedeDAO = sedeDAO;
        this.empleadoDAO = empleadoDAO;
        this.matrizSedesDAO = matrizSedesDAO;
        this.propiedadDAO = propiedadDAO;
    }

    /**
     * Registra una nueva sede en la matriz de sedes
     */
    public void registrarSede(Sede sede, int fila, int columna) throws SedeExistenteException {
        // Verificar que no exista una sede en la misma ciudad
        if (sedeDAO.existeSede(sede.getCiudad())) {
            throw new SedeExistenteException(sede.getCiudad());
        }

        // Registrar la sede en la matriz
        matrizSedesDAO.agregarSede(sede, fila, columna);

        // Guardar la sede en la lista
        sedeDAO.guardar(sede);
    }

    /**
     * Actualiza la capacidad de inmuebles de una sede
     */
    public void actualizarCapacidadSede(String ciudad, int nuevaCapacidad) {
        Sede sede = sedeDAO.buscarPorCiudad(ciudad);
        if (sede != null) {
            sede.setCapacidadInmuebles(nuevaCapacidad);
            sedeDAO.actualizar(sede);
        }
    }

    /**
     * Obtiene todas las sedes registradas
     */
    public ListaEnlazada<Sede> listarTodasSedes() {
        return sedeDAO.listarTodas();
    }

    /**
     * Obtiene la matriz de sedes
     */
    public MatrizSedes obtenerMatrizSedes() {
        return matrizSedesDAO.obtenerMatriz();
    }

    // Verificar que el empleado no esté asignado a otra sede (por email o cédula)
    public void registrarEmpleado(Empleado empleado, String ciudadSede)
            throws UsuarioExistenteException {

        // Verificar que la sede exista
        Sede sede = sedeDAO.buscarPorCiudad(ciudadSede);
        if (sede == null) {
            throw new RuntimeException("No existe una sede en la ciudad: " + ciudadSede);
        }

        // Verificar que el empleado no esté asignado a otra sede
        ListaEnlazada<Sede> sedes = sedeDAO.listarTodas();
        for (int i = 0; i < sedes.size(); i++) {
            Sede s = sedes.get(i);
            ListaEnlazada<Empleado> empleadosSede = s.getEmpleados();

            for (int j = 0; j < empleadosSede.size(); j++) {
                Empleado e = empleadosSede.get(j);
                if (e.getEmail().equals(empleado.getEmail())) {
                    throw new RuntimeException("El empleado ya está asignado a la sede: " + s.getCiudad() + " con el mismo email");
                }
                if (e.getCedula().equals(empleado.getCedula())) {
                    throw new RuntimeException("El empleado ya está asignado a la sede: " + s.getCiudad() + " con la misma cédula");
                }
            }
        }
        // Guardar el empleado
        empleadoDAO.guardar(empleado);

        // Asignar el empleado a la sede
        sedeDAO.asignarEmpleado(sede, empleado);
    }

    /**
     * Obtiene la siguiente propiedad pendiente de asignación
     */
    public Propiedad obtenerSiguientePropiedadPendiente() {
        Queue<Propiedad> colaPropiedades = Singleton.getINSTANCIA().getColaPropiedades();
        if (!colaPropiedades.isEmpty()) {
            return colaPropiedades.peek();
        }
        return null;
    }

    /**
     * Asigna una propiedad pendiente a un empleado y una sede
     */
    public void asignarPropiedadPendiente(String emailEmpleado, String ciudadSede)
            throws UsuarioNoEncontradoException {

        Queue<Propiedad> colaPropiedades = Singleton.getINSTANCIA().getColaPropiedades();
        if (colaPropiedades.isEmpty()) {
            throw new RuntimeException("No hay propiedades pendientes de asignación");
        }

        // Obtener el empleado y la sede
        Empleado empleado = empleadoDAO.buscarPorEmail(emailEmpleado);
        Sede sede = sedeDAO.buscarPorCiudad(ciudadSede);

        if (sede == null) {
            throw new RuntimeException("No existe una sede en la ciudad: " + ciudadSede);
        }

        // Verificar que el empleado pertenezca a la sede seleccionada
        if (!empleado.getSede().getCiudad().equals(ciudadSede)) {
            throw new RuntimeException("El empleado no pertenece a la sede seleccionada");
        }

        // Obtener la siguiente propiedad de la cola
        Propiedad propiedad = colaPropiedades.deQueue();
        propiedad.setEstado(Propiedad.ESTADO_DISPONIBLE);

        try {
            // Asignar la propiedad al empleado
            propiedadDAO.asignarEmpleado(propiedad, empleado);

            // Asignar la propiedad a la sede
            propiedadDAO.asignarSede(propiedad, sede);

        } catch (Exception e) {
            // Si hay algún error, volver a poner la propiedad en la cola
            colaPropiedades.enQueue(propiedad);
            throw new RuntimeException("Error al asignar la propiedad: " + e.getMessage());
        }
    }

    /**
     * Obtiene historial de propiedades vendidas o arrendadas
     */
    public ListaEnlazada<Propiedad> obtenerHistorialPropiedades(String filtroEmpleado, String filtroSede,
            boolean soloVendidas, boolean soloArrendadas) {

        ListaEnlazada<Propiedad> propiedades = propiedadDAO.listarTodas();
        ListaEnlazada<Propiedad> resultado = new ListaEnlazada<>();

        for (int i = 0; i < propiedades.size(); i++) {
            Propiedad p = propiedades.get(i);
            boolean estaVendidaOArrendada = p.getEstado().equals(Propiedad.ESTADO_VENDIDA)
                    || p.getEstado().equals(Propiedad.ESTADO_ARRENDADA);

            if (estaVendidaOArrendada) {
                boolean cumpleFiltroEmpleado = filtroEmpleado == null || filtroEmpleado.isEmpty()
                        || (p.getEncargado() != null && p.getEncargado().getEmail().equals(filtroEmpleado));

                boolean cumpleFiltroSede = filtroSede == null || filtroSede.isEmpty()
                        || (p.getSede() != null && p.getSede().getCiudad().equals(filtroSede));

                boolean cumpleFiltroVendidas = !soloVendidas || p.getEstado().equals(Propiedad.ESTADO_VENDIDA);

                boolean cumpleFiltroArrendadas = !soloArrendadas || p.getEstado().equals(Propiedad.ESTADO_ARRENDADA);

                if (cumpleFiltroEmpleado && cumpleFiltroSede && cumpleFiltroVendidas && cumpleFiltroArrendadas) {
                    resultado.add(p);
                }
            }
        }

        return resultado;
    }

    /**
     * Obtiene empleados por sede
     */
    public ListaEnlazada<Empleado> listarEmpleadosPorSede(String ciudad) {
        return sedeDAO.listarEmpleadosPorSede(ciudad);
    }
}
