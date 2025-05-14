package vista.administrador;

import controlador.ControladorAdministrador;
import dao.implementacion.EmpleadoDAOImpl;
import dao.implementacion.MatrizSedesDAOImpl;
import dao.implementacion.PropiedadDAOImpl;
import dao.implementacion.SedeDAOImpl;
import excepciones.SedeExistenteException;
import modelo.MatrizSedes;
import modelo.Sede;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class VentanaMatrizSedes extends JFrame {

    private ControladorAdministrador controlador;
    private JButton[][] botonesMatriz;
    private JPanel panelMatriz;

    public VentanaMatrizSedes() {
        setTitle("Matriz de Sedes");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        controlador = new ControladorAdministrador(
                new SedeDAOImpl(),
                new EmpleadoDAOImpl(),
                new MatrizSedesDAOImpl(),
                new PropiedadDAOImpl()
        );

        inicializarComponentes();
        actualizarMatriz();
    }

    private void inicializarComponentes() {
        setLayout(new BorderLayout());

        // Panel superior con título
        JPanel panelTitulo = new JPanel();
        JLabel lblTitulo = new JLabel("MATRIZ DE SEDES");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 24));
        panelTitulo.add(lblTitulo);
        add(panelTitulo, BorderLayout.NORTH);

        // Panel de matriz
        panelMatriz = new JPanel(new GridLayout(4, 5, 10, 10));
        panelMatriz.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        botonesMatriz = new JButton[4][5];

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 5; j++) {
                final int fila = i;
                final int columna = j;

                botonesMatriz[i][j] = new JButton();
                botonesMatriz[i][j].setPreferredSize(new Dimension(120, 80));
                botonesMatriz[i][j].addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        seleccionarCasilla(fila, columna);
                    }
                });

                panelMatriz.add(botonesMatriz[i][j]);
            }
        }

        add(panelMatriz, BorderLayout.CENTER);

        // Panel de leyenda
        JPanel panelLeyenda = new JPanel(new FlowLayout());
        panelLeyenda.setBorder(BorderFactory.createTitledBorder("Leyenda"));

        JPanel cuadroBlanco = new JPanel();
        cuadroBlanco.setBackground(Color.WHITE);
        cuadroBlanco.setPreferredSize(new Dimension(20, 20));
        panelLeyenda.add(cuadroBlanco);
        panelLeyenda.add(new JLabel("No asignada"));

        JPanel cuadroGris = new JPanel();
        cuadroGris.setBackground(Color.DARK_GRAY);
        cuadroGris.setPreferredSize(new Dimension(20, 20));
        panelLeyenda.add(cuadroGris);
        panelLeyenda.add(new JLabel("Asignada"));

        JPanel cuadroAmarillo = new JPanel();
        cuadroAmarillo.setBackground(Color.YELLOW);
        cuadroAmarillo.setPreferredSize(new Dimension(20, 20));
        panelLeyenda.add(cuadroAmarillo);
        panelLeyenda.add(new JLabel("Sin cupo"));

        // Panel contenedor para leyenda y botones
        JPanel panelInferior = new JPanel(new BorderLayout());
        panelInferior.add(panelLeyenda, BorderLayout.CENTER);

        // Panel de botones completo
        JPanel panelBotones = new JPanel(new FlowLayout());

        // Botón Refrescar
        JButton btnRefrescar = new JButton("Refrescar");
        btnRefrescar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                actualizarMatriz();
            }
        });
        panelBotones.add(btnRefrescar);

        // Botón Volver
        JButton btnVolver = new JButton("Volver");
        btnVolver.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        panelBotones.add(btnVolver);

        // Añadir panel de botones al panel inferior
        panelInferior.add(panelBotones, BorderLayout.SOUTH);

        // Añadir panel inferior al marco principal
        add(panelInferior, BorderLayout.SOUTH);
    }

    private void actualizarMatriz() {
        MatrizSedes matriz = controlador.obtenerMatrizSedes();

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 5; j++) {
                Sede sede = matriz.obtenerSede(i, j);

                if (sede == null) {
                    botonesMatriz[i][j].setText("");
                    botonesMatriz[i][j].setBackground(Color.WHITE);
                    botonesMatriz[i][j].setForeground(Color.BLACK);
                } else {
                    botonesMatriz[i][j].setText("<html>" + sede.getCiudad() + "<br>Cupo: "
                            + sede.getCantidadInmueblesActual() + "/" + sede.getCapacidadInmuebles() + "</html>");

                    if (sede.tieneCupo()) {
                        botonesMatriz[i][j].setBackground(Color.DARK_GRAY);
                        botonesMatriz[i][j].setForeground(Color.WHITE);
                    } else {
                        botonesMatriz[i][j].setBackground(Color.YELLOW);
                        botonesMatriz[i][j].setForeground(Color.BLACK);
                    }
                }
            }
        }
    }

    private void seleccionarCasilla(int fila, int columna) {
        MatrizSedes matriz = controlador.obtenerMatrizSedes();
        Sede sede = matriz.obtenerSede(fila, columna);

        if (sede == null) {
            // Registrar nueva sede
            JTextField txtCiudad = new JTextField();
            JTextField txtCapacidad = new JTextField();

            Object[] campos = {
                "Ciudad:", txtCiudad,
                "Capacidad:", txtCapacidad
            };

            int resultado = JOptionPane.showConfirmDialog(this, campos,
                    "Registrar Nueva Sede", JOptionPane.OK_CANCEL_OPTION);

            if (resultado == JOptionPane.OK_OPTION) {
                if (txtCiudad.getText().isEmpty() || txtCapacidad.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                try {
                    int capacidad = Integer.parseInt(txtCapacidad.getText());

                    if (capacidad <= 0) {
                        JOptionPane.showMessageDialog(this, "La capacidad debe ser mayor a cero",
                                "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    Sede nuevaSede = new Sede(txtCiudad.getText(), capacidad);
                    controlador.registrarSede(nuevaSede, fila, columna);

                    JOptionPane.showMessageDialog(this, "Sede registrada correctamente",
                            "Éxito", JOptionPane.INFORMATION_MESSAGE);

                    actualizarMatriz();

                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(this, "La capacidad debe ser un número entero",
                            "Error", JOptionPane.ERROR_MESSAGE);
                } catch (SedeExistenteException e) {
                    JOptionPane.showMessageDialog(this, e.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, "Error al registrar la sede: " + e.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            // Editar sede existente
            JTextField txtCapacidad = new JTextField(String.valueOf(sede.getCapacidadInmuebles()));

            Object[] campos = {
                "Ciudad: " + sede.getCiudad(),
                "Nueva capacidad:", txtCapacidad
            };

            int resultado = JOptionPane.showConfirmDialog(this, campos,
                    "Editar Capacidad", JOptionPane.OK_CANCEL_OPTION);

            if (resultado == JOptionPane.OK_OPTION) {
                try {
                    int nuevaCapacidad = Integer.parseInt(txtCapacidad.getText());

                    if (nuevaCapacidad <= 0) {
                        JOptionPane.showMessageDialog(this, "La capacidad debe ser mayor a cero",
                                "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    if (nuevaCapacidad < sede.getCantidadInmueblesActual()) {
                        JOptionPane.showMessageDialog(this, "La nueva capacidad no puede ser menor a la cantidad actual de inmuebles",
                                "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    controlador.actualizarCapacidadSede(sede.getCiudad(), nuevaCapacidad);

                    JOptionPane.showMessageDialog(this, "Capacidad actualizada correctamente",
                            "Éxito", JOptionPane.INFORMATION_MESSAGE);

                    actualizarMatriz();

                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(this, "La capacidad debe ser un número entero",
                            "Error", JOptionPane.ERROR_MESSAGE);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, "Error al actualizar la capacidad: " + e.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }
}
