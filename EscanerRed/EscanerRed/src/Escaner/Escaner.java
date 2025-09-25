package Escaner;

import java.net.InetAddress;

import javax.swing.SwingUtilities;

public class Escaner {

    public static boolean pingHost(String ip, int timeout) {
        try {
            InetAddress address = InetAddress.getByName(ip);
            return address.isReachable(timeout);
        } catch (Exception ignored) {}
        return false;
    }

    public static String getHostName(String ip) {
        try {
            InetAddress address = InetAddress.getByName(ip);
            return address.getCanonicalHostName();
        } catch (Exception ignored) {}
        return "Nombre desconocido";
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Gui ventana = new Gui();
            ventana.setVisible(true);
        });
    }
}
