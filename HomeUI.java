import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class HomeUI extends JFrame {

    private JTextField nameField;
    private JButton startButton;
    private JButton exitButton;
    public static String playerName = "";
    private BufferedImage backgroundImage;
    private Font pixelFont;

    public HomeUI() {
        setTitle("MANOWDENG - Home");
        setMinimumSize(new Dimension(800, 600));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    
        loadResources();
        setupUI();
        
        setVisible(true);
    }

    private void loadResources() {
        try {
            backgroundImage = ImageIO.read(new File("brick_background.png"));
            pixelFont = Font.createFont(Font.TRUETYPE_FONT, new File("Jersey10-Regular.ttf"));
            GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(pixelFont);
        } catch (Exception e) {
            e.printStackTrace();
            pixelFont = new Font("Arial", Font.BOLD, 60);
        }
    }

    private void setupUI() {
        BackgroundPanel mainPanel = new BackgroundPanel();
        mainPanel.setLayout(new GridBagLayout());
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.gridx = 0;

        JLabel titleLabel = new JLabel("MANOWDENG");
        titleLabel.setFont(pixelFont.deriveFont(72f));
        titleLabel.setForeground(new Color(50, 200, 50));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridy = 0;
        mainPanel.add(titleLabel, gbc);

        JLabel nameLabel = new JLabel("Enter your name");
        nameLabel.setFont(pixelFont.deriveFont(28f));
        nameLabel.setForeground(Color.WHITE);
        nameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridy = 1;
        mainPanel.add(nameLabel, gbc);

        nameField = new JTextField(20);
        nameField.setFont(pixelFont.deriveFont(24f));
        nameField.setHorizontalAlignment(JTextField.CENTER);
        nameField.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
        nameField.setOpaque(false);
        nameField.setForeground(Color.WHITE);
        nameField.setCaretColor(Color.WHITE);
        gbc.gridy = 2;
        mainPanel.add(nameField, gbc);

        startButton = new JButton("START");
        styleButton(startButton, 30f, new Color(200, 50, 50));
        gbc.gridy = 3;
        mainPanel.add(startButton, gbc);

        exitButton = new JButton("EXIT");
        styleButton(exitButton, 28f, new Color(100, 100, 100));
        gbc.gridy = 4;
        mainPanel.add(exitButton, gbc);

        setupEventListeners();

        setContentPane(mainPanel);
    }

    private void styleButton(JButton button, float fontSize, Color bgColor) {
        button.setFont(pixelFont.deriveFont(fontSize));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.WHITE, 3, true),
            BorderFactory.createEmptyBorder(5, 25, 5, 25)
        ));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private void setupEventListeners() {
        nameField.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                startButton.setEnabled(!nameField.getText().trim().isEmpty());
            }
        });

        startButton.addActionListener(e -> {
            playerName = nameField.getText().trim();
            dispose();
            new ModeSelectUI();
        });

        exitButton.addActionListener(e -> {
            int choice = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to exit?",
                "Exit Game",
                JOptionPane.YES_NO_OPTION
            );
            if (choice == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });
    }

    class BackgroundPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (backgroundImage != null) {
                Graphics2D g2d = (Graphics2D)g;
                g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                g2d.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                
                g2d.setColor(new Color(0, 0, 0, 150));
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new HomeUI();
        });
    }
}