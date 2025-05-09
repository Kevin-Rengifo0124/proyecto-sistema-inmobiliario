/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vista;

import controlador.ControladorLogin;
import excepciones.CredencialesInvalidasException;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import modelo.Usuario;

public class LoginVista extends JFrame {

    private JTextField txtEmail;
    private JPasswordField txtClave;
    private JButton btnIngresar;
    private JButton btnRegistrarCliente;
    private JLabel lblError;

    private final ControladorLogin controlador;

    public LoginVista() {
        // Inicializar el controlador
        this.controlador = new ControladorLogin();

        // Configurar la ventana
        inicializarComponentes();
        configurarEventos();

        // Mostrar ventana
        setVisible(true);
    }

    private void inicializarComponentes() {
        setTitle("Sistema Inmobiliaria - Inicio de Sesión");
        setSize(550, 450);
        setLocationRelativeTo(null); // Centrar en pantalla
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        // Panel principal con GridBagLayout
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        panel.setBackground(new Color(240, 248, 255)); // Azul muy claro

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        // Logo o imagen (puedes reemplazar esto con tu propio logo)
        JLabel lblLogo = new JLabel();
        try {
            // Intenta cargar una imagen de logo (puedes reemplazar con tu propio logo)
            lblLogo = new JLabel(new ImageIcon(getClass().getResource("/recursos/logo.png")));
        } catch (Exception e) {
            // Si no se puede cargar la imagen, usa un texto
            lblLogo = new JLabel("INMOBILIARIA", JLabel.CENTER);
            lblLogo.setFont(new Font("Arial", Font.BOLD, 36));
            lblLogo.setForeground(new Color(0, 102, 204));
        }

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(lblLogo, gbc);

        // Subtítulo
        JLabel lblSubtitulo = new JLabel("Sistema de Gestión Inmobiliaria", JLabel.CENTER);
        lblSubtitulo.setFont(new Font("Arial", Font.ITALIC, 18));
        lblSubtitulo.setForeground(new Color(70, 130, 180));
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        panel.add(lblSubtitulo, gbc);

        // Espaciador
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        panel.add(Box.createVerticalStrut(20), gbc);

        // Panel de login con borde
        JPanel loginPanel = new JPanel(new GridBagLayout());
        loginPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(70, 130, 180), 2),
                "Iniciar Sesión",
                javax.swing.border.TitledBorder.CENTER,
                javax.swing.border.TitledBorder.TOP,
                new Font("Arial", Font.BOLD, 14),
                new Color(70, 130, 180)));
        loginPanel.setBackground(new Color(240, 248, 255));

        GridBagConstraints loginGbc = new GridBagConstraints();
        loginGbc.fill = GridBagConstraints.HORIZONTAL;
        loginGbc.insets = new Insets(8, 15, 8, 15);

        // Email
        JLabel lblEmail = new JLabel("Email:", JLabel.LEFT);
        lblEmail.setFont(new Font("Arial", Font.BOLD, 14));
        loginGbc.gridx = 0;
        loginGbc.gridy = 0;
        loginGbc.anchor = GridBagConstraints.WEST;
        loginPanel.add(lblEmail, loginGbc);

        txtEmail = new JTextField(20);
        txtEmail.setFont(new Font("Arial", Font.PLAIN, 14));
        txtEmail.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(70, 130, 180)),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        loginGbc.gridx = 0;
        loginGbc.gridy = 1;
        loginGbc.gridwidth = 2;
        loginPanel.add(txtEmail, loginGbc);

        // Clave
        JLabel lblClave = new JLabel("Contraseña:", JLabel.LEFT);
        lblClave.setFont(new Font("Arial", Font.BOLD, 14));
        loginGbc.gridx = 0;
        loginGbc.gridy = 2;
        loginGbc.gridwidth = 1;
        loginPanel.add(lblClave, loginGbc);

        txtClave = new JPasswordField(20);
        txtClave.setFont(new Font("Arial", Font.PLAIN, 14));
        txtClave.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(70, 130, 180)),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        loginGbc.gridx = 0;
        loginGbc.gridy = 3;
        loginGbc.gridwidth = 2;
        loginPanel.add(txtClave, loginGbc);

        // Mensaje de error (inicialmente oculto)
        lblError = new JLabel("");
        lblError.setForeground(new Color(178, 34, 34)); // Rojo oscuro
        lblError.setFont(new Font("Arial", Font.BOLD, 12));
        lblError.setHorizontalAlignment(JLabel.CENTER);
        lblError.setVisible(false);
        loginGbc.gridx = 0;
        loginGbc.gridy = 4;
        loginGbc.gridwidth = 2;
        loginPanel.add(lblError, loginGbc);

        // Botón Ingresar
        btnIngresar = new JButton("INGRESAR");
        btnIngresar.setFont(new Font("Arial", Font.BOLD, 14));
        btnIngresar.setBackground(new Color(0, 102, 204)); // Azul
        btnIngresar.setForeground(Color.WHITE);
        btnIngresar.setFocusPainted(false);
        btnIngresar.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        btnIngresar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        loginGbc.gridx = 0;
        loginGbc.gridy = 5;
        loginGbc.gridwidth = 2;
        loginGbc.fill = GridBagConstraints.NONE;
        loginGbc.anchor = GridBagConstraints.CENTER;
        loginGbc.insets = new Insets(15, 15, 8, 15);
        loginPanel.add(btnIngresar, loginGbc);

        // Agregar el panel de login al panel principal
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.BOTH;
        panel.add(loginPanel, gbc);

        // Botón Registrar Cliente
        btnRegistrarCliente = new JButton("Registrarse como cliente");
        btnRegistrarCliente.setFont(new Font("Arial", Font.PLAIN, 12));
        btnRegistrarCliente.setBackground(new Color(220, 220, 220));
        btnRegistrarCliente.setForeground(new Color(0, 102, 204));
        btnRegistrarCliente.setFocusPainted(false);
        btnRegistrarCliente.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        btnRegistrarCliente.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(btnRegistrarCliente, gbc);

        // Agregar panel a la ventana
        add(panel);
    }

    private void configurarEventos() {
        // Evento del botón Ingresar
        btnIngresar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                iniciarSesion();
            }
        });

        // Evento de tecla Enter en campo de clave
        txtClave.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    iniciarSesion();
                }
            }
        });

        // Evento del botón Registrar Cliente
        btnRegistrarCliente.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                abrirRegistroCliente();
            }
        });
    }

    private void iniciarSesion() {
        String email = txtEmail.getText().trim();
        String clave = new String(txtClave.getPassword());

        // Resetear mensaje de error
        ocultarError();

        // Validación básica
        if (email.isEmpty()) {
            mostrarError("¡El campo de Email no puede estar vacío!");
            txtEmail.requestFocus();
            return;
        }

        // Validación de formato de email
        if (!email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            mostrarError("¡Formato de email inválido!");
            txtEmail.requestFocus();
            return;
        }

        if (clave.isEmpty()) {
            mostrarError("¡La contraseña no puede estar vacía!");
            txtClave.requestFocus();
            return;
        }

        try {
            // Mostrar cursor de espera
            setCursor(new java.awt.Cursor(java.awt.Cursor.WAIT_CURSOR));

            // Intentar iniciar sesión
            Usuario usuario = controlador.iniciarSesion(email, clave);

            // Restaurar cursor normal
            setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

            // Ocultar mensaje de error si había uno
            ocultarError();

            // Redirigir según el rol
            switch (usuario.getRol()) {
                case Usuario.ADMINISTRATIVO:
                    abrirVistaAdministrador();
                    break;
                case Usuario.EMPLEADO:
                    abrirVistaEmpleado();
                    break;
                case Usuario.CLIENTE:
                    abrirVistaCliente();
                    break;
                default:
                    mostrarError("Rol de usuario no reconocido");
            }

        } catch (CredencialesInvalidasException ex) {
            // Restaurar cursor normal
            setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
            mostrarError("¡Credenciales inválidas! Verifique su email y contraseña");
        } catch (Exception ex) {
            // Restaurar cursor normal
            setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
            mostrarError("Error: " + ex.getMessage());
        }
    }

    private void mostrarError(String mensaje) {
        lblError.setText(mensaje);
        lblError.setVisible(true);

        // Hacer que el panel revalide el layout para mostrar el error
        lblError.getParent().revalidate();
    }

    private void ocultarError() {
        lblError.setVisible(false);

        // Hacer que el panel revalide el layout para ocultar el error
        lblError.getParent().revalidate();
    }

    private void abrirRegistroCliente() {
        // Abrir ventana de registro de cliente
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
            }
        });
    }

    private void abrirVistaAdministrador() {
        // Cerrar la ventana de login
        dispose();

        // Abrir la vista de administrador
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new AdministradorVista();
            }
        });
    }

    private void abrirVistaEmpleado() {
        // Cerrar la ventana de login
        dispose();

        // Implementar redirección a vista de empleado
        JOptionPane.showMessageDialog(this,
                "La vista de empleado será implementada próximamente.",
                "Información",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void abrirVistaCliente() {
        // Cerrar la ventana de login
        dispose();

        // Implementar redirección a vista de cliente
        JOptionPane.showMessageDialog(this,
                "La vista de cliente será implementada próximamente.",
                "Información",
                JOptionPane.INFORMATION_MESSAGE);
    }

    // Método main para probar la vista
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new LoginVista();
            }
        });
    }
}
