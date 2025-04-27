import javax.swing.*;

public class D9_Multi extends JFrame {
    public D9_Multi(boolean isHost) {
        setTitle("Game Started!");
        setSize(400, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JLabel label = new JLabel("Game is running...", SwingConstants.CENTER);
        add(label);

        setVisible(true);
    }
}
