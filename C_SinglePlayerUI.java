import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class C_SinglePlayerUI extends JFrame {

    private JButton readyButton;
    private JButton backButton;
    private JLabel countdownLabel;
    private BufferedImage backgroundImage;
    private Font pixelFont;

    public C_SinglePlayerUI() {
        setTitle("MANOWDENG - Single Player");
        setMinimumSize(new Dimension(800, 600));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        loadResources();
        setupUI();

        setVisible(true);
    }

    private void loadResources() {
        try {
            // ใช้ ClassLoader โหลดไฟล์จากโฟลเดอร์ปัจจุบัน
            backgroundImage = ImageIO.read(getClass().getResource("zzz_brick_background.png"));
            pixelFont = Font.createFont(Font.TRUETYPE_FONT, 
                getClass().getResourceAsStream("zz_Jersey10-Regular.ttf"));
        } catch (Exception e) {
            e.printStackTrace();
            // Fallback
            pixelFont = new Font("Arial", Font.BOLD, 60);
            backgroundImage = new BufferedImage(800, 600, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2d = backgroundImage.createGraphics();
            g2d.setColor(Color.DARK_GRAY);
            g2d.fillRect(0, 0, 800, 600);
            g2d.dispose();
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

        // Player name label
        JLabel playerNameLabel = new JLabel("Single Player - " + A_HomeUI.playerName, SwingConstants.CENTER);
        playerNameLabel.setForeground(Color.WHITE);
        playerNameLabel.setFont(pixelFont.deriveFont(36f));
        contentPanel.add(playerNameLabel, BorderLayout.NORTH);

        // Center panel with ready button
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 20, 20, 20);

        readyButton = createStyledButton("I'M READY TO DENG!!", 32f, new Color(200, 50, 50));
        readyButton.setPreferredSize(new Dimension(400, 100));
        gbc.gridy = 0;
        centerPanel.add(readyButton, gbc);

        countdownLabel = new JLabel("", SwingConstants.CENTER);
        countdownLabel.setForeground(Color.WHITE);
        countdownLabel.setFont(pixelFont.deriveFont(72f));
        gbc.gridy = 1;
        centerPanel.add(countdownLabel, gbc);

        contentPanel.add(centerPanel, BorderLayout.CENTER);

        // Bottom panel with back button
        backButton = createStyledButton("BACK", 24f, new Color(100, 100, 100));
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.setOpaque(false);
        bottomPanel.add(backButton);
        contentPanel.add(bottomPanel, BorderLayout.SOUTH);

        setupEventListeners();
    }

    private JButton createStyledButton(String text, float fontSize, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(pixelFont.deriveFont(fontSize));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.WHITE, 3, true),
            BorderFactory.createEmptyBorder(10, 30, 10, 30)
        ));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    private void setupEventListeners() {
        readyButton.addActionListener(e -> startCountdown());
        backButton.addActionListener(e -> {
            dispose();
            new B_ModeSelectUI();
        });
    }

    private void startCountdown() {
        readyButton.setEnabled(false);
        backButton.setEnabled(false);
        
        Timer countdownTimer = new Timer(1000, new ActionListener() {
            int count = 3;
            
            @Override
            public void actionPerformed(ActionEvent e) {
                if (count > 0) {
                    countdownLabel.setText(String.valueOf(count));
                    count--;
                } else {
                    ((Timer)e.getSource()).stop();
                    countdownLabel.setText("GO!");
                    
                    Timer delayTimer = new Timer(1000, ev -> {
                        dispose();
                        new C1_GameUI(true); // true for single player mode
                    });
                    delayTimer.setRepeats(false);
                    delayTimer.start();
                }
            }
        });
        countdownTimer.start();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            A_HomeUI.playerName = "Player1";
            new C_SinglePlayerUI();
        });
    }
}