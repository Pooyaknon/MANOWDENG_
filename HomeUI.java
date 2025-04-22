import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class HomeUI extends JFrame {

    private JTextField nameField;
    private JButton startButton;
    public static String playerName = ""; // เก็บชื่อผู้เล่นไว้ใช้ข้ามไฟล์

    public HomeUI() {
        setTitle("MANOWDENG - Home");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // จัดให้อยู่กลางจอ

        // ตั้ง layout
        setLayout(new BorderLayout());

        // ส่วนบน - ชื่อเกม
        JLabel titleLabel = new JLabel("MANOWDENG", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        add(titleLabel, BorderLayout.NORTH);

        // ส่วนกลาง - ช่องกรอกชื่อ
        JPanel centerPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        nameField = new JTextField();
        nameField.setHorizontalAlignment(JTextField.CENTER);
        centerPanel.add(new JLabel("Enter your name:", SwingConstants.CENTER));
        centerPanel.add(nameField);
        add(centerPanel, BorderLayout.CENTER);

        // ส่วนล่าง - ปุ่ม START
        startButton = new JButton("START");
        startButton.setEnabled(false); // ยังไม่ให้กดจนกว่าจะกรอกชื่อ
        add(startButton, BorderLayout.SOUTH);

        // ตรวจจับการพิมพ์ชื่อ
        nameField.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                startButton.setEnabled(!nameField.getText().trim().isEmpty());
            }
        });

        // เมื่อกดปุ่ม START
        startButton.addActionListener(e -> {
            playerName = nameField.getText().trim();
            dispose(); // ปิดหน้านี้
            new ModeSelectUI(); // ไปหน้าเลือกโหมด
        });

        setVisible(true);
    }

    // สำหรับทดสอบ run
    public static void main(String[] args) {
        new HomeUI();
    }
}
