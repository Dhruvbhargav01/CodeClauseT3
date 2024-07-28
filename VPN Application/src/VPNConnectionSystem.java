import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class VPNConnectionSystem extends JFrame {
    private static SecretKey secretKey;
    private static Cipher cipher;

    public VPNConnectionSystem() {
        setTitle("VPN Connection System");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        initializeUI();
    }

    private void initializeUI() {
        JPanel panel = new JPanel(new BorderLayout());

        JLabel welcomeLabel = new JLabel("WELCOME TO THE VPN CONNECTION SYSTEM", JLabel.CENTER);
        panel.add(welcomeLabel, BorderLayout.NORTH);

        JButton startServerButton = new JButton("Start Server");
        panel.add(startServerButton, BorderLayout.CENTER);

        startServerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startServer();
            }
        });

        add(panel);
    }

    private void startServer() {
        JFrame serverFrame = new JFrame("Server");
        serverFrame.setSize(400, 300);
        serverFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        serverFrame.setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel serverLabel = new JLabel("Server is started");
        serverLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(serverLabel);

        JLabel locationLabel = new JLabel("Select Location:");
        locationLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(locationLabel);

        String[] locations = {"USA", "Canada", "UK", "Australia"};
        JComboBox<String> locationComboBox = new JComboBox<>(locations);
        locationComboBox.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(locationComboBox);

        JButton connectButton = new JButton("Connect");
        connectButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(connectButton);

        connectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedLocation = (String) locationComboBox.getSelectedItem();
                connectToLocation(selectedLocation);
            }
        });

        serverFrame.add(panel);
        serverFrame.setVisible(true);
    }

    private void connectToLocation(String location) {
        JFrame connectionFrame = new JFrame("Connection");
        connectionFrame.setSize(400, 300);
        connectionFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        connectionFrame.setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel connectionLabel = new JLabel("Connected to " + location + " successfully");
        connectionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(connectionLabel);

        JButton changeLocationButton = new JButton("Change Location");
        changeLocationButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(changeLocationButton);

        changeLocationButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                connectionFrame.dispose();
                startServer();
            }
        });

        connectionFrame.add(panel);
        connectionFrame.setVisible(true);

        // Placeholder for actual connection logic
        System.out.println("Connected to " + location);
    }

    public static void main(String[] args) {
        generateKey();
        prepareCipher();

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new VPNConnectionSystem().setVisible(true);
            }
        });
    }

    private static void generateKey() {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
            keyGenerator.init(128);
            secretKey = keyGenerator.generateKey();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    private static void prepareCipher() {
        try {
            cipher = Cipher.getInstance("AES");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String encrypt(String data) {
        try {
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] encryptedData = cipher.doFinal(data.getBytes());
            return Base64.getEncoder().encodeToString(encryptedData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String decrypt(String encryptedData) {
        try {
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            byte[] decodedData = Base64.getDecoder().decode(encryptedData);
            byte[] decryptedData = cipher.doFinal(decodedData);
            return new String(decryptedData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
