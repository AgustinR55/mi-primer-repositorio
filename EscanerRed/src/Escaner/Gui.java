/*
package Escaner;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Gui extends JFrame {

    private JTextField txtIPInicio;
    private JTextField txtIPFin;
    private JTextField txtTimeout;
    private JTextArea txtResultado;
    private JProgressBar progressBar;

    public Gui() {
        setTitle("Escáner de IPs");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Panel superior con campos de entrada
        JPanel panelSuperior = new JPanel(new GridLayout(4, 2, 5, 5));
        panelSuperior.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        panelSuperior.add(new JLabel("IP de inicio:"));
        txtIPInicio = new JTextField("8.8.8.1");
        panelSuperior.add(txtIPInicio);

        panelSuperior.add(new JLabel("IP de fin:"));
        txtIPFin = new JTextField("8.8.8.5");
        panelSuperior.add(txtIPFin);

        panelSuperior.add(new JLabel("Timeout (ms):"));
        txtTimeout = new JTextField("1000");
        panelSuperior.add(txtTimeout);

        JButton btnEscanear = new JButton("Escanear");
        JButton btnLimpiar = new JButton("Limpiar");
        panelSuperior.add(btnEscanear);
        panelSuperior.add(btnLimpiar);

        add(panelSuperior, BorderLayout.NORTH);

        // Área de resultados
        txtResultado = new JTextArea();
        txtResultado.setEditable(false);
        JScrollPane scroll = new JScrollPane(txtResultado);
        add(scroll, BorderLayout.CENTER);

        // Barra de progreso
        progressBar = new JProgressBar();
        progressBar.setStringPainted(true);
        add(progressBar, BorderLayout.SOUTH);

        // Acción de escanear
        btnEscanear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                escanear();
            }
        });

        // Acción de limpiar
        btnLimpiar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                txtResultado.setText("");
                progressBar.setValue(0);
            }
        });
    }

    private void escanear() {
        String ipInicio = txtIPInicio.getText().trim();
        String ipFin = txtIPFin.getText().trim();
        int timeout;

        try {
            timeout = Integer.parseInt(txtTimeout.getText().trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Tiempo de espera inválido", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        txtResultado.setText("Escaneando...\n");

        new Thread(() -> {
            try {
                long start = ipToLong(ipInicio);
                long end = ipToLong(ipFin);

                progressBar.setMinimum(0);
                progressBar.setMaximum((int) (end - start + 1));

                int progress = 0;

                for (long ip = start; ip <= end; ip++) {
                    String ipActual = longToIp(ip);

                    boolean activa = pingHost(ipActual, timeout);
                    String estado = activa ? "ACTIVA" : "Inactiva";

                    String nombreHost = activa ? getHostName(ipActual) : "-";

                    txtResultado.append(ipActual + " - " + estado + " / " + nombreHost + "\n");

                    progress++;
                    progressBar.setValue(progress);
                }

            } catch (Exception e) {
                SwingUtilities.invokeLater(() ->
                        JOptionPane.showMessageDialog(this, "Error al escanear: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE)
                );
            }
        }).start();
    }

    private boolean pingHost(String ip, int timeout) {
        try {
            Process process = new ProcessBuilder("ping", "-n", "1", "-w", String.valueOf(timeout), ip).start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains("tiempo") || line.contains("TTL") || line.contains("time=")) {
                    return true;
                }
            }
        } catch (Exception ignored) {
        }
        return false;
    }

    private String getHostName(String ip) {
        try {
            Process process = new ProcessBuilder("nslookup", ip).start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.toLowerCase().contains("name") || line.toLowerCase().contains("nombre")) {
                    return line.split(":")[1].trim();
                }
            }
        } catch (Exception ignored) {
        }
        return "Nombre desconocido";
    }

    private long ipToLong(String ip) {
        String[] parts = ip.split("\\.");
        return (Long.parseLong(parts[0]) << 24)
                + (Long.parseLong(parts[1]) << 16)
                + (Long.parseLong(parts[2]) << 8)
                + Long.parseLong(parts[3]);
    }

    private String longToIp(long ip) {
        return ((ip >> 24) & 0xFF) + "." +
                ((ip >> 16) & 0xFF) + "." +
                ((ip >> 8) & 0xFF) + "." +
                (ip & 0xFF);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Gui ventana = new Gui();
            ventana.setVisible(true);
        });
    }
}
*/
package Escaner;


import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.*;
import java.net.InetAddress;
import java.util.regex.Pattern;

public class Gui extends JFrame {

    private JTextField txtIPInicio;
    private JTextField txtIPFin;
    private JTextField txtTimeout;
    private JTable tablaResultados;
    private DefaultTableModel modeloTabla;
    private JProgressBar progressBar;
    private JLabel lblResumen;
    private int equiposConectados = 0;

    public Gui() {
        setTitle("Escáner de IPs");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Panel superior con campos de entrada
        JPanel panelSuperior = new JPanel(new GridLayout(4, 2, 5, 5));
        panelSuperior.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        panelSuperior.add(new JLabel("IP de inicio:"));
        txtIPInicio = new JTextField("8.8.8.1");
        panelSuperior.add(txtIPInicio);

        panelSuperior.add(new JLabel("IP de fin:"));
        txtIPFin = new JTextField("8.8.8.5");
        panelSuperior.add(txtIPFin);

        panelSuperior.add(new JLabel("Timeout (ms):"));
        txtTimeout = new JTextField("1000");
        panelSuperior.add(txtTimeout);

        JButton btnEscanear = new JButton("Escanear");
        JButton btnLimpiar = new JButton("Limpiar");
        panelSuperior.add(btnEscanear);
        panelSuperior.add(btnLimpiar);

        add(panelSuperior, BorderLayout.NORTH);

        // Modelo y tabla
        modeloTabla = new DefaultTableModel(new String[]{"Dirección IP", "Nombre", "Estado", "Tiempo (ms)"}, 0);
        tablaResultados = new JTable(modeloTabla);
        JScrollPane scrollTabla = new JScrollPane(tablaResultados);
        add(scrollTabla, BorderLayout.CENTER);

        // Barra de progreso y resumen
        JPanel panelInferior = new JPanel(new BorderLayout());
        progressBar = new JProgressBar();
        progressBar.setStringPainted(true);
        lblResumen = new JLabel("Equipos conectados: 0");
        panelInferior.add(progressBar, BorderLayout.CENTER);
        panelInferior.add(lblResumen, BorderLayout.SOUTH);
        add(panelInferior, BorderLayout.SOUTH);

        // Panel de botones inferiores
        JPanel panelBotones = new JPanel();
        JButton btnGuardar = new JButton("Guardar en archivo");
        panelBotones.add(btnGuardar);
        add(panelBotones, BorderLayout.EAST);

        // Acción de escanear
        btnEscanear.addActionListener(e -> escanear());

        // Acción de limpiar
        btnLimpiar.addActionListener(e -> {
            modeloTabla.setRowCount(0);
            progressBar.setValue(0);
            lblResumen.setText("Equipos conectados: 0");
            equiposConectados = 0;
        });

        // Acción de guardar
        btnGuardar.addActionListener(e -> guardarArchivo());
    }

    private void escanear() {
        String ipInicio = txtIPInicio.getText().trim();
        String ipFin = txtIPFin.getText().trim();
        int timeout;

        // Validar IPs
        if (!validarIP(ipInicio) || !validarIP(ipFin)) {
            JOptionPane.showMessageDialog(this, "Formato de IP inválido", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            timeout = Integer.parseInt(txtTimeout.getText().trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Tiempo de espera inválido", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        modeloTabla.setRowCount(0);
        equiposConectados = 0;

        new Thread(() -> {
            try {
                long start = ipToLong(ipInicio);
                long end = ipToLong(ipFin);

                progressBar.setMinimum(0);
                progressBar.setMaximum((int) (end - start + 1));

                int progress = 0;

                for (long ip = start; ip <= end; ip++) {
                    String ipActual = longToIp(ip);

                    long tiempoInicio = System.currentTimeMillis();
                    boolean activa = pingHost(ipActual, timeout);
                    long tiempoRespuesta = System.currentTimeMillis() - tiempoInicio;

                    String estado = activa ? "Conectado" : "Inactivo";
                    String nombreHost = activa ? getHostName(ipActual) : "-";

                    if (activa) {
                        equiposConectados++;
                    }

                    SwingUtilities.invokeLater(() -> {
                        modeloTabla.addRow(new Object[]{ipActual, nombreHost, estado, activa ? tiempoRespuesta : "-"});
                        lblResumen.setText("Equipos conectados: " + equiposConectados);
                    });

                    progress++;
                    progressBar.setValue(progress);
                }

            } catch (Exception e) {
                SwingUtilities.invokeLater(() ->
                        JOptionPane.showMessageDialog(this, "Error al escanear: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE)
                );
            }
        }).start();
    }

    private boolean pingHost(String ip, int timeout) {
        try {
            InetAddress address = InetAddress.getByName(ip);
            return address.isReachable(timeout);
        } catch (Exception ignored) {
        }
        return false;
    }

    private String getHostName(String ip) {
        try {
            InetAddress address = InetAddress.getByName(ip);
            return address.getCanonicalHostName();
        } catch (Exception ignored) {
        }
        return "Nombre desconocido";
    }

    private boolean validarIP(String ip) {
        String ipRegex =
                "^((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)\\.){3}" +
                "(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)$";
        return Pattern.matches(ipRegex, ip);
    }

    private long ipToLong(String ip) {
        String[] parts = ip.split("\\.");
        return (Long.parseLong(parts[0]) << 24)
                + (Long.parseLong(parts[1]) << 16)
                + (Long.parseLong(parts[2]) << 8)
                + Long.parseLong(parts[3]);
    }

    private String longToIp(long ip) {
        return ((ip >> 24) & 0xFF) + "." +
                ((ip >> 16) & 0xFF) + "." +
                ((ip >> 8) & 0xFF) + "." +
                (ip & 0xFF);
    }

    private void guardarArchivo() {
        JFileChooser fileChooser = new JFileChooser();
        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            File archivo = fileChooser.getSelectedFile();
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(archivo))) {
                for (int i = 0; i < modeloTabla.getRowCount(); i++) {
                    writer.write(
                        modeloTabla.getValueAt(i, 0) + " - " +
                        modeloTabla.getValueAt(i, 1) + " - " +
                        modeloTabla.getValueAt(i, 2) + " - " +
                        modeloTabla.getValueAt(i, 3) + " ms"
                    );
                    writer.newLine();
                }
                JOptionPane.showMessageDialog(this, "Archivo guardado correctamente");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error al guardar el archivo", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Gui ventana = new Gui();
            ventana.setVisible(true);
        });
    }
}



 
