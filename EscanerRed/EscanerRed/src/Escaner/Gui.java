/*
package Escaner;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.io.*;

public class Gui extends JFrame {

    private JTextField txtIPInicio;
    private JTextField txtIPFin;
    private JTextField txtTimeout;
    private JTable tablaResultados;
    private DefaultTableModel modeloTabla;
    private JProgressBar progressBar;
    private JLabel lblResumen;
    private int equiposConectados = 0;

    private TableRowSorter<DefaultTableModel> sorter;
    private JTextField txtFiltro;

    public Gui() {
        setTitle("Escáner de IPs");
        setSize(800, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        Color colorFondo = new Color(40, 44, 52);
        Color colorPanel = new Color(60, 63, 70);
        Color colorTexto = new Color(230, 230, 230);
        Color colorBoton = new Color(100, 149, 237);
        Color colorTablaHeader = new Color(75, 110, 175);

        getContentPane().setBackground(colorFondo);

        // --- Panel superior
        JPanel panelSuperior = new JPanel(new GridLayout(4, 2, 5, 5));
        panelSuperior.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panelSuperior.setBackground(colorPanel);

        JLabel lblInicio = new JLabel("IP de inicio:");
        lblInicio.setForeground(colorTexto);
        panelSuperior.add(lblInicio);
        txtIPInicio = new JTextField("8.8.8.1");
        panelSuperior.add(txtIPInicio);

        JLabel lblFin = new JLabel("IP de fin:");
        lblFin.setForeground(colorTexto);
        panelSuperior.add(lblFin);
        txtIPFin = new JTextField("8.8.8.5");
        panelSuperior.add(txtIPFin);

        JLabel lblTimeout = new JLabel("Timeout (ms):");
        lblTimeout.setForeground(colorTexto);
        panelSuperior.add(lblTimeout);
        txtTimeout = new JTextField("1000");
        panelSuperior.add(txtTimeout);

        JButton btnEscanear = new JButton("Escanear");
        JButton btnLimpiar = new JButton("Limpiar");

        btnEscanear.setBackground(colorBoton);
        btnEscanear.setForeground(Color.WHITE);
        btnEscanear.setFocusPainted(false);

        btnLimpiar.setBackground(new Color(220, 20, 60));
        btnLimpiar.setForeground(Color.WHITE);
        btnLimpiar.setFocusPainted(false);

        panelSuperior.add(btnEscanear);
        panelSuperior.add(btnLimpiar);
        add(panelSuperior, BorderLayout.NORTH);

        // --- Tabla
        modeloTabla = new DefaultTableModel(new String[]{"Dirección IP", "Nombre", "Estado", "Tiempo (ms)"}, 0);
        tablaResultados = new JTable(modeloTabla);

        sorter = new TableRowSorter<>(modeloTabla);
        tablaResultados.setRowSorter(sorter);

        tablaResultados.setBackground(colorFondo);
        tablaResultados.setForeground(colorTexto);
        tablaResultados.setGridColor(Color.GRAY);
        tablaResultados.setRowHeight(25);
        tablaResultados.setSelectionBackground(new Color(100, 149, 237));
        tablaResultados.setSelectionForeground(Color.WHITE);

        JTableHeader header = tablaResultados.getTableHeader();
        header.setBackground(colorTablaHeader);
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));

        JScrollPane scrollTabla = new JScrollPane(tablaResultados);
        scrollTabla.getViewport().setBackground(colorFondo);
        add(scrollTabla, BorderLayout.CENTER);

        // --- Barra inferior
        JPanel panelInferior = new JPanel(new BorderLayout());
        panelInferior.setBackground(colorPanel);

        progressBar = new JProgressBar();
        progressBar.setStringPainted(true);
        progressBar.setForeground(new Color(50, 205, 50));

        lblResumen = new JLabel("Equipos conectados: 0");
        lblResumen.setForeground(colorTexto);

        panelInferior.add(progressBar, BorderLayout.CENTER);
        panelInferior.add(lblResumen, BorderLayout.SOUTH);
        add(panelInferior, BorderLayout.SOUTH);

        // --- Panel lateral
        JPanel panelDerecho = new JPanel();
        panelDerecho.setLayout(new BoxLayout(panelDerecho, BoxLayout.Y_AXIS));
        panelDerecho.setBackground(colorPanel);

        JButton btnGuardar = new JButton("Guardar en archivo");
        btnGuardar.setBackground(new Color(46, 139, 87));
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.setFocusPainted(false);
        panelDerecho.add(btnGuardar);

        panelDerecho.add(Box.createVerticalStrut(15));
        JLabel lblFiltro = new JLabel("Filtrar resultados:");
        lblFiltro.setForeground(colorTexto);
        panelDerecho.add(lblFiltro);

        txtFiltro = new JTextField();
        panelDerecho.add(txtFiltro);

        add(panelDerecho, BorderLayout.EAST);

        // Acciones
        btnEscanear.addActionListener(e -> escanear());
        btnLimpiar.addActionListener(e -> {
            modeloTabla.setRowCount(0);
            progressBar.setValue(0);
            lblResumen.setText("Equipos conectados: 0");
            equiposConectados = 0;
        });
        btnGuardar.addActionListener(e -> guardarArchivo());
        txtFiltro.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { filtrar(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { filtrar(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { filtrar(); }
        });
    }

    private void escanear() {
        String ipInicio = txtIPInicio.getText().trim();
        String ipFin = txtIPFin.getText().trim();
        int timeout;

        if (!Utilidades.validarIP(ipInicio) || !Utilidades.validarIP(ipFin)) {
            JOptionPane.showMessageDialog(this, "Formato de IP inválido", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            timeout = Integer.parseInt(txtTimeout.getText().trim());
            if (timeout <= 0) {
                JOptionPane.showMessageDialog(this, "El tiempo de espera debe ser mayor a 0 ms", "Rango incorrecto", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Tiempo de espera inválido", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        long start = Utilidades.ipToLong(ipInicio);
        long end = Utilidades.ipToLong(ipFin);

        if (start > end) {
            JOptionPane.showMessageDialog(this, "Rango de IP incorrecto (la IP de inicio es mayor que la de fin)", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        modeloTabla.setRowCount(0);
        equiposConectados = 0;

        new Thread(() -> {
            try {
                progressBar.setMinimum(0);
                progressBar.setMaximum((int) (end - start + 1));

                int progress = 0;

                for (long ip = start; ip <= end; ip++) {
                    String ipActual = Utilidades.longToIp(ip);

                    long tiempoInicio = System.currentTimeMillis();
                    boolean activa = Escaner.pingHost(ipActual, timeout);
                    long tiempoRespuesta = System.currentTimeMillis() - tiempoInicio;

                    String estado = activa ? "Conectado" : "Inactivo";
                    String nombreHost = activa ? Escaner.getHostName(ipActual) : "-";

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

    private void filtrar() {
        String texto = txtFiltro.getText();
        if (texto.trim().isEmpty()) {
            sorter.setRowFilter(null);
        } else {
            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + texto));
        }
    }
}
*/
package Escaner;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.io.*;
import java.util.regex.Pattern; // Asegúrate de tener esta importación si utilizas el código original

public class Gui extends JFrame {

    private JTextField txtIPInicio;
    private JTextField txtIPFin;
    private JTextField txtTimeout;
    private JTable tablaResultados;
    private DefaultTableModel modeloTabla;
    private JProgressBar progressBar;
    private JLabel lblResumen;
    private int equiposConectados = 0;

    private TableRowSorter<DefaultTableModel> sorter;
    private JTextField txtFiltro;

    public Gui() {
        setTitle("Escáner de IPs");
        setSize(800, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        Color colorFondo = new Color(40, 44, 52);
        Color colorPanel = new Color(60, 63, 70);
        Color colorTexto = new Color(230, 230, 230);
        Color colorBoton = new Color(100, 149, 237);
        Color colorTablaHeader = new Color(75, 110, 175);

        getContentPane().setBackground(colorFondo);

        // --- Panel superior
        JPanel panelSuperior = new JPanel(new GridLayout(4, 2, 5, 5));
        panelSuperior.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panelSuperior.setBackground(colorPanel);

        JLabel lblInicio = new JLabel("IP de inicio:");
        lblInicio.setForeground(colorTexto);
        panelSuperior.add(lblInicio);
        txtIPInicio = new JTextField("8.8.8.1");
        panelSuperior.add(txtIPInicio);

        JLabel lblFin = new JLabel("IP de fin:");
        lblFin.setForeground(colorTexto);
        panelSuperior.add(lblFin);
        txtIPFin = new JTextField("8.8.8.5");
        panelSuperior.add(txtIPFin);

        JLabel lblTimeout = new JLabel("Timeout (ms):");
        lblTimeout.setForeground(colorTexto);
        panelSuperior.add(lblTimeout);
        txtTimeout = new JTextField("1000");
        panelSuperior.add(txtTimeout);

        JButton btnEscanear = new JButton("Escanear");
        JButton btnLimpiar = new JButton("Limpiar");

        btnEscanear.setBackground(colorBoton);
        btnEscanear.setForeground(Color.WHITE);
        btnEscanear.setFocusPainted(false);

        btnLimpiar.setBackground(new Color(220, 20, 60));
        btnLimpiar.setForeground(Color.WHITE);
        btnLimpiar.setFocusPainted(false);

        panelSuperior.add(btnEscanear);
        panelSuperior.add(btnLimpiar);
        add(panelSuperior, BorderLayout.NORTH);

        // --- Tabla
        modeloTabla = new DefaultTableModel(new String[]{"Dirección IP", "Nombre", "Estado", "Tiempo (ms)"}, 0);
        tablaResultados = new JTable(modeloTabla);

        sorter = new TableRowSorter<>(modeloTabla);
        tablaResultados.setRowSorter(sorter);

        tablaResultados.setBackground(colorFondo);
        tablaResultados.setForeground(colorTexto);
        tablaResultados.setGridColor(Color.GRAY);
        tablaResultados.setRowHeight(25);
        tablaResultados.setSelectionBackground(new Color(100, 149, 237));
        tablaResultados.setSelectionForeground(Color.WHITE);

        JTableHeader header = tablaResultados.getTableHeader();
        header.setBackground(colorTablaHeader);
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));

        JScrollPane scrollTabla = new JScrollPane(tablaResultados);
        scrollTabla.getViewport().setBackground(colorFondo);
        add(scrollTabla, BorderLayout.CENTER);

        // --- Barra inferior
        JPanel panelInferior = new JPanel(new BorderLayout());
        panelInferior.setBackground(colorPanel);

        progressBar = new JProgressBar();
        progressBar.setStringPainted(true);
        progressBar.setForeground(new Color(50, 205, 50));

        lblResumen = new JLabel("Equipos conectados: 0");
        lblResumen.setForeground(colorTexto);

        panelInferior.add(progressBar, BorderLayout.CENTER);
        panelInferior.add(lblResumen, BorderLayout.SOUTH);
        add(panelInferior, BorderLayout.SOUTH);

        // --- Panel lateral
        JPanel panelDerecho = new JPanel();
        panelDerecho.setLayout(new BoxLayout(panelDerecho, BoxLayout.Y_AXIS));
        panelDerecho.setBackground(colorPanel);
        panelDerecho.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton btnGuardar = new JButton("Guardar en archivo");
        btnGuardar.setBackground(new Color(46, 139, 87));
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.setFocusPainted(false);
        btnGuardar.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelDerecho.add(btnGuardar);

        // --- NUEVO BOTÓN para Netstat ---
        panelDerecho.add(Box.createVerticalStrut(15));
        JButton btnNetstat = new JButton("Ver Puertos Locales (netstat)");
        btnNetstat.setBackground(new Color(255, 140, 0)); // Color naranja
        btnNetstat.setForeground(Color.WHITE);
        btnNetstat.setFocusPainted(false);
        btnNetstat.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelDerecho.add(btnNetstat);
        // ---------------------------------

        panelDerecho.add(Box.createVerticalStrut(15));
        JLabel lblFiltro = new JLabel("Filtrar resultados:");
        lblFiltro.setForeground(colorTexto);
        lblFiltro.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelDerecho.add(lblFiltro);

        txtFiltro = new JTextField();
        txtFiltro.setMaximumSize(new Dimension(200, 25));
        panelDerecho.add(txtFiltro);

        add(panelDerecho, BorderLayout.EAST);

        // Acciones
        btnEscanear.addActionListener(e -> escanear());
        btnLimpiar.addActionListener(e -> {
            modeloTabla.setRowCount(0);
            progressBar.setValue(0);
            lblResumen.setText("Equipos conectados: 0");
            equiposConectados = 0;
        });
        btnGuardar.addActionListener(e -> guardarArchivo());
        btnNetstat.addActionListener(e -> ejecutarNetstat()); // Acción del nuevo botón

        txtFiltro.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { filtrar(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { filtrar(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { filtrar(); }
        });
    }

    private void escanear() {
        String ipInicio = txtIPInicio.getText().trim();
        String ipFin = txtIPFin.getText().trim();
        int timeout;

        if (!Utilidades.validarIP(ipInicio) || !Utilidades.validarIP(ipFin)) {
            JOptionPane.showMessageDialog(this, "Formato de IP inválido", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            timeout = Integer.parseInt(txtTimeout.getText().trim());
            if (timeout <= 0) {
                JOptionPane.showMessageDialog(this, "El tiempo de espera debe ser mayor a 0 ms", "Rango incorrecto", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Tiempo de espera inválido", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        long start = Utilidades.ipToLong(ipInicio);
        long end = Utilidades.ipToLong(ipFin);

        if (start > end) {
            JOptionPane.showMessageDialog(this, "Rango de IP incorrecto (la IP de inicio es mayor que la de fin)", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        modeloTabla.setRowCount(0);
        equiposConectados = 0;

        new Thread(() -> {
            try {
                progressBar.setMinimum(0);
                progressBar.setMaximum((int) (end - start + 1));

                int progress = 0;

                for (long ip = start; ip <= end; ip++) {
                    String ipActual = Utilidades.longToIp(ip);

                    long tiempoInicio = System.currentTimeMillis();
                    boolean activa = Escaner.pingHost(ipActual, timeout);
                    long tiempoRespuesta = System.currentTimeMillis() - tiempoInicio;

                    String estado = activa ? "Conectado" : "Inactivo";
                    String nombreHost = activa ? Escaner.getHostName(ipActual) : "-";

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

    private void guardarArchivo() {
        JFileChooser fileChooser = new JFileChooser();
        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            File archivo = fileChooser.getSelectedFile();
            if (!archivo.getAbsolutePath().endsWith(".txt")) {
                archivo = new File(archivo.getAbsolutePath() + ".txt");
            }
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(archivo))) {
                // Escribir cabecera
                writer.write("Dirección IP - Nombre - Estado - Tiempo (ms)");
                writer.newLine();
                
                // Escribir datos
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

    private void filtrar() {
        String texto = txtFiltro.getText();
        if (texto.trim().isEmpty()) {
            sorter.setRowFilter(null);
        } else {
            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + texto));
        }
    }
    
    // --- NUEVO MÉTODO para ejecutar el comando del sistema operativo (netstat) ---
    private void ejecutarNetstat() {
        String comando;
        // Obtenemos el sistema operativo para ajustar el comando
        String os = System.getProperty("os.name").toLowerCase();
        
        // El comando netstat -a -n muestra todas las conexiones y puertos en formato numérico
        if (os.contains("win")) {
            // En Windows, se necesita cmd /c para que el proceso se ejecute correctamente
            comando = "cmd /c netstat -a -n";
        } else {
            // En Linux/macOS
            comando = "netstat -a -n";
        }

        try {
            // Ejecutamos el comando
            Process process = Runtime.getRuntime().exec(comando);
            
            // Leemos la salida estándar del comando
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder output = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }

            // Esperamos a que el proceso termine
            process.waitFor();

            // Mostramos la salida en un JTextArea dentro de una JDialog
            JTextArea textArea = new JTextArea(output.toString());
            textArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
            textArea.setEditable(false);
            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new Dimension(600, 400)); // Tamaño de la ventana de diálogo

            JOptionPane.showMessageDialog(this, scrollPane, "Salida de Netstat Local (" + comando + ")", JOptionPane.PLAIN_MESSAGE);

        } catch (IOException | InterruptedException ex) {
            JOptionPane.showMessageDialog(this, "Error al ejecutar netstat. Asegúrate de tener permisos o que el comando esté disponible: " + ex.getMessage(), "Error de Comando", JOptionPane.ERROR_MESSAGE);
        }
    }
}
 
