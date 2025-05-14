package vista.empleado;

import controlador.ControladorEmpleado;
import dao.implementacion.EmpleadoDAOImpl;
import dao.implementacion.PropiedadDAOImpl;
import dao.implementacion.SedeDAOImpl;
import modelo.Agenda;
import modelo.Propiedad;
import util.implementacion.ListaEnlazada;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;

public class VentanaVerVisitasAgendadas extends JFrame {

    private ControladorEmpleado controlador;
    private Propiedad propiedad;

    private JTable tblVisitas;
    private JTextField txtFecha;
    private JButton btnFiltrar;
    private JButton btnVolver;

    public VentanaVerVisitasAgendadas(Propiedad propiedad) {
        this.propiedad = propiedad;

        setTitle("Visitas Agendadas - " + propiedad.getDireccion());
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        controlador = new ControladorEmpleado(
                new EmpleadoDAOImpl(),
                new SedeDAOImpl(),
                new PropiedadDAOImpl()
        );

        inicializarComponentes();
        cargarVisitas(null);
    }

    private void inicializarComponentes() {
        JPanel panel = new JPanel(new BorderLayout());

        // Panel superior con info de propiedad
        JPanel panelInfo = new JPanel();
        panelInfo.setBorder(BorderFactory.createTitledBorder("Propiedad"));

        JLabel lblInfo = new JLabel("<html><b>Dirección:</b> " + propiedad.getDireccion()
                + "<br><b>Ciudad:</b> " + propiedad.getCiudad()
                + "<br><b>Tipo:</b> " + propiedad.getTipo() + "</html>");

        panelInfo.add(lblInfo);
        panel.add(panelInfo, BorderLayout.NORTH);

        // Panel de filtro
        JPanel panelFiltro = new JPanel(new FlowLayout());
        panelFiltro.setBorder(BorderFactory.createTitledBorder("Filtrar por fecha"));

        panelFiltro.add(new JLabel("Fecha (dd/MM/yyyy):"));
        txtFecha = new JTextField(10);
        panelFiltro.add(txtFecha);

        btnFiltrar = new JButton("Filtrar");
        btnFiltrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                filtrarPorFecha();
            }
        });
        panelFiltro.add(btnFiltrar);

        JButton btnLimpiar = new JButton("Ver Todas");
        btnLimpiar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                txtFecha.setText("");
                cargarVisitas(null);
            }
        });
        panelFiltro.add(btnLimpiar);

        panel.add(panelFiltro, BorderLayout.SOUTH);

        // Tabla de visitas
        String[] columnas = {"ID", "Cliente", "Fecha", "Hora", "Duración", "Estado"};
        tblVisitas = new JTable(new DefaultTableModel(columnas, 0));
        JScrollPane scrollPane = new JScrollPane(tblVisitas);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Botón volver
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnVolver = new JButton("Volver");
        btnVolver.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        panelBotones.add(btnVolver);

        panel.add(panelBotones, BorderLayout.SOUTH);

        getContentPane().add(panel);
    }

    private void filtrarPorFecha() {
        if (txtFecha.getText().trim().isEmpty()) {
            cargarVisitas(null);
            return;
        }

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            sdf.setLenient(false);
            Date fecha = sdf.parse(txtFecha.getText());

            cargarVisitas(fecha);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Formato de fecha inválido (use dd/MM/yyyy)",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cargarVisitas(Date fecha) {
        ListaEnlazada<Agenda> visitas = controlador.obtenerVisitasPropiedad(propiedad.getId());

        // Filtrar por fecha si corresponde
        if (fecha != null) {
            visitas = controlador.filtrarVisitasPorFecha(visitas, fecha);
        }

        // Llenar tabla
        DefaultTableModel modelo = (DefaultTableModel) tblVisitas.getModel();
        modelo.setRowCount(0);

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        for (int i = 0; i < visitas.size(); i++) {
            Agenda a = visitas.get(i);

            String estado;
            if (a.isCancelada()) {
                estado = "Cancelada";
            } else if (a.isCompletada()) {
                estado = "Completada";
            } else {
                estado = "Pendiente";
            }

            modelo.addRow(new Object[]{
                a.getId(),
                a.getCliente().getNombreCompleto(),
                sdf.format(a.getFecha()),
                a.getHoraInicio() + ":00",
                a.getDuracion() + " hora(s)",
                estado
            });
        }
    }
}
