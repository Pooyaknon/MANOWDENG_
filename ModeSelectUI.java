import javax.swing.*;
import java.awt.*;

public class ModeSelectUI extends JFrame {

    public ModeSelectUI() {
        setTitle("MANOWDENG - Select Mode");
        setMinimumSize(new Dimension(600, 400)); // ขนาดขั้นต่ำสำหรับ desktop
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // กลางจอ

        // ใช้ BorderLayout หลัก
        setLayout(new BorderLayout(20, 20));
        getContentPane().setBackground(new Color(30, 30, 30)); // พื้นหลังเข้ม

        // ==== ส่วนบน - ชื่อผู้เล่น ====
        JLabel playerNameLabel = new JLabel("Welcome, " + HomeUI.playerName, SwingConstants.CENTER);
        playerNameLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        playerNameLabel.setForeground(Color.WHITE);
        add(playerNameLabel, BorderLayout.NORTH);

        // ==== ส่วนกลาง - ปุ่มเลือกโหมด ====
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setOpaque(false); // ให้พื้นหลังโปร่ง
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 0, 15, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;

        JButton singlePlayerButton = createStyledButton("🎮 Single Player");
        JButton multiplayerButton = createStyledButton("🌐 Multiplayer");

        gbc.gridy = 0;
        centerPanel.add(singlePlayerButton, gbc);
        gbc.gridy = 1;
        centerPanel.add(multiplayerButton, gbc);
        add(centerPanel, BorderLayout.CENTER);

        // ==== ส่วนล่าง - ปุ่ม Home ====
        JButton homeButton = createStyledButton("🏠 Home");
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.setOpaque(false);
        bottomPanel.add(homeButton);
        add(bottomPanel, BorderLayout.SOUTH);

        // ==== Event Listener ====
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

    // ปุ่มสไตล์สวยงาม
    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFocusPainted(false);
        button.setBackground(new Color(60, 63, 65));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        button.setPreferredSize(new Dimension(250, 50));
        return button;
    }

    public static void main(String[] args) {
        // จำลอง playerName ถ้ายังไม่มี
        HomeUI.playerName = "Player1";
        SwingUtilities.invokeLater(ModeSelectUI::new);
    }
}
