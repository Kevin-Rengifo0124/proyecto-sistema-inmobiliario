package vista.cliente;

import controlador.ControladorCliente;
import dao.implementacion.AgendaDAOImpl;
import dao.implementacion.ClienteDAOImpl;
import dao.implementacion.MensajeDAOImpl;
import dao.implementacion.PropiedadDAOImpl;
import excepciones.ConflictoHorarioException;
import excepciones.MensajePendienteException;
import excepciones.UsuarioNoEncontradoException;
import excepciones.VisitasExcedidasException;
import modelo.Cliente;
import modelo.Propiedad;
import singleton.Singleton;
import util.implementacion.ListaEnlazada;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;

public class VentanaCliente extends JFrame {

    private ControladorCliente controlador;
    private Cliente clienteActual;

    private JTabbedPane tabbedPane;

    // Componentes para búsqueda de propiedades
    private JPanel panelBusqueda;
    private JComboBox<String> cmbTipoPropiedad;
    private JTextField txtPrecioMin;
    private JTextField txtPrecioMax;
    private JComboBox<String> cmbCiudad;
    private JButton btnBuscar;
    private JTable tblPropiedades;
    private JButton btnVerDetalles;
    private JButton btnAgendarVisita;
    private JButton btnEnviarMensaje;
    private JButton btnComprarArrendar;

    // Componentes para mis propiedades
    private JPanel panelMisPropiedades;
    private JTable tblMisPropiedades;
    private JButton btnRegistrarPropiedad;

    // Componentes para mis visitas
    private JPanel panelMisVisitas;
    private JTable tblMisVisitas;

    public VentanaCliente() {
        setTitle("Portal del Cliente");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        clienteActual = (Cliente) Singleton.getINSTANCIA().getUsuarioActual();

        controlador = new ControladorCliente(
                new ClienteDAOImpl(),
                new PropiedadDAOImpl(),
                new AgendaDAOImpl(),
                new MensajeDAOImpl()
        );

        inicializarComponentes();
        cargarDatosIniciales();
    }

    private void inicializarComponentes() {
        tabbedPane = new JTabbedPane();

        // Inicializar panel de búsqueda
        inicializarPanelBusqueda();
        tabbedPane.addTab("Buscar Propiedades", panelBusqueda);

        // Inicializar panel de mis propiedades
        inicializarPanelMisPropiedades();
        tabbedPane.addTab("Mis Propiedades", panelMisPropiedades);

        // Inicializar panel de mis visitas
        inicializarPanelMisVisitas();
        tabbedPane.addTab("Mis Visitas", panelMisVisitas);

        getContentPane().add(tabbedPane);

        // Agregar opción para cerrar sesión
        JMenuBar menuBar = new JMenuBar();
        JMenu menuSesion = new JMenu("Sesión");
        JMenuItem itemCerrarSesion = new JMenuItem("Cerrar Sesión");

        itemCerrarSesion.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cerrarSesion();
            }
        });

        menuSesion.add(itemCerrarSesion);
        menuBar.add(menuSesion);
        setJMenuBar(menuBar);
    }

    private void inicializarPanelBusqueda() {
        panelBusqueda = new JPanel(new BorderLayout());

        // Panel de filtros
        JPanel panelFiltros = new JPanel(new FlowLayout());

        panelFiltros.add(new JLabel("Tipo:"));
        cmbTipoPropiedad = new JComboBox<>();
        cmbTipoPropiedad.addItem("Todos");
        cmbTipoPropiedad.addItem(Propiedad.TIPO_VENTA);
        cmbTipoPropiedad.addItem(Propiedad.TIPO_ARRIENDO);
        panelFiltros.add(cmbTipoPropiedad);

        panelFiltros.add(new JLabel("Precio Min:"));
        txtPrecioMin = new JTextField(10);
        panelFiltros.add(txtPrecioMin);

        panelFiltros.add(new JLabel("Precio Max:"));
        txtPrecioMax = new JTextField(10);
        panelFiltros.add(txtPrecioMax);

        panelFiltros.add(new JLabel("Ciudad:"));
        cmbCiudad = new JComboBox<>();
        cmbCiudad.addItem("Todas");
        panelFiltros.add(cmbCiudad);

        btnBuscar = new JButton("Buscar");
        btnBuscar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buscarPropiedades();
            }
        });
        panelFiltros.add(btnBuscar);

        panelBusqueda.add(panelFiltros, BorderLayout.NORTH);

        // Tabla de propiedades
        String[] columnas = {"ID", "Dirección", "Ciudad", "Tipo", "Precio", "Encargado", "Teléfono"};
        Object[][] datos = new Object[0][7];

        tblPropiedades = new JTable(datos, columnas);
        JScrollPane scrollPane = new JScrollPane(tblPropiedades);

        panelBusqueda.add(scrollPane, BorderLayout.CENTER);

        // Panel de botones
        JPanel panelBotones = new JPanel(new FlowLayout());

        btnVerDetalles = new JButton("Ver Detalles");
        btnVerDetalles.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                verDetallesPropiedad();
            }
        });
        panelBotones.add(btnVerDetalles);

        btnAgendarVisita = new JButton("Agendar Visita");
        btnAgendarVisita.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                agendarVisita();
            }
        });
        panelBotones.add(btnAgendarVisita);

        btnEnviarMensaje = new JButton("Enviar Mensaje");
        btnEnviarMensaje.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                enviarMensaje();
            }
        });
        panelBotones.add(btnEnviarMensaje);

        btnComprarArrendar = new JButton("Comprar/Arrendar");
        btnComprarArrendar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                comprarArrendar();
            }
        });
        panelBotones.add(btnComprarArrendar);

        panelBusqueda.add(panelBotones, BorderLayout.SOUTH);
    }

    private void inicializarPanelMisPropiedades() {
        panelMisPropiedades = new JPanel(new BorderLayout());

        // Botón para registrar propiedad
        JPanel panelBoton = new JPanel(new FlowLayout(FlowLayout.LEFT));
        btnRegistrarPropiedad = new JButton("Registrar Nueva Propiedad");
        btnRegistrarPropiedad.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registrarPropiedad();
            }
        });
        panelBoton.add(btnRegistrarPropiedad);

        panelMisPropiedades.add(panelBoton, BorderLayout.NORTH);

        // Tabla de mis propiedades
        String[] columnas = {"ID", "Dirección", "Ciudad", "Tipo", "Precio", "Estado", "Encargado"};
        Object[][] datos = new Object[0][7];

        tblMisPropiedades = new JTable(datos, columnas);
        JScrollPane scrollPane = new JScrollPane(tblMisPropiedades);

        panelMisPropiedades.add(scrollPane, BorderLayout.CENTER);
    }

    private void inicializarPanelMisVisitas() {
        panelMisVisitas = new JPanel(new BorderLayout());

        // Tabla de mis visitas
        String[] columnas = {"ID", "Propiedad", "Dirección", "Fecha", "Hora", "Duración", "Estado"};
        Object[][] datos = new Object[0][7];

        tblMisVisitas = new JTable(datos, columnas);
        JScrollPane scrollPane = new JScrollPane(tblMisVisitas);

        panelMisVisitas.add(scrollPane, BorderLayout.CENTER);
    }

    private void cargarDatosIniciales() {
        // Cargar lista de ciudades
        cargarListaCiudades();

        // Cargar propiedades iniciales
        buscarPropiedades();

        // Cargar mis propiedades
        cargarMisPropiedades();

        // Cargar mis visitas
        cargarMisVisitas();
    }

    private void cargarListaCiudades() {
        cmbCiudad.removeAllItems();
        cmbCiudad.addItem("Todas");

        ListaEnlazada<modelo.Sede> sedes = Singleton.getINSTANCIA().getListaSedes();
        for (int i = 0; i < sedes.size(); i++) {
            cmbCiudad.addItem(sedes.get(i).getCiudad());
        }
    }

    private void buscarPropiedades() {
        String tipo = null;
        if (cmbTipoPropiedad.getSelectedItem() != null
                && !cmbTipoPropiedad.getSelectedItem().equals("Todos")) {
            tipo = (String) cmbTipoPropiedad.getSelectedItem();
        }

        double precioMin = 0;
        try {
            if (!txtPrecioMin.getText().isEmpty()) {
                precioMin = Double.parseDouble(txtPrecioMin.getText());
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "El precio mínimo debe ser un número válido",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        double precioMax = 0;
        try {
            if (!txtPrecioMax.getText().isEmpty()) {
                precioMax = Double.parseDouble(txtPrecioMax.getText());
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "El precio máximo debe ser un número válido",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String ciudad = null;
        if (cmbCiudad.getSelectedItem() != null
                && !cmbCiudad.getSelectedItem().equals("Todas")) {
            ciudad = (String) cmbCiudad.getSelectedItem();
        }

        ListaEnlazada<Propiedad> propiedades = controlador.buscarPropiedades(tipo, precioMin, precioMax, ciudad);

        // Llenar la tabla con los resultados
        String[] columnas = {"ID", "Dirección", "Ciudad", "Tipo", "Precio", "Encargado", "Teléfono"};
        Object[][] datos = new Object[propiedades.size()][7];

        for (int i = 0; i < propiedades.size(); i++) {
            Propiedad p = propiedades.get(i);
            datos[i][0] = p.getId();
            datos[i][1] = p.getDireccion();
            datos[i][2] = p.getCiudad();
            datos[i][3] = p.getTipo();
            datos[i][4] = p.getPrecio();
            datos[i][5] = p.getEncargado() != null ? p.getEncargado().getNombreCompleto() : "Sin asignar";
            datos[i][6] = p.getEncargado() != null ? p.getEncargado().getTelefono() : "-";
        }

        tblPropiedades.setModel(new DefaultTableModel(datos, columnas));
    }

    private void cargarMisPropiedades() {
        try {
            ListaEnlazada<Propiedad> propiedades = controlador.obtenerPropiedadesCliente(clienteActual.getEmail());

            // Llenar la tabla con las propiedades
            String[] columnas = {"ID", "Dirección", "Ciudad", "Tipo", "Precio", "Estado", "Encargado"};
            Object[][] datos = new Object[propiedades.size()][7];

            for (int i = 0; i < propiedades.size(); i++) {
                Propiedad p = propiedades.get(i);
                datos[i][0] = p.getId();
                datos[i][1] = p.getDireccion();
                datos[i][2] = p.getCiudad();
                datos[i][3] = p.getTipo();
                datos[i][4] = p.getPrecio();
                datos[i][5] = p.getEstado();
                datos[i][6] = p.getEncargado() != null ? p.getEncargado().getNombreCompleto() : "Sin asignar";
            }

            tblMisPropiedades.setModel(new DefaultTableModel(datos, columnas));

        } catch (UsuarioNoEncontradoException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cargarMisVisitas() {
        // Esta función tendría que obtener las visitas agendadas por el cliente
        // Realmente necesita un método en el controlador para listar visitas por cliente
        // Como no veo ese método en el ControladorCliente, dejaré esta implementación incompleta
        JOptionPane.showMessageDialog(this, "Funcionalidad pendiente de implementar");
    }

    private void verDetallesPropiedad() {
        int filaSeleccionada = tblPropiedades.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar una propiedad",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String idPropiedad = (String) tblPropiedades.getValueAt(filaSeleccionada, 0);
        // Recuperar la propiedad completa
        Propiedad propiedad = Singleton.getINSTANCIA().getListaPropiedades().get(0);
        for (int i = 0; i < Singleton.getINSTANCIA().getListaPropiedades().size(); i++) {
            if (Singleton.getINSTANCIA().getListaPropiedades().get(i).getId().equals(idPropiedad)) {
                propiedad = Singleton.getINSTANCIA().getListaPropiedades().get(i);
                break;
            }
        }

        // Mostrar detalles en un diálogo
        JTextArea txtDetalles = new JTextArea(10, 40);
        txtDetalles.setEditable(false);

        txtDetalles.append("ID: " + propiedad.getId() + "\n");
        txtDetalles.append("Dirección: " + propiedad.getDireccion() + "\n");
        txtDetalles.append("Ciudad: " + propiedad.getCiudad() + "\n");
        txtDetalles.append("Tipo: " + propiedad.getTipo() + "\n");
        txtDetalles.append("Precio: $" + propiedad.getPrecio() + "\n");
        txtDetalles.append("Descripción: " + propiedad.getDescripcion() + "\n");
        txtDetalles.append("Estado: " + propiedad.getEstado() + "\n");

        if (propiedad.getEncargado() != null) {
            txtDetalles.append("Encargado: " + propiedad.getEncargado().getNombreCompleto() + "\n");
            txtDetalles.append("Teléfono: " + propiedad.getEncargado().getTelefono() + "\n");
            txtDetalles.append("Email: " + propiedad.getEncargado().getEmail() + "\n");
        } else {
            txtDetalles.append("Encargado: Sin asignar\n");
        }

        txtDetalles.append("¿Permite agendar visita?: " + (propiedad.isPermiteAgendarVisita() ? "Sí" : "No") + "\n");

        if (propiedad.getInmueble() != null) {
            txtDetalles.append("\nDatos del Inmueble:\n");
            txtDetalles.append("Tipo: " + propiedad.getInmueble().getTipoInmueble() + "\n");
            txtDetalles.append("Estrato: " + propiedad.getInmueble().getEstrato() + "\n");
            txtDetalles.append("Características: " + propiedad.getInmueble().getCaracteristicasAdicionales() + "\n");
        }

        JScrollPane scrollPane = new JScrollPane(txtDetalles);

        JOptionPane.showMessageDialog(this, scrollPane,
                "Detalles de la Propiedad", JOptionPane.INFORMATION_MESSAGE);
    }

    private void agendarVisita() {
        int filaSeleccionada = tblPropiedades.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar una propiedad",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String idPropiedad = (String) tblPropiedades.getValueAt(filaSeleccionada, 0);

        // Recuperar la propiedad completa
        Propiedad propiedad = null;
        for (int i = 0; i < Singleton.getINSTANCIA().getListaPropiedades().size(); i++) {
            if (Singleton.getINSTANCIA().getListaPropiedades().get(i).getId().equals(idPropiedad)) {
                propiedad = Singleton.getINSTANCIA().getListaPropiedades().get(i);
                break;
            }
        }

        if (propiedad == null || !propiedad.isPermiteAgendarVisita()) {
            JOptionPane.showMessageDialog(this, "Esta propiedad no permite agendar visitas",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Mostrar diálogo para agendar visita
        JTextField txtFecha = new JTextField(10);
        JComboBox<Integer> cmbHora = new JComboBox<>();
        JComboBox<Integer> cmbDuracion = new JComboBox<>();

        // Agregar horas laborales (8am - 5pm)
        for (int i = 8; i <= 16; i++) {
            cmbHora.addItem(i);
        }

        // Duraciones permitidas (1-3 horas)
        for (int i = 1; i <= 3; i++) {
            cmbDuracion.addItem(i);
        }

        Object[] campos = {
            "Fecha (dd/mm/yyyy):", txtFecha,
            "Hora de inicio:", cmbHora,
            "Duración (horas):", cmbDuracion
        };

        int resultado = JOptionPane.showConfirmDialog(this, campos,
                "Agendar Visita", JOptionPane.OK_CANCEL_OPTION);

        if (resultado == JOptionPane.OK_OPTION) {
            try {
                // Validar y parsear la fecha
                // Aquí se debería incluir una validación apropiada
                Date fecha = new Date();
                int hora = (int) cmbHora.getSelectedItem();
                int duracion = (int) cmbDuracion.getSelectedItem();

                controlador.agendarVisita(idPropiedad, clienteActual.getEmail(), fecha, hora, duracion);

                JOptionPane.showMessageDialog(this, "Visita agendada correctamente",
                        "Éxito", JOptionPane.INFORMATION_MESSAGE);

                cargarMisVisitas();

            } catch (UsuarioNoEncontradoException | ConflictoHorarioException | VisitasExcedidasException e) {
                JOptionPane.showMessageDialog(this, e.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error al agendar la visita: " + e.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void enviarMensaje() {
        int filaSeleccionada = tblPropiedades.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar una propiedad",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String idPropiedad = (String) tblPropiedades.getValueAt(filaSeleccionada, 0);

        // Mostrar diálogo para escribir mensaje
        JTextArea txtMensaje = new JTextArea(10, 40);
        JScrollPane scrollPane = new JScrollPane(txtMensaje);

        int resultado = JOptionPane.showConfirmDialog(this, scrollPane,
                "Escribir Mensaje", JOptionPane.OK_CANCEL_OPTION);

        if (resultado == JOptionPane.OK_OPTION) {
            if (txtMensaje.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "El mensaje no puede estar vacío",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                controlador.enviarMensaje(idPropiedad, clienteActual.getEmail(), txtMensaje.getText());

                JOptionPane.showMessageDialog(this, "Mensaje enviado correctamente",
                        "Éxito", JOptionPane.INFORMATION_MESSAGE);

            } catch (UsuarioNoEncontradoException | MensajePendienteException e) {
                JOptionPane.showMessageDialog(this, e.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error al enviar el mensaje: " + e.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void comprarArrendar() {
        int filaSeleccionada = tblPropiedades.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar una propiedad",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String idPropiedad = (String) tblPropiedades.getValueAt(filaSeleccionada, 0);
        String tipo = (String) tblPropiedades.getValueAt(filaSeleccionada, 3);

        String accion = tipo.equals(Propiedad.TIPO_VENTA) ? "comprar" : "arrendar";

        int confirmacion = JOptionPane.showConfirmDialog(this,
                "¿Está seguro que desea " + accion + " esta propiedad?",
                "Confirmar " + accion, JOptionPane.YES_NO_OPTION);

        if (confirmacion == JOptionPane.YES_OPTION) {
            try {
                controlador.comprarArrendar(idPropiedad, clienteActual.getEmail(), tipo);

                JOptionPane.showMessageDialog(this, "Propiedad " + (tipo.equals(Propiedad.TIPO_VENTA) ? "comprada" : "arrendada") + " correctamente",
                        "Éxito", JOptionPane.INFORMATION_MESSAGE);

                buscarPropiedades();

            } catch (UsuarioNoEncontradoException e) {
                JOptionPane.showMessageDialog(this, e.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void registrarPropiedad() {
        // Abrir formulario para registrar nueva propiedad
        new VentanaRegistrarPropiedad(clienteActual, this).setVisible(true);
    }

    public void actualizarDatos() {
        buscarPropiedades();
        cargarMisPropiedades();
        cargarMisVisitas();
    }

    private void cerrarSesion() {
        Singleton.getINSTANCIA().setUsuarioActual(null);
        new vista.VentanaLogin().setVisible(true);
        this.dispose();
    }
}
