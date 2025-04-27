import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultListModel;

public class HostRoomServer {
    private ServerSocket serverSocket;
    private DefaultListModel<String> playerListModel;
    private List<ObjectOutputStream> clientOutputStreams = new ArrayList<>();

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

                    if (playerListModel != null) {
                        String playerName = "Player " + playerListModel.getSize();
                        playerListModel.addElement(playerName);

                        broadcastPlayerList();
                    }
                } catch (IOException e) {
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
            List<String> players = new ArrayList<>();
            for (int i = 0; i < playerListModel.size(); i++) {
                players.add(playerListModel.getElementAt(i));
            }

            for (ObjectOutputStream out : clientOutputStreams) {
                out.writeObject(players);
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
                out.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
}


