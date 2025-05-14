package vista.cliente;

import modelo.Propiedad;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class VentanaVerDetallesPropiedad extends JFrame {
    
    private Propiedad propiedad;
    private JTextArea txtDetalles;
    private JButton btnVolver;
    
    public VentanaVerDetallesPropiedad(Propiedad propiedad) {
        this.propiedad = propiedad;
        
        setTitle("Detalles de Propiedad");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        
        inicializarComponentes();
    }
    
    private void inicializarComponentes() {
        JPanel panel = new JPanel(new BorderLayout());
        
        // Panel de detalles
        txtDetalles = new JTextArea();
        txtDetalles.setEditable(false);
        cargarDetalles();
        JScrollPane scrollPane = new JScrollPane(txtDetalles);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        // Panel de botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER));
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
    
    private void cargarDetalles() {
        StringBuilder sb = new StringBuilder();
        sb.append("ID: ").append(propiedad.getId()).append("\n");
        sb.append("Dirección: ").append(propiedad.getDireccion()).append("\n");
        sb.append("Ciudad: ").append(propiedad.getCiudad()).append("\n");
        sb.append("Tipo: ").append(propiedad.getTipo()).append("\n");
        sb.append("Precio: $").append(propiedad.getPrecio()).append("\n");
        sb.append("Estado: ").append(propiedad.getEstado()).append("\n");
        sb.append("Descripción: ").append(propiedad.getDescripcion()).append("\n\n");
        
        if (propiedad.getEncargado() != null) {
            sb.append("Encargado: ").append(propiedad.getEncargado().getNombreCompleto()).append("\n");
            sb.append("Teléfono: ").append(propiedad.getEncargado().getTelefono()).append("\n");
            sb.append("Email: ").append(propiedad.getEncargado().getEmail()).append("\n\n");
        }
        
        sb.append("Permite agendar visita: ").append(propiedad.isPermiteAgendarVisita() ? "Sí" : "No").append("\n\n");
        
        if (propiedad.getInmueble() != null) {
            sb.append("DETALLES DEL INMUEBLE\n");
            sb.append("Tipo de inmueble: ").append(propiedad.getInmueble().getTipoInmueble()).append("\n");
            sb.append("Estrato: ").append(propiedad.getInmueble().getEstrato()).append("\n");
            sb.append("Características adicionales: ").append(propiedad.getInmueble().getCaracteristicasAdicionales()).append("\n");
        }
        
        txtDetalles.setText(sb.toString());
    }
}