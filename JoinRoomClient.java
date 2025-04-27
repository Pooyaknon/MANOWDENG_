import java.io.IOException;
import java.net.Socket;
import javax.swing.DefaultListModel;

public class JoinRoomClient {
    private Socket socket;
    private DefaultListModel<String> playerListModel;

    public JoinRoomClient(String roomCode) throws IOException {
        String ip = "127.0.0.1"; // ตอนนี้ fix เป็น localhost ก่อน (เพราะ room code ยังไม่ผูก IP จริง)
        int port = 5000;          // fix port ไว้ด้วย ตอน dev (อนาคตค่อย upgrade ให้ map ได้)

        socket = new Socket(ip, port);
        System.out.println("Connected to server with room code: " + roomCode);
    }

    public void setPlayerListModel(DefaultListModel<String> model) {
        this.playerListModel = model;
        if (model != null) {
            model.addElement("You (Client)");
        }
    }
}
