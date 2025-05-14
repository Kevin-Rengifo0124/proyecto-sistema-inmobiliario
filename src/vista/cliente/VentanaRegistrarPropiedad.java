package vista.cliente;

import controlador.ControladorCliente;
import dao.implementacion.AgendaDAOImpl;
import dao.implementacion.ClienteDAOImpl;
import dao.implementacion.MensajeDAOImpl;
import dao.implementacion.PropiedadDAOImpl;
import excepciones.UsuarioNoEncontradoException;
import modelo.Cliente;
import modelo.Inmueble;
import modelo.Propiedad;
import modelo.Sede;
import singleton.Singleton;
import util.implementacion.ListaEnlazada;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;

public class VentanaRegistrarPropiedad extends JFrame {

    private ControladorCliente controlador;
    private Cliente cliente;
    private VentanaCliente ventanaCliente;

    private JTextField txtDireccion;
    private JComboBox<String> cmbCiudad;
    private JComboBox<String> cmbTipo;
    private JTextField txtPrecio;
    private JTextArea txtDescripcion;

    // Campos para inmueble
    private JTextField txtTipoInmueble;
    private JComboBox<Integer> cmbEstrato;
    private JCheckBox chkAmoblado;
    private JTextArea txtCaracteristicas;

    private JButton btnRegistrar;
    private JButton btnCancelar;

    public VentanaRegistrarPropiedad(Cliente cliente, VentanaCliente ventanaCliente) {
        this.cliente = cliente;
        this.ventanaCliente = ventanaCliente;

        setTitle("Registrar Propiedad");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        controlador = new ControladorCliente(
                new ClienteDAOImpl(),
                new PropiedadDAOImpl(),
                new AgendaDAOImpl(),
                new MensajeDAOImpl()
        );

        inicializarComponentes();
    }

    private void inicializarComponentes() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblTitulo = new JLabel("REGISTRAR PROPIEDAD");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(lblTitulo, gbc);

        // Dirección
        JLabel lblDireccion = new JLabel("Dirección:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        panel.add(lblDireccion, gbc);

        txtDireccion = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 1;
        panel.add(txtDireccion, gbc);

        // Ciudad
        JLabel lblCiudad = new JLabel("Ciudad:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(lblCiudad, gbc);

        cmbCiudad = new JComboBox<>();
        // Cargar ciudades
        ListaEnlazada<Sede> sedes = Singleton.getINSTANCIA().getListaSedes();
        for (int i = 0; i < sedes.size(); i++) {
            cmbCiudad.addItem(sedes.get(i).getCiudad());
        }

        gbc.gridx = 1;
        gbc.gridy = 2;
        panel.add(cmbCiudad, gbc);

        // Tipo
        JLabel lblTipo = new JLabel("Tipo:");
        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(lblTipo, gbc);

        cmbTipo = new JComboBox<>();
        cmbTipo.addItem(Propiedad.TIPO_VENTA);
        cmbTipo.addItem(Propiedad.TIPO_ARRIENDO);
        gbc.gridx = 1;
        gbc.gridy = 3;
        panel.add(cmbTipo, gbc);

        // Precio
        JLabel lblPrecio = new JLabel("Precio:");
        gbc.gridx = 0;
        gbc.gridy = 4;
        panel.add(lblPrecio, gbc);

        txtPrecio = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 4;
        panel.add(txtPrecio, gbc);

        // Descripción
        JLabel lblDescripcion = new JLabel("Descripción:");
        gbc.gridx = 0;
        gbc.gridy = 5;
        panel.add(lblDescripcion, gbc);

        txtDescripcion = new JTextArea(5, 20);
        JScrollPane scrollDescripcion = new JScrollPane(txtDescripcion);
        gbc.gridx = 1;
        gbc.gridy = 5;
        panel.add(scrollDescripcion, gbc);

        // Separador
        JSeparator separador = new JSeparator();
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        panel.add(separador, gbc);

        // Título para información del inmueble
        JLabel lblTituloInmueble = new JLabel("INFORMACIÓN DEL INMUEBLE");
        lblTituloInmueble.setFont(new Font("Arial", Font.BOLD, 14));
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 2;
        panel.add(lblTituloInmueble, gbc);

        // Tipo de inmueble
        JLabel lblTipoInmueble = new JLabel("Tipo de inmueble:");
        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.gridwidth = 1;
        panel.add(lblTipoInmueble, gbc);

        txtTipoInmueble = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 8;
        panel.add(txtTipoInmueble, gbc);

        // Estrato
        JLabel lblEstrato = new JLabel("Estrato:");
        gbc.gridx = 0;
        gbc.gridy = 9;
        panel.add(lblEstrato, gbc);

        cmbEstrato = new JComboBox<>();
        for (int i = 1; i <= 6; i++) {
            cmbEstrato.addItem(i);
        }
        gbc.gridx = 1;
        gbc.gridy = 9;
        panel.add(cmbEstrato, gbc);

        // Amoblado
        chkAmoblado = new JCheckBox("Amoblado");
        gbc.gridx = 0;
        gbc.gridy = 10;
        gbc.gridwidth = 2;
        panel.add(chkAmoblado, gbc);

        // Características adicionales
        JLabel lblCaracteristicas = new JLabel("Características adicionales:");
        gbc.gridx = 0;
        gbc.gridy = 11;
        gbc.gridwidth = 2;
        panel.add(lblCaracteristicas, gbc);

        txtCaracteristicas = new JTextArea(5, 20);
        JScrollPane scrollCaracteristicas = new JScrollPane(txtCaracteristicas);
        gbc.gridx = 0;
        gbc.gridy = 12;
        gbc.gridwidth = 2;
        panel.add(scrollCaracteristicas, gbc);

        // Botones
        // Panel de botones completo
        JPanel panelBotones = new JPanel(new FlowLayout());

// Botón Registrar
        btnRegistrar = new JButton("Registrar");
        btnRegistrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registrarPropiedad();
            }
        });
        panelBotones.add(btnRegistrar);

// Botón Cancelar
        btnCancelar = new JButton("Cancelar");
        btnCancelar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        panelBotones.add(btnCancelar);

// Botón Volver
        JButton btnVolver = new JButton("Volver");
        btnVolver.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                ventanaCliente.setVisible(true); // Asegura que la ventana cliente se muestre de nuevo
            }
        });
        panelBotones.add(btnVolver);

// Añadir el panel de botones al contenedor principal
        gbc.gridx = 0;
        gbc.gridy = 13; // Ajustar según la posición correcta en tu interfaz
        gbc.gridwidth = 2;
        panel.add(panelBotones, gbc);
    }

    private void registrarPropiedad() {
        // Validar campos
        if (txtDireccion.getText().isEmpty() || txtPrecio.getText().isEmpty()
                || txtTipoInmueble.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos los campos marcados son obligatorios",
                    "Error de validación", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            // Validar precio
            double precio = Double.parseDouble(txtPrecio.getText());
            if (precio <= 0) {
                JOptionPane.showMessageDialog(this, "El precio debe ser mayor a cero",
                        "Error de validación", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Crear inmueble
            String idInmueble = "INM" + System.currentTimeMillis();
            Inmueble inmueble = new Inmueble(
                    idInmueble,
                    txtTipoInmueble.getText(),
                    (int) cmbEstrato.getSelectedItem(),
                    chkAmoblado.isSelected(),
                    txtCaracteristicas.getText()
            );

            // Crear propiedad
            String idPropiedad = "PROP" + System.currentTimeMillis();
            String direccion = txtDireccion.getText();
            String ciudad = (String) cmbCiudad.getSelectedItem();
            String tipo = (String) cmbTipo.getSelectedItem();
            String descripcion = txtDescripcion.getText();

            Propiedad propiedad = new Propiedad();
            propiedad.setId(idPropiedad);
            propiedad.setDireccion(direccion);
            propiedad.setCiudad(ciudad);
            propiedad.setTipo(tipo);
            propiedad.setPrecio(precio);
            propiedad.setDescripcion(descripcion);
            propiedad.setEstado(Propiedad.ESTADO_PENDIENTE_ASIGNACION);
            propiedad.setPropietario(cliente);
            propiedad.setInmueble(inmueble);
            propiedad.setFechaRegistro(new Date());
            propiedad.setVisitas(new util.implementacion.ListaEnlazada<>());
            propiedad.setMensajes(new util.implementacion.ListaEnlazada<>());

            // Registrar propiedad
            controlador.registrarPropiedadParaVenta(propiedad, cliente.getEmail());

            JOptionPane.showMessageDialog(this, "Propiedad registrada exitosamente. Será asignada a un encargado.",
                    "Registro exitoso", JOptionPane.INFORMATION_MESSAGE);

            // Actualizar datos de la ventana cliente
            ventanaCliente.actualizarDatos();

            dispose();

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "El precio debe ser un número válido",
                    "Error de validación", JOptionPane.ERROR_MESSAGE);
        } catch (UsuarioNoEncontradoException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al registrar la propiedad: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
