package vista.cliente;

import controlador.ControladorCliente;
import dao.implementacion.AgendaDAOImpl;
import dao.implementacion.ClienteDAOImpl;
import dao.implementacion.MensajeDAOImpl;
import dao.implementacion.PropiedadDAOImpl;
import excepciones.MensajePendienteException;
import excepciones.UsuarioNoEncontradoException;
import modelo.Cliente;
import modelo.Propiedad;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class VentanaEnviarMensaje extends JFrame {

    private ControladorCliente controlador;
    private Cliente cliente;
    private Propiedad propiedad;

    private JTextArea txtMensaje;
    private JButton btnEnviar;
    private JButton btnVolver;

    public VentanaEnviarMensaje(Cliente cliente, Propiedad propiedad) {
        this.cliente = cliente;
        this.propiedad = propiedad;

        setTitle("Enviar Mensaje - " + propiedad.getDireccion());
        setSize(500, 400);
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
        JPanel panel = new JPanel(new BorderLayout());

        // Panel de info de propiedad
        JPanel panelInfo = new JPanel();
        panelInfo.setBorder(BorderFactory.createTitledBorder("Propiedad"));

        JLabel lblInfo = new JLabel("<html><b>Dirección:</b> " + propiedad.getDireccion()
                + "<br><b>Ciudad:</b> " + propiedad.getCiudad()
                + "<br><b>Tipo:</b> " + propiedad.getTipo() + "</html>");

        panelInfo.add(lblInfo);
        panel.add(panelInfo, BorderLayout.NORTH);

        // Panel de mensaje
        JPanel panelMensaje = new JPanel(new BorderLayout());
        panelMensaje.setBorder(BorderFactory.createTitledBorder("Mensaje"));

        txtMensaje = new JTextArea(10, 40);
        JScrollPane scrollPane = new JScrollPane(txtMensaje);
        panelMensaje.add(scrollPane, BorderLayout.CENTER);

        // Panel de botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER));

        btnEnviar = new JButton("Enviar");
        btnEnviar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                enviarMensaje();
            }
        });
        panelBotones.add(btnEnviar);

        btnVolver = new JButton("Volver");
        btnVolver.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        panelBotones.add(btnVolver);

        panelMensaje.add(panelBotones, BorderLayout.SOUTH);
        panel.add(panelMensaje, BorderLayout.CENTER);

        getContentPane().add(panel);
    }

    private void enviarMensaje() {
        if (txtMensaje.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debe escribir un mensaje",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            controlador.enviarMensaje(propiedad.getId(), cliente.getEmail(), txtMensaje.getText());

            JOptionPane.showMessageDialog(this, "Mensaje enviado correctamente",
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);

            dispose();

        } catch (UsuarioNoEncontradoException | MensajePendienteException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al enviar mensaje: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
