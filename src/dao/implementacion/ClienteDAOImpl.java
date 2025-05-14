package dao.implementacion;

import dao.ClienteDAO;
import excepciones.UsuarioExistenteException;
import excepciones.UsuarioNoEncontradoException;
import modelo.Cliente;
import modelo.Propiedad;
import modelo.Usuario;
import singleton.Singleton;
import util.implementacion.ListaEnlazada;

public class ClienteDAOImpl implements ClienteDAO {

    private ListaEnlazada<Usuario> listaUsuarios;
    private ListaEnlazada<Cliente> listaClientes;

    public ClienteDAOImpl() {
        this.listaUsuarios = Singleton.getINSTANCIA().getListaUsuarios();
        this.listaClientes = Singleton.getINSTANCIA().getListaClientes();
    }

    @Override
    public void guardar(Cliente cliente) throws UsuarioExistenteException {
        // Verificar si ya existe un usuario con el mismo email
        for (int i = 0; i < listaUsuarios.size(); i++) {
            if (listaUsuarios.get(i).getEmail().equals(cliente.getEmail())) {
                throw new UsuarioExistenteException(cliente.getEmail());
            }
        }

        // Verificar si ya existe un usuario con la misma cédula
        for (int i = 0; i < listaUsuarios.size(); i++) {
            if (listaUsuarios.get(i) instanceof Cliente) {
                Cliente c = (Cliente) listaUsuarios.get(i);
                if (c.getCedula().equals(cliente.getCedula())) {
                    throw new UsuarioExistenteException(cliente.getCedula(), true);
                }
            }
        }

        listaUsuarios.add(cliente);
        listaClientes.add(cliente);
        Singleton.getINSTANCIA().escribirUsuarios();
        Singleton.getINSTANCIA().escribirClientes();
    }

    @Override
    public void actualizar(Cliente cliente) throws UsuarioNoEncontradoException {
        boolean encontrado = false;

        // Actualizar en listaUsuarios
        for (int i = 0; i < listaUsuarios.size(); i++) {
            if (listaUsuarios.get(i).getEmail().equals(cliente.getEmail())) {
                listaUsuarios.remove(i);
                listaUsuarios.add(cliente, i);
                encontrado = true;
                break;
            }
        }

        // Actualizar en listaClientes
        for (int i = 0; i < listaClientes.size(); i++) {
            if (listaClientes.get(i).getEmail().equals(cliente.getEmail())) {
                listaClientes.remove(i);
                listaClientes.add(cliente, i);
                break;
            }
        }

        if (!encontrado) {
            throw new UsuarioNoEncontradoException(cliente.getEmail());
        }

        Singleton.getINSTANCIA().escribirUsuarios();
        Singleton.getINSTANCIA().escribirClientes();
    }

    @Override
    public void eliminar(String email) throws UsuarioNoEncontradoException {
        Cliente cliente = buscarPorEmail(email);

        // Eliminar de listaUsuarios
        for (int i = 0; i < listaUsuarios.size(); i++) {
            if (listaUsuarios.get(i).getEmail().equals(email)) {
                listaUsuarios.remove(i);
                break;
            }
        }

        // Eliminar de listaClientes
        for (int i = 0; i < listaClientes.size(); i++) {
            if (listaClientes.get(i).getEmail().equals(email)) {
                listaClientes.remove(i);
                break;
            }
        }

        Singleton.getINSTANCIA().escribirUsuarios();
        Singleton.getINSTANCIA().escribirClientes();
    }

    @Override
    public Cliente buscarPorEmail(String email) throws UsuarioNoEncontradoException {
        for (int i = 0; i < listaClientes.size(); i++) {
            if (listaClientes.get(i).getEmail().equals(email)) {
                return listaClientes.get(i);
            }
        }
        throw new UsuarioNoEncontradoException(email);
    }

    @Override
    public Cliente buscarPorCedula(String cedula) throws UsuarioNoEncontradoException {
        for (int i = 0; i < listaClientes.size(); i++) {
            if (listaClientes.get(i).getCedula().equals(cedula)) {
                return listaClientes.get(i);
            }
        }
        throw new UsuarioNoEncontradoException(cedula);
    }

    @Override
    public ListaEnlazada<Cliente> listarTodos() {
        return listaClientes;
    }

    @Override
    public ListaEnlazada<Propiedad> listarPropiedades(String emailCliente) throws UsuarioNoEncontradoException {
        Cliente cliente = buscarPorEmail(emailCliente);
        return cliente.getPropiedades();
    }

    @Override
    public void agregarPropiedad(Cliente cliente, Propiedad propiedad) {
        cliente.agregarPropiedad(propiedad);
        try {
            actualizar(cliente);
        } catch (UsuarioNoEncontradoException e) {
            // Este error no debería ocurrir porque ya validamos que el cliente existe
            e.printStackTrace();
        }
    }
}
