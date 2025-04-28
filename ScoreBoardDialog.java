import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Map;

public class ScoreBoardDialog extends JDialog {
    private C2_MultiplayerGameUI parent; // <<< เปลี่ยน type ให้เป็น C2_MultiplayerGameUI

    public ScoreBoardDialog(C2_MultiplayerGameUI parent, Map<String, Integer> scores) {
        super(parent, "Scoreboard", true);
        this.parent = parent;
        setSize(400, 300);
        setLocationRelativeTo(parent);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel headerLabel = new JLabel("Scoreboard");
        headerLabel.setFont(new Font("Arial", Font.BOLD, 20));
        headerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(headerLabel);

        for (Map.Entry<String, Integer> entry : scores.entrySet()) {
            JLabel scoreLabel = new JLabel(entry.getKey() + ": " + entry.getValue());
            scoreLabel.setFont(new Font("Arial", Font.PLAIN, 16));
            scoreLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            panel.add(scoreLabel);
        }

        JButton closeButton = new JButton("Close");
        closeButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        closeButton.addActionListener(e -> closeAllAndReturnHome());
        panel.add(Box.createVerticalStrut(10));
        panel.add(closeButton);

        add(panel);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                closeAllAndReturnHome();
            }
        });

        setVisible(true);
    }

    private void closeAllAndReturnHome() {
        if (parent != null) {
            parent.returnToHome(); // <<< ให้ parent กลับ home เอง
        }
        dispose(); // ปิดตัว ScoreboardDialog ตัวเอง
    }
}
