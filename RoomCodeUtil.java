import java.util.Random;

public class RoomCodeUtil {

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int ROOM_CODE_LENGTH = 10;
    private static Random random = new Random();

    public static String generateRoomCode() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < ROOM_CODE_LENGTH; i++) {
            sb.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
        }
        return sb.toString();
    }
}
