/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package proyectoinmobiliaria;

import vista.VentanaLogin;

/**
 *
 * @author kevinrengifo
 */
public class ProyectoInmobiliaria {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // Asegurar que la aplicación se inicie desde la ventana de login
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                // Iniciar la aplicación con la pantalla de login
                VentanaLogin ventanaLogin = new VentanaLogin();
                ventanaLogin.setVisible(true);
            }
        });
    }

}
