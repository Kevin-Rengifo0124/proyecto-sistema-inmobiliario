package vista.administrador;

import controlador.ControladorAdministrador;
import dao.implementacion.EmpleadoDAOImpl;
import dao.implementacion.MatrizSedesDAOImpl;
import dao.implementacion.PropiedadDAOImpl;
import dao.implementacion.SedeDAOImpl;
import modelo.MatrizSedes;
import modelo.Propiedad;
import modelo.Sede;
import singleton.Singleton;
import util.implementacion.ListaEnlazada;
import util.implementacion.Queue;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class VentanaAdministrador extends JFrame {

    private ControladorAdministrador controlador;
    private JTabbedPane tabbedPane;

    // Componentes para matriz de sedes
    private JPanel panelMatrizSedes;
    private JButton[][] botonesMatriz;

    // Componentes para gestión de propiedades pendientes
    private JPanel panelPropiedadesPendientes;
    private JLabel lblPropiedadActual;
    private JComboBox<String> cmbSedes;
    private JComboBox<String> cmbEmpleados;
    private JButton btnAsignarPropiedad;

    // Componentes para historial de propiedades
    private JPanel panelHistorial;
    private JComboBox<String> cmbFiltroEmpleado;
    private JComboBox<String> cmbFiltroSede;
    private JCheckBox chkSoloVendidas;
    private JCheckBox chkSoloArrendadas;
    private JButton btnFiltrar;
    private JTable tblHistorial;

    public VentanaAdministrador() {
        setTitle("Panel de Administrador");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        controlador = new ControladorAdministrador(
                new SedeDAOImpl(),
                new EmpleadoDAOImpl(),
                new MatrizSedesDAOImpl(),
                new PropiedadDAOImpl()
        );

        inicializarComponentes();
        cargarDatosIniciales();
    }

    private void inicializarComponentes() {
        tabbedPane = new JTabbedPane();

        // Inicializar panel Matriz de Sedes
        inicializarPanelMatrizSedes();
        tabbedPane.addTab("Matriz de Sedes", panelMatrizSedes);

        // Inicializar panel Propiedades Pendientes
        inicializarPanelPropiedadesPendientes();
        tabbedPane.addTab("Propiedades Pendientes", panelPropiedadesPendientes);

        // Inicializar panel Historial
        inicializarPanelHistorial();
        tabbedPane.addTab("Historial", panelHistorial);

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

    private void inicializarPanelMatrizSedes() {
        panelMatrizSedes = new JPanel(new BorderLayout());

        // Panel de controles para agregar sede
        JPanel panelControles = new JPanel(new FlowLayout());
        panelControles.add(new JLabel("Ciudad:"));
        JTextField txtCiudad = new JTextField(15);
        panelControles.add(txtCiudad);

        panelControles.add(new JLabel("Capacidad:"));
        JTextField txtCapacidad = new JTextField(5);
        panelControles.add(txtCapacidad);

        JButton btnAgregarSede = new JButton("Agregar Sede");
        btnAgregarSede.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                agregarSede(txtCiudad.getText(), txtCapacidad.getText());
            }
        });
        panelControles.add(btnAgregarSede);

        JButton btnRegistrarEmpleado = new JButton("Registrar Empleado");
        btnRegistrarEmpleado.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                abrirVentanaRegistrarEmpleado();
            }
        });
        panelControles.add(btnRegistrarEmpleado);

        panelMatrizSedes.add(panelControles, BorderLayout.NORTH);
        // Panel de matriz
        JPanel panelGrid = new JPanel(new GridLayout(4, 5, 5, 5));
        botonesMatriz = new JButton[4][5];

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 5; j++) {
                botonesMatriz[i][j] = new JButton("");
                botonesMatriz[i][j].setBackground(Color.WHITE);

                final int fila = i;
                final int columna = j;

                botonesMatriz[i][j].addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        seleccionarCasilla(fila, columna);
                    }
                });

                panelGrid.add(botonesMatriz[i][j]);
            }
        }

        panelMatrizSedes.add(panelGrid, BorderLayout.CENTER);

        // Leyenda
        JPanel panelLeyenda = new JPanel(new FlowLayout());

        JPanel cuadroBlanco = new JPanel();
        cuadroBlanco.setBackground(Color.WHITE);
        cuadroBlanco.setPreferredSize(new Dimension(20, 20));
        panelLeyenda.add(cuadroBlanco);
        panelLeyenda.add(new JLabel("No asignada"));

        JPanel cuadroGris = new JPanel();
        cuadroGris.setBackground(Color.DARK_GRAY);
        cuadroGris.setPreferredSize(new Dimension(20, 20));
        panelLeyenda.add(cuadroGris);
        panelLeyenda.add(new JLabel("Asignada"));

        JPanel cuadroAmarillo = new JPanel();
        cuadroAmarillo.setBackground(Color.YELLOW);
        cuadroAmarillo.setPreferredSize(new Dimension(20, 20));
        panelLeyenda.add(cuadroAmarillo);
        panelLeyenda.add(new JLabel("Sin cupo"));

        panelMatrizSedes.add(panelLeyenda, BorderLayout.SOUTH);
    }

    private void abrirVentanaRegistrarEmpleado() {
        new VentanaRegistrarEmpleado().setVisible(true);
    }

    private void inicializarPanelPropiedadesPendientes() {
        panelPropiedadesPendientes = new JPanel(new BorderLayout());

        JPanel panelInfo = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        lblPropiedadActual = new JLabel("No hay propiedades pendientes");
        panelInfo.add(lblPropiedadActual, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        panelInfo.add(new JLabel("Sede:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        cmbSedes = new JComboBox<>();
        cmbSedes.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cargarEmpleadosPorSede((String) cmbSedes.getSelectedItem());
            }
        });
        panelInfo.add(cmbSedes, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panelInfo.add(new JLabel("Empleado:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        cmbEmpleados = new JComboBox<>();
        panelInfo.add(cmbEmpleados, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        btnAsignarPropiedad = new JButton("Asignar Propiedad");
        btnAsignarPropiedad.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                asignarPropiedad();
            }
        });
        panelInfo.add(btnAsignarPropiedad, gbc);

        panelPropiedadesPendientes.add(panelInfo, BorderLayout.CENTER);
    }

    private void inicializarPanelHistorial() {
        panelHistorial = new JPanel(new BorderLayout());

        // Panel de filtros
        JPanel panelFiltros = new JPanel(new FlowLayout());

        panelFiltros.add(new JLabel("Empleado:"));
        cmbFiltroEmpleado = new JComboBox<>();
        cmbFiltroEmpleado.addItem("Todos");
        panelFiltros.add(cmbFiltroEmpleado);

        panelFiltros.add(new JLabel("Sede:"));
        cmbFiltroSede = new JComboBox<>();
        cmbFiltroSede.addItem("Todas");
        panelFiltros.add(cmbFiltroSede);

        chkSoloVendidas = new JCheckBox("Solo Vendidas");
        panelFiltros.add(chkSoloVendidas);

        chkSoloArrendadas = new JCheckBox("Solo Arrendadas");
        panelFiltros.add(chkSoloArrendadas);

        btnFiltrar = new JButton("Filtrar");
        btnFiltrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                filtrarHistorial();
            }
        });
        panelFiltros.add(btnFiltrar);

        panelHistorial.add(panelFiltros, BorderLayout.NORTH);

        // Tabla de propiedades
        String[] columnas = {"ID", "Dirección", "Ciudad", "Tipo", "Precio", "Estado", "Encargado"};
        Object[][] datos = new Object[0][7];

        tblHistorial = new JTable(datos, columnas);
        JScrollPane scrollPane = new JScrollPane(tblHistorial);

        panelHistorial.add(scrollPane, BorderLayout.CENTER);
    }

    private void cargarDatosIniciales() {
        // Cargar estado de la matriz de sedes
        actualizarMatrizSedes();

        // Cargar lista de sedes para los combos
        cargarListaSedes();

        // Cargar lista de empleados para filtros
        cargarListaEmpleados();

        // Cargar propiedad pendiente actual
        actualizarPropiedadPendiente();

        // Cargar historial
        filtrarHistorial();
    }

    private void actualizarMatrizSedes() {
        MatrizSedes matriz = controlador.obtenerMatrizSedes();

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 5; j++) {
                Sede sede = matriz.obtenerSede(i, j);

                if (sede == null) {
                    botonesMatriz[i][j].setText("");
                    botonesMatriz[i][j].setBackground(Color.WHITE);
                } else {
                    botonesMatriz[i][j].setText(sede.getCiudad());

                    if (sede.tieneCupo()) {
                        botonesMatriz[i][j].setBackground(Color.DARK_GRAY);
                        botonesMatriz[i][j].setForeground(Color.WHITE);
                    } else {
                        botonesMatriz[i][j].setBackground(Color.YELLOW);
                        botonesMatriz[i][j].setForeground(Color.BLACK);
                    }
                }
            }
        }
    }

    private void cargarListaSedes() {
        cmbSedes.removeAllItems();
        cmbFiltroSede.removeAllItems();
        cmbFiltroSede.addItem("Todas");

        ListaEnlazada<Sede> sedes = controlador.listarTodasSedes();
        for (int i = 0; i < sedes.size(); i++) {
            String ciudad = sedes.get(i).getCiudad();
            cmbSedes.addItem(ciudad);
            cmbFiltroSede.addItem(ciudad);
        }

        if (cmbSedes.getItemCount() > 0) {
            cargarEmpleadosPorSede((String) cmbSedes.getSelectedItem());
        }
    }

    private void cargarEmpleadosPorSede(String ciudad) {
        if (ciudad == null) {
            return;
        }

        cmbEmpleados.removeAllItems();

        try {
            ListaEnlazada<modelo.Empleado> empleados = controlador.listarEmpleadosPorSede(ciudad);
            for (int i = 0; i < empleados.size(); i++) {
                modelo.Empleado empleado = empleados.get(i);
                cmbEmpleados.addItem(empleado.getEmail());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void cargarListaEmpleados() {
        cmbFiltroEmpleado.removeAllItems();
        cmbFiltroEmpleado.addItem("Todos");

        ListaEnlazada<modelo.Empleado> empleados = Singleton.getINSTANCIA().getListaEmpleados();
        for (int i = 0; i < empleados.size(); i++) {
            cmbFiltroEmpleado.addItem(empleados.get(i).getEmail());
        }
    }

    private void actualizarPropiedadPendiente() {
        Propiedad propiedad = controlador.obtenerSiguientePropiedadPendiente();

        if (propiedad == null) {
            lblPropiedadActual.setText("No hay propiedades pendientes");
            btnAsignarPropiedad.setEnabled(false);
        } else {
            lblPropiedadActual.setText("<html>Propiedad pendiente:<br>"
                    + "ID: " + propiedad.getId() + "<br>"
                    + "Dirección: " + propiedad.getDireccion() + "<br>"
                    + "Ciudad: " + propiedad.getCiudad() + "<br>"
                    + "Tipo: " + propiedad.getTipo() + "<br>"
                    + "Precio: $" + propiedad.getPrecio() + "</html>");
            btnAsignarPropiedad.setEnabled(true);
        }
    }

    private void seleccionarCasilla(int fila, int columna) {
        Sede sede = controlador.obtenerMatrizSedes().obtenerSede(fila, columna);

        if (sede == null) {
            // Mostrar diálogo para agregar sede
            JTextField txtCiudad = new JTextField();
            JTextField txtCapacidad = new JTextField();

            Object[] campos = {
                "Ciudad:", txtCiudad,
                "Capacidad:", txtCapacidad
            };

            int resultado = JOptionPane.showConfirmDialog(this, campos,
                    "Agregar Sede", JOptionPane.OK_CANCEL_OPTION);

            if (resultado == JOptionPane.OK_OPTION) {
                agregarSede(txtCiudad.getText(), txtCapacidad.getText());
            }
        } else {
            // Mostrar diálogo para editar capacidad
            JTextField txtCapacidad = new JTextField(String.valueOf(sede.getCapacidadInmuebles()));

            Object[] campos = {
                "Ciudad: " + sede.getCiudad(),
                "Nueva capacidad:", txtCapacidad
            };

            int resultado = JOptionPane.showConfirmDialog(this, campos,
                    "Editar Capacidad", JOptionPane.OK_CANCEL_OPTION);

            if (resultado == JOptionPane.OK_OPTION) {
                try {
                    int nuevaCapacidad = Integer.parseInt(txtCapacidad.getText());
                    controlador.actualizarCapacidadSede(sede.getCiudad(), nuevaCapacidad);
                    actualizarMatrizSedes();
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(this, "La capacidad debe ser un número entero",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private void agregarSede(String ciudad, String capacidadStr) {
        if (ciudad.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debe ingresar la ciudad",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            int capacidad = Integer.parseInt(capacidadStr);

            if (capacidad <= 0) {
                JOptionPane.showMessageDialog(this, "La capacidad debe ser mayor a cero",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Buscar posición disponible
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 5; j++) {
                    if (botonesMatriz[i][j].getBackground().equals(Color.WHITE)) {
                        try {
                            Sede nuevaSede = new Sede(ciudad, capacidad);
                            controlador.registrarSede(nuevaSede, i, j);
                            actualizarMatrizSedes();
                            cargarListaSedes();
                            return;
                        } catch (excepciones.SedeExistenteException ex) {
                            JOptionPane.showMessageDialog(this, ex.getMessage(),
                                    "Error", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                    }
                }
            }

            JOptionPane.showMessageDialog(this, "No hay casillas disponibles en la matriz",
                    "Error", JOptionPane.ERROR_MESSAGE);

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "La capacidad debe ser un número entero",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void asignarPropiedad() {
        if (cmbEmpleados.getSelectedItem() == null || cmbSedes.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un empleado y una sede",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            String emailEmpleado = (String) cmbEmpleados.getSelectedItem();
            String ciudadSede = (String) cmbSedes.getSelectedItem();

            controlador.asignarPropiedadPendiente(emailEmpleado, ciudadSede);

            JOptionPane.showMessageDialog(this, "Propiedad asignada correctamente",
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);

            actualizarPropiedadPendiente();
            actualizarMatrizSedes();
            filtrarHistorial();

        } catch (excepciones.UsuarioNoEncontradoException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        } catch (RuntimeException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void filtrarHistorial() {
        String filtroEmpleado = null;
        String filtroSede = null;
        boolean soloVendidas = chkSoloVendidas.isSelected();
        boolean soloArrendadas = chkSoloArrendadas.isSelected();

        if (cmbFiltroEmpleado.getSelectedItem() != null
                && !cmbFiltroEmpleado.getSelectedItem().equals("Todos")) {
            filtroEmpleado = (String) cmbFiltroEmpleado.getSelectedItem();
        }

        if (cmbFiltroSede.getSelectedItem() != null
                && !cmbFiltroSede.getSelectedItem().equals("Todas")) {
            filtroSede = (String) cmbFiltroSede.getSelectedItem();
        }

        ListaEnlazada<Propiedad> propiedades = controlador.obtenerHistorialPropiedades(
                filtroEmpleado, filtroSede, soloVendidas, soloArrendadas);

        // Llenar la tabla con los resultados
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
            datos[i][6] = p.getEncargado() != null ? p.getEncargado().getEmail() : "Sin asignar";
        }

        tblHistorial.setModel(new javax.swing.table.DefaultTableModel(datos, columnas));
    }

    private void cerrarSesion() {
        Singleton.getINSTANCIA().setUsuarioActual(null);
        new vista.VentanaLogin().setVisible(true);
        this.dispose();
    }
}
