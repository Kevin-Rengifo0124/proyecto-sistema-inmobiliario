/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vista;

import controlador.ControladorLogin;
import controlador.ControladorAdministrador;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import modelo.Usuario;
import singleton.Singleton;

/**
 * Vista principal para el administrador
 */
public class AdministradorVista extends JFrame {

    private final ControladorLogin loginControlador;
    private final ControladorAdministrador controlador;

    // Componentes principales
    private JPanel panelMenu;
    private JPanel panelContenido;

    // Botones del menú
    private JButton btnSedes;
    private JButton btnEmpleados;
    private JButton btnClientes;
    private JButton btnPropiedades;
    private JButton btnHistorial;
    private JButton btnCerrarSesion;

    // Estado actual
    private JButton botonActivo = null;

    public AdministradorVista() {
        // Inicializar controladores
        this.loginControlador = new ControladorLogin();
        this.controlador = new ControladorAdministrador();

        // Configurar ventana
        inicializarComponentes();
        configurarEventos();

        // Mostrar ventana
        setVisible(true);
    }

    private void inicializarComponentes() {
        setTitle("Sistema Inmobiliaria - Panel de Administrador");
        setSize(1200, 750);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setMinimumSize(new Dimension(1000, 700));

        // Layout principal
        setLayout(new BorderLayout(0, 0));

        // Panel de encabezado
        JPanel panelEncabezado = new JPanel(new BorderLayout());
        panelEncabezado.setBackground(new Color(25, 118, 210)); // Azul material design
        panelEncabezado.setBorder(new EmptyBorder(15, 20, 15, 20));

        JLabel lblTitulo = new JLabel("SISTEMA DE GESTIÓN INMOBILIARIA");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitulo.setForeground(Color.WHITE);
        panelEncabezado.add(lblTitulo, BorderLayout.WEST);

        // Obtener datos del administrador
        Usuario usuario = Singleton.getInstancia().getUsuarioActual();
        String nombreUsuario = "Administrador";

        JLabel lblUsuario = new JLabel(nombreUsuario);
        lblUsuario.setFont(new Font("Arial", Font.BOLD, 16));
        lblUsuario.setForeground(Color.WHITE);
        lblUsuario.setIcon(crearIconoUsuario());
        lblUsuario.setHorizontalTextPosition(JLabel.RIGHT);
        lblUsuario.setIconTextGap(10);
        panelEncabezado.add(lblUsuario, BorderLayout.EAST);

        add(panelEncabezado, BorderLayout.NORTH);

        // Panel de menú lateral
        panelMenu = new JPanel();
        panelMenu.setLayout(new GridLayout(9, 1, 0, 0));
        panelMenu.setBackground(new Color(33, 33, 33)); // Gris oscuro
        panelMenu.setBorder(new EmptyBorder(15, 0, 15, 0));
        panelMenu.setPreferredSize(new Dimension(250, getHeight()));

        // Etiqueta de Panel Admin
        JLabel lblPanelAdmin = new JLabel("PANEL ADMIN", JLabel.CENTER);
        lblPanelAdmin.setFont(new Font("Arial", Font.BOLD, 18));
        lblPanelAdmin.setForeground(Color.WHITE);
        lblPanelAdmin.setBorder(new EmptyBorder(0, 0, 20, 0));
        panelMenu.add(lblPanelAdmin);

        // Botones del menú
        Font menuFont = new Font("Arial", Font.BOLD, 14);

        btnSedes = crearBotonMenu("Gestión de Sedes", menuFont);
        btnEmpleados = crearBotonMenu("Gestión de Empleados", menuFont);
        btnClientes = crearBotonMenu("Gestión de Clientes", menuFont);
        btnPropiedades = crearBotonMenu("Asignación de Propiedades", menuFont);
        btnHistorial = crearBotonMenu("Historial de Propiedades", menuFont);

        // Separador
        JPanel separador = new JPanel();
        separador.setBackground(new Color(33, 33, 33));

        // Botón de cerrar sesión con color diferente
        btnCerrarSesion = new JButton("Cerrar Sesión");
        btnCerrarSesion.setFont(menuFont);
        btnCerrarSesion.setForeground(Color.WHITE);
        btnCerrarSesion.setBackground(new Color(211, 47, 47)); // Rojo
        btnCerrarSesion.setFocusPainted(false);
        btnCerrarSesion.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        btnCerrarSesion.setHorizontalAlignment(JButton.LEFT);
        btnCerrarSesion.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        panelMenu.add(btnSedes);
        panelMenu.add(btnEmpleados);
        panelMenu.add(btnClientes);
        panelMenu.add(btnPropiedades);
        panelMenu.add(btnHistorial);
        panelMenu.add(separador);
        panelMenu.add(separador);
        panelMenu.add(btnCerrarSesion);

        add(panelMenu, BorderLayout.WEST);

        // Panel de contenido principal
        panelContenido = new JPanel();
        panelContenido.setLayout(new BorderLayout());
        panelContenido.setBackground(new Color(245, 245, 245)); // Gris muy claro

        // Mostrar panel de bienvenida
        mostrarPanelBienvenida();

        add(panelContenido, BorderLayout.CENTER);
    }

    private ImageIcon crearIconoUsuario() {
        // Aquí puedes cargar un icono real o crear uno básico
        try {
            return new ImageIcon(getClass().getResource("/recursos/admin_icon.png"));
        } catch (Exception e) {
            return null; // Si no se puede cargar, no mostrar icono
        }
    }

    private JButton crearBotonMenu(String texto, Font font) {
        JButton boton = new JButton(texto);
        boton.setFont(font);
        boton.setForeground(Color.WHITE);
        boton.setBackground(new Color(33, 33, 33)); // Gris oscuro
        boton.setFocusPainted(false);
        boton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(66, 66, 66)), // Borde inferior
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        boton.setHorizontalAlignment(JButton.LEFT);
        boton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        // Efecto hover
        boton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (boton != botonActivo) {
                    boton.setBackground(new Color(66, 66, 66));
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (boton != botonActivo) {
                    boton.setBackground(new Color(33, 33, 33));
                }
            }
        });

        return boton;
    }

    private void configurarEventos() {
        // Evento de cierre de ventana
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                confirmarSalida();
            }
        });

        // Eventos de los botones del menú
        btnSedes.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                activarBoton(btnSedes);
                // TODO: Implementar vista de gestión de sedes
                mostrarMensajeNoImplementado("Gestión de Sedes");
            }
        });

        btnEmpleados.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                activarBoton(btnEmpleados);
                mostrarRegistroEmpleados();
            }
        });

        btnClientes.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                activarBoton(btnClientes);
                mostrarRegistroClientes();
            }
        });

        btnPropiedades.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                activarBoton(btnPropiedades);
                // TODO: Implementar vista de asignación de propiedades
                mostrarMensajeNoImplementado("Asignación de Propiedades");
            }
        });

        btnHistorial.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                activarBoton(btnHistorial);
                // TODO: Implementar vista de historial de propiedades
                mostrarMensajeNoImplementado("Historial de Propiedades");
            }
        });

        btnCerrarSesion.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cerrarSesion();
            }
        });
    }

    private void activarBoton(JButton boton) {
        // Desactivar botón anterior
        if (botonActivo != null && botonActivo != boton) {
            botonActivo.setBackground(new Color(33, 33, 33));
        }

        // Activar nuevo botón
        boton.setBackground(new Color(25, 118, 210)); // Azul
        botonActivo = boton;
    }

    private void mostrarPanelBienvenida() {
        // Limpiar panel de contenido
        panelContenido.removeAll();
        panelContenido.setLayout(new BorderLayout());

        // Panel de bienvenida
        JPanel panelBienvenida = new JPanel();
        panelBienvenida.setLayout(new BorderLayout(0, 20));
        panelBienvenida.setBackground(new Color(245, 245, 245));
        panelBienvenida.setBorder(new EmptyBorder(50, 50, 50, 50));

        // Imagen de bienvenida (podría ser el logo de la inmobiliaria)
        JLabel lblImagen = new JLabel();
        try {
            lblImagen = new JLabel(new ImageIcon(getClass().getResource("/recursos/inmobiliaria.png")));
            lblImagen.setHorizontalAlignment(JLabel.CENTER);
            panelBienvenida.add(lblImagen, BorderLayout.CENTER);
        } catch (Exception e) {
            // Si no se puede cargar la imagen, no mostrarla
        }

        // Panel para los textos
        JPanel panelTextos = new JPanel(new GridLayout(3, 1, 10, 10));
        panelTextos.setBackground(new Color(245, 245, 245));

        JLabel lblBienvenida = new JLabel("¡Bienvenido al Panel de Administrador!", JLabel.CENTER);
        lblBienvenida.setFont(new Font("Arial", Font.BOLD, 32));
        lblBienvenida.setForeground(new Color(25, 118, 210));

        JLabel lblDescripcion = new JLabel("Sistema de Gestión Inmobiliaria", JLabel.CENTER);
        lblDescripcion.setFont(new Font("Arial", Font.ITALIC, 20));
        lblDescripcion.setForeground(new Color(66, 66, 66));

        JLabel lblInstrucciones = new JLabel("Seleccione una opción del menú lateral para comenzar", JLabel.CENTER);
        lblInstrucciones.setFont(new Font("Arial", Font.PLAIN, 16));
        lblInstrucciones.setForeground(new Color(97, 97, 97));

        panelTextos.add(lblBienvenida);
        panelTextos.add(lblDescripcion);
        panelTextos.add(lblInstrucciones);

        panelBienvenida.add(panelTextos, BorderLayout.SOUTH);

        panelContenido.add(panelBienvenida, BorderLayout.CENTER);

        // Actualizar panel
        panelContenido.revalidate();
        panelContenido.repaint();
    }

    private void mostrarRegistroClientes() {
        // Limpiar panel de contenido
        panelContenido.removeAll();

        // Crear panel de registro de clientes
        RegistroClienteVista registroCliente = new RegistroClienteVista(panelContenido);

        // Actualizar panel
        panelContenido.revalidate();
        panelContenido.repaint();
    }

    private void mostrarRegistroEmpleados() {
        // Limpiar panel de contenido
        panelContenido.removeAll();

        // Crear panel de registro de empleados
        RegistroEmpleadoVista registroEmpleado = new RegistroEmpleadoVista(panelContenido);

        // Actualizar panel
        panelContenido.revalidate();
        panelContenido.repaint();
    }

    private void mostrarMensajeNoImplementado(String funcionalidad) {
        JOptionPane.showMessageDialog(this,
                "La funcionalidad '" + funcionalidad + "' se implementará próximamente.",
                "En desarrollo",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void confirmarSalida() {
        int opcion = JOptionPane.showConfirmDialog(this,
                "¿Desea guardar los cambios antes de salir?",
                "Confirmar salida",
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        if (opcion == JOptionPane.YES_OPTION) {
            // Mostrar mensaje de guardando
            JOptionPane.showMessageDialog(this,
                    "Guardando datos...",
                    "Información",
                    JOptionPane.INFORMATION_MESSAGE);

            // Guardar datos
            Singleton.getInstancia().guardarTodo();
            System.exit(0);
        } else if (opcion == JOptionPane.NO_OPTION) {
            System.exit(0);
        }
        // Si es CANCEL_OPTION, no hace nada
    }

    private void cerrarSesion() {
        int opcion = JOptionPane.showConfirmDialog(this,
                "¿Está seguro que desea cerrar la sesión?",
                "Cerrar sesión",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        if (opcion == JOptionPane.YES_OPTION) {
            // Guardar datos
            Singleton.getInstancia().guardarTodo();

            // Cerrar sesión y volver al login
            loginControlador.cerrarSesion();

            // Cerrar ventana actual
            dispose();

            // Abrir ventana de login
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    new LoginVista();
                }
            });
        }
    }

}
