import java.net.InetAddress;

public class RoomCodeUtil {
    public static String generateRoomCode(int port) {
        try {
            String ip = InetAddress.getLocalHost().getHostAddress();
            return ip + ":" + port;
        } catch (Exception e) {
            e.printStackTrace();
            return "ERROR:0";
        }
    }

    public static String[] parseRoomCode(String code) {
        return code.split(":");
    }
}
