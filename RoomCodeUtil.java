import java.net.InetAddress;
import java.net.UnknownHostException;

public class RoomCodeUtil {
    public static String generateRoomCode(int port) throws UnknownHostException {
        InetAddress localhost = InetAddress.getLocalHost();
        String ip = localhost.getHostAddress(); // เอา IP จริง
        return ip + ":" + port;
    }

    public static String[] parseRoomCode(String code) {
        return code.split(":");
    }

    public static String getLocalIpAddress() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return "127.0.0.1"; // fallback ถ้า error
        }
    }
}
