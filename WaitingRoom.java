import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.Socket;
import java.util.List;

public class WaitingRoom extends JFrame {
    private boolean isHost;
    private HostRoomServer server;
    private JoinRoomClient client;
    private JButton startButton;

    public WaitingRoom(boolean isHost, HostRoomServer server, JoinRoomClient client) {
        this.isHost = isHost;
        this.server = server;
        this.client = client;

        setTitle("Waiting Room");
        setSize(300, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setupUI();

        if (!isHost) {
            new Thread(this::waitForStart).start();
        }
        
        setVisible(true);
    }

    private void setupUI() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        JLabel label = new JLabel(isHost ? "Waiting for players..." : "Waiting for host...", SwingConstants.CENTER);
        panel.add(label, BorderLayout.CENTER);

        if (isHost) {
            startButton = new JButton("Start Game");
            panel.add(startButton, BorderLayout.SOUTH);

            startButton.addActionListener(e -> {
                server.sendStartSignal();
                startGame();
            });
        }

        add(panel);
    }

    private void waitForStart() {
        try {
            client.waitForStartSignal();
            startGame();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void startGame() {
        SwingUtilities.invokeLater(() -> {
            new C1_GameUI(false);
            if (isHost && server != null) server.close();
            if (!isHost && client != null) {
                try { client.close(); } catch (IOException e) {}
            }
            dispose();
        });
    }
}
