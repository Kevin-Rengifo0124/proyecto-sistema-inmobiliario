package vista.empleado;

import dao.implementacion.MensajeDAOImpl;
import excepciones.MensajePendienteException;
import modelo.Empleado;
import modelo.Mensaje;
import modelo.Propiedad;
import singleton.Singleton;
import util.implementacion.ListaEnlazada;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class VentanaMensajes extends JFrame {

    private Empleado empleado;
    private Propiedad propiedad;
    private MensajeDAOImpl mensajeDAO;

    private JTable tblMensajes;
    private JTextArea txtMensaje;
    private JButton btnResponder;
    private JButton btnCerrar;

    public VentanaMensajes(Empleado empleado, Propiedad propiedad) {
        this.empleado = empleado;
        this.propiedad = propiedad;
        this.mensajeDAO = new MensajeDAOImpl();

        setTitle("Mensajes - " + propiedad.getDireccion());
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        inicializarComponentes();
        cargarMensajes();
    }

    private void inicializarComponentes() {
        setLayout(new BorderLayout());

        // Panel superior con información de la propiedad
        JPanel panelInfo = new JPanel();
        panelInfo.setBorder(BorderFactory.createTitledBorder("Información de la Propiedad"));

        JLabel lblInfo = new JLabel("<html><b>Dirección:</b> " + propiedad.getDireccion()
                + "<br><b>Ciudad:</b> " + propiedad.getCiudad()
                + "<br><b>Tipo:</b> " + propiedad.getTipo()
                + "<br><b>Precio:</b> $" + propiedad.getPrecio() + "</html>");

        panelInfo.add(lblInfo);
        add(panelInfo, BorderLayout.NORTH);

        // Tabla de mensajes
        String[] columnas = {"Cliente", "Fecha", "Mensaje", "Respondido"};
        DefaultTableModel modelo = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tblMensajes = new JTable(modelo);
        JScrollPane scrollTabla = new JScrollPane(tblMensajes);
        add(scrollTabla, BorderLayout.CENTER);

        // Panel inferior para responder
        JPanel panelRespuesta = new JPanel(new BorderLayout());
        panelRespuesta.setBorder(BorderFactory.createTitledBorder("Responder Mensaje"));

        txtMensaje = new JTextArea(5, 40);
        JScrollPane scrollMensaje = new JScrollPane(txtMensaje);
        panelRespuesta.add(scrollMensaje, BorderLayout.CENTER);

        // Panel de botones completo (dentro del JPanel panelRespuesta)
        JPanel panelBotones = new JPanel(new FlowLayout());

// Botón Responder
        btnResponder = new JButton("Responder");
        btnResponder.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                responderMensaje();
            }
        });
        panelBotones.add(btnResponder);

// Botón Cerrar
        btnCerrar = new JButton("Cerrar");
        btnCerrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        panelBotones.add(btnCerrar);

// Botón Volver (opcional si ya tienes Cerrar, pero para mantener consistencia)
        JButton btnVolver = new JButton("Volver");
        btnVolver.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        panelBotones.add(btnVolver);

// Añadir el panel de botones al panel de respuesta
        panelRespuesta.add(panelBotones, BorderLayout.SOUTH);
    }

    private void cargarMensajes() {
        ListaEnlazada<Mensaje> mensajes = mensajeDAO.listarPorPropiedad(propiedad);

        DefaultTableModel modelo = (DefaultTableModel) tblMensajes.getModel();
        modelo.setRowCount(0); // Limpiar tabla

        for (int i = 0; i < mensajes.size(); i++) {
            Mensaje m = mensajes.get(i);

            // Solo mostrar mensajes para el empleado
            if (m.getDestinatario().equals(empleado.getEmail())
                    || m.getRemitente().equals(empleado.getEmail())) {

                String nombreCliente = "Desconocido";
                try {
                    // Buscar el nombre del cliente
                    for (int j = 0; j < Singleton.getINSTANCIA().getListaClientes().size(); j++) {
                        if (Singleton.getINSTANCIA().getListaClientes().get(j).getEmail().equals(m.getRemitente())) {
                            nombreCliente = Singleton.getINSTANCIA().getListaClientes().get(j).getNombreCompleto();
                            break;
                        }
                    }
                } catch (Exception e) {
                    // Ignorar error y usar "Desconocido"
                }

                modelo.addRow(new Object[]{
                    nombreCliente,
                    m.getFecha(),
                    m.getContenido(),
                    m.isRespondido() ? "Sí" : "No"
                });
            }
        }
    }

    private void responderMensaje() {
        int filaSeleccionada = tblMensajes.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un mensaje para responder",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        boolean yaRespondido = tblMensajes.getValueAt(filaSeleccionada, 3).equals("Sí");
        if (yaRespondido) {
            JOptionPane.showMessageDialog(this, "Este mensaje ya fue respondido",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (txtMensaje.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debe escribir una respuesta",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Buscar el mensaje original
        String clienteNombre = (String) tblMensajes.getValueAt(filaSeleccionada, 0);
        String clienteEmail = null;

        // Buscar el email del cliente
        for (int i = 0; i < Singleton.getINSTANCIA().getListaClientes().size(); i++) {
            if (Singleton.getINSTANCIA().getListaClientes().get(i).getNombreCompleto().equals(clienteNombre)) {
                clienteEmail = Singleton.getINSTANCIA().getListaClientes().get(i).getEmail();
                break;
            }
        }

        if (clienteEmail == null) {
            JOptionPane.showMessageDialog(this, "No se pudo identificar el cliente",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            // Marcar mensaje original como respondido
            ListaEnlazada<Mensaje> mensajes = mensajeDAO.listarPorPropiedad(propiedad);
            for (int i = 0; i < mensajes.size(); i++) {
                Mensaje m = mensajes.get(i);
                if (m.getRemitente().equals(clienteEmail) && !m.isRespondido()) {
                    mensajeDAO.marcarRespondido(m);
                    break;
                }
            }

            // Crear y enviar respuesta
            String id = "MSG" + System.currentTimeMillis();
            Mensaje respuesta = new Mensaje(id, propiedad, empleado.getEmail(), clienteEmail, txtMensaje.getText());
            mensajeDAO.guardar(respuesta);

            JOptionPane.showMessageDialog(this, "Respuesta enviada correctamente",
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);

            txtMensaje.setText("");
            cargarMensajes();

        } catch (MensajePendienteException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al enviar la respuesta: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
