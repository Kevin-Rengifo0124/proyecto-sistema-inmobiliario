package vista.empleado;

import controlador.ControladorEmpleado;
import dao.implementacion.EmpleadoDAOImpl;
import dao.implementacion.PropiedadDAOImpl;
import dao.implementacion.SedeDAOImpl;
import excepciones.UsuarioNoEncontradoException;
import modelo.Agenda;
import modelo.Empleado;
import modelo.Propiedad;
import singleton.Singleton;
import util.implementacion.ListaEnlazada;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;

public class VentanaEmpleado extends JFrame {

    private ControladorEmpleado controlador;
    private Empleado empleadoActual;
    
    private JTabbedPane tabbedPane;
    
    // Componentes para propiedades activas
    private JPanel panelPropiedadesActivas;
    private JTable tblPropiedadesActivas;
    private JButton btnHabilitarVisita;
    private JButton btnVerVisitas;
    private JButton btnMarcarVendida;
    private JButton btnMarcarArrendada;
    
    // Componentes para propiedades cerradas (historial)
    private JPanel panelHistorial;
    private JTable tblHistorial;
    
    // Panel de información del empleado
    private JPanel panelInfo;
    private JLabel lblInfoEmpleado;
    
    public VentanaEmpleado() {
        setTitle("Panel de Empleado");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        empleadoActual = (Empleado) Singleton.getINSTANCIA().getUsuarioActual();
        
        controlador = new ControladorEmpleado(
                new EmpleadoDAOImpl(),
                new SedeDAOImpl(),
                new PropiedadDAOImpl()
        );
        
        inicializarComponentes();
        cargarDatosIniciales();
    }
    
    private void inicializarComponentes() {
        // Panel principal
        JPanel panelPrincipal = new JPanel(new BorderLayout());
        
        // Panel de información del empleado
        panelInfo = new JPanel();
        lblInfoEmpleado = new JLabel();
        panelInfo.add(lblInfoEmpleado);
        panelPrincipal.add(panelInfo, BorderLayout.NORTH);
        
        // Tabs
        tabbedPane = new JTabbedPane();
        
        // Inicializar panel de propiedades activas
        inicializarPanelPropiedadesActivas();
        tabbedPane.addTab("Propiedades Activas", panelPropiedadesActivas);
        
        // Inicializar panel de historial
        inicializarPanelHistorial();
        tabbedPane.addTab("Historial", panelHistorial);
        
        panelPrincipal.add(tabbedPane, BorderLayout.CENTER);
        
        getContentPane().add(panelPrincipal);
        
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
    
    private void inicializarPanelPropiedadesActivas() {
        panelPropiedadesActivas = new JPanel(new BorderLayout());
        
        // Tabla de propiedades
        String[] columnas = {"ID", "Dirección", "Ciudad", "Tipo", "Precio", "Permite Visitas"};
        Object[][] datos = new Object[0][6];
        
        tblPropiedadesActivas = new JTable(datos, columnas);
        JScrollPane scrollPane = new JScrollPane(tblPropiedadesActivas);
        
        panelPropiedadesActivas.add(scrollPane, BorderLayout.CENTER);
        
        // Panel de botones
        JPanel panelBotones = new JPanel(new FlowLayout());
        
        btnHabilitarVisita = new JButton("Habilitar/Deshabilitar Visitas");
        btnHabilitarVisita.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                habilitarVisitas();
            }
        });
        panelBotones.add(btnHabilitarVisita);
        
        btnVerVisitas = new JButton("Ver Visitas Agendadas");
        btnVerVisitas.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                verVisitasAgendadas();
            }
        });
        panelBotones.add(btnVerVisitas);
        
        btnMarcarVendida = new JButton("Marcar como Vendida");
        btnMarcarVendida.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                marcarPropiedad(Propiedad.ESTADO_VENDIDA);
            }
        });
        panelBotones.add(btnMarcarVendida);
        
        btnMarcarArrendada = new JButton("Marcar como Arrendada");
        btnMarcarArrendada.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                marcarPropiedad(Propiedad.ESTADO_ARRENDADA);
            }
        });
        panelBotones.add(btnMarcarArrendada);
        
        panelPropiedadesActivas.add(panelBotones, BorderLayout.SOUTH);
    }
    
    private void inicializarPanelHistorial() {
        panelHistorial = new JPanel(new BorderLayout());
        
        // Tabla de historial
        String[] columnas = {"ID", "Dirección", "Ciudad", "Tipo", "Precio", "Estado", "Fecha Cierre"};
        Object[][] datos = new Object[0][7];
        
        tblHistorial = new JTable(datos, columnas);
        JScrollPane scrollPane = new JScrollPane(tblHistorial);
        
        panelHistorial.add(scrollPane, BorderLayout.CENTER);
    }
    
    private void cargarDatosIniciales() {
        // Cargar información del empleado
        actualizarInfoEmpleado();
        
        // Cargar propiedades activas
        cargarPropiedadesActivas();
        
        // Cargar historial
        cargarHistorial();
    }
    
    private void actualizarInfoEmpleado() {
        StringBuilder info = new StringBuilder("<html><h2>Información del Empleado</h2>");
        info.append("Nombre: ").append(empleadoActual.getNombreCompleto()).append("<br>");
        info.append("Email: ").append(empleadoActual.getEmail()).append("<br>");
        info.append("Sede: ").append(empleadoActual.getSede().getCiudad()).append("<br>");
        info.append("Permisos: ");
        
        if (empleadoActual.isPuedeVender() && empleadoActual.isPuedeArrendar()) {
            info.append("Venta y Arriendo");
        } else if (empleadoActual.isPuedeVender()) {
            info.append("Solo Venta");
        } else if (empleadoActual.isPuedeArrendar()) {
            info.append("Solo Arriendo");
        }
        
        info.append("</html>");
        
        lblInfoEmpleado.setText(info.toString());
    }
    
    private void cargarPropiedadesActivas() {
        try {
            ListaEnlazada<Propiedad> propiedades = controlador.obtenerPropiedadesActivas(empleadoActual.getEmail());
            
            // Llenar la tabla con las propiedades
            String[] columnas = {"ID", "Dirección", "Ciudad", "Tipo", "Precio", "Permite Visitas"};
            Object[][] datos = new Object[propiedades.size()][6];
            
            for (int i = 0; i < propiedades.size(); i++) {
                Propiedad p = propiedades.get(i);
                datos[i][0] = p.getId();
                datos[i][1] = p.getDireccion();
                datos[i][2] = p.getCiudad();
                datos[i][3] = p.getTipo();
                datos[i][4] = p.getPrecio();
                datos[i][5] = p.isPermiteAgendarVisita() ? "Sí" : "No";
            }
            
            tblPropiedadesActivas.setModel(new DefaultTableModel(datos, columnas));
            
        } catch (UsuarioNoEncontradoException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void cargarHistorial() {
        try {
            ListaEnlazada<Propiedad> propiedades = controlador.obtenerPropiedadesCerradas(empleadoActual.getEmail());
            
            // Llenar la tabla con las propiedades
            String[] columnas = {"ID", "Dirección", "Ciudad", "Tipo", "Precio", "Estado", "Fecha Cierre"};
            Object[][] datos = new Object[propiedades.size()][7];
            
            for (int i = 0; i < propiedades.size(); i++) {
                Propiedad p = propiedades.get(i);
                datos[i][0] = p.getId();
                datos[i][1] = p.getDireccion();
                datos[i][2] = p.getCiudad();
                datos[i][3] = p.getTipo();
                datos[i][4] = p.getPrecio();
                datos[i][5] = p.getEstado();
                datos[i][6] = p.getFechaCierre() != null ? p.getFechaCierre().toString() : "-";
            }
            
            tblHistorial.setModel(new DefaultTableModel(datos, columnas));
            
        } catch (UsuarioNoEncontradoException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void habilitarVisitas() {
        int filaSeleccionada = tblPropiedadesActivas.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar una propiedad", 
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        String idPropiedad = (String) tblPropiedadesActivas.getValueAt(filaSeleccionada, 0);
        boolean permiteVisitas = tblPropiedadesActivas.getValueAt(filaSeleccionada, 5).equals("Sí");
        
        int confirmacion = JOptionPane.showConfirmDialog(this, 
                "¿Está seguro que desea " + (permiteVisitas ? "deshabilitar" : "habilitar") + " las visitas para esta propiedad?", 
                "Confirmar", JOptionPane.YES_NO_OPTION);
        
        if (confirmacion == JOptionPane.YES_OPTION) {
            controlador.habilitarAgendarVisitas(idPropiedad, !permiteVisitas);
            cargarPropiedadesActivas();
        }
    }
    
    private void verVisitasAgendadas() {
        int filaSeleccionada = tblPropiedadesActivas.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar una propiedad", 
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        String idPropiedad = (String) tblPropiedadesActivas.getValueAt(filaSeleccionada, 0);
        
        // Obtener las visitas de la propiedad
        ListaEnlazada<Agenda> visitas = controlador.obtenerVisitasPropiedad(idPropiedad);
        
        // Mostrar diálogo para filtrar por fecha
        JTextField txtFecha = new JTextField(10);
        
        Object[] campos = {
            "Fecha (dd/mm/yyyy) [opcional]:", txtFecha,
            "Dejar en blanco para ver todas las visitas"
        };
        
        int resultado = JOptionPane.showConfirmDialog(this, campos, 
                "Filtrar Visitas", JOptionPane.OK_CANCEL_OPTION);
        
        if (resultado == JOptionPane.OK_OPTION) {
            // Si se ingresa una fecha, filtrar por esa fecha
            if (!txtFecha.getText().isEmpty()) {
                try {
                    // Aquí se debería parsear la fecha correctamente
                    Date fecha = new Date();
                    visitas = controlador.filtrarVisitasPorFecha(visitas, fecha);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, "Formato de fecha inválido", 
                            "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
            
            // Mostrar las visitas en un diálogo
            if (visitas.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No hay visitas agendadas para esta propiedad", 
                        "Información", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            
            String[] columnas = {"ID", "Cliente", "Fecha", "Hora", "Duración", "Estado"};
            Object[][] datos = new Object[visitas.size()][6];
            
            for (int i = 0; i < visitas.size(); i++) {
                Agenda a = visitas.get(i);
                datos[i][0] = a.getId();
                datos[i][1] = a.getCliente().getNombreCompleto();
                datos[i][2] = a.getFecha().toString();
                datos[i][3] = a.getHoraInicio() + ":00";
                datos[i][4] = a.getDuracion() + " hora(s)";
                
                if (a.isCancelada()) {
                    datos[i][5] = "Cancelada";
                } else if (a.isCompletada()) {
                    datos[i][5] = "Completada";
                } else {
                    datos[i][5] = "Pendiente";
                }
            }
            
            JTable tblVisitas = new JTable(datos, columnas);
            JScrollPane scrollPane = new JScrollPane(tblVisitas);
            scrollPane.setPreferredSize(new Dimension(600, 300));
            
            JOptionPane.showMessageDialog(this, scrollPane, 
                    "Visitas Agendadas", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void marcarPropiedad(String estado) {
        int filaSeleccionada = tblPropiedadesActivas.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar una propiedad", 
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        String idPropiedad = (String) tblPropiedadesActivas.getValueAt(filaSeleccionada, 0);
        
        int confirmacion = JOptionPane.showConfirmDialog(this, 
                "¿Está seguro que desea marcar esta propiedad como " + 
                (estado.equals(Propiedad.ESTADO_VENDIDA) ? "vendida" : "arrendada") + "?", 
                "Confirmar", JOptionPane.YES_NO_OPTION);
        
        if (confirmacion == JOptionPane.YES_OPTION) {
            controlador.cerrarPropiedad(idPropiedad, estado);
            
            JOptionPane.showMessageDialog(this, "Propiedad marcada como " + 
                    (estado.equals(Propiedad.ESTADO_VENDIDA) ? "vendida" : "arrendada") + " correctamente", 
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);
            
            cargarPropiedadesActivas();
            cargarHistorial();
        }
    }
    
    private void cerrarSesion() {
        Singleton.getINSTANCIA().setUsuarioActual(null);
        new vista.VentanaLogin().setVisible(true);
        this.dispose();
    }
}