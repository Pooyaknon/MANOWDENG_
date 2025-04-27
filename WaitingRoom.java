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

    public WaitingRoom(boolean isHost, HostRoomServer server, JoinRoomClient client) {
        this.isHost = isHost;
        this.server = server;
        this.client = client;

        setTitle("Waiting Room");
        setSize(400, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setupUI();
        setVisible(true);
    }

    private void setupUI() {
        setLayout(new BorderLayout());

        playerListModel = new DefaultListModel<>();
        playerList = new JList<>(playerListModel);
        add(new JScrollPane(playerList), BorderLayout.CENTER);

        if (isHost) {
            playerListModel.addElement("Host (You)");
            // สมมติให้ Host มีชื่อ "Host"
            server.setPlayerListModel(playerListModel);
        } else {
            playerListModel.addElement("You");
            // สมมติให้ Client มีชื่อ "Client"
            client.setPlayerListModel(playerListModel);
        }

        JLabel infoLabel = new JLabel("Waiting for players...", SwingConstants.CENTER);
        add(infoLabel, BorderLayout.NORTH);
    }
}
