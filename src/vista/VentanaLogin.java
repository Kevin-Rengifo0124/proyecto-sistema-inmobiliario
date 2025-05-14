package vista;

import controladores.ControladorLogin;
import dao.implementacion.ClienteDAOImpl;
import dao.implementacion.UsuarioDAOImpl;
import excepciones.CredencialesInvalidasException;
import modelo.Cliente;
import modelo.Usuario;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import vista.cliente.VentanaRegistroCliente;

public class VentanaLogin extends JFrame {

    private JTextField txtEmail;
    private JPasswordField txtPassword;
    private JButton btnIngresar;
    private JButton btnRegistrarCliente;
    private ControladorLogin controlador;

    public VentanaLogin() {
        setTitle("Sistema Inmobiliaria - Login");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        controlador = new ControladorLogin(new UsuarioDAOImpl(), new ClienteDAOImpl());

        inicializarComponentes();
        configurarEventos();
    }

    private void inicializarComponentes() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        JLabel lblTitulo = new JLabel("INMOBILIARIA");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 24));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(lblTitulo, gbc);

        JLabel lblEmail = new JLabel("Email:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        panel.add(lblEmail, gbc);

        txtEmail = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 1;
        panel.add(txtEmail, gbc);

        JLabel lblPassword = new JLabel("Contraseña:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(lblPassword, gbc);

        txtPassword = new JPasswordField(20);
        gbc.gridx = 1;
        gbc.gridy = 2;
        panel.add(txtPassword, gbc);

        btnIngresar = new JButton("Ingresar");
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        panel.add(btnIngresar, gbc);

        btnRegistrarCliente = new JButton("Registrarse como Cliente");
        gbc.gridx = 0;
        gbc.gridy = 4;
        panel.add(btnRegistrarCliente, gbc);

        getContentPane().add(panel);
    }

    private void configurarEventos() {
        btnIngresar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                iniciarSesion();
            }
        });

        btnRegistrarCliente.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mostrarRegistroCliente();
            }
        });
    }

    private void iniciarSesion() {
        String email = txtEmail.getText();
        String clave = new String(txtPassword.getPassword());

        if (email.isEmpty() || clave.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor ingrese email y contraseña",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            Usuario usuario = controlador.iniciarSesion(email, clave);
            String rol = controlador.determinarRol(usuario);

            abrirVentanaSegunRol(rol);

        } catch (CredencialesInvalidasException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(),
                    "Error de autenticación", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void abrirVentanaSegunRol(String rol) {
        this.dispose();

        if (rol.equals(Usuario.ADMINISTRATIVO)) {
            new vista.administrador.VentanaAdministrador().setVisible(true);
        } else if (rol.equals(Usuario.EMPLEADO)) {
            new vista.empleado.VentanaEmpleado().setVisible(true);
        } else if (rol.equals(Usuario.CLIENTE)) {
            new vista.cliente.VentanaCliente().setVisible(true);
        }
    }

    private void mostrarRegistroCliente() {
        // Asegurarnos de que creamos una nueva instancia y la mostramos
        VentanaRegistroCliente ventanaRegistro = new VentanaRegistroCliente();
        ventanaRegistro.setVisible(true);
        this.dispose();
    }
}
