package dao.implementacion;

import dao.UsuarioDAO;
import excepciones.CredencialesInvalidasException;
import excepciones.UsuarioExistenteException;
import excepciones.UsuarioNoEncontradoException;
import modelo.Persona;
import modelo.Usuario;
import singleton.Singleton;
import util.implementacion.ListaEnlazada;

public class UsuarioDAOImpl implements UsuarioDAO {

    private ListaEnlazada<Usuario> listaUsuarios;

    public UsuarioDAOImpl() {
        this.listaUsuarios = Singleton.getINSTANCIA().getListaUsuarios();
    }

    @Override
    public Usuario autenticar(String email, String clave) throws CredencialesInvalidasException {
        for (int i = 0; i < listaUsuarios.size(); i++) {
            Usuario usuario = listaUsuarios.get(i);
            if (usuario.getEmail().equals(email) && usuario.getClave().equals(clave)) {
                return usuario;
            }
        }
        throw new CredencialesInvalidasException();
    }

    @Override
    public void guardar(Usuario usuario) throws UsuarioExistenteException {
        if (existeUsuario(usuario.getEmail())) {
            throw new UsuarioExistenteException(usuario.getEmail());
        }

        // Si es Persona, verificar también por cédula
        if (usuario instanceof Persona) {
            Persona persona = (Persona) usuario;
            for (int i = 0; i < listaUsuarios.size(); i++) {
                if (listaUsuarios.get(i) instanceof Persona) {
                    Persona p = (Persona) listaUsuarios.get(i);
                    if (p.getCedula().equals(persona.getCedula())) {
                        throw new UsuarioExistenteException(persona.getCedula(), true);
                    }
                }
            }
        }

        listaUsuarios.add(usuario);
        Singleton.getINSTANCIA().escribirUsuarios();
    }

    @Override
    public void actualizar(Usuario usuario) throws UsuarioNoEncontradoException {
        int index = -1;
        for (int i = 0; i < listaUsuarios.size(); i++) {
            if (listaUsuarios.get(i).getEmail().equals(usuario.getEmail())) {
                index = i;
                break;
            }
        }

        if (index != -1) {
            listaUsuarios.remove(index);
            listaUsuarios.add(usuario, index);
            Singleton.getINSTANCIA().escribirUsuarios();
        } else {
            throw new UsuarioNoEncontradoException(usuario.getEmail());
        }
    }

    @Override
    public void eliminar(String email) throws UsuarioNoEncontradoException {
        Usuario usuario = buscarPorEmail(email);
        listaUsuarios.remove(usuario);
        Singleton.getINSTANCIA().escribirUsuarios();
    }

    @Override
    public Usuario buscarPorEmail(String email) throws UsuarioNoEncontradoException {
        for (int i = 0; i < listaUsuarios.size(); i++) {
            if (listaUsuarios.get(i).getEmail().equals(email)) {
                return listaUsuarios.get(i);
            }
        }
        throw new UsuarioNoEncontradoException(email);
    }

    @Override
    public Usuario buscarPorCedula(String cedula) throws UsuarioNoEncontradoException {
        for (int i = 0; i < listaUsuarios.size(); i++) {
            if (listaUsuarios.get(i) instanceof Persona) {
                Persona persona = (Persona) listaUsuarios.get(i);
                if (persona.getCedula().equals(cedula)) {
                    return listaUsuarios.get(i);
                }
            }
        }
        throw new UsuarioNoEncontradoException(cedula);
    }

    @Override
    public ListaEnlazada<Usuario> listarTodos() {
        return listaUsuarios;
    }

    @Override
    public boolean existeUsuario(String email) {
        for (int i = 0; i < listaUsuarios.size(); i++) {
            if (listaUsuarios.get(i).getEmail().equals(email)) {
                return true;
            }
        }
        return false;
    }
}
