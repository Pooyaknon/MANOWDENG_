import java.io.*;
import java.net.*;
import java.util.*;

public class JoinRoomClient {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private WaitingRoom waitingRoom;
    private boolean running = true;

    public JoinRoomClient(String host, int port) throws IOException {
        socket = new Socket(host, port);
        out = new PrintWriter(socket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        // ส่งชื่อเข้า server ทันที
        out.println(A_HomeUI.playerName);

        new Thread(this::listenForUpdates).start();
    }

    public void setWaitingRoom(WaitingRoom waitingRoom) {
        this.waitingRoom = waitingRoom;
    }

    private void listenForUpdates() {
        try {
            while (running) {
                String line = in.readLine();
                if (line == null) break;

                if (line.equals("UPDATE_PLAYER_LIST")) {
                    List<String> names = new ArrayList<>();
                    while (!(line = in.readLine()).equals("END_OF_LIST")) {
                        names.add(line);
                    }
                    if (waitingRoom != null) {
                        waitingRoom.updatePlayerList(names);
                    }
                }
            }
        } catch (IOException e) {
            if (running) e.printStackTrace();
        }
    }

    public void waitForStartSignal() throws IOException {
        while (true) {
            String line = in.readLine();
            if (line != null && line.equals("START_GAME")) {
                break;
            }
        }
    }

    public void close() throws IOException {
        running = false;
        socket.close();
    }
}
