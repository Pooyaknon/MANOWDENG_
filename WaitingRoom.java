import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;

public class WaitingRoom extends JFrame {
    private boolean isHost;
    private HostRoomServer server;
    private JoinRoomClient client;

    private DefaultListModel<String> playerListModel;
    private JList<String> playerList;

    public WaitingRoom(boolean isHost, HostRoomServer server, JoinRoomClient client, String playerName) {
        this.isHost = isHost;
        this.server = server;
        this.client = client;
    
        setTitle("Waiting Room");
        setSize(400, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setupUI(playerName); // ส่ง playerName ไป
        setVisible(true);
    }
    

    private void setupUI(String playerName) {
        setLayout(new BorderLayout());
    
        playerListModel = new DefaultListModel<>();
        playerList = new JList<>(playerListModel);
        add(new JScrollPane(playerList), BorderLayout.CENTER);
    
        if (isHost) {
            playerListModel.addElement(playerName + " (Host)");  // ใส่ชื่อ host จริงๆ
            server.setPlayerListModel(playerListModel);
        } else {
            playerListModel.addElement(playerName);  // ใส่ชื่อ player จริงๆ
            client.setPlayerListModel(playerListModel);
        }
    
        JLabel infoLabel = new JLabel("Waiting for players...", SwingConstants.CENTER);
        add(infoLabel, BorderLayout.NORTH);
    }
    
}
