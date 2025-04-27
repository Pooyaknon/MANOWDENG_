import java.io.*;
import java.net.*;

public class JoinRoomClient {
    private Socket socket;
    private BufferedReader in;

    public JoinRoomClient(String ip, int port) throws IOException {
        socket = new Socket(ip, port);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    public void waitForStartSignal() throws IOException {
        while (true) {
            String message = in.readLine();
            if ("START_GAME".equals(message)) {
                break;
            }
        }
    }

    public void close() throws IOException {
        socket.close();
    }
}
