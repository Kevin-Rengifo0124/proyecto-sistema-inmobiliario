package vista.administrador;

import controlador.ControladorAdministrador;
import dao.implementacion.EmpleadoDAOImpl;
import dao.implementacion.MatrizSedesDAOImpl;
import dao.implementacion.PropiedadDAOImpl;
import dao.implementacion.SedeDAOImpl;
import excepciones.UsuarioExistenteException;
import modelo.Empleado;
import modelo.Sede;
import singleton.Singleton;
import util.implementacion.ListaEnlazada;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class VentanaRegistrarEmpleado extends JFrame {

    private ControladorAdministrador controlador;
    private JTextField txtNombre;
    private JTextField txtCedula;
    private JTextField txtTelefono;
    private JTextField txtDireccion;
    private JTextField txtEmail;
    private JPasswordField txtPassword;
    private JComboBox<String> cmbSede;
    private JCheckBox chkPuedeVender;
    private JCheckBox chkPuedeArrendar;
    private JButton btnRegistrar;
    private JButton btnCancelar;

    public VentanaRegistrarEmpleado() {
        setTitle("Registrar Empleado");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        controlador = new ControladorAdministrador(
                new SedeDAOImpl(),
                new EmpleadoDAOImpl(),
                new MatrizSedesDAOImpl(),
                new PropiedadDAOImpl()
        );

        // Verificar que existan sedes antes de permitir registrar empleados
        ListaEnlazada<Sede> sedes = controlador.listarTodasSedes();
        if (sedes.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "No hay sedes registradas. Debe registrar al menos una sede primero.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            dispose();
            return;
        }

        inicializarComponentes();
    }

    private void inicializarComponentes() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblTitulo = new JLabel("REGISTRO DE EMPLEADO");
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

        // Sede
        JLabel lblSede = new JLabel("Sede:");
        gbc.gridx = 0;
        gbc.gridy = 7;
        panel.add(lblSede, gbc);

        cmbSede = new JComboBox<>();
        // Cargar sedes
        ListaEnlazada<Sede> sedes = controlador.listarTodasSedes();
        for (int i = 0; i < sedes.size(); i++) {
            cmbSede.addItem(sedes.get(i).getCiudad());
        }

        gbc.gridx = 1;
        gbc.gridy = 7;
        panel.add(cmbSede, gbc);

        // Permisos
        JPanel panelPermisos = new JPanel();
        panelPermisos.setBorder(BorderFactory.createTitledBorder("Permisos"));
        panelPermisos.setLayout(new BoxLayout(panelPermisos, BoxLayout.Y_AXIS));

        chkPuedeVender = new JCheckBox("Puede vender propiedades");
        chkPuedeVender.setSelected(true);
        panelPermisos.add(chkPuedeVender);

        chkPuedeArrendar = new JCheckBox("Puede arrendar propiedades");
        chkPuedeArrendar.setSelected(true);
        panelPermisos.add(chkPuedeArrendar);

        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.gridwidth = 2;
        panel.add(panelPermisos, gbc);

        // Botones
        JPanel panelBotones = new JPanel(new FlowLayout());

        btnRegistrar = new JButton("Registrar");
        btnRegistrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registrarEmpleado();
            }
        });
        panelBotones.add(btnRegistrar);

        btnCancelar = new JButton("Cancelar");
        btnCancelar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        panelBotones.add(btnCancelar);

        gbc.gridx = 0;
        gbc.gridy = 10;
        gbc.gridwidth = 2;
        panel.add(panelBotones, gbc);

        // Añadir el panel al contenedor principal (esto faltaba)
        getContentPane().add(panel);
    }

    private void registrarEmpleado() {
        // Validar campos
        if (txtNombre.getText().isEmpty() || txtCedula.getText().isEmpty()
                || txtTelefono.getText().isEmpty() || txtDireccion.getText().isEmpty()
                || txtEmail.getText().isEmpty() || new String(txtPassword.getPassword()).isEmpty()) {

            JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios",
                    "Error de validación", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Validar que se haya seleccionado al menos un permiso
        if (!chkPuedeVender.isSelected() && !chkPuedeArrendar.isSelected()) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar al menos un permiso",
                    "Error de validación", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Validar que exista una sede seleccionada
        if (cmbSede.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar una sede",
                    "Error de validación", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Validar que no exista un empleado con la misma cédula en cualquier sede (nueva validación)
        ListaEnlazada<Empleado> empleados = Singleton.getINSTANCIA().getListaEmpleados();
        for (int i = 0; i < empleados.size(); i++) {
            Empleado empleado = empleados.get(i);
            if (empleado.getCedula().equals(txtCedula.getText())) {
                JOptionPane.showMessageDialog(this,
                        "Ya existe un empleado con esta cédula en la sede: "
                        + (empleado.getSede() != null ? empleado.getSede().getCiudad() : "Sin sede"),
                        "Error de validación", JOptionPane.ERROR_MESSAGE);
                return;
            }
            // Validar también por email para evitar duplicados
            if (empleado.getEmail().equals(txtEmail.getText())) {
                JOptionPane.showMessageDialog(this,
                        "Ya existe un empleado con este email en la sede: "
                        + (empleado.getSede() != null ? empleado.getSede().getCiudad() : "Sin sede"),
                        "Error de validación", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        try {
            String ciudadSede = (String) cmbSede.getSelectedItem();

            // Crear el empleado
            Empleado empleado = new Empleado(
                    txtNombre.getText(),
                    txtCedula.getText(),
                    txtTelefono.getText(),
                    txtDireccion.getText(),
                    txtEmail.getText(),
                    new String(txtPassword.getPassword()),
                    null, // Sede se asignará en el controlador
                    chkPuedeVender.isSelected(),
                    chkPuedeArrendar.isSelected()
            );

            // Registrar empleado
            controlador.registrarEmpleado(empleado, ciudadSede);

            JOptionPane.showMessageDialog(this, "Empleado registrado exitosamente",
                    "Registro exitoso", JOptionPane.INFORMATION_MESSAGE);

            dispose();

        } catch (UsuarioExistenteException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        } catch (RuntimeException e) {
            // Capturar cualquier excepción de tiempo de ejecución, incluida la validación de sede
            JOptionPane.showMessageDialog(this, e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al registrar el empleado: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
