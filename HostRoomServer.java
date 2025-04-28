import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.DefaultListModel;

public class HostRoomServer {
    private ServerSocket serverSocket;
    private DefaultListModel<String> playerListModel;
    private List<ObjectOutputStream> clientOutputStreams = new ArrayList<>();
    private Map<String, Integer> playerScores = new HashMap<>();
    private List<String> playerNames = new ArrayList<>();

    public HostRoomServer(int port) throws IOException {
        serverSocket = new ServerSocket(port);
    }

    public void startAcceptingPlayers() {
        Thread acceptThread = new Thread(() -> {
            while (true) {
                try {
                    Socket clientSocket = serverSocket.accept();
                    System.out.println("A player connected: " + clientSocket.getInetAddress());

                    ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
                    clientOutputStreams.add(out);

                    ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());
                    String playerName = (String) in.readObject(); // อ่านชื่อจริงจาก client

                    playerNames.add(playerName);
                    playerScores.put(playerName, 0);

                    if (playerListModel != null) {
                        playerListModel.addElement(playerName);
                    }

                    broadcastPlayerList();
                } catch (Exception e) {
                    e.printStackTrace();
                    break;
                }
            }
        });
        acceptThread.start();
    }

    public void setPlayerListModel(DefaultListModel<String> model) {
        this.playerListModel = model;
    }

    private void broadcastPlayerList() {
        try {
            for (ObjectOutputStream out : clientOutputStreams) {
                out.writeObject(new ArrayList<>(playerNames));
                out.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void broadcastStartGame() {
        try {
            for (ObjectOutputStream out : clientOutputStreams) {
                out.writeObject("START_GAME");
                out.writeObject(new ArrayList<>(playerNames));
                out.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void broadcastScores() {
        try {
            for (ObjectOutputStream out : clientOutputStreams) {
                out.writeObject(playerScores);
                out.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updatePlayerScore(String playerName, int newScore) {
        playerScores.put(playerName, newScore);
    }
}
