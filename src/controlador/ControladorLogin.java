package controladores;

import dao.ClienteDAO;
import dao.UsuarioDAO;
import excepciones.CredencialesInvalidasException;
import excepciones.UsuarioExistenteException;
import excepciones.UsuarioNoEncontradoException;
import modelo.Administrador;
import modelo.Cliente;
import modelo.Usuario;
import singleton.Singleton;
import util.implementacion.ListaEnlazada;

public class ControladorLogin {

    private UsuarioDAO usuarioDAO;
    private ClienteDAO clienteDAO;

    public ControladorLogin(UsuarioDAO usuarioDAO, ClienteDAO clienteDAO) {
        this.usuarioDAO = usuarioDAO;
        this.clienteDAO = clienteDAO;

        if (!existeAdministrador()) {
            crearAdministradorInicial();
        }
    }

    private void crearAdministradorInicial() {
        try {
            Administrador admin = new Administrador("Administrador Sistema", "1000000000",
                    "3001234567", "Calle Principal #123", "admin@inmobiliaria.com", "admin123", Usuario.ADMINISTRATIVO);
            usuarioDAO.guardar(admin);
            Singleton.getINSTANCIA().getListaAdministradores().add(admin);
            Singleton.getINSTANCIA().escribirAdministradores();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean existeAdministrador() {
        ListaEnlazada<Usuario> usuarios = usuarioDAO.listarTodos();
        for (int i = 0; i < usuarios.size(); i++) {
            if (usuarios.get(i).getRol().equals(Usuario.ADMINISTRATIVO)) {
                return true;
            }
        }
        return false;
    }

    public Usuario iniciarSesion(String email, String clave) throws CredencialesInvalidasException {
        Usuario usuario = usuarioDAO.autenticar(email, clave);
        // Guardar el usuario en sesión
        Singleton.getINSTANCIA().setUsuarioActual(usuario);
        return usuario;
    }

    public String determinarRol(Usuario usuario) {
        return usuario.getRol();
    }

    public void cerrarSesion() {
        Singleton.getINSTANCIA().setUsuarioActual(null);
    }

    /**
     * Registra un nuevo cliente desde la ventana de login
     */
    public void registrarCliente(Cliente cliente) throws UsuarioExistenteException {
        clienteDAO.guardar(cliente);
    }

    /**
     * Verifica si un email ya está registrado
     */
    public boolean existeUsuario(String email) {
        return usuarioDAO.existeUsuario(email);
    }

    /**
     * Verifica si una cédula ya está registrada
     */
    public boolean existeCedula(String cedula) {
        try {
            usuarioDAO.buscarPorCedula(cedula);
            return true;
        } catch (UsuarioNoEncontradoException e) {
            return false;
        }
    }
}
