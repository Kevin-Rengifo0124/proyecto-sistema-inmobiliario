package dao.implementacion;

import dao.EmpleadoDAO;
import excepciones.CapacidadExcedidaException;
import excepciones.EmpleadoIncompatibleException;
import excepciones.UsuarioExistenteException;
import excepciones.UsuarioNoEncontradoException;
import modelo.Empleado;
import modelo.Propiedad;
import modelo.Sede;
import modelo.Usuario;
import singleton.Singleton;
import util.implementacion.ListaEnlazada;

public class EmpleadoDAOImpl implements EmpleadoDAO {

    private ListaEnlazada<Usuario> listaUsuarios;
    private ListaEnlazada<Empleado> listaEmpleados;
    
    public EmpleadoDAOImpl() {
        this.listaUsuarios = Singleton.getINSTANCIA().getListaUsuarios();
        this.listaEmpleados = Singleton.getINSTANCIA().getListaEmpleados();
    }

    @Override
    public void guardar(Empleado empleado) throws UsuarioExistenteException {
        // Verificar si ya existe un usuario con el mismo email
        for (int i = 0; i < listaUsuarios.size(); i++) {
            if (listaUsuarios.get(i).getEmail().equals(empleado.getEmail())) {
                throw new UsuarioExistenteException(empleado.getEmail());
            }
        }
        
        // Verificar si ya existe un usuario con la misma cédula
        for (int i = 0; i < listaUsuarios.size(); i++) {
            if (listaUsuarios.get(i) instanceof Empleado) {
                Empleado e = (Empleado) listaUsuarios.get(i);
                if (e.getCedula().equals(empleado.getCedula())) {
                    throw new UsuarioExistenteException(empleado.getCedula(), true);
                }
            }
        }
        
        listaUsuarios.add(empleado);
        listaEmpleados.add(empleado);
        Singleton.getINSTANCIA().escribirUsuarios();
        Singleton.getINSTANCIA().escribirEmpleados();
    }

    @Override
    public void actualizar(Empleado empleado) throws UsuarioNoEncontradoException {
        boolean encontrado = false;
        
        // Actualizar en listaUsuarios
        for (int i = 0; i < listaUsuarios.size(); i++) {
            if (listaUsuarios.get(i).getEmail().equals(empleado.getEmail())) {
                listaUsuarios.remove(i);
                listaUsuarios.add(empleado, i);
                encontrado = true;
                break;
            }
        }
        
        // Actualizar en listaEmpleados
        for (int i = 0; i < listaEmpleados.size(); i++) {
            if (listaEmpleados.get(i).getEmail().equals(empleado.getEmail())) {
                listaEmpleados.remove(i);
                listaEmpleados.add(empleado, i);
                break;
            }
        }
        
        if (!encontrado) {
            throw new UsuarioNoEncontradoException(empleado.getEmail());
        }
        
        Singleton.getINSTANCIA().escribirUsuarios();
        Singleton.getINSTANCIA().escribirEmpleados();
    }

    @Override
    public void eliminar(String email) throws UsuarioNoEncontradoException {
        Empleado empleado = buscarPorEmail(email);
        
        // Eliminar de listaUsuarios
        for (int i = 0; i < listaUsuarios.size(); i++) {
            if (listaUsuarios.get(i).getEmail().equals(email)) {
                listaUsuarios.remove(i);
                break;
            }
        }
        
        // Eliminar de listaEmpleados
        for (int i = 0; i < listaEmpleados.size(); i++) {
            if (listaEmpleados.get(i).getEmail().equals(email)) {
                listaEmpleados.remove(i);
                break;
            }
        }
        
        Singleton.getINSTANCIA().escribirUsuarios();
        Singleton.getINSTANCIA().escribirEmpleados();
    }

    @Override
    public Empleado buscarPorEmail(String email) throws UsuarioNoEncontradoException {
        for (int i = 0; i < listaEmpleados.size(); i++) {
            if (listaEmpleados.get(i).getEmail().equals(email)) {
                return listaEmpleados.get(i);
            }
        }
        throw new UsuarioNoEncontradoException(email);
    }

    @Override
    public Empleado buscarPorCedula(String cedula) throws UsuarioNoEncontradoException {
        for (int i = 0; i < listaEmpleados.size(); i++) {
            if (listaEmpleados.get(i).getCedula().equals(cedula)) {
                return listaEmpleados.get(i);
            }
        }
        throw new UsuarioNoEncontradoException(cedula);
    }

    @Override
    public ListaEnlazada<Empleado> listarTodos() {
        return listaEmpleados;
    }

    @Override
    public ListaEnlazada<Empleado> listarPorSede(Sede sede) {
        ListaEnlazada<Empleado> empleadosSede = new ListaEnlazada<>();
        for (int i = 0; i < listaEmpleados.size(); i++) {
            Empleado e = listaEmpleados.get(i);
            if (e.getSede() != null && e.getSede().getCiudad().equals(sede.getCiudad())) {
                empleadosSede.add(e);
            }
        }
        return empleadosSede;
    }

    @Override
    public void asignarSede(Empleado empleado, Sede sede) {
        empleado.setSede(sede);
        sede.agregarEmpleado(empleado);
        try {
            actualizar(empleado);
            Singleton.getINSTANCIA().escribirSedes();
        } catch (UsuarioNoEncontradoException e) {
            // Este error no debería ocurrir porque ya validamos que el empleado existe
            e.printStackTrace();
        }
    }

    @Override
    public void asignarPropiedad(Empleado empleado, Propiedad propiedad) throws EmpleadoIncompatibleException, CapacidadExcedidaException {
        // Verificar si el empleado puede administrar ese tipo de propiedad
        if (!empleado.validarTipoPropiedad(propiedad)) {
            if (propiedad.getTipo().equals(Propiedad.TIPO_VENTA)) {
                throw new EmpleadoIncompatibleException("venta");
            } else {
                throw new EmpleadoIncompatibleException("arriendo");
            }
        }
        
        // Verificar si la sede tiene cupo
        if (!empleado.getSede().tieneCupo()) {
            throw new CapacidadExcedidaException(empleado.getSede().getCiudad());
        }
        
        empleado.asignarPropiedad(propiedad);
        propiedad.setEncargado(empleado);
        propiedad.setSede(empleado.getSede());
        empleado.getSede().agregarPropiedad(propiedad);
        
        try {
            actualizar(empleado);
            Singleton.getINSTANCIA().escribirPropiedades();
            Singleton.getINSTANCIA().escribirSedes();
        } catch (UsuarioNoEncontradoException e) {
            // Este error no debería ocurrir porque ya validamos que el empleado existe
            e.printStackTrace();
        }
    }

    @Override
    public ListaEnlazada<Propiedad> listarPropiedadesAsignadas(String emailEmpleado) throws UsuarioNoEncontradoException {
        Empleado empleado = buscarPorEmail(emailEmpleado);
        return empleado.getPropiedadesAsignadas();
    }
}