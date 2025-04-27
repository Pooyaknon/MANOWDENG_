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
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // ขอบเว้นรอบๆ

        JLabel titleLabel = new JLabel("Multiplayer Lobby", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel roomCodeLabel = new JLabel("Enter Room Code:");
        roomCodeLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        gbc.gridx = 0;
        gbc.gridy = 0;
        centerPanel.add(roomCodeLabel, gbc);

        roomCodeField = new JTextField();
        roomCodeField.setFont(new Font("Arial", Font.PLAIN, 16));
        gbc.gridx = 0;
        gbc.gridy = 1;
        centerPanel.add(roomCodeField, gbc);

        joinRoomButton = new JButton("Join Room");
        joinRoomButton.setFont(new Font("Arial", Font.BOLD, 16));
        gbc.gridx = 0;
        gbc.gridy = 2;
        centerPanel.add(joinRoomButton, gbc);

        createRoomButton = new JButton("Create Room");
        createRoomButton.setFont(new Font("Arial", Font.BOLD, 16));
        gbc.gridx = 0;
        gbc.gridy = 3;
        centerPanel.add(createRoomButton, gbc);

        mainPanel.add(centerPanel, BorderLayout.CENTER);
        add(mainPanel);

        // Event
        createRoomButton.addActionListener(e -> createRoom());
        joinRoomButton.addActionListener(e -> joinRoom());
    }

    private void createRoom() {
        try {
            int port = 5000 + (int)(Math.random() * 1000);
            HostRoomServer server = new HostRoomServer(port);
            server.startAcceptingPlayers();
    
            String roomCode = RoomCodeUtil.generateRoomCode(port); // ส่ง port เข้าไป
    
            JOptionPane.showMessageDialog(this,
                "Room Created!\nRoom Code: " + roomCode,
                "Room Created",
                JOptionPane.INFORMATION_MESSAGE);
    
            new WaitingRoom(true, server, null, A_HomeUI.playerName);
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
            new WaitingRoom(false, null, client, A_HomeUI.playerName);
            dispose();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to join room", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
}
