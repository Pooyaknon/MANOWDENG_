import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.SwingUtilities;

public class JoinRoomClient {
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private WaitingRoom waitingRoom;
    private DefaultListModel<String> playerListModel;

    public JoinRoomClient(String host, int port, String playerName) {
        try {
            socket = new Socket(host, port);
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());

            // ส่งชื่อผู้เล่นไปให้ Host
            out.writeObject(playerName);
            out.flush();

            // เริ่มฟังรอข้อความจาก Host
            new Thread(() -> listenForServerMessages()).start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setPlayerListModel(DefaultListModel<String> model) {
        this.playerListModel = model;
    }

    private void listenForServerMessages() {
        try {
            while (true) {
                Object obj = in.readObject();  // << ตรงนี้คือสิ่งที่คุณลืม "ประกาศ obj"

                if (obj instanceof List) {
                    List<String> playersFromServer = (List<String>) obj;

                    // อัปเดต playerListModel ให้ลิสต์ชื่อใหม่
                    SwingUtilities.invokeLater(() -> {
                        if (playerListModel != null) {
                            playerListModel.clear();
                            for (String player : playersFromServer) {
                                playerListModel.addElement(player);
                            }
                        }
                    });
                } else if (obj instanceof String) {
                    String command = (String) obj;
                    if (command.equals("START_GAME")) {
                        @SuppressWarnings("unchecked")
                        List<String> players = (List<String>) in.readObject();
                        SwingUtilities.invokeLater(() -> {
                            if (waitingRoom != null) {
                                waitingRoom.dispose();
                            }
                            new C2_MultiplayerGameUI(false, players, out); // << เพิ่มส่ง out ไปด้วย
                        });
                        break;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setWaitingRoom(WaitingRoom waitingRoom) {
        this.waitingRoom = waitingRoom;
    }
}
