import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class Main {
    private static final String USER_DATABASE_FILE = "userDatabase.txt";
    private static Map<String, String> userDatabase = new HashMap<>();

    public static void main(String[] args) {
        loadUserDatabase(); // Load existing user data

        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("App");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(600, 400); // Larger window size

            JPanel panel = new JPanel(new GridBagLayout());
            panel.setBackground(Color.BLACK);

            JButton signUpButton = new JButton("Sign Up");
            JButton loginButton = new JButton("Login");

            signUpButton.setPreferredSize(new Dimension(150, 50));
            loginButton.setPreferredSize(new Dimension(150, 50));

            signUpButton.addActionListener(e -> signUpUser());
            loginButton.addActionListener(e -> loginUser(frame, panel));

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.insets = new Insets(10, 10, 10, 10); // Add some padding
            panel.add(signUpButton, gbc);

            gbc.gridy = 1;
            panel.add(loginButton, gbc);

            frame.setContentPane(panel);
            frame.setLocationRelativeTo(null); // Center the frame on the screen
            frame.setVisible(true);
        });
    }

    private static void signUpUser() {
        String username = JOptionPane.showInputDialog("Enter your username:");
        String password = JOptionPane.showInputDialog("Enter your password:");

        if (username != null && password != null) {
            userDatabase.put(username, password);
            saveUserDatabase(); // Save updated user data
            JOptionPane.showMessageDialog(null, "User successfully signed up!");
        }
    }

    private static void loginUser(JFrame frame, JPanel originalPanel) {
        String username = JOptionPane.showInputDialog("Enter your username:");
        String password = JOptionPane.showInputDialog("Enter your password:");

        if (username != null && password != null) {
            if (userDatabase.containsKey(username) && userDatabase.get(username).equals(password)) {
                displayDashboard(frame, username);
            } else {
                JOptionPane.showMessageDialog(null, "Login failed. Incorrect username or password.");
            }
        }
    }

    private static void displayDashboard(JFrame frame, String username) {
        JPanel dashboardPanel = new JPanel(new GridBagLayout());
        dashboardPanel.setBackground(Color.BLACK);

        JLabel greetingLabel = new JLabel("Hello, " + username + ", This is your dashboard");
        greetingLabel.setForeground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 10, 10, 10); // Add some padding
        dashboardPanel.add(greetingLabel, gbc);

        frame.setContentPane(dashboardPanel);
        frame.revalidate();
    }

    private static void loadUserDatabase() {
        try (BufferedReader reader = new BufferedReader(new FileReader(USER_DATABASE_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts.length == 2) {
                    userDatabase.put(parts[0], parts[1]);
                }
            }
        } catch (IOException e) {
            // Ignore errors when loading the database (file might not exist yet)
        }
    }

    private static void saveUserDatabase() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(USER_DATABASE_FILE))) {
            for (Map.Entry<String, String> entry : userDatabase.entrySet()) {
                writer.println(entry.getKey() + ":" + entry.getValue());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
