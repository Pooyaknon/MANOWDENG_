import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class SinglePlayerUI extends JFrame {

    private JButton readyButton;
    private JLabel countdownLabel;

    public SinglePlayerUI() {
        setTitle("MANOWDENG - Single Player");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // จัดให้อยู่กลางจอ

        // ตั้ง Layout
        setLayout(new BorderLayout());

        // ส่วนบน - แสดงชื่อผู้เล่น
        JLabel playerNameLabel = new JLabel("Welcome, " + HomeUI.playerName, SwingConstants.CENTER);
        playerNameLabel.setFont(new Font("Arial", Font.BOLD, 20));
        add(playerNameLabel, BorderLayout.NORTH);

        // ส่วนกลาง - ปุ่ม "IM READY TO DENG!!"
        JPanel centerPanel = new JPanel();
        readyButton = new JButton("IM READY TO DENG!!");
        centerPanel.add(readyButton);
        add(centerPanel, BorderLayout.CENTER);

        // ส่วนล่าง - แสดง countdown
        countdownLabel = new JLabel("", SwingConstants.CENTER);
        countdownLabel.setFont(new Font("Arial", Font.BOLD, 50));
        add(countdownLabel, BorderLayout.SOUTH);

        // การคลิกปุ่ม readyButton
        readyButton.addActionListener(e -> startCountdown());

        setVisible(true);
    }

    // เริ่มนับถอยหลัง
    private void startCountdown() {
        readyButton.setEnabled(false); // ไม่ให้กดปุ่มอีก
        new Thread(() -> {
            for (int i = 3; i > 0; i--) {
                try {
                    countdownLabel.setText(String.valueOf(i));
                    Thread.sleep(1000); // นับถอยหลัง 1 วินาที
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
            countdownLabel.setText("GO!");
            try {
                Thread.sleep(1000); // พักอีก 1 วินาที
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
            dispose(); // ปิดหน้า Single Player
            new GameUI(true); // เริ่มเกม Single Player (true คือโหมด Single Player)
        }).start();
    }

    // สำหรับทดสอบ run
    public static void main(String[] args) {
        new SinglePlayerUI();
    }
}
