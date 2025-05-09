/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vista;

import controlador.ControladorAdministrador;
import controlador.ControladorLogin;
import controlador.ControladorSede;
import excepciones.UsuarioExistenteException;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import modelo.Cliente;
import modelo.Empleado;
import modelo.Sede;
import singleton.Singleton;
import util.ListaEnlazada;


/**
 * Panel para el registro y visualización de clientes
 */
public class RegistroClienteVista extends JPanel {

    private final ControladorLogin controlador;
    private final ControladorSede controladorSede;
    private final JPanel panelContenedor;

    // Componentes del formulario
    private JTextField txtNombre;
    private JTextField txtCedula;
    private JTextField txtTelefono;
    private JTextField txtDireccion;
    private JTextField txtEmail;
    private JPasswordField txtClave;
    private JComboBox<String> cmbCiudad;
    private JButton btnRegistrar;
    private JButton btnActualizar;
    private JButton btnEliminar;
    private JButton btnLimpiar;
    private JLabel lblError;

    // Componentes de la tabla
    private JTable tblClientes;
    private DefaultTableModel modeloTabla;

    // Cliente seleccionado actualmente
    private Cliente clienteSeleccionado = null;
    private int filaSeleccionada = -1;

    /**
     * Constructor que recibe el panel contenedor
     *
     * @param panelContenedor Panel donde se mostrará esta vista
     */
    public RegistroClienteVista(JPanel panelContenedor) {
        this.controlador = new ControladorLogin();
        this.controladorSede = new ControladorSede();
        this.panelContenedor = panelContenedor;

        inicializarComponentes();
        cargarDatos();

        // Añadir este panel al contenedor
        panelContenedor.add(this);
    }

    private void inicializarComponentes() {
        // Configurar este panel
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(245, 245, 245));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Panel de título
        JPanel panelTitulo = new JPanel(new BorderLayout());
        panelTitulo.setBackground(new Color(25, 118, 210));
        panelTitulo.setBorder(new EmptyBorder(15, 15, 15, 15));

        JLabel lblTitulo = new JLabel("Gestión de Clientes");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 22));
        lblTitulo.setForeground(Color.WHITE);
        panelTitulo.add(lblTitulo, BorderLayout.WEST);

        // Botón de refrescar en el panel de título
        JButton btnRefrescar = new JButton("Refrescar Tabla");
        btnRefrescar.setFont(new Font("Arial", Font.BOLD, 12));
        btnRefrescar.setBackground(new Color(33, 150, 243));
        btnRefrescar.setForeground(Color.WHITE);
        btnRefrescar.setFocusPainted(false);
        btnRefrescar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnRefrescar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cargarDatosTabla();
            }
        });
        panelTitulo.add(btnRefrescar, BorderLayout.EAST);

        add(panelTitulo, BorderLayout.NORTH);

        // Panel de formulario y tabla
        JPanel panelCentral = new JPanel(new BorderLayout(20, 0));
        panelCentral.setBackground(new Color(245, 245, 245));

        // Panel del formulario
        JPanel panelFormulario = crearPanelFormulario();
        panelCentral.add(panelFormulario, BorderLayout.WEST);

        // Panel de la tabla
        JPanel panelTabla = crearPanelTabla();
        panelCentral.add(panelTabla, BorderLayout.CENTER);

        add(panelCentral, BorderLayout.CENTER);
    }

    private JPanel crearPanelFormulario() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(25, 118, 210), 2),
                "Registro y Edición de Clientes",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("Arial", Font.BOLD, 14),
                new Color(25, 118, 210)));
        panel.setPreferredSize(new Dimension(400, 500));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 10, 8, 10);

        // Nombre
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        JLabel lblNombre = new JLabel("Nombre Completo:");
        lblNombre.setFont(new Font("Arial", Font.BOLD, 12));
        panel.add(lblNombre, gbc);

        txtNombre = new JTextField(20);
        txtNombre.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        panel.add(txtNombre, gbc);

        // Cédula
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        JLabel lblCedula = new JLabel("Cédula:");
        lblCedula.setFont(new Font("Arial", Font.BOLD, 12));
        panel.add(lblCedula, gbc);

        txtCedula = new JTextField(20);
        txtCedula.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        panel.add(txtCedula, gbc);

        // Teléfono
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 1;
        JLabel lblTelefono = new JLabel("Teléfono:");
        lblTelefono.setFont(new Font("Arial", Font.BOLD, 12));
        panel.add(lblTelefono, gbc);

        txtTelefono = new JTextField(20);
        txtTelefono.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        panel.add(txtTelefono, gbc);

        // Dirección
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 1;
        JLabel lblDireccion = new JLabel("Dirección:");
        lblDireccion.setFont(new Font("Arial", Font.BOLD, 12));
        panel.add(lblDireccion, gbc);

        txtDireccion = new JTextField(20);
        txtDireccion.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 2;
        panel.add(txtDireccion, gbc);

        // Email
        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.gridwidth = 1;
        JLabel lblEmail = new JLabel("Email:");
        lblEmail.setFont(new Font("Arial", Font.BOLD, 12));
        panel.add(lblEmail, gbc);

        txtEmail = new JTextField(20);
        txtEmail.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        gbc.gridx = 0;
        gbc.gridy = 9;
        gbc.gridwidth = 2;
        panel.add(txtEmail, gbc);

        // Clave
        gbc.gridx = 0;
        gbc.gridy = 10;
        gbc.gridwidth = 1;
        JLabel lblClave = new JLabel("Contraseña:");
        lblClave.setFont(new Font("Arial", Font.BOLD, 12));
        panel.add(lblClave, gbc);

        txtClave = new JPasswordField(20);
        txtClave.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        gbc.gridx = 0;
        gbc.gridy = 11;
        gbc.gridwidth = 2;
        panel.add(txtClave, gbc);

        // Ciudad de Residencia
        gbc.gridx = 0;
        gbc.gridy = 12;
        gbc.gridwidth = 1;
        JLabel lblCiudad = new JLabel("Ciudad Residencia:");
        lblCiudad.setFont(new Font("Arial", Font.BOLD, 12));
        panel.add(lblCiudad, gbc);

        cmbCiudad = new JComboBox<>();
        cmbCiudad.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        gbc.gridx = 0;
        gbc.gridy = 13;
        gbc.gridwidth = 2;
        panel.add(cmbCiudad, gbc);

        // Mensaje de error
        lblError = new JLabel("");
        lblError.setForeground(new Color(211, 47, 47)); // Rojo
        lblError.setFont(new Font("Arial", Font.BOLD, 12));
        lblError.setHorizontalAlignment(SwingConstants.CENTER);
        lblError.setVisible(false);

        gbc.gridx = 0;
        gbc.gridy = 14;
        gbc.gridwidth = 2;
        panel.add(lblError, gbc);

        // Panel de botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        panelBotones.setBackground(Color.WHITE);

        // Botón Registrar
        btnRegistrar = new JButton("Registrar");
        btnRegistrar.setFont(new Font("Arial", Font.BOLD, 12));
        btnRegistrar.setBackground(new Color(76, 175, 80)); // Verde
        btnRegistrar.setForeground(Color.WHITE);
        btnRegistrar.setFocusPainted(false);
        btnRegistrar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnRegistrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registrarCliente();
            }
        });

        // Botón Actualizar
        btnActualizar = new JButton("Actualizar");
        btnActualizar.setFont(new Font("Arial", Font.BOLD, 12));
        btnActualizar.setBackground(new Color(255, 193, 7)); // Amarillo
        btnActualizar.setForeground(Color.WHITE);
        btnActualizar.setFocusPainted(false);
        btnActualizar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnActualizar.setEnabled(false); // Inicialmente deshabilitado
        btnActualizar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                actualizarCliente();
            }
        });

        // Botón Eliminar
        btnEliminar = new JButton("Eliminar");
        btnEliminar.setFont(new Font("Arial", Font.BOLD, 12));
        btnEliminar.setBackground(new Color(211, 47, 47)); // Rojo
        btnEliminar.setForeground(Color.WHITE);
        btnEliminar.setFocusPainted(false);
        btnEliminar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnEliminar.setEnabled(false); // Inicialmente deshabilitado
        btnEliminar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                eliminarCliente();
            }
        });

        // Botón Limpiar
        btnLimpiar = new JButton("Limpiar");
        btnLimpiar.setFont(new Font("Arial", Font.BOLD, 12));
        btnLimpiar.setBackground(new Color(158, 158, 158)); // Gris
        btnLimpiar.setForeground(Color.WHITE);
        btnLimpiar.setFocusPainted(false);
        btnLimpiar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnLimpiar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                limpiarFormulario();
            }
        });

        panelBotones.add(btnRegistrar);
        panelBotones.add(btnActualizar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnLimpiar);

        gbc.gridx = 0;
        gbc.gridy = 15;
        gbc.gridwidth = 2;
        panel.add(panelBotones, gbc);

        return panel;
    }

    private JPanel crearPanelTabla() {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(25, 118, 210), 2),
                "Clientes Registrados",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("Arial", Font.BOLD, 14),
                new Color(25, 118, 210)));

        // Panel de instrucciones
        JPanel panelInstrucciones = new JPanel();
        panelInstrucciones.setBackground(new Color(232, 245, 233)); // Verde claro
        panelInstrucciones.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        JLabel lblInstrucciones = new JLabel("Haga clic en un cliente para seleccionarlo y poder editarlo o eliminarlo");
        lblInstrucciones.setFont(new Font("Arial", Font.ITALIC, 12));
        lblInstrucciones.setForeground(new Color(46, 125, 50)); // Verde oscuro
        panelInstrucciones.add(lblInstrucciones);

        panel.add(panelInstrucciones, BorderLayout.NORTH);

        // Modelo de la tabla
        modeloTabla = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Hacer la tabla no editable
            }
        };

        // Añadir columnas
        modeloTabla.addColumn("Nombre");
        modeloTabla.addColumn("Cédula");
        modeloTabla.addColumn("Email");
        modeloTabla.addColumn("Teléfono");
        modeloTabla.addColumn("Ciudad");

        // Crear la tabla
        tblClientes = new JTable(modeloTabla);
        tblClientes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblClientes.getTableHeader().setReorderingAllowed(false);
        tblClientes.getTableHeader().setResizingAllowed(true);
        tblClientes.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        tblClientes.getTableHeader().setBackground(new Color(225, 245, 254)); // Azul muy claro
        tblClientes.getTableHeader().setForeground(new Color(25, 118, 210)); // Azul
        tblClientes.setRowHeight(25);
        tblClientes.setGridColor(new Color(225, 225, 225));
        tblClientes.setFont(new Font("Arial", Font.PLAIN, 12));

        // Manejar selección de fila
        tblClientes.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    filaSeleccionada = tblClientes.getSelectedRow();
                    if (filaSeleccionada != -1) {
                        seleccionarCliente(filaSeleccionada);
                    }
                }
            }
        });

        // Añadir la tabla a un scroll pane
        JScrollPane scrollPane = new JScrollPane(tblClientes);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private void cargarDatos() {
        // Cargar ciudades en el combo
        cargarCiudades();

        // Cargar datos en la tabla
        cargarDatosTabla();
    }

    private void cargarCiudades() {
        cmbCiudad.removeAllItems();

        // Añadir ciudades desde las sedes
        ListaEnlazada<Sede> sedes = controladorSede.getListaSedes();
        for (int i = 0; i < sedes.size(); i++) {
            cmbCiudad.addItem(sedes.get(i).getCiudad());
        }

        // Añadir opciones adicionales si es necesario
        if (cmbCiudad.getItemCount() == 0) {
            cmbCiudad.addItem("Bogotá");
            cmbCiudad.addItem("Medellín");
            cmbCiudad.addItem("Cali");
            cmbCiudad.addItem("Barranquilla");
            cmbCiudad.addItem("Cartagena");
        }
    }

    private void cargarDatosTabla() {
        // Limpiar la tabla
        modeloTabla.setRowCount(0);

        // Obtener la lista de clientes
        ListaEnlazada<Cliente> clientes = Singleton.getInstancia().getListaClientes();

        // Añadir cada cliente a la tabla
        for (int i = 0; i < clientes.size(); i++) {
            Cliente cliente = clientes.get(i);
            modeloTabla.addRow(new Object[]{
                cliente.getNombreCompleto(),
                cliente.getCedula(),
                cliente.getEmail(),
                cliente.getTelefono(),
                cliente.getCiudadResidencia()
            });
        }

        // Si no hay clientes, mostrar mensaje
        if (clientes.size() == 0) {
            JOptionPane.showMessageDialog(this,
                    "No hay clientes registrados en el sistema.",
                    "Información",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void seleccionarCliente(int fila) {
        try {
            // Obtener cédula del cliente seleccionado
            String cedula = (String) modeloTabla.getValueAt(fila, 1);

            // Buscar el cliente en la lista
            ListaEnlazada<Cliente> clientes = Singleton.getInstancia().getListaClientes();
            for (int i = 0; i < clientes.size(); i++) {
                Cliente cliente = clientes.get(i);
                if (cliente.getCedula().equals(cedula)) {
                    clienteSeleccionado = cliente;
                    break;
                }
            }

            // Cargar datos en el formulario
            if (clienteSeleccionado != null) {
                txtNombre.setText(clienteSeleccionado.getNombreCompleto());
                txtCedula.setText(clienteSeleccionado.getCedula());
                txtTelefono.setText(clienteSeleccionado.getTelefono());
                txtDireccion.setText(clienteSeleccionado.getDireccion());
                txtEmail.setText(clienteSeleccionado.getEmail());
                txtClave.setText(clienteSeleccionado.getClave());

                // Seleccionar ciudad en el combo
                for (int i = 0; i < cmbCiudad.getItemCount(); i++) {
                    if (cmbCiudad.getItemAt(i).equals(clienteSeleccionado.getCiudadResidencia())) {
                        cmbCiudad.setSelectedIndex(i);
                        break;
                    }
                }

                // Habilitar botones de edición
                btnActualizar.setEnabled(true);
                btnEliminar.setEnabled(true);
                btnRegistrar.setEnabled(false);

                // Deshabilitar campos críticos
                txtCedula.setEditable(false);
                txtEmail.setEditable(false);
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error al seleccionar cliente: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void registrarCliente() {
        // Obtener datos del formulario
        String nombre = txtNombre.getText().trim();
        String cedula = txtCedula.getText().trim();
        String telefono = txtTelefono.getText().trim();
        String direccion = txtDireccion.getText().trim();
        String email = txtEmail.getText().trim();
        String clave = new String(txtClave.getPassword());
        String ciudad = (String) cmbCiudad.getSelectedItem();

        // Resetear mensaje de error
        ocultarError();

        // Validar campos requeridos
        if (nombre.isEmpty()) {
            mostrarError("¡El nombre es obligatorio!");
            txtNombre.requestFocus();
            return;
        }

        if (cedula.isEmpty()) {
            mostrarError("¡La cédula es obligatoria!");
            txtCedula.requestFocus();
            return;
        }

        if (telefono.isEmpty()) {
            mostrarError("¡El teléfono es obligatorio!");
            txtTelefono.requestFocus();
            return;
        }

        if (direccion.isEmpty()) {
            mostrarError("¡La dirección es obligatoria!");
            txtDireccion.requestFocus();
            return;
        }

        if (email.isEmpty()) {
            mostrarError("¡El email es obligatorio!");
            txtEmail.requestFocus();
            return;
        }

        // Validar formato de email
        if (!email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            mostrarError("¡Formato de email inválido!");
            txtEmail.requestFocus();
            return;
        }

        if (clave.isEmpty()) {
            mostrarError("¡La contraseña es obligatoria!");
            txtClave.requestFocus();
            return;
        }

        // Validar longitud de contraseña
        if (clave.length() < 6) {
            mostrarError("¡La contraseña debe tener al menos 6 caracteres!");
            txtClave.requestFocus();
            return;
        }

        if (ciudad == null) {
            mostrarError("¡Debe seleccionar una ciudad!");
            cmbCiudad.requestFocus();
            return;
        }

        try {
            // Mostrar cursor de espera
            setCursor(new java.awt.Cursor(java.awt.Cursor.WAIT_CURSOR));

            // Registrar cliente
            Cliente cliente = controlador.registrarCliente(nombre, cedula, telefono, direccion, email, clave, ciudad);

            // Restaurar cursor normal
            setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

            // Mostrar mensaje de éxito
            JOptionPane.showMessageDialog(this,
                    "Cliente registrado exitosamente",
                    "Registro exitoso",
                    JOptionPane.INFORMATION_MESSAGE);

            // Limpiar formulario
            limpiarFormulario();

            // Actualizar tabla
            cargarDatosTabla();

        } catch (UsuarioExistenteException ex) {
            // Restaurar cursor normal
            setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
            mostrarError(ex.getMessage());
        } catch (Exception ex) {
            // Restaurar cursor normal
            setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
            mostrarError("Error al registrar: " + ex.getMessage());
        }
    }

    private void actualizarCliente() {
        if (clienteSeleccionado == null) {
            mostrarError("Debe seleccionar un cliente primero");
            return;
        }

        // Obtener datos del formulario
        String nombre = txtNombre.getText().trim();
        String telefono = txtTelefono.getText().trim();
        String direccion = txtDireccion.getText().trim();
        String clave = new String(txtClave.getPassword());
        String ciudad = (String) cmbCiudad.getSelectedItem();

        // Resetear mensaje de error
        ocultarError();

        // Validar campos requeridos
        if (nombre.isEmpty() || telefono.isEmpty() || direccion.isEmpty()
                || clave.isEmpty() || ciudad == null) {
            mostrarError("Todos los campos son obligatorios");
            return;
        }

        try {
            // Actualizar datos del cliente
            clienteSeleccionado.setNombreCompleto(nombre);
            clienteSeleccionado.setTelefono(telefono);
            clienteSeleccionado.setDireccion(direccion);
            clienteSeleccionado.setClave(clave);
            clienteSeleccionado.setCiudadResidencia(ciudad);

            // Guardar cambios
            Singleton.getInstancia().escribirClientes();
            Singleton.getInstancia().escribirUsuarios();
            Singleton.getInstancia().escribirPersonas();

            // Mostrar mensaje de éxito
            JOptionPane.showMessageDialog(this,
                    "Cliente actualizado exitosamente",
                    "Actualización exitosa",
                    JOptionPane.INFORMATION_MESSAGE);

            // Limpiar formulario
            limpiarFormulario();

            // Actualizar tabla
            cargarDatosTabla();

        } catch (Exception ex) {
            mostrarError("Error al actualizar: " + ex.getMessage());
        }
    }

    private void eliminarCliente() {
        if (clienteSeleccionado == null) {
            mostrarError("Debe seleccionar un cliente primero");
            return;
        }

        // Confirmar eliminación
        int opcion = JOptionPane.showConfirmDialog(this,
                "¿Está seguro que desea eliminar este cliente?\nEsta acción no se puede deshacer.",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (opcion == JOptionPane.YES_OPTION) {
            try {
                // Eliminar cliente de las listas
                Singleton.getInstancia().getListaClientes().remove(clienteSeleccionado);
                Singleton.getInstancia().getListaPersonas().remove(clienteSeleccionado);
                Singleton.getInstancia().getListaUsuarios().remove(clienteSeleccionado);

                // Guardar cambios
                Singleton.getInstancia().escribirClientes();
                Singleton.getInstancia().escribirPersonas();
                Singleton.getInstancia().escribirUsuarios();

                // Mostrar mensaje de éxito
                JOptionPane.showMessageDialog(this,
                        "Cliente eliminado exitosamente",
                        "Eliminación exitosa",
                        JOptionPane.INFORMATION_MESSAGE);

                // Limpiar formulario
                limpiarFormulario();

                // Actualizar tabla
                cargarDatosTabla();

            } catch (Exception ex) {
                mostrarError("Error al eliminar: " + ex.getMessage());
            }
        }
    }

    private void limpiarFormulario() {
        txtNombre.setText("");
        txtCedula.setText("");
        txtTelefono.setText("");
        txtDireccion.setText("");
        txtEmail.setText("");
        txtClave.setText("");
        if (cmbCiudad.getItemCount() > 0) {
            cmbCiudad.setSelectedIndex(0);
        }

        // Resetear cliente seleccionado
        clienteSeleccionado = null;
        filaSeleccionada = -1;

        // Resetear estado de botones
        btnRegistrar.setEnabled(true);
        btnActualizar.setEnabled(false);
        btnEliminar.setEnabled(false);

        // Habilitar campos críticos
        txtCedula.setEditable(true);
        txtEmail.setEditable(true);

        // Ocultar error
        ocultarError();

        // Deseleccionar fila en la tabla
        tblClientes.clearSelection();

        // Enfocar primer campo
        txtNombre.requestFocus();
    }

    private void mostrarError(String mensaje) {
        lblError.setText(mensaje);
        lblError.setVisible(true);
    }

    private void ocultarError() {
        lblError.setVisible(false);
    }

}
