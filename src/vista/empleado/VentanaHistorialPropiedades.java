package vista.empleado;

import controlador.ControladorEmpleado;
import dao.implementacion.EmpleadoDAOImpl;
import dao.implementacion.PropiedadDAOImpl;
import dao.implementacion.SedeDAOImpl;
import excepciones.UsuarioNoEncontradoException;
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

public class VentanaHistorialPropiedades extends JFrame {

    private ControladorEmpleado controlador;
    private Empleado empleado;
    
    private JTable tblHistorial;
    private JComboBox<String> cmbFiltroEstado;
    private JButton btnFiltrar;
    private JButton btnVolver;
    
    public VentanaHistorialPropiedades(Empleado empleado) {
        this.empleado = empleado;
        
        setTitle("Historial de Propiedades Cerradas");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        
        controlador = new ControladorEmpleado(
                new EmpleadoDAOImpl(),
                new SedeDAOImpl(),
                new PropiedadDAOImpl()
        );
        
        inicializarComponentes();
        cargarHistorial();
    }
    
    private void inicializarComponentes() {
        JPanel panel = new JPanel(new BorderLayout());
        
        // Panel superior con información del empleado
        JPanel panelInfo = new JPanel();
        panelInfo.setBorder(BorderFactory.createTitledBorder("Información del Empleado"));
        
        JLabel lblInfo = new JLabel("<html><b>Nombre:</b> " + empleado.getNombreCompleto()
                + "<br><b>Email:</b> " + empleado.getEmail()
                + "<br><b>Sede:</b> " + (empleado.getSede() != null ? empleado.getSede().getCiudad() : "Sin asignar")
                + "</html>");
        
        panelInfo.add(lblInfo);
        panel.add(panelInfo, BorderLayout.NORTH);
        
        // Tabla de historial
        String[] columnas = {"ID", "Dirección", "Ciudad", "Tipo", "Precio", "Estado", "Fecha Cierre"};
        DefaultTableModel modelo = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tblHistorial = new JTable(modelo);
        tblHistorial.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblHistorial.getTableHeader().setReorderingAllowed(false);
        
        tblHistorial.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) { // Doble clic
                    verDetallesPropiedad();
                }
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(tblHistorial);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        // Panel inferior con filtros y botones
        JPanel panelInferior = new JPanel(new BorderLayout());
        
        // Panel de filtros
        JPanel panelFiltros = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelFiltros.setBorder(BorderFactory.createTitledBorder("Filtros"));
        
        panelFiltros.add(new JLabel("Estado:"));
        cmbFiltroEstado = new JComboBox<>();
        cmbFiltroEstado.addItem("Todos");
        cmbFiltroEstado.addItem(Propiedad.ESTADO_VENDIDA);
        cmbFiltroEstado.addItem(Propiedad.ESTADO_ARRENDADA);
        panelFiltros.add(cmbFiltroEstado);
        
        btnFiltrar = new JButton("Filtrar");
        btnFiltrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                filtrarHistorial();
            }
        });
        panelFiltros.add(btnFiltrar);
        
        panelInferior.add(panelFiltros, BorderLayout.WEST);
        
        // Panel de botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
        btnVolver = new JButton("Volver");
        btnVolver.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        panelBotones.add(btnVolver);
        
        panelInferior.add(panelBotones, BorderLayout.EAST);
        
        panel.add(panelInferior, BorderLayout.SOUTH);
        
        getContentPane().add(panel);
    }
    
    private void filtrarHistorial() {
        String filtroEstado = cmbFiltroEstado.getSelectedItem().toString();
        
        try {
            ListaEnlazada<Propiedad> propiedades = controlador.obtenerPropiedadesCerradas(empleado.getEmail());
            DefaultTableModel modelo = (DefaultTableModel) tblHistorial.getModel();
            modelo.setRowCount(0); // Limpiar tabla
            
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            
            for (int i = 0; i < propiedades.size(); i++) {
                Propiedad p = propiedades.get(i);
                
                // Aplicar filtro por estado
                if (filtroEstado.equals("Todos") || p.getEstado().equals(filtroEstado)) {
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
    
    private void cargarHistorial() {
        // Cargar todas las propiedades cerradas
        cmbFiltroEstado.setSelectedItem("Todos");
        filtrarHistorial();
    }
    
    private void verDetallesPropiedad() {
        int filaSeleccionada = tblHistorial.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar una propiedad", 
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        String idPropiedad = (String) tblHistorial.getValueAt(filaSeleccionada, 0);
        
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
        
        // Mostrar detalles en un diálogo
        JTextArea txtDetalles = new JTextArea(20, 40);
        txtDetalles.setEditable(false);
        
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        
        txtDetalles.append("ID: " + propiedad.getId() + "\n");
        txtDetalles.append("Dirección: " + propiedad.getDireccion() + "\n");
        txtDetalles.append("Ciudad: " + propiedad.getCiudad() + "\n");
        txtDetalles.append("Tipo: " + propiedad.getTipo() + "\n");
        txtDetalles.append("Precio: $" + propiedad.getPrecio() + "\n");
        txtDetalles.append("Estado: " + propiedad.getEstado() + "\n");
        txtDetalles.append("Fecha de cierre: " + (propiedad.getFechaCierre() != null ? sdf.format(propiedad.getFechaCierre()) : "-") + "\n");
        
        if (propiedad.getPropietario() != null) {
            txtDetalles.append("\nDatos del Propietario:\n");
            txtDetalles.append("Nombre: " + propiedad.getPropietario().getNombreCompleto() + "\n");
            txtDetalles.append("Email: " + propiedad.getPropietario().getEmail() + "\n");
            txtDetalles.append("Teléfono: " + propiedad.getPropietario().getTelefono() + "\n");
        }
        
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
}