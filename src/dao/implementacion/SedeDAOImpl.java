package dao.implementacion;

import dao.SedeDAO;
import excepciones.SedeExistenteException;
import modelo.Empleado;
import modelo.MatrizSedes;
import modelo.Propiedad;
import modelo.Sede;
import singleton.Singleton;
import util.implementacion.ListaEnlazada;

public class SedeDAOImpl implements SedeDAO {

    private ListaEnlazada<Sede> listaSedes;
    private MatrizSedes matrizSedes;

    public SedeDAOImpl() {
        this.listaSedes = Singleton.getINSTANCIA().getListaSedes();
        this.matrizSedes = Singleton.getINSTANCIA().getMatrizSedes();
    }

    @Override
    public void guardar(Sede sede) throws SedeExistenteException {
        // Verificar que no exista una sede en la misma ciudad
        if (buscarPorCiudad(sede.getCiudad()) != null) {
            throw new SedeExistenteException(sede.getCiudad());
        }

        listaSedes.add(sede);
        Singleton.getINSTANCIA().escribirSedes();
    }

    @Override
    public void actualizar(Sede sede) {
        // Buscar la sede por ciudad y actualizarla
        for (int i = 0; i < listaSedes.size(); i++) {
            if (listaSedes.get(i).getCiudad().equals(sede.getCiudad())) {
                listaSedes.remove(i);
                listaSedes.add(sede, i);
                Singleton.getINSTANCIA().escribirSedes();
                return;
            }
        }
    }

    @Override
    public void eliminar(String ciudad) {
        // Eliminar la sede por ciudad
        for (int i = 0; i < listaSedes.size(); i++) {
            if (listaSedes.get(i).getCiudad().equals(ciudad)) {
                // Verificar que no tenga empleados asignados
                if (listaSedes.get(i).getEmpleados().size() > 0) {
                    throw new RuntimeException("No se puede eliminar la sede porque tiene empleados asignados");
                }
                // Verificar que no tenga propiedades asignadas
                if (listaSedes.get(i).getPropiedades().size() > 0) {
                    throw new RuntimeException("No se puede eliminar la sede porque tiene propiedades asignadas");
                }

                // Eliminar de la matriz de sedes
                int[] posicion = matrizSedes.getPosicionSede(ciudad);
                if (posicion != null) {
                    matrizSedes.removerSede(posicion[0], posicion[1]);
                    Singleton.getINSTANCIA().escribirMatrizSedes();
                }

                listaSedes.remove(i);
                Singleton.getINSTANCIA().escribirSedes();
                return;
            }
        }
    }

    @Override
    public Sede buscarPorCiudad(String ciudad) {
        // Buscar sede por ciudad
        for (int i = 0; i < listaSedes.size(); i++) {
            if (listaSedes.get(i).getCiudad().equals(ciudad)) {
                return listaSedes.get(i);
            }
        }
        return null; // No se encontró la sede
    }

    @Override
    public ListaEnlazada<Sede> listarTodas() {
        return listaSedes;
    }

    @Override
    public void asignarEmpleado(Sede sede, Empleado empleado) {
        // Verificar que el empleado no esté asignado a otra sede
        for (int i = 0; i < listaSedes.size(); i++) {
            Sede s = listaSedes.get(i);
            if (!s.getCiudad().equals(sede.getCiudad())) { // No verificar la sede actual
                ListaEnlazada<Empleado> empleadosSede = s.getEmpleados();

                for (int j = 0; j < empleadosSede.size(); j++) {
                    if (empleadosSede.get(j).getEmail().equals(empleado.getEmail())) {
                        throw new RuntimeException("El empleado ya está asignado a otra sede: " + s.getCiudad());
                    }
                }
            }
        }

        // Asignar empleado a la sede
        empleado.setSede(sede);
        sede.agregarEmpleado(empleado);

        // Actualizar en singleton
        actualizar(sede);
        Singleton.getINSTANCIA().escribirEmpleados();
    }

    @Override
    public void asignarPropiedad(Sede sede, Propiedad propiedad) {
        // Verificar si la sede tiene cupo
        if (!sede.tieneCupo()) {
            throw new RuntimeException("La sede " + sede.getCiudad() + " ha alcanzado su capacidad máxima de inmuebles");
        }

        // Asignar propiedad a la sede
        sede.agregarPropiedad(propiedad);
        propiedad.setSede(sede);

        // Actualizar en singleton
        actualizar(sede);
        Singleton.getINSTANCIA().escribirPropiedades();
    }

    @Override
    public ListaEnlazada<Empleado> listarEmpleadosPorSede(String ciudad) {
        Sede sede = buscarPorCiudad(ciudad);
        if (sede != null) {
            return sede.getEmpleados();
        }
        return new ListaEnlazada<>();
    }

    @Override
    public ListaEnlazada<Propiedad> listarPropiedadesPorSede(String ciudad) {
        Sede sede = buscarPorCiudad(ciudad);
        if (sede != null) {
            return sede.getPropiedades();
        }
        return new ListaEnlazada<>();
    }

    @Override
    public boolean existeSede(String ciudad) {
        return buscarPorCiudad(ciudad) != null;
    }
}
