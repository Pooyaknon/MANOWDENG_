import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class HomeUI extends JFrame {

    private JTextField nameField;
    private JButton startButton;
    public static String playerName = "";
    private BufferedImage backgroundImage;
    private Font pixelFont;

    public HomeUI() {
        setTitle("MANOWDENG - Home");
        setMinimumSize(new Dimension(600, 400)); // ตั้งขนาดขั้นต่ำให้เท่ากับ ModeSelectUI
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        try {
            backgroundImage = ImageIO.read(new File("brick_background.png"));
            pixelFont = Font.createFont(Font.TRUETYPE_FONT, new File("Jersey10-Regular.ttf")).deriveFont(60f);
            GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(pixelFont);
        } catch (Exception e) {
            e.printStackTrace();
        }

        BackgroundPanel mainPanel = new BackgroundPanel();
        mainPanel.setLayout(new GridBagLayout()); // Layout ที่ยืดหยุ่น
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.gridx = 0;

        // Title
        JLabel titleLabel = new JLabel("MANOWDENG");
        titleLabel.setFont(pixelFont.deriveFont(72f));
        titleLabel.setForeground(Color.GREEN);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridy = 0;
        mainPanel.add(titleLabel, gbc);

        // Label "Enter your name"
        JLabel nameLabel = new JLabel("Enter your name");
        nameLabel.setFont(pixelFont.deriveFont(24f));
        nameLabel.setForeground(Color.WHITE);
        nameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridy = 1;
        mainPanel.add(nameLabel, gbc);

        // Name Input
        nameField = new JTextField(20);
        nameField.setFont(new Font("Arial", Font.PLAIN, 20));
        nameField.setHorizontalAlignment(JTextField.CENTER);
        gbc.gridy = 2;
        mainPanel.add(nameField, gbc);

        // START Button
        startButton = new JButton("START");
        startButton.setFont(pixelFont.deriveFont(30f));
        startButton.setBackground(Color.RED);
        startButton.setForeground(Color.WHITE);
        startButton.setFocusPainted(false);
        startButton.setEnabled(false);
        startButton.setBorder(BorderFactory.createLineBorder(Color.WHITE, 4, true));
        gbc.gridy = 3;
        mainPanel.add(startButton, gbc);

        // ฟังชันตรวจพิมพ์ชื่อ
        nameField.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                startButton.setEnabled(!nameField.getText().trim().isEmpty());
            }
        });

        // ปุ่ม START
        startButton.addActionListener(e -> {
            playerName = nameField.getText().trim();
            dispose();
            new ModeSelectUI();
        });

        setContentPane(mainPanel);
        setVisible(true);
    }

    // Panel พื้นหลังแบบ Resizable
    class BackgroundPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (backgroundImage != null) {
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        }
    }

    public static void main(String[] args) {
        new HomeUI();
    }
}
