package vista.empleado;

import controlador.ControladorEmpleado;
import dao.implementacion.EmpleadoDAOImpl;
import dao.implementacion.PropiedadDAOImpl;
import dao.implementacion.SedeDAOImpl;
import modelo.Propiedad;
import singleton.Singleton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Esta clase extiende a VentanaEmpleado para añadir funcionalidad adicional
 * Esta implementación muestra cómo añadir un botón para acceder a la vista de historial
 * detallada de propiedades cerradas
 */
public class ModificadorVentanaEmpleado {

    /**
     * Método para añadir el botón de ver historial completo a la ventana de empleado
     * Este método debe ser llamado desde el constructor de VentanaEmpleado después
     * de inicializar el panel de historial
     * 
     * @param ventana La ventana actual del empleado
     * @param panelBotones El panel de botones donde se añadirá el nuevo botón
     */
    public static void añadirBotonHistorialCompleto(VentanaEmpleado ventana, JPanel panelBotones) {
        JButton btnVerHistorialCompleto = new JButton("Ver Historial Completo");
        btnVerHistorialCompleto.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                abrirVentanaHistorial();
            }
        });
        panelBotones.add(btnVerHistorialCompleto);
    }
    
    /**
     * Método para abrir la ventana de historial detallado
     */
    private static void abrirVentanaHistorial() {
        if (Singleton.getINSTANCIA().getUsuarioActual() instanceof modelo.Empleado) {
            modelo.Empleado empleado = (modelo.Empleado) Singleton.getINSTANCIA().getUsuarioActual();
            VentanaHistorialPropiedades ventanaHistorial = new VentanaHistorialPropiedades(empleado);
            ventanaHistorial.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(null, 
                    "Error de autenticación: el usuario no es un empleado", 
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Método alternativo para integrar directamente en la clase VentanaEmpleado
     * 
     * Este código debe ser añadido al método inicializarPanelHistorial() en la clase VentanaEmpleado
     */
    public static String obtenerCodigoBotonHistorial() {
        return """
            // Botón para ver historial completo
            JButton btnVerHistorialCompleto = new JButton("Ver Historial Completo");
            btnVerHistorialCompleto.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    VentanaHistorialPropiedades ventanaHistorial = new VentanaHistorialPropiedades(empleadoActual);
                    ventanaHistorial.setVisible(true);
                }
            });
            panelBotones.add(btnVerHistorialCompleto);
        """;
    }
}