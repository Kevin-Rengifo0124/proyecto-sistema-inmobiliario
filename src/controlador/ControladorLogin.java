/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

import excepciones.CredencialesInvalidasException;
import excepciones.UsuarioExistenteException;
import excepciones.UsuarioNoEncontradoException;
import modelo.Administrador;
import modelo.Cliente;
import modelo.Persona;
import modelo.Usuario;
import util.ListaEnlazada;
import singleton.Singleton;

public class ControladorLogin {

    private final Singleton singleton;

    public ControladorLogin() {
        this.singleton = Singleton.getInstancia();
        // Verificar si existe un administrador, si no, crearlo
        verificarAdministradorPredeterminado();
    }

    /**
     * Verifica si existe un administrador en el sistema, y si no, crea uno
     * predeterminado
     */
    private void verificarAdministradorPredeterminado() {
        boolean existeAdmin = false;
        ListaEnlazada<Persona> personas = singleton.getListaPersonas();

        // Verificar si ya existe un administrador
        for (int i = 0; i < personas.size(); i++) {
            Persona persona = personas.get(i);
            if (persona.getRol().equals(Usuario.ADMINISTRATIVO)) {
                existeAdmin = true;
                break;
            }
        }

        // Si no existe administrador, crear uno predeterminado
        if (!existeAdmin) {
            try {
                // Datos predeterminados del administrador
                String nombreCompleto = "Administrador Sistema";
                String cedula = "1111111111";
                String telefono = "3000000000";
                String direccion = "Dirección Predeterminada";
                String email = "admin@inmobiliaria.com";
                String clave = "admin123";

                // Crear y guardar el administrador
                Administrador admin = new Administrador(
                        nombreCompleto, cedula, telefono, direccion, email, clave, Usuario.ADMINISTRATIVO
                );

                singleton.guardarPersona(admin);
                System.out.println("Se ha creado un administrador predeterminado: " + email + " / " + clave);
            } catch (Exception e) {
                System.err.println("Error al crear el administrador predeterminado: " + e.getMessage());
            }
        }
    }

    /**
     * Inicia sesión validando las credenciales del usuario
     *
     * @param email Email del usuario
     * @param clave Clave del usuario
     * @return Usuario autenticado
     * @throws CredencialesInvalidasException Si las credenciales son inválidas
     */
    public Usuario iniciarSesion(String email, String clave) throws CredencialesInvalidasException {
        Usuario usuario = singleton.validarCredenciales(email, clave);
        if (usuario == null) {
            throw new CredencialesInvalidasException();
        }
        singleton.setUsuarioActual(usuario);
        return usuario;
    }

    /**
     * Registra un nuevo cliente en el sistema
     *
     * @param nombreCompleto Nombre completo del cliente
     * @param cedula Cédula del cliente
     * @param telefono Teléfono del cliente
     * @param direccion Dirección del cliente
     * @param email Email del cliente
     * @param clave Clave del cliente
     * @param ciudadResidencia Ciudad de residencia del cliente
     * @return Cliente registrado
     * @throws UsuarioExistenteException Si ya existe un usuario con ese email o
     * cédula
     */
    public Cliente registrarCliente(String nombreCompleto, String cedula, String telefono,
            String direccion, String email, String clave, String ciudadResidencia) throws UsuarioExistenteException {

        // Verificar si ya existe un usuario con ese email
        if (singleton.buscarUsuarioPorEmail(email) != null) {
            throw new UsuarioExistenteException(email);
        }

        // Verificar si ya existe una persona con esa cédula
        if (singleton.buscarPersonaPorCedula(cedula) != null) {
            throw new UsuarioExistenteException(cedula, true);
        }

        // Crear y guardar el nuevo cliente
        Cliente cliente = new Cliente(nombreCompleto, cedula, telefono, direccion, email, clave, ciudadResidencia);
        boolean resultado = singleton.guardarPersona(cliente);

        if (!resultado) {
            throw new UsuarioExistenteException(email);
        }

        return cliente;
    }

    /**
     * Cierra la sesión actual
     */
    public void cerrarSesion() {
        singleton.setUsuarioActual(null);
    }

    /**
     * Verifica si hay un usuario con la sesión iniciada
     *
     * @return true si hay un usuario con la sesión iniciada, false si no
     */
    public boolean haySesionIniciada() {
        return singleton.getUsuarioActual() != null;
    }

    /**
     * Obtiene el usuario actual con sesión iniciada
     *
     * @return Usuario actual con sesión iniciada
     * @throws UsuarioNoEncontradoException Si no hay sesión iniciada
     */
    public Usuario getUsuarioActual() throws UsuarioNoEncontradoException {
        if (!haySesionIniciada()) {
            throw new UsuarioNoEncontradoException("No hay sesión iniciada");
        }
        return singleton.getUsuarioActual();
    }
}
