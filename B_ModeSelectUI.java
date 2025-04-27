import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class B_ModeSelectUI extends JFrame {

    private BufferedImage backgroundImage;
    private Font pixelFont;

    public B_ModeSelectUI() {
        setTitle("MANOWDENG - Select Mode");
        setMinimumSize(new Dimension(800, 600));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        loadResources();
        setupUI();

        setVisible(true);
    }

    private void loadResources() {
        try {
            backgroundImage = ImageIO.read(new File("zzz_brick_background.png"));
            pixelFont = Font.createFont(Font.TRUETYPE_FONT, new File("zz_Jersey10-Regular.ttf"));
            GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(pixelFont);
        } catch (Exception e) {
            e.printStackTrace();
            pixelFont = new Font("Arial", Font.BOLD, 32);
        }
    }

    private void setupUI() {
        JPanel contentPanel = new JPanel() {
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
        };
        contentPanel.setLayout(new BorderLayout(20, 20));
        setContentPane(contentPanel);

        JLabel playerNameLabel = new JLabel("Welcome, " + A_HomeUI.playerName, SwingConstants.CENTER);
        playerNameLabel.setForeground(Color.WHITE);
        playerNameLabel.setFont(pixelFont.deriveFont(36f));
        contentPanel.add(playerNameLabel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 0, 20, 0);
        gbc.gridx = 0;

        JButton singlePlayerButton = createModeButton("SINGLE PLAYER", new Color(50, 150, 250));
        JButton multiplayerButton = createModeButton("MULTIPLAYER", new Color(250, 150, 50));

        gbc.gridy = 0;
        centerPanel.add(singlePlayerButton, gbc);
        gbc.gridy = 1;
        centerPanel.add(multiplayerButton, gbc);

        contentPanel.add(centerPanel, BorderLayout.CENTER);

        JButton homeButton = createStyledButton("HOME", 24f, new Color(100, 100, 100));
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.setOpaque(false);
        bottomPanel.add(homeButton);
        contentPanel.add(bottomPanel, BorderLayout.SOUTH);

        setupEventListeners(singlePlayerButton, multiplayerButton, homeButton);
    }

    private JButton createModeButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(pixelFont.deriveFont(28f));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.WHITE, 3, true),
            BorderFactory.createEmptyBorder(10, 40, 10, 40)
        ));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    private JButton createStyledButton(String text, float fontSize, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(pixelFont.deriveFont(fontSize));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.WHITE, 2, true),
            BorderFactory.createEmptyBorder(5, 20, 5, 20)
        ));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    private void setupEventListeners(JButton singlePlayerButton, JButton multiplayerButton, JButton homeButton) {
        singlePlayerButton.addActionListener(e -> {
            dispose();
            new C_SinglePlayerUI();
        });
        
        multiplayerButton.addActionListener(e -> {
            dispose();
            new D_MultiplayerLobbyUI();
        });
        
        homeButton.addActionListener(e -> {
            dispose();
            new A_HomeUI();
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            A_HomeUI.playerName = "Player1";
            new B_ModeSelectUI();
        });
    }
}