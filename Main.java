import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class Main {
    private static final String DATABASE_FILE = "database.txt";

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ImageIcon icon = new ImageIcon("icon.png");
            JFrame frame = new JFrame("App");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(400, 400);
            frame.setIconImage(icon.getImage()); // Set the icon

            JPanel panel = new JPanel(new GridBagLayout());
            panel.setBackground(Color.LIGHT_GRAY);

            GridBagConstraints modeGbc = new GridBagConstraints();
            modeGbc.gridx = 0;
            modeGbc.gridy = 0;
            modeGbc.insets = new Insets(10, 10, 10, 10); // Add some padding

            JPanel calculatorPanel = new JPanel(new GridBagLayout());
            GridBagConstraints calculatorGbc = new GridBagConstraints();
            calculatorGbc.gridx = 0;
            calculatorGbc.gridy = 1;
            calculatorGbc.insets = new Insets(10, 10, 10, 10); // Add some padding
            panel.add(calculatorPanel, calculatorGbc);

            JButton signUpButton = createButton("Sign Up");
            JButton loginButton = createButton("Login");

            signUpButton.addActionListener(e -> signUpUser());
            loginButton.addActionListener(e -> loginUser(frame));

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = 0;
            gbc.gridy = 2;
            gbc.insets = new Insets(10, 10, 10, 10); // Add some padding
            panel.add(signUpButton, gbc);

            gbc.gridy = 3;
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
                    // Display standard calculator interface by default
                    displayStandardCalculator(frame.getContentPane());
                } else {
                    JOptionPane.showMessageDialog(null, "Login failed. Incorrect username or password.");
                }
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
            }
        }
    }

    private static void displayStandardCalculator(Container calculatorPanel) {
        // Clear the calculator panel
        calculatorPanel.removeAll();
    
        JComboBox<String> modeDropdown = new JComboBox<>(new String[]{"Mode 1", "Mode 2", "Mode 3"});
        JTextField inputField = new JTextField();
        inputField.setPreferredSize(new Dimension(300, 50));
        inputField.setHorizontalAlignment(JTextField.RIGHT);
        inputField.setFont(new Font("Arial", Font.PLAIN, 20));
    
        // Create a panel for the input field and dropdown list
        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints inputGbc = new GridBagConstraints();
        inputGbc.gridx = 0;
        inputGbc.gridy = 0;
        inputPanel.add(inputField, inputGbc);
        
        inputGbc.gridy = 1;
        inputPanel.add(modeDropdown, inputGbc);
    
        // Add the input panel to the calculator panel
        calculatorPanel.add(inputPanel);
    
        // Disable direct input
        inputField.setEditable(false);
    
        JPanel buttonPanel = new JPanel(new GridLayout(5, 4, 5, 5)); // Increase rows to accommodate additional buttons
    
        String[] buttonLabels = {
                "7", "8", "9", "+",
                "4", "5", "6", "-",
                "1", "2", "3", "*",
                "0", ".", "=", "/",
                "C", "Del" // Add "C" and "Del" buttons
        };
    
        modeDropdown.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedMode = (String) modeDropdown.getSelectedItem();
                switch (selectedMode) {
                    case "Mode 1":
                        displayStandardCalculator(calculatorPanel);
                        break;
                    case "Mode 2":
                        displayScientificCalculator(calculatorPanel);
                        break;
                    case "Mode 3":
                        // Display programmer calculator interface
                        break;
                }
                modeDropdown.setSelectedItem(selectedMode); // Update the selected item
            }
        });
        
    
        for (String label : buttonLabels) {
            JButton button = new JButton(label);
            button.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    String buttonText = ((JButton) e.getSource()).getText();
                    if (buttonText.equals("=")) {
                        calculate(inputField);
                    } else if (buttonText.equals("C")) {
                        inputField.setText("");
                    } else if (buttonText.equals("Del")) {
                        String currentText = inputField.getText();
                        if (!currentText.isEmpty()) {
                            inputField.setText(currentText.substring(0, currentText.length() - 1));
                        }
                    } else {
                        inputField.setText(inputField.getText() + buttonText);
                    }
                }
            });
            buttonPanel.add(button);
        }
    
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.insets = new Insets(10, 10, 10, 10); // Add some padding
        calculatorPanel.add(buttonPanel, gbc);
    
        calculatorPanel.revalidate();
        calculatorPanel.repaint();
    }    
    

    private static void displayScientificCalculator(Container calculatorPanel) {
        // Clear the calculator panel
        calculatorPanel.removeAll();
    
        JComboBox<String> modeDropdown = new JComboBox<>(new String[]{"Mode 1", "Mode 2", "Mode 3"});
        JTextField inputField = new JTextField();
        inputField.setPreferredSize(new Dimension(300, 50));
        inputField.setHorizontalAlignment(JTextField.RIGHT);
        inputField.setFont(new Font("Arial", Font.PLAIN, 20));
    
        // Create a panel for the input field and dropdown list
        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints inputGbc = new GridBagConstraints();
        inputGbc.gridx = 0;
        inputGbc.gridy = 0;
        inputPanel.add(inputField, inputGbc);
        
        inputGbc.gridy = 1;
        inputPanel.add(modeDropdown, inputGbc);
    
        // Add the input panel to the calculator panel
        calculatorPanel.add(inputPanel);
    
        // Disable direct input
        inputField.setEditable(false);
    
        JPanel buttonPanel = new JPanel(new GridLayout(5, 4, 5, 5)); // Increase rows to accommodate additional buttons
    
        String[] buttonLabels = {
                "sin", "cos", "tan", "x^2",
                "log", "ln", "sqrt", "^",
                "pi", "e", "(", ")",
                "0", ".", "=", "/"
        };
    
        modeDropdown.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedMode = (String) modeDropdown.getSelectedItem();
                switch (selectedMode) {
                    case "Mode 1":
                        displayStandardCalculator(calculatorPanel);
                        break;
                    case "Mode 2":
                        displayScientificCalculator(calculatorPanel);
                        break;
                    case "Mode 3":
                        // Display programmer calculator interface
                        break;
                    default:
                        break;
                }
                modeDropdown.setSelectedItem(selectedMode); // Update the selected item
            }
        });        
        
    
        for (String label : buttonLabels) {
            JButton button = new JButton(label);
            button.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    String buttonText = ((JButton) e.getSource()).getText();
                    if (buttonText.equals("=")) {
                        calculate(inputField);
                    } else if (buttonText.equals("C")) {
                        inputField.setText("");
                    } else if (buttonText.equals("Del")) {
                        String currentText = inputField.getText();
                        if (!currentText.isEmpty()) {
                            inputField.setText(currentText.substring(0, currentText.length() - 1));
                        }
                    } else {
                        if (buttonText.equals("sin")) {
                            inputField.setText(String.valueOf(Math.sin(Double.parseDouble(inputField.getText()))));
                        } else if (buttonText.equals("cos")) {
                            inputField.setText(String.valueOf(Math.cos(Double.parseDouble(inputField.getText()))));
                        } else if (buttonText.equals("tan")) {
                            inputField.setText(String.valueOf(Math.tan(Double.parseDouble(inputField.getText()))));
                        } else if (buttonText.equals("log")) {
                            inputField.setText(String.valueOf(Math.log10(Double.parseDouble(inputField.getText()))));
                        } else if (buttonText.equals("ln")) {
                            inputField.setText(String.valueOf(Math.log(Double.parseDouble(inputField.getText()))));
                        } else if (buttonText.equals("sqrt")) {
                            inputField.setText(String.valueOf(Math.sqrt(Double.parseDouble(inputField.getText()))));
                        } else if (buttonText.equals("^")) {
                            // Implement exponentiation logic here
                        } else if (buttonText.equals("pi")) {
                            inputField.setText(String.valueOf(Math.PI));
                        } else if (buttonText.equals("e")) {
                            inputField.setText(String.valueOf(Math.E));
                        } else {
                            inputField.setText(inputField.getText() + buttonText);
                        }
                    }
                }
            });
            buttonPanel.add(button);
        }
    
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.insets = new Insets(10, 10, 10, 10); // Add some padding
        calculatorPanel.add(buttonPanel, gbc);
    
        calculatorPanel.revalidate();
        calculatorPanel.repaint();
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
