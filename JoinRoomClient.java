import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.List;
import javax.swing.DefaultListModel;

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
}
