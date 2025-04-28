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
    private WaitingRoom waitingRoom; // <<< เพิ่มตัวแปรเก็บ WaitingRoom

    public JoinRoomClient(String host, int port) throws IOException {
        socket = new Socket(host, port);
        in = new ObjectInputStream(socket.getInputStream());
        listenForUpdates();
    }

    public void setPlayerListModel(DefaultListModel<String> model) {
        this.playerListModel = model;
    }

    public void setWaitingRoom(WaitingRoom waitingRoom) {
        this.waitingRoom = waitingRoom;
    }

    private void listenForUpdates() {
        Thread listenThread = new Thread(() -> {
            try {
                while (true) {
                    Object obj = in.readObject();
                    if (obj instanceof List) {
                        List<String> players = (List<String>) obj;
                        updatePlayerList(players);
                    } else if (obj instanceof String) {
                        String command = (String) obj;
                        if (command.equals("START_GAME")) {
                            // ฝั่ง Client เริ่มเกม
                            SwingUtilities.invokeLater(() -> {
                                if (waitingRoom != null) {
                                    waitingRoom.dispose();
                                }
                                new C2_MultiplayerGameUI(false); // เริ่มเกมแบบ Multiplayer
                            });
                        }
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
            SwingUtilities.invokeLater(() -> {
                playerListModel.clear();
                for (String player : players) {
                    playerListModel.addElement(player);
                }
            });
        }
    }
}
