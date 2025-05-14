package vista.cliente;

import controlador.ControladorCliente;
import dao.implementacion.AgendaDAOImpl;
import dao.implementacion.ClienteDAOImpl;
import dao.implementacion.MensajeDAOImpl;
import dao.implementacion.PropiedadDAOImpl;
import excepciones.ConflictoHorarioException;
import excepciones.UsuarioNoEncontradoException;
import excepciones.VisitasExcedidasException;
import modelo.Cliente;
import modelo.Propiedad;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class VentanaAgendarVisita extends JFrame {
    
    private ControladorCliente controlador;
    private Cliente cliente;
    private Propiedad propiedad;
    
    private JTextField txtFecha;
    private JComboBox<Integer> cmbHora;
    private JComboBox<Integer> cmbDuracion;
    private JButton btnAgendar;
    private JButton btnVolver;
    
    public VentanaAgendarVisita(Cliente cliente, Propiedad propiedad) {
        this.cliente = cliente;
        this.propiedad = propiedad;
        
        setTitle("Agendar Visita - " + propiedad.getDireccion());
        setSize(400, 300);
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
        
        // Título
        JLabel lblTitulo = new JLabel("Agendar Visita");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 16));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(lblTitulo, gbc);
        
        // Fecha
        JLabel lblFecha = new JLabel("Fecha (dd/MM/yyyy):");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        panel.add(lblFecha, gbc);
        
        txtFecha = new JTextField(10);
        gbc.gridx = 1;
        gbc.gridy = 1;
        panel.add(txtFecha, gbc);
        
        // Hora
        JLabel lblHora = new JLabel("Hora de inicio:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(lblHora, gbc);
        
        cmbHora = new JComboBox<>();
        for (int i = 8; i <= 16; i++) {
            cmbHora.addItem(i);
        }
        gbc.gridx = 1;
        gbc.gridy = 2;
        panel.add(cmbHora, gbc);
        
        // Duración
        JLabel lblDuracion = new JLabel("Duración (horas):");
        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(lblDuracion, gbc);
        
        cmbDuracion = new JComboBox<>();
        for (int i = 1; i <= 3; i++) {
            cmbDuracion.addItem(i);
        }
        gbc.gridx = 1;
        gbc.gridy = 3;
        panel.add(cmbDuracion, gbc);
        
        // Botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER));
        
        btnAgendar = new JButton("Agendar");
        btnAgendar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                agendarVisita();
            }
        });
        panelBotones.add(btnAgendar);
        
        btnVolver = new JButton("Volver");
        btnVolver.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        panelBotones.add(btnVolver);
        
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        panel.add(panelBotones, gbc);
        
        getContentPane().add(panel);
    }
    
    private void agendarVisita() {
        try {
            // Validar y parsear fecha
            if (txtFecha.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Debe ingresar una fecha", 
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            sdf.setLenient(false);
            Date fecha = sdf.parse(txtFecha.getText());
            
            // Validar fecha (no puede ser en el pasado)
            Date hoy = new Date();
            if (fecha.before(hoy)) {
                JOptionPane.showMessageDialog(this, "La fecha debe ser futura", 
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Validar ciudad diferente
            if (!cliente.getCiudadResidencia().equals(propiedad.getCiudad())) {
                // Calcular 5 días después
                long cincosDiasDespues = hoy.getTime() + (5L * 24 * 60 * 60 * 1000);
                Date fechaMinima = new Date(cincosDiasDespues);
                
                if (fecha.before(fechaMinima)) {
                    JOptionPane.showMessageDialog(this, 
                            "Para inmuebles en otra ciudad, la visita debe ser al menos 5 días después", 
                            "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
            
            int hora = (int) cmbHora.getSelectedItem();
            int duracion = (int) cmbDuracion.getSelectedItem();
            
            // Agendar visita
            controlador.agendarVisita(propiedad.getId(), cliente.getEmail(), fecha, hora, duracion);
            
            JOptionPane.showMessageDialog(this, "Visita agendada correctamente", 
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);
            
            dispose();
            
        } catch (ParseException e) {
            JOptionPane.showMessageDialog(this, "Formato de fecha inválido (use dd/MM/yyyy)", 
                    "Error", JOptionPane.ERROR_MESSAGE);
        } catch (UsuarioNoEncontradoException | ConflictoHorarioException | VisitasExcedidasException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al agendar visita: " + e.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}