import java.io.*;
import java.net.*;
import javax.sound.sampled.*;

public class VoiceClient {
    private DatagramSocket socket;
    private InetAddress serverAddress;
    private int serverPort;
    private TargetDataLine microphone;

    public VoiceClient(String serverIP, int serverPort) throws Exception {
        this.serverAddress = InetAddress.getByName(serverIP);
        this.serverPort = serverPort;
        this.socket = new DatagramSocket();
        setupMicrophone();
    }

    private void setupMicrophone() throws LineUnavailableException {
        AudioFormat format = new AudioFormat(16000, 16, 1, true, false);
        DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
        microphone = (TargetDataLine) AudioSystem.getLine(info);
        microphone.open(format);
    }

    public void startRecording() {
        byte[] buffer = new byte[1024];
        microphone.start();
        
        new Thread(() -> {
            while (true) {
                int bytesRead = microphone.read(buffer, 0, buffer.length);
                if (bytesRead > 0) {
                    sendVoiceData(buffer, bytesRead);
                }
            }
        }).start();
    }

    private void sendVoiceData(byte[] data, int bytesRead) {
        DatagramPacket packet = new DatagramPacket(data, bytesRead, serverAddress, serverPort);
        try {
            socket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stopRecording() {
        microphone.stop();
    }
}
