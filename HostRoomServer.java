import java.io.*;
import java.net.*;
import java.util.*;

public class HostRoomServer {
    private ServerSocket serverSocket;
    private List<ClientHandler> clients = new ArrayList<>();
    private List<String> playerNames = new ArrayList<>();
    private boolean running = true;

    public HostRoomServer(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        new Thread(this::acceptClients).start();
    }

    private void acceptClients() {
        while (running) {
            try {
                Socket socket = serverSocket.accept();
                ClientHandler handler = new ClientHandler(socket);
                clients.add(handler);
                new Thread(handler).start();
            } catch (IOException e) {
                if (running) e.printStackTrace();
            }
        }
    }

    private class ClientHandler implements Runnable {
        private Socket socket;
        private PrintWriter out;
        private BufferedReader in;

        public ClientHandler(Socket socket) throws IOException {
            this.socket = socket;
            this.out = new PrintWriter(socket.getOutputStream(), true);
            this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        }

        public void send(String message) {
            out.println(message);
        }

        @Override
        public void run() {
            try {
                String name = in.readLine(); // อ่านชื่อจาก client
                synchronized (playerNames) {
                    playerNames.add(name);
                }
                broadcastPlayerList();

                // รอจนกว่าจะมีคำสั่ง start (ไม่ทำอะไรต่อ)
                while (running && in.readLine() != null) {}
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void broadcastPlayerList() {
        synchronized (playerNames) {
            for (ClientHandler client : clients) {
                client.send("UPDATE_PLAYER_LIST");
                for (String name : playerNames) {
                    client.send(name);
                }
                client.send("END_OF_LIST");
            }
        }
    }

    public void sendStartSignal() {
        for (ClientHandler client : clients) {
            client.send("START_GAME");
        }
    }

    public void close() {
        running = false;
        try {
            serverSocket.close();
        } catch (IOException e) {}
    }

    public void startAcceptingPlayers() {
        new Thread(this::acceptClients).start();
    }
    
}
