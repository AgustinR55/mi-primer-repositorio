package Escaner;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Gui extends JFrame {

    private JTextField txtIPInicio;
    private JTextField txtIPFin;
    private JTextField txtTimeout;
    private JTextArea txtResultado;

    public Gui() {
        setTitle("Escáner de Rango de IP");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 400);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Panel superior para entradas
        JPanel panelEntrada = new JPanel(new GridLayout(4, 2, 10, 10));
        panelEntrada.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        panelEntrada.add(new JLabel("IP de Inicio:"));
        txtIPInicio = new JTextField();
        panelEntrada.add(txtIPInicio);

        panelEntrada.add(new JLabel("IP de Fin:"));
        txtIPFin = new JTextField();
        panelEntrada.add(txtIPFin);

        panelEntrada.add(new JLabel("Tiempo de espera (ms):"));
        txtTimeout = new JTextField("1000");
        panelEntrada.add(txtTimeout);

        panelEntrada.add(new JLabel()); // Espacio vacío

        // Panel de botones
        JPanel panelBotones = new JPanel(new FlowLayout());

        JButton btnEscanear = new JButton("Escanear");
        JButton btnLimpiar = new JButton("Limpiar");

        panelBotones.add(btnEscanear);
        panelBotones.add(btnLimpiar);

        // Área de resultados
        txtResultado = new JTextArea();
        txtResultado.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(txtResultado);

        // Agregar a la ventana
        add(panelEntrada, BorderLayout.NORTH);
        add(panelBotones, BorderLayout.CENTER);
        add(scrollPane, BorderLayout.SOUTH);

        // Eventos
        btnEscanear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                escanearRango();
            }
        });

        btnLimpiar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                limpiarPantalla();
            }
        });
    }

    private void escanearRango() {
        String ipInicio = txtIPInicio.getText().trim();
        String ipFin = txtIPFin.getText().trim();
        String timeoutStr = txtTimeout.getText().trim();

        // Validaciones mínimas
        if (ipInicio.isEmpty() || ipFin.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor ingrese ambas IPs.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int timeout;
        try {
            timeout = Integer.parseInt(timeoutStr);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Tiempo de espera no válido.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // (Lógica simulada por ahora)
        txtResultado.append("Escaneando desde " + ipInicio + " hasta " + ipFin + " con timeout " + timeout + "ms...\n");
        txtResultado.append(">> Resultado simulado. Aquí se mostrarían IPs activas.\n");
    }

    private void limpiarPantalla() {
        txtIPInicio.setText("");
        txtIPFin.setText("");
        txtTimeout.setText("1000");
        txtResultado.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new Gui().setVisible(true);
        });
    }
}