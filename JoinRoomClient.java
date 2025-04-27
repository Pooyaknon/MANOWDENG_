import java.io.IOException;
import java.net.Socket;
import javax.swing.DefaultListModel;

public class JoinRoomClient {
    private Socket socket;
    private DefaultListModel<String> playerListModel;

    public JoinRoomClient(String host, int port) throws IOException {
        socket = new Socket(host, port);
    }

    public void setPlayerListModel(DefaultListModel<String> model) {
        this.playerListModel = model;
        if (model != null) {
            model.addElement("You (Client)");
        }
    }
}
