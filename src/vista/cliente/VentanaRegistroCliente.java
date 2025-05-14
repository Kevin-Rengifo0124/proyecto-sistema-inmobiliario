package vista.cliente;

import controladores.ControladorLogin;
import dao.implementacion.ClienteDAOImpl;
import dao.implementacion.UsuarioDAOImpl;
import excepciones.UsuarioExistenteException;
import modelo.Cliente;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class VentanaRegistroCliente extends JFrame {

    private JTextField txtNombre;
    private JTextField txtCedula;
    private JTextField txtTelefono;
    private JTextField txtDireccion;
    private JTextField txtEmail;
    private JPasswordField txtPassword;
    private JTextField txtCiudadResidencia;
    private JButton btnRegistrar;
    private JButton btnVolver;

    private ControladorLogin controlador;

    public VentanaRegistroCliente() {
        setTitle("Registro de Cliente");
        setSize(500, 400);
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
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblTitulo = new JLabel("REGISTRO DE CLIENTE");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(lblTitulo, gbc);

        // Nombre completo
        JLabel lblNombre = new JLabel("Nombre completo:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        panel.add(lblNombre, gbc);

        txtNombre = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 1;
        panel.add(txtNombre, gbc);

        // Cédula
        JLabel lblCedula = new JLabel("Cédula:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(lblCedula, gbc);

        txtCedula = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 2;
        panel.add(txtCedula, gbc);

        // Teléfono
        JLabel lblTelefono = new JLabel("Teléfono:");
        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(lblTelefono, gbc);

        txtTelefono = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 3;
        panel.add(txtTelefono, gbc);

        // Dirección
        JLabel lblDireccion = new JLabel("Dirección:");
        gbc.gridx = 0;
        gbc.gridy = 4;
        panel.add(lblDireccion, gbc);

        txtDireccion = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 4;
        panel.add(txtDireccion, gbc);

        // Email
        JLabel lblEmail = new JLabel("Email:");
        gbc.gridx = 0;
        gbc.gridy = 5;
        panel.add(lblEmail, gbc);

        txtEmail = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 5;
        panel.add(txtEmail, gbc);

        // Contraseña
        JLabel lblPassword = new JLabel("Contraseña:");
        gbc.gridx = 0;
        gbc.gridy = 6;
        panel.add(lblPassword, gbc);

        txtPassword = new JPasswordField(20);
        gbc.gridx = 1;
        gbc.gridy = 6;
        panel.add(txtPassword, gbc);

        // Ciudad de residencia
        JLabel lblCiudadResidencia = new JLabel("Ciudad de residencia:");
        gbc.gridx = 0;
        gbc.gridy = 7;
        panel.add(lblCiudadResidencia, gbc);

        txtCiudadResidencia = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 7;
        panel.add(txtCiudadResidencia, gbc);

        // Panel de botones completo
        JPanel panelBotones = new JPanel(new FlowLayout());

// Botón Registrar
        btnRegistrar = new JButton("Registrar");
        btnRegistrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registrarCliente();
            }
        });
        panelBotones.add(btnRegistrar);

// Botón Volver
        btnVolver = new JButton("Volver");
        btnVolver.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                volverLogin();
            }
        });
        panelBotones.add(btnVolver);

// Añadir el panel de botones al contenedor principal
        gbc.gridx = 0;
        gbc.gridy = 8; // Ajustar según la posición correcta en tu interfaz
        gbc.gridwidth = 2;
        panel.add(panelBotones, gbc);
    }

    private void configurarEventos() {
        btnRegistrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registrarCliente();
            }
        });

        btnVolver.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                volverLogin();
            }
        });
    }

    private void registrarCliente() {
        // Validar campos
        if (txtNombre.getText().isEmpty() || txtCedula.getText().isEmpty()
                || txtTelefono.getText().isEmpty() || txtDireccion.getText().isEmpty()
                || txtEmail.getText().isEmpty() || new String(txtPassword.getPassword()).isEmpty()
                || txtCiudadResidencia.getText().isEmpty()) {

            JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios",
                    "Error de validación", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Validar cédula
        if (controlador.existeCedula(txtCedula.getText())) {
            JOptionPane.showMessageDialog(this, "Ya existe un usuario con esa cédula",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Validar email
        if (controlador.existeUsuario(txtEmail.getText())) {
            JOptionPane.showMessageDialog(this, "Ya existe un usuario con ese email",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            Cliente cliente = new Cliente(
                    txtNombre.getText(),
                    txtCedula.getText(),
                    txtTelefono.getText(),
                    txtDireccion.getText(),
                    txtEmail.getText(),
                    new String(txtPassword.getPassword()),
                    txtCiudadResidencia.getText()
            );

            controlador.registrarCliente(cliente);

            JOptionPane.showMessageDialog(this, "Cliente registrado exitosamente",
                    "Registro exitoso", JOptionPane.INFORMATION_MESSAGE);

            volverLogin();

        } catch (UsuarioExistenteException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void volverLogin() {
        new vista.VentanaLogin().setVisible(true);
        this.dispose();
    }
}
