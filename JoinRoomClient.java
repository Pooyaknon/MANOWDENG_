import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.SwingUtilities;

public class JoinRoomClient {
    private Socket socket;
    private ObjectInputStream in;
    private DefaultListModel<String> playerListModel;

    public JoinRoomClient(String host, int port) throws IOException {
        socket = new Socket(host, port);
        in = new ObjectInputStream(socket.getInputStream());

        listenForUpdates();
    }

    public void setPlayerListModel(DefaultListModel<String> model) {
        this.playerListModel = model;
    }

    private void listenForUpdates() {
        Thread listenThread = new Thread(() -> {
            try {
                while (true) {
                    Object obj = in.readObject();
                    if (obj instanceof List) {
                        List<String> players = (List<String>) obj;
                        updatePlayerList(players);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        listenThread.start();
    }

    private void updatePlayerList(List<String> players) {
        if (playerListModel != null) {
            playerListModel.clear();
            for (String player : players) {
                playerListModel.addElement(player);
            }
        }
    }

        // ฟัง server อยู่ตลอด
    public void listenToServer(WaitingRoom waitingRoom) {
        new Thread(() -> {
            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String line;
                while ((line = in.readLine()) != null) {
                    if (line.equals("START_GAME")) {
                        SwingUtilities.invokeLater(() -> {
                            waitingRoom.dispose();
                            new C2_MultiplayerGameUI(false); // เริ่มเกมฝั่ง client
                        });
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

}
