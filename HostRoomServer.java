import java.io.*;
import java.net.*;
import java.util.*;

public class HostRoomServer {
    private ServerSocket serverSocket;
    private List<Socket> players = Collections.synchronizedList(new ArrayList<>());

    public HostRoomServer(int port) throws IOException {
        serverSocket = new ServerSocket(port);
    }

    public void startAcceptingPlayers() {
        new Thread(() -> {
            while (true) {
                try {
                    Socket socket = serverSocket.accept();
                    players.add(socket);
                    System.out.println("Player joined: " + socket.getInetAddress());
                } catch (IOException e) {
                    break;
                }
            }
        }).start();
    }

    public List<Socket> getPlayers() {
        return players;
    }

    public void sendStartSignal() {
        synchronized (players) {
            for (Socket s : players) {
                try {
                    PrintWriter out = new PrintWriter(s.getOutputStream(), true);
                    out.println("START_GAME");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void close() {
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
