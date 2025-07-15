package game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Arrays;

public class Console2 extends JFrame {
    private final String PROMPT = "> ";
    private final JTextArea consoleArea;
    private final JTextField inputField;
    private final GameManagerRevised game;
    private final JLabel statusLabel;

    public Console2() {
        // Set up the JFrame
        setTitle("Text Adventure Game Console");
        setSize(780, 480);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.BLACK);

       // setLocation(400,400);
        setLocationRelativeTo(null);


        // JLabel for static line
        statusLabel = new JLabel("Welcome!");
        statusLabel.setOpaque(true);
        statusLabel.setBackground(Color.BLACK);
        statusLabel.setForeground(Color.WHITE);
        statusLabel.setFont(new Font("Monospaced", Font.PLAIN, 20));
        statusLabel.setHorizontalAlignment(SwingConstants.LEFT);
        add(statusLabel, BorderLayout.NORTH);


        // JTextArea for game output
        consoleArea = new JTextArea();
        consoleArea.setEditable(false);
        add(new JScrollPane(consoleArea), BorderLayout.CENTER);
        // Customize
        consoleArea.setBackground(Color.black);
        consoleArea.setForeground(Color.GREEN);
        consoleArea.setFont(new Font("Monospaced", Font.PLAIN, 20));
        consoleArea.setCaretColor(Color.black);
        consoleArea.setBorder(null);
        consoleArea.setBorder(BorderFactory.createEmptyBorder());
        consoleArea.setMargin(new Insets(0, 0, 0, 0));

        JScrollPane scrollPane = new JScrollPane(consoleArea);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());  // Remove scroll pane border
        add(scrollPane, BorderLayout.CENTER);

        inputField = new JTextField();
        inputField.setBackground(Color.BLACK);  // Set input background to black
        inputField.setForeground(Color.WHITE);  // Set input text color to white
        inputField.setFont(new Font("Monospaced", Font.PLAIN, 14));  // Set font style and size
        inputField.setBorder(null);
        // JTextField for user input
        // inputField.addActionListener(this);  // Adds listener for "Enter" key press
        add(inputField, BorderLayout.SOUTH);

        // JPanel for input with prompt label
        JPanel inputPanel = new JPanel(new BorderLayout());

        // JLabel for prompt ("> ")
        JLabel promptLabel = new JLabel("> ");
        promptLabel.setForeground(Color.WHITE);
        promptLabel.setFont(new Font("Monospaced", Font.PLAIN, 14));


        inputField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                // Ensure the cursor stays after the prompt
                if (inputField.getCaretPosition() < PROMPT.length()) {
                    e.consume();  // Ignore key events that would move caret before prompt
                    SwingUtilities.invokeLater(() -> {
                        if (inputField.getText().length() >= PROMPT.length()) {
                            inputField.setCaretPosition(PROMPT.length());
                        }
                    });
                }

                // Enter key handling
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    actionPerformed();
                    e.consume();
                }

            }

            @Override
            public void keyReleased(KeyEvent e) {
                // Ensure the prompt text is not deleted or modified
                if (!inputField.getText().startsWith(PROMPT)) {
                    SwingUtilities.invokeLater(() -> {
                        inputField.setText(PROMPT);
                        inputField.setCaretPosition(PROMPT.length());
                    });
                }
            }


        });



        add(inputPanel, BorderLayout.SOUTH);
        inputPanel.add(inputField, BorderLayout.CENTER);

        game = new GameManagerRevised(this);

        setVisible(true);
        //On start
        inputField.setText(PROMPT);
        inputField.setCaretPosition(PROMPT.length());
    }

    public void setStatus(String text) {
        statusLabel.setText(text);
    }
    public void clearChatHistory(String status) {
        setStatus(status);
        consoleArea.setText(""); // Retains the static line
    }
    public String getText(){
        return consoleArea.getText();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Console2());

    }

    public void appendToConsole(String text) {
        consoleArea.append(text + "\n");
    }

    public void writeToConsole(String text){
        consoleArea.append(text);
    }

    //@Override
    private void actionPerformed() {
        String userInput = inputField.getText().substring(PROMPT.length());
        consoleArea.append(PROMPT + userInput + "\n");
        //String command = inputField.getText();
        inputField.setText(PROMPT);
        // Send command to game
        game.processCommand(userInput);
    }
    public void clearFirstLines(int numberOfLines) {
        String text = consoleArea.getText();
        String[] lines = text.split("\n");

        // If numberOfLines is greater than available lines, clear all
        if (numberOfLines >= lines.length) {
            consoleArea.setText("");
        } else {
            // Keep all lines except the first 'numberOfLines'
            String newText = String.join("\n", Arrays.copyOfRange(lines, numberOfLines, lines.length));
            consoleArea.setText(newText);
        }
    }

}