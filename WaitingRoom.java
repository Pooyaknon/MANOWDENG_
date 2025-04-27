import javax.swing.*;
import java.awt.*;
import java.util.List;

public class WaitingRoom extends JFrame {
    private boolean isHost;
    private HostRoomServer server;
    private JoinRoomClient client;

    private DefaultListModel<String> playerListModel;
    private JList<String> playerList;
    private JButton startButton; // <<<< เพิ่มตรงนี้

    public WaitingRoom(boolean isHost, HostRoomServer server, JoinRoomClient client, String playerName) {
        this.isHost = isHost;
        this.server = server;
        this.client = client;
    
        setTitle("Waiting Room");
        setSize(400, 400); // เพิ่มสูงนิดนึง
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
        }
    
        JLabel infoLabel = new JLabel("Waiting for players...", SwingConstants.CENTER);
        add(infoLabel, BorderLayout.NORTH);

        // ---- ส่วนเพิ่มปุ่ม START GAME ด้านล่าง ----
        if (isHost) {
            startButton = new JButton("Start Game");
            startButton.setFont(new Font("Arial", Font.BOLD, 20));
            startButton.setBackground(new Color(50, 200, 50));
            startButton.setForeground(Color.WHITE);
            startButton.setFocusPainted(false);
            startButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            
            startButton.addActionListener(e -> {
                JOptionPane.showMessageDialog(this, "Game Started!");
                // TODO: ใส่โค้ดไปหน้าเกมจริง ๆ ที่นี่
            });

            JPanel bottomPanel = new JPanel();
            bottomPanel.setLayout(new FlowLayout());
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
