import java.io.*;
import java.net.*;
import javax.sound.sampled.*;

public class VoiceServer {
    private DatagramSocket socket;
    private int port;
    private SourceDataLine speakers;

    public VoiceServer(int port) throws Exception {
        this.port = port;
        this.socket = new DatagramSocket(port);
        setupSpeakers();
    }

    private void setupSpeakers() throws LineUnavailableException {
        AudioFormat format = new AudioFormat(16000, 16, 1, true, false);
        DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
        speakers = (SourceDataLine) AudioSystem.getLine(info);
        speakers.open(format);
        speakers.start();
    }

    public void startReceiving() {
        byte[] buffer = new byte[1024];
        
        while (true) {
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
            try {
                socket.receive(packet);
                playVoiceData(packet.getData(), packet.getLength());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void playVoiceData(byte[] data, int bytesRead) {
        speakers.write(data, 0, bytesRead);
    }
}
