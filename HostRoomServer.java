import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import javax.swing.DefaultListModel;

public class HostRoomServer {
    private ServerSocket serverSocket;
    private DefaultListModel<String> playerListModel;

    public HostRoomServer(int port) throws IOException {
        serverSocket = new ServerSocket(port);
    }

    public void startAcceptingPlayers() {
        Thread acceptThread = new Thread(() -> {
            while (true) {
                try {
                    Socket clientSocket = serverSocket.accept();
                    System.out.println("A player connected: " + clientSocket.getInetAddress());

                    if (playerListModel != null) {
                        String playerName = "Player " + (playerListModel.getSize()); 
                        playerListModel.addElement(playerName);
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
}
