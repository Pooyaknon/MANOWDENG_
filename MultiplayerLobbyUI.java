import java.awt.*;
import javax.swing.*;

public class MultiplayerLobbyUI extends JFrame {

    private JTextField roomCodeField;
    private JButton createRoomButton;
    private JButton joinRoomButton;
    private JLabel roomListLabel;

    public MultiplayerLobbyUI() {
        setTitle("MANOWDENG - Multiplayer Lobby");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // ตั้ง Layout
        setLayout(new BorderLayout());

        // ส่วนบน - ชื่อผู้เล่น
        JLabel playerNameLabel = new JLabel("Welcome, " + HomeUI.playerName, SwingConstants.CENTER);
        playerNameLabel.setFont(new Font("Arial", Font.BOLD, 20));
        add(playerNameLabel, BorderLayout.NORTH);

        // ส่วนกลาง - ห้องเกม
        JPanel centerPanel = new JPanel();
        roomListLabel = new JLabel("Available Rooms:");
        centerPanel.add(roomListLabel);
        add(centerPanel, BorderLayout.CENTER);

        // กรอกข้อมูลห้อง
        JPanel bottomPanel = new JPanel(new GridLayout(3, 1, 10, 10));
        roomCodeField = new JTextField();
        createRoomButton = new JButton("Create Room");
        joinRoomButton = new JButton("Join Room");

        bottomPanel.add(roomCodeField);
        bottomPanel.add(createRoomButton);
        bottomPanel.add(joinRoomButton);
        add(bottomPanel, BorderLayout.SOUTH);

        // การคลิกปุ่ม
        createRoomButton.addActionListener(e -> createRoom());
        joinRoomButton.addActionListener(e -> joinRoom());

        setVisible(true);
    }

    // ฟังก์ชั่นสำหรับสร้างห้อง
    private void createRoom() {
        // Logic สำหรับสร้างห้อง
        String roomCode = generateRoomCode();
        JOptionPane.showMessageDialog(this, "Room Created! Room Code: " + roomCode);
        new GameUI(false); // ไปหน้าเกม Multiplayer
        dispose();
    }

    // ฟังก์ชั่นสำหรับเข้าห้อง
    private void joinRoom() {
        String roomCode = roomCodeField.getText();
        if (roomCode.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a valid room code.");
            return;
        }
        // Logic สำหรับเข้าห้องผ่านรหัส
        JOptionPane.showMessageDialog(this, "Joined Room: " + roomCode);
        new GameUI(false); // ไปหน้าเกม Multiplayer
        dispose();
    }

    // สร้างรหัสห้องแบบสุ่ม
    private String generateRoomCode() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder roomCode = new StringBuilder();
        for (int i = 0; i < 5; i++) {
            int index = (int) (Math.random() * characters.length());
            roomCode.append(characters.charAt(index));
        }
        return roomCode.toString();
    }

    // สำหรับทดสอบ run
    public static void main(String[] args) {
        new MultiplayerLobbyUI();
    }
}
