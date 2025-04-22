import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ModeSelectUI extends JFrame {

    public ModeSelectUI() {
        setTitle("MANOWDENG - Select Mode");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // จัดให้อยู่กลางจอ

        // ตั้ง Layout
        setLayout(new BorderLayout());

        // ส่วนบน - ชื่อผู้เล่น
        JLabel playerNameLabel = new JLabel("Welcome, " + HomeUI.playerName, SwingConstants.CENTER);
        playerNameLabel.setFont(new Font("Arial", Font.BOLD, 20));
        add(playerNameLabel, BorderLayout.NORTH);

        // ส่วนกลาง - เลือกโหมด
        JPanel centerPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        JButton singlePlayerButton = new JButton("Single Player");
        JButton multiplayerButton = new JButton("Multiplayer");

        centerPanel.add(singlePlayerButton);
        centerPanel.add(multiplayerButton);
        add(centerPanel, BorderLayout.CENTER);

        // ส่วนล่าง - ปุ่ม Home
        JButton homeButton = new JButton("Home");
        add(homeButton, BorderLayout.SOUTH);

        // การคลิกปุ่ม
        singlePlayerButton.addActionListener(e -> {
            dispose(); // ปิดหน้าเลือกโหมด
            new SinglePlayerUI(); // ไปหน้า Single Player
        });

        multiplayerButton.addActionListener(e -> {
            dispose(); // ปิดหน้าเลือกโหมด
            new MultiplayerLobbyUI(); // ไปหน้า Lobby Multiplayer
        });

        homeButton.addActionListener(e -> {
            dispose(); // ปิดหน้าเลือกโหมด
            new HomeUI(); // กลับไปหน้าแรก
        });

        setVisible(true);
    }

    // สำหรับทดสอบ run
    public static void main(String[] args) {
        new ModeSelectUI();
    }
}
