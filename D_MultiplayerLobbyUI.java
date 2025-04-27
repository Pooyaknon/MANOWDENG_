import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

public class D_MultiplayerLobbyUI extends JFrame {
    private JTextField roomCodeField;
    private JButton createRoomButton, joinRoomButton;

    public D_MultiplayerLobbyUI() {
        setTitle("Multiplayer Lobby");
        setSize(400, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setupUI();
        setVisible(true);
    }

    private void setupUI() {
        JPanel panel = new JPanel(new GridLayout(5, 1, 10, 10));

        JLabel instructionLabel = new JLabel("Enter Room Code:");
        instructionLabel.setHorizontalAlignment(JLabel.CENTER);

        roomCodeField = new JTextField();
        createRoomButton = new JButton("Create Room");
        joinRoomButton = new JButton("Join Room");

        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.add(instructionLabel);
        panel.add(roomCodeField);
        panel.add(joinRoomButton);
        panel.add(createRoomButton);

        add(panel);

        // กดปุ่มแล้วทำงาน
        createRoomButton.addActionListener(e -> createRoom());
        joinRoomButton.addActionListener(e -> joinRoom());
    }

    private void createRoom() {
        try {
            int port = 5000 + (int)(Math.random() * 1000); // random port 5000-5999
            HostRoomServer server = new HostRoomServer(port);
            server.startAcceptingPlayers();  // เริ่มรอคนเข้า

            String roomCode = RoomCodeUtil.generateRoomCode(port); // สร้าง room code

            JOptionPane.showMessageDialog(this, 
                "Room Created!\nRoom Code: " + roomCode,
                "Room Created",
                JOptionPane.INFORMATION_MESSAGE);

            // ไปหน้า waiting room
            new WaitingRoom(true, server, null);
            dispose();

        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to create room", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void joinRoom() {
        String code = roomCodeField.getText().trim();
        if (code.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter room code", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            String[] parts = RoomCodeUtil.parseRoomCode(code);
            String ip = parts[0];
            int port = Integer.parseInt(parts[1]);

            JoinRoomClient client = new JoinRoomClient(ip, port);

            // ไปหน้า waiting room
            new WaitingRoom(false, null, client);
            dispose();

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to join room", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
