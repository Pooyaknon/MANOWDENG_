import javax.swing.*;
import java.awt.*;
import java.util.List;

public class WaitingRoom extends JFrame {
    private boolean isHost;
    private HostRoomServer server;
    private JoinRoomClient client;

    private DefaultListModel<String> playerListModel;
    private JList<String> playerList;
    private JButton startButton; // ปุ่ม Start สำหรับ Host

    public WaitingRoom(boolean isHost, HostRoomServer server, JoinRoomClient client, String playerName) {
        this.isHost = isHost;
        this.server = server;
        this.client = client;
    
        setTitle("Waiting Room");
        setSize(400, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setupUI(playerName);
        setVisible(true);
    }

    private void setupUI(String playerName) {
        setLayout(new BorderLayout());
        playerListModel = new DefaultListModel<>();
        playerList = new JList<>(playerListModel);
        add(new JScrollPane(playerList), BorderLayout.CENTER);
    
        if (isHost) {
            playerListModel.addElement(playerName + " (Host)");
            server.setPlayerListModel(playerListModel);
        } else {
            playerListModel.addElement(playerName);
            client.setPlayerListModel(playerListModel);
            client.setWaitingRoom(this); // <<< สำคัญมาก! ต้องมี
        }
    
        JLabel infoLabel = new JLabel("Waiting for players...", SwingConstants.CENTER);
        add(infoLabel, BorderLayout.NORTH);
    
        if (isHost) {
            startButton = new JButton("Start Game");
            startButton.addActionListener(e -> {
                server.broadcastStartGame(); // ส่งสัญญาณให้ทุก client เริ่มเกม
                dispose();
                new C2_MultiplayerGameUI(false); // เริ่มเกมฝั่ง host เอง
            });
            JPanel bottomPanel = new JPanel();
            bottomPanel.add(startButton);
            add(bottomPanel, BorderLayout.SOUTH);
        }
    }
    
    public void updatePlayerList(List<String> players) {
        playerListModel.clear();
        for (String player : players) {
            playerListModel.addElement(player);
        }
    }
}
