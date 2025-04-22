import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class ModeSelectUI extends JFrame {

    private BufferedImage backgroundImage;
    private Font pixelFont;

    public ModeSelectUI() {
        setTitle("MANOWDENG - Select Mode");
        setMinimumSize(new Dimension(600, 400));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // โหลดภาพพื้นหลัง + ฟอนต์
        try {
            backgroundImage = ImageIO.read(new File("brick_background.png"));
            pixelFont = Font.createFont(Font.TRUETYPE_FONT, new File("Jersey10-Regular.ttf")).deriveFont(32f);
            GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(pixelFont);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // สร้าง content panel แบบ custom background
        JPanel contentPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (backgroundImage != null) {
                    g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                }
            }
        };
        contentPanel.setLayout(new BorderLayout(20, 20));
        setContentPane(contentPanel);

        // ===== ส่วนบน - ป้ายชื่อผู้เล่น =====
        JLabel playerNameLabel = new JLabel("Welcome, " + HomeUI.playerName, SwingConstants.CENTER);
        playerNameLabel.setForeground(Color.WHITE);
        playerNameLabel.setFont(pixelFont != null ? pixelFont.deriveFont(36f) : new Font("Segoe UI", Font.BOLD, 28));
        contentPanel.add(playerNameLabel, BorderLayout.NORTH);

        // ===== ส่วนกลาง - ปุ่ม =====
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 0, 15, 0);
        gbc.gridx = 0;

        JButton singlePlayerButton = createStyledButton("🎮 Single Player");
        JButton multiplayerButton = createStyledButton("🌐 Multiplayer");

        gbc.gridy = 0;
        centerPanel.add(singlePlayerButton, gbc);
        gbc.gridy = 1;
        centerPanel.add(multiplayerButton, gbc);

        contentPanel.add(centerPanel, BorderLayout.CENTER);

        // ===== ส่วนล่าง - ปุ่ม Home =====
        JButton homeButton = createStyledButton("🏠 Home");
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.setOpaque(false);
        bottomPanel.add(homeButton);
        contentPanel.add(bottomPanel, BorderLayout.SOUTH);

        // ===== Event =====
        singlePlayerButton.addActionListener(e -> {
            dispose();
            new SinglePlayerUI();
        });

        multiplayerButton.addActionListener(e -> {
            dispose();
            new MultiplayerLobbyUI();
        });

        homeButton.addActionListener(e -> {
            dispose();
            new HomeUI();
        });

        setVisible(true);
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFocusPainted(false);
        button.setBackground(new Color(255, 255, 255, 180)); // โปร่งใสนิดๆ
        button.setForeground(Color.BLACK);
        button.setFont(pixelFont != null ? pixelFont.deriveFont(20f) : new Font("Segoe UI", Font.PLAIN, 20));
        button.setPreferredSize(new Dimension(260, 50));
        return button;
    }

    public static void main(String[] args) {
        HomeUI.playerName = "Player1";
        SwingUtilities.invokeLater(ModeSelectUI::new);
    }
}
