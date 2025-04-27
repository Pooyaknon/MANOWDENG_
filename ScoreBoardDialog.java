import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ScoreBoardDialog extends JDialog {

    public ScoreBoardDialog(JFrame parent) {
        super(parent, "Scoreboard", true);
        setSize(400, 300);
        setLocationRelativeTo(parent);

        // Example data for the scoreboard (you can replace this with actual data from the server)
        List<String> scores = getScoreboardData();

        // Panel to display the scoreboard
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        // Add header label
        JLabel headerLabel = new JLabel("Scoreboard");
        headerLabel.setFont(new Font("Arial", Font.BOLD, 20));
        headerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(headerLabel);

        // Add scores
        for (String score : scores) {
            JLabel scoreLabel = new JLabel(score);
            scoreLabel.setFont(new Font("Arial", Font.PLAIN, 16));
            scoreLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            panel.add(scoreLabel);
        }

        // Close button
        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> dispose());
        panel.add(closeButton);

        // Add the panel to the dialog
        add(panel);

        // Make the dialog visible
        setVisible(true);
    }

    private List<String> getScoreboardData() {
        // Example scores, you can replace this with actual data fetching logic
        return List.of(
                "Player1: 1000",
                "Player2: 850",
                "Player3: 720",
                "Player4: 650"
        );
    }

}
