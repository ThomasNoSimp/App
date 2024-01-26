import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class Main {
    private static final String DATABASE_FILE = "database.txt";

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("App");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(400, 400);

            JPanel panel = new JPanel(new GridBagLayout());
            panel.setBackground(Color.LIGHT_GRAY);

            JButton signUpButton = createButton("Sign Up");
            JButton loginButton = createButton("Login");

            signUpButton.addActionListener(e -> signUpUser());
            loginButton.addActionListener(e -> loginUser(frame));

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

    private static JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(150, 50));
        return button;
    }

    private static void signUpUser() {
        String username = JOptionPane.showInputDialog("Enter your username:");
        String password = JOptionPane.showInputDialog("Enter your password:");

        if (username != null && password != null) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(DATABASE_FILE, true))) {
                writer.write(username + ":" + password + "\n");
                JOptionPane.showMessageDialog(null, "User successfully signed up!");
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
            }
        }
    }

    private static void loginUser(JFrame frame) {
        String username = JOptionPane.showInputDialog("Enter your username:");
        String password = JOptionPane.showInputDialog("Enter your password:");

        if (username != null && password != null) {
            try (BufferedReader reader = new BufferedReader(new FileReader(DATABASE_FILE))) {
                String line;
                boolean found = false;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(":");
                    if (parts.length == 2 && parts[0].equals(username) && parts[1].equals(password)) {
                        found = true;
                        break;
                    }
                }
                if (found) {
                    JOptionPane.showMessageDialog(null, "Login successful!");
                    displayDashboard(frame);
                } else {
                    JOptionPane.showMessageDialog(null, "Login failed. Incorrect username or password.");
                }
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
            }
        }
    }

    private static void displayDashboard(JFrame frame) {
        JPanel dashboardPanel = new JPanel(new GridBagLayout());
        dashboardPanel.setBackground(Color.LIGHT_GRAY);
    
        JButton calculatorButton = createButton("Calculator");
        JButton donateButton = createButton("Donate");
        JButton contactUsButton = createButton("Contact Us");
        JButton logoutButton = createButton("Log Out");
    
        calculatorButton.addActionListener(e -> {
            displayCalculator(frame);
        });
    
        logoutButton.addActionListener(e -> {
            frame.dispose(); // Dispose of the current frame
            // Show the login/signup frame again
            JFrame loginSignupFrame = new JFrame("Login/Signup");
            loginSignupFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            loginSignupFrame.setSize(400, 400);
    
            JPanel panel = new JPanel(new GridBagLayout());
            panel.setBackground(Color.LIGHT_GRAY);
    
            JButton signUpButton = createButton("Sign Up");
            JButton loginButton = createButton("Login");
    
            signUpButton.addActionListener(ev -> signUpUser());
            loginButton.addActionListener(ev -> loginUser(loginSignupFrame));
    
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.insets = new Insets(10, 10, 10, 10); // Add some padding
            panel.add(signUpButton, gbc);
    
            gbc.gridy = 1;
            panel.add(loginButton, gbc);
    
            loginSignupFrame.setContentPane(panel);
            loginSignupFrame.setLocationRelativeTo(null); // Center the frame on the screen
            loginSignupFrame.setVisible(true);
        });
    
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 10, 10, 10); // Add some padding
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;
    
        dashboardPanel.add(calculatorButton, gbc);
    
        gbc.gridy = 1;
        dashboardPanel.add(donateButton, gbc);
    
        gbc.gridy = 2;
        dashboardPanel.add(contactUsButton, gbc);
    
        gbc.gridy = 3;
        dashboardPanel.add(logoutButton, gbc);
    
        frame.setContentPane(dashboardPanel);
        frame.revalidate();
    }    

    private static void displayCalculator(JFrame frame) {
        JPanel calculatorPanel = new JPanel(new GridBagLayout());
        calculatorPanel.setBackground(Color.LIGHT_GRAY);
    
        JTextField inputField = new JTextField();
        inputField.setPreferredSize(new Dimension(300, 50));
        inputField.setHorizontalAlignment(JTextField.RIGHT);
        inputField.setFont(new Font("Arial", Font.PLAIN, 20));
    
        // Disable direct input
        inputField.setEditable(false);
    
        JPanel buttonPanel = new JPanel(new GridLayout(5, 4, 5, 5));
    
        String[] buttonLabels = {
                "7", "8", "9", "+",
                "4", "5", "6", "-",
                "1", "2", "3", "*",
                "0", ".", "=", "/",
                "C", "Del" // Added "C" and "Del" buttons
        };
    
        for (String label : buttonLabels) {
            JButton button = new JButton(label);
            button.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    String buttonText = ((JButton) e.getSource()).getText();
    
                    switch (buttonText) {
                        case "=":
                            calculate(inputField);
                            break;
                        case "C":
                            inputField.setText("");
                            break;
                        case "Del":
                            String currentText = inputField.getText();
                            if (!currentText.isEmpty()) {
                                inputField.setText(currentText.substring(0, currentText.length() - 1));
                            }
                            break;
                        default:
                            inputField.setText(inputField.getText() + buttonText);
                            break;
                    }
                }
            });
            buttonPanel.add(button);
        }
    
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 10, 10, 10); // Add some padding
        calculatorPanel.add(inputField, gbc);
    
        gbc.gridy = 1;
        calculatorPanel.add(buttonPanel, gbc);
    
        frame.setContentPane(calculatorPanel);
        frame.revalidate();
    }
    

    private static void calculate(JTextField inputField) {
        String expression = inputField.getText();
        try {
            double result = evaluateExpression(expression);
            inputField.setText(Double.toString(result));
        } catch (IllegalArgumentException e) {
            inputField.setText("Error");
        }
    }

    private static double evaluateExpression(String expression) {
        // Implement the custom expression parser and evaluator here
        // For simplicity, let's just evaluate the expression as a mathematical expression
        return evaluateMathExpression(expression);
    }

    private static double evaluateMathExpression(String expression) {
        try {
            return new Object() {
                int pos = -1, ch;

                void nextChar() {
                    ch = (++pos < expression.length()) ? expression.charAt(pos) : -1;
                }

                boolean eat(int charToEat) {
                    while (ch == ' ') nextChar();
                    if (ch == charToEat) {
                        nextChar();
                        return true;
                    }
                    return false;
                }

                double parse() {
                    nextChar();
                    double x = parseExpression();
                    if (pos < expression.length()) throw new IllegalArgumentException("Unexpected: " + (char) ch);
                    return x;
                }

                // Grammar:
                // expression = term | expression `+` term | expression `-` term
                // term = factor | term `*` factor | term `/` factor
                // factor = `+` factor | `-` factor | `(` expression `)` | number
                // number = [0-9]+
                double parseExpression() {
                    double x = parseTerm();
                    for (; ; ) {
                        if (eat('+')) x += parseTerm(); // addition
                        else if (eat('-')) x -= parseTerm(); // subtraction
                        else return x;
                    }
                }

                double parseTerm() {
                    double x = parseFactor();
                    for (; ; ) {
                        if (eat('*')) x *= parseFactor(); // multiplication
                        else if (eat('/')) x /= parseFactor(); // division
                        else return x;
                    }
                }

                double parseFactor() {
                    if (eat('+')) return parseFactor(); // unary plus
                    if (eat('-')) return -parseFactor(); // unary minus

                    double x;
                    int startPos = this.pos;
                    if (eat('(')) { // parentheses
                        x = parseExpression();
                        eat(')');
                    } else if ((ch >= '0' && ch <= '9') || ch == '.') { // numbers
                        while ((ch >= '0' && ch <= '9') || ch == '.') nextChar();
                        x = Double.parseDouble(expression.substring(startPos, this.pos));
                    } else {
                        throw new IllegalArgumentException("Unexpected: " + (char) ch);
                    }

                    return x;
                }
            }.parse();
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid expression");
        }
    }
}
