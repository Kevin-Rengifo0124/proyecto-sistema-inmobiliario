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
import java.text.SimpleDateFormat;
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
    private JButton btnVerMensajes;
    
    // Componentes para propiedades cerradas (historial)
    private JPanel panelHistorial;
    private JTable tblHistorial;
    
    // Panel de información del empleado
    private JPanel panelInfo;
    private JLabel lblInfoEmpleado;
    
    public VentanaEmpleado() {
        setTitle("Panel de Empleado");
        setSize(900, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Verificar que el usuario actual sea un empleado
        if (!(Singleton.getINSTANCIA().getUsuarioActual() instanceof Empleado)) {
            JOptionPane.showMessageDialog(this, 
                    "Error de autenticación: el usuario no es un empleado", 
                    "Error", JOptionPane.ERROR_MESSAGE);
            new vista.VentanaLogin().setVisible(true);
            this.dispose();
            return;
        }
        
        empleadoActual = (Empleado) Singleton.getINSTANCIA().getUsuarioActual();
        
        // Verificar que el empleado tenga una sede asignada
        if (empleadoActual.getSede() == null) {
            JOptionPane.showMessageDialog(this, 
                    "Error: El empleado no tiene una sede asignada", 
                    "Error", JOptionPane.ERROR_MESSAGE);
            new vista.VentanaLogin().setVisible(true);
            this.dispose();
            return;
        }
        
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
        panelInfo.setBorder(BorderFactory.createTitledBorder("Información del Empleado"));
        
        lblInfoEmpleado = new JLabel();
        lblInfoEmpleado.setFont(new Font("Arial", Font.PLAIN, 12));
        panelInfo.add(lblInfoEmpleado);
        
        panelPrincipal.add(panelInfo, BorderLayout.NORTH);
        
        // Tabs
        tabbedPane = new JTabbedPane();
        
        // Inicializar panel de propiedades activas
        inicializarPanelPropiedadesActivas();
        tabbedPane.addTab("Propiedades Activas", panelPropiedadesActivas);
        
        // Inicializar panel de historial
        inicializarPanelHistorial();
        tabbedPane.addTab("Historial de Propiedades", panelHistorial);
        
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
        String[] columnas = {"ID", "Dirección", "Ciudad", "Tipo", "Precio", "Permite Visitas", "Estado"};
        Object[][] datos = new Object[0][7];
        
        DefaultTableModel modelo = new DefaultTableModel(datos, columnas) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tblPropiedadesActivas = new JTable(modelo);
        tblPropiedadesActivas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblPropiedadesActivas.getTableHeader().setReorderingAllowed(false);
        
        // Ajustar el ancho de las columnas
        tblPropiedadesActivas.getColumnModel().getColumn(0).setPreferredWidth(60);  // ID
        tblPropiedadesActivas.getColumnModel().getColumn(1).setPreferredWidth(150); // Dirección
        tblPropiedadesActivas.getColumnModel().getColumn(2).setPreferredWidth(100); // Ciudad
        tblPropiedadesActivas.getColumnModel().getColumn(3).setPreferredWidth(80);  // Tipo
        tblPropiedadesActivas.getColumnModel().getColumn(4).setPreferredWidth(80);  // Precio
        tblPropiedadesActivas.getColumnModel().getColumn(5).setPreferredWidth(100); // Permite Visitas
        tblPropiedadesActivas.getColumnModel().getColumn(6).setPreferredWidth(80);  // Estado
        
        JScrollPane scrollPane = new JScrollPane(tblPropiedadesActivas);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Propiedades Asignadas"));
        
        panelPropiedadesActivas.add(scrollPane, BorderLayout.CENTER);
        
        // Panel de botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        panelBotones.setBorder(BorderFactory.createTitledBorder("Acciones"));
        
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
        
        btnVerMensajes = new JButton("Ver Mensajes");
        btnVerMensajes.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                verMensajes();
            }
        });
        panelBotones.add(btnVerMensajes);
        
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
        
        // Botón Actualizar
        JButton btnActualizar = new JButton("Actualizar");
        btnActualizar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cargarPropiedadesActivas();
            }
        });
        panelBotones.add(btnActualizar);
        
        panelPropiedadesActivas.add(panelBotones, BorderLayout.SOUTH);
    }
    
    private void inicializarPanelHistorial() {
        panelHistorial = new JPanel(new BorderLayout());
        
        // Tabla de historial
        String[] columnas = {"ID", "Dirección", "Ciudad", "Tipo", "Precio", "Estado", "Fecha Cierre"};
        Object[][] datos = new Object[0][7];
        
        DefaultTableModel modelo = new DefaultTableModel(datos, columnas) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tblHistorial = new JTable(modelo);
        tblHistorial.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblHistorial.getTableHeader().setReorderingAllowed(false);
        
        // Ajustar el ancho de las columnas
        tblHistorial.getColumnModel().getColumn(0).setPreferredWidth(60);  // ID
        tblHistorial.getColumnModel().getColumn(1).setPreferredWidth(150); // Dirección
        tblHistorial.getColumnModel().getColumn(2).setPreferredWidth(100); // Ciudad
        tblHistorial.getColumnModel().getColumn(3).setPreferredWidth(80);  // Tipo
        tblHistorial.getColumnModel().getColumn(4).setPreferredWidth(80);  // Precio
        tblHistorial.getColumnModel().getColumn(5).setPreferredWidth(100); // Estado
        tblHistorial.getColumnModel().getColumn(6).setPreferredWidth(100); // Fecha Cierre
        
        JScrollPane scrollPane = new JScrollPane(tblHistorial);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Propiedades Cerradas"));
        
        panelHistorial.add(scrollPane, BorderLayout.CENTER);
        
        // Panel de botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER));
        
        JButton btnActualizar = new JButton("Actualizar");
        btnActualizar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cargarHistorial();
            }
        });
        panelBotones.add(btnActualizar);
        
        panelHistorial.add(panelBotones, BorderLayout.SOUTH);
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
        StringBuilder info = new StringBuilder("<html>");
        info.append("<b>Nombre:</b> ").append(empleadoActual.getNombreCompleto()).append("<br>");
        info.append("<b>Email:</b> ").append(empleadoActual.getEmail()).append("<br>");
        info.append("<b>Sede:</b> ").append(empleadoActual.getSede().getCiudad()).append("<br>");
        info.append("<b>Permisos:</b> ");
        
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
            DefaultTableModel modelo = (DefaultTableModel) tblPropiedadesActivas.getModel();
            modelo.setRowCount(0); // Limpiar tabla
            
            for (int i = 0; i < propiedades.size(); i++) {
                Propiedad p = propiedades.get(i);
                modelo.addRow(new Object[]{
                    p.getId(),
                    p.getDireccion(),
                    p.getCiudad(),
                    p.getTipo(),
                    p.getPrecio(),
                    p.isPermiteAgendarVisita() ? "Sí" : "No",
                    p.getEstado()
                });
            }
            
            // Habilitar/deshabilitar botones según selección
            boolean haySeleccion = tblPropiedadesActivas.getSelectedRow() != -1;
            btnHabilitarVisita.setEnabled(haySeleccion);
            btnVerVisitas.setEnabled(haySeleccion);
            btnMarcarVendida.setEnabled(haySeleccion);
            btnMarcarArrendada.setEnabled(haySeleccion);
            btnVerMensajes.setEnabled(haySeleccion);
            
        } catch (UsuarioNoEncontradoException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar propiedades: " + e.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    private void cargarHistorial() {
        try {
            ListaEnlazada<Propiedad> propiedades = controlador.obtenerPropiedadesCerradas(empleadoActual.getEmail());
            
            // Llenar la tabla con las propiedades
            DefaultTableModel modelo = (DefaultTableModel) tblHistorial.getModel();
            modelo.setRowCount(0); // Limpiar tabla
            
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            
            for (int i = 0; i < propiedades.size(); i++) {
                Propiedad p = propiedades.get(i);
                String fechaCierre = p.getFechaCierre() != null ? sdf.format(p.getFechaCierre()) : "-";
                
                modelo.addRow(new Object[]{
                    p.getId(),
                    p.getDireccion(),
                    p.getCiudad(),
                    p.getTipo(),
                    p.getPrecio(),
                    p.getEstado(),
                    fechaCierre
                });
            }
            
        } catch (UsuarioNoEncontradoException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar historial: " + e.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
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
        
        String mensaje = permiteVisitas ? 
                "¿Está seguro que desea deshabilitar las visitas para esta propiedad?" :
                "¿Está seguro que desea habilitar las visitas para esta propiedad?";
        
        int confirmacion = JOptionPane.showConfirmDialog(this, mensaje, 
                "Confirmar", JOptionPane.YES_NO_OPTION);
        
        if (confirmacion == JOptionPane.YES_OPTION) {
            controlador.habilitarAgendarVisitas(idPropiedad, !permiteVisitas);
            
            // Actualizar la tabla
            cargarPropiedadesActivas();
            
            // Seleccionar la misma fila nuevamente
            if (filaSeleccionada < tblPropiedadesActivas.getRowCount()) {
                tblPropiedadesActivas.setRowSelectionInterval(filaSeleccionada, filaSeleccionada);
            }
            
            JOptionPane.showMessageDialog(this, 
                    "Se han " + (permiteVisitas ? "deshabilitado" : "habilitado") + " las visitas para esta propiedad", 
                    "Operación exitosa", JOptionPane.INFORMATION_MESSAGE);
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
        
        // Obtener la propiedad completa
        Propiedad propiedad = null;
        for (int i = 0; i < Singleton.getINSTANCIA().getListaPropiedades().size(); i++) {
            if (Singleton.getINSTANCIA().getListaPropiedades().get(i).getId().equals(idPropiedad)) {
                propiedad = Singleton.getINSTANCIA().getListaPropiedades().get(i);
                break;
            }
        }
        
        if (propiedad == null) {
            JOptionPane.showMessageDialog(this, "No se encontró la propiedad", 
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Abrir ventana de visitas
        VentanaVerVisitasAgendadas ventanaVisitas = new VentanaVerVisitasAgendadas(propiedad);
        ventanaVisitas.setVisible(true);
    }
    
    private void verMensajes() {
        int filaSeleccionada = tblPropiedadesActivas.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar una propiedad", 
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        String idPropiedad = (String) tblPropiedadesActivas.getValueAt(filaSeleccionada, 0);
        
        // Obtener la propiedad completa
        Propiedad propiedad = null;
        for (int i = 0; i < Singleton.getINSTANCIA().getListaPropiedades().size(); i++) {
            if (Singleton.getINSTANCIA().getListaPropiedades().get(i).getId().equals(idPropiedad)) {
                propiedad = Singleton.getINSTANCIA().getListaPropiedades().get(i);
                break;
            }
        }
        
        if (propiedad == null) {
            JOptionPane.showMessageDialog(this, "No se encontró la propiedad", 
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Abrir ventana de mensajes
        VentanaMensajes ventanaMensajes = new VentanaMensajes(empleadoActual, propiedad);
        ventanaMensajes.setVisible(true);
    }
    
    private void marcarPropiedad(String estado) {
        int filaSeleccionada = tblPropiedadesActivas.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar una propiedad", 
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        String idPropiedad = (String) tblPropiedadesActivas.getValueAt(filaSeleccionada, 0);
        
        String tipoOperacion = estado.equals(Propiedad.ESTADO_VENDIDA) ? "vendida" : "arrendada";
        int confirmacion = JOptionPane.showConfirmDialog(this, 
                "¿Está seguro que desea marcar esta propiedad como " + tipoOperacion + "?", 
                "Confirmar", JOptionPane.YES_NO_OPTION);
        
        if (confirmacion == JOptionPane.YES_OPTION) {
            try {
                controlador.cerrarPropiedad(idPropiedad, estado);
                
                JOptionPane.showMessageDialog(this, "Propiedad marcada como " + tipoOperacion + " correctamente", 
                        "Éxito", JOptionPane.INFORMATION_MESSAGE);
                
                // Actualizar ambas tablas
                cargarPropiedadesActivas();
                cargarHistorial();
                
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error al marcar la propiedad: " + e.getMessage(), 
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void cerrarSesion() {
        Singleton.getINSTANCIA().setUsuarioActual(null);
        new vista.VentanaLogin().setVisible(true);
        this.dispose();
    }
}