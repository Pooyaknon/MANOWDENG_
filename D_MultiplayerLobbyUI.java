import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class D_MultiplayerLobbyUI extends JFrame {
    private JTextField roomCodeField;
    private JButton createRoomButton, joinRoomButton;
    private BufferedImage backgroundImage;
    private Font pixelFont;
    private String playerName;


    public D_MultiplayerLobbyUI(String playerName) {
        setTitle("MANOWDENG - Multiplayer Lobby");
        setMinimumSize(new Dimension(800, 600));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        loadResources();
        setupUI();
        setVisible(true);
    }

    private void loadResources() {
        try {
            backgroundImage = ImageIO.read(getClass().getResource("zzz_brick_background.png"));
            pixelFont = Font.createFont(Font.TRUETYPE_FONT, getClass().getResourceAsStream("zz_Jersey10-Regular.ttf"));
        } catch (Exception e) {
            e.printStackTrace();
            pixelFont = new Font("Arial", Font.BOLD, 60);
            backgroundImage = new BufferedImage(800, 600, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2d = backgroundImage.createGraphics();
            g2d.setColor(Color.DARK_GRAY);
            g2d.fillRect(0, 0, 800, 600);
            g2d.dispose();
        }
    }

    private void setupUI() {
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (backgroundImage != null) {
                    Graphics2D g2d = (Graphics2D) g;
                    g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                    g2d.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);

                    g2d.setColor(new Color(0, 0, 0, 150)); // มืดทับ
                    g2d.fillRect(0, 0, getWidth(), getHeight());
                }
            }
        };
        mainPanel.setLayout(new BorderLayout(20, 20));
        setContentPane(mainPanel);

        JLabel titleLabel = new JLabel("Multiplayer Lobby", SwingConstants.CENTER);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(pixelFont.deriveFont(36f));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;

        JLabel roomCodeLabel = new JLabel("Enter Room Code:");
        roomCodeLabel.setForeground(Color.WHITE);
        roomCodeLabel.setFont(pixelFont.deriveFont(24f));
        gbc.gridy = 0;
        centerPanel.add(roomCodeLabel, gbc);

        roomCodeField = new JTextField();
        roomCodeField.setFont(pixelFont.deriveFont(20f));
        gbc.gridy = 1;
        centerPanel.add(roomCodeField, gbc);

        joinRoomButton = createStyledButton("JOIN ROOM", new Color(50, 200, 100));
        gbc.gridy = 2;
        centerPanel.add(joinRoomButton, gbc);

        createRoomButton = createStyledButton("CREATE ROOM", new Color(200, 150, 50));
        gbc.gridy = 3;
        centerPanel.add(createRoomButton, gbc);

        mainPanel.add(centerPanel, BorderLayout.CENTER);

        setupEventListeners();
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(pixelFont.deriveFont(28f));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.WHITE, 3, true),
            BorderFactory.createEmptyBorder(10, 40, 10, 40)
        ));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    private void setupEventListeners() {
        createRoomButton.addActionListener(e -> createRoom());
        joinRoomButton.addActionListener(e -> joinRoom());
    }

    private void createRoom() {
        try {
            int port = 5000 + (int)(Math.random() * 1000);
            HostRoomServer server = new HostRoomServer(port);
            server.startAcceptingPlayers();
    
            String roomCode = RoomCodeUtil.generateRoomCode(port);
    
            JOptionPane.showMessageDialog(this,
                "Room Created!\nRoom Code: " + roomCode,
                "Room Created",
                JOptionPane.INFORMATION_MESSAGE);
    
            new WaitingRoom(true, server, null, A_HomeUI.playerName);
            dispose();
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to create room", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void joinRoom() {
        String code = roomCodeField.getText().trim();
        if (code.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter room code", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
    
        try {
            String[] parts = RoomCodeUtil.parseRoomCode(code);
            String ip = parts[0];
            int port = Integer.parseInt(parts[1]);
    
            JoinRoomClient client = new JoinRoomClient(ip, port, playerName);
            new WaitingRoom(false, null, client, A_HomeUI.playerName);
            dispose();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to join room", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
