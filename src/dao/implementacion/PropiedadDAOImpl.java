package dao.implementacion;

import dao.PropiedadDAO;
import excepciones.EmpleadoIncompatibleException;
import excepciones.PropiedadNoDisponibleException;
import excepciones.UsuarioNoEncontradoException;
import modelo.Cliente;
import modelo.Empleado;
import modelo.Propiedad;
import modelo.Sede;
import singleton.Singleton;
import util.implementacion.ListaEnlazada;

public class PropiedadDAOImpl implements PropiedadDAO {

    private ListaEnlazada<Propiedad> listaPropiedades;

    public PropiedadDAOImpl() {
        this.listaPropiedades = Singleton.getINSTANCIA().getListaPropiedades();
    }

    @Override
    public void guardar(Propiedad propiedad) {
        listaPropiedades.add(propiedad);
        Singleton.getINSTANCIA().escribirPropiedades();
    }

    @Override
    public void actualizar(Propiedad propiedad) throws PropiedadNoDisponibleException {
        int index = -1;
        for (int i = 0; i < listaPropiedades.size(); i++) {
            if (listaPropiedades.get(i).getId().equals(propiedad.getId())) {
                index = i;
                break;
            }
        }

        if (index != -1) {
            listaPropiedades.remove(index);
            listaPropiedades.add(propiedad, index);
            Singleton.getINSTANCIA().escribirPropiedades();
        } else {
            throw new PropiedadNoDisponibleException("No se encontró la propiedad con ID: " + propiedad.getId());
        }
    }

    @Override
    public void eliminar(String id) {
        for (int i = 0; i < listaPropiedades.size(); i++) {
            if (listaPropiedades.get(i).getId().equals(id)) {
                listaPropiedades.remove(i);
                Singleton.getINSTANCIA().escribirPropiedades();
                return;
            }
        }
    }

    @Override
    public Propiedad buscarPorId(String id) {
        for (int i = 0; i < listaPropiedades.size(); i++) {
            if (listaPropiedades.get(i).getId().equals(id)) {
                return listaPropiedades.get(i);
            }
        }
        return null;
    }

    @Override
    public ListaEnlazada<Propiedad> listarTodas() {
        return listaPropiedades;
    }

    @Override
    public ListaEnlazada<Propiedad> listarPorEstado(String estado) {
        ListaEnlazada<Propiedad> resultado = new ListaEnlazada<>();
        for (int i = 0; i < listaPropiedades.size(); i++) {
            Propiedad p = listaPropiedades.get(i);
            if (p.getEstado().equals(estado)) {
                resultado.add(p);
            }
        }
        return resultado;
    }

    @Override
    public ListaEnlazada<Propiedad> listarPorTipo(String tipo) {
        ListaEnlazada<Propiedad> resultado = new ListaEnlazada<>();
        for (int i = 0; i < listaPropiedades.size(); i++) {
            Propiedad p = listaPropiedades.get(i);
            if (p.getTipo().equals(tipo)) {
                resultado.add(p);
            }
        }
        return resultado;
    }

    @Override
    public ListaEnlazada<Propiedad> listarPorCiudad(String ciudad) {
        ListaEnlazada<Propiedad> resultado = new ListaEnlazada<>();
        for (int i = 0; i < listaPropiedades.size(); i++) {
            Propiedad p = listaPropiedades.get(i);
            if (p.getCiudad().equals(ciudad)) {
                resultado.add(p);
            }
        }
        return resultado;
    }

    @Override
    public ListaEnlazada<Propiedad> listarPorRangoPrecio(double precioMin, double precioMax) {
        ListaEnlazada<Propiedad> resultado = new ListaEnlazada<>();
        for (int i = 0; i < listaPropiedades.size(); i++) {
            Propiedad p = listaPropiedades.get(i);
            if (p.getPrecio() >= precioMin && (precioMax <= 0 || p.getPrecio() <= precioMax)) {
                resultado.add(p);
            }
        }
        return resultado;
    }

    @Override
    public ListaEnlazada<Propiedad> listarPorEmpleado(String emailEmpleado) throws UsuarioNoEncontradoException {
        ListaEnlazada<Propiedad> resultado = new ListaEnlazada<>();
        for (int i = 0; i < listaPropiedades.size(); i++) {
            Propiedad p = listaPropiedades.get(i);
            if (p.getEncargado() != null && p.getEncargado().getEmail().equals(emailEmpleado)) {
                resultado.add(p);
            }
        }

        if (resultado.isEmpty()) {
            throw new UsuarioNoEncontradoException("No se encontraron propiedades para el empleado: " + emailEmpleado);
        }

        return resultado;
    }

    @Override
    public ListaEnlazada<Propiedad> listarPorPropietario(String emailCliente) throws UsuarioNoEncontradoException {
        ListaEnlazada<Propiedad> resultado = new ListaEnlazada<>();
        for (int i = 0; i < listaPropiedades.size(); i++) {
            Propiedad p = listaPropiedades.get(i);
            if (p.getPropietario() != null && p.getPropietario().getEmail().equals(emailCliente)) {
                resultado.add(p);
            }
        }

        if (resultado.isEmpty()) {
            throw new UsuarioNoEncontradoException("No se encontraron propiedades para el cliente: " + emailCliente);
        }

        return resultado;
    }

    @Override
    public void asignarEmpleado(Propiedad propiedad, Empleado empleado) throws EmpleadoIncompatibleException {
        // Verificar si el empleado puede administrar este tipo de propiedad
        if (!empleado.validarTipoPropiedad(propiedad)) {
            throw new EmpleadoIncompatibleException(propiedad.getTipo());
        }

        propiedad.setEncargado(empleado);
        empleado.asignarPropiedad(propiedad);

        try {
            actualizar(propiedad);
        } catch (PropiedadNoDisponibleException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void asignarSede(Propiedad propiedad, Sede sede) {
        propiedad.setSede(sede);
        try {
            actualizar(propiedad);
        } catch (PropiedadNoDisponibleException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void cambiarEstado(Propiedad propiedad, String nuevoEstado) {
        propiedad.setEstado(nuevoEstado);
        try {
            actualizar(propiedad);
        } catch (PropiedadNoDisponibleException e) {
            e.printStackTrace();
        }
    }
}
