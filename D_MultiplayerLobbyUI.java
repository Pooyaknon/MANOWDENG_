import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class D_MultiplayerLobbyUI extends JFrame {

    private BufferedImage backgroundImage;
    private Font pixelFont;
    private JTextField roomCodeField;
    private JButton createRoomButton;
    private JButton joinRoomButton;
    private JButton backButton;
    private JList<String> roomList;
    private DefaultListModel<String> roomListModel;

    public D_MultiplayerLobbyUI() {
        setTitle("MANOWDENG - Multiplayer Lobby");
        setMinimumSize(new Dimension(800, 600));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        loadResources();
        setupUI();

        setVisible(true);
    }

    private void loadResources() {
        try {
            // ใช้ ClassLoader โหลดไฟล์จากโฟลเดอร์ปัจจุบัน
            backgroundImage = ImageIO.read(getClass().getResource("zzz_brick_background.png"));
            pixelFont = Font.createFont(Font.TRUETYPE_FONT, 
                getClass().getResourceAsStream("zz_Jersey10-Regular.ttf"));
        } catch (Exception e) {
            e.printStackTrace();
            // Fallback
            pixelFont = new Font("Arial", Font.BOLD, 60);
            backgroundImage = new BufferedImage(800, 600, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2d = backgroundImage.createGraphics();
            g2d.setColor(Color.DARK_GRAY);
            g2d.fillRect(0, 0, 800, 600);
            g2d.dispose();
        }
    }

    private void setupUI() {
        JPanel contentPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (backgroundImage != null) {
                    Graphics2D g2d = (Graphics2D)g;
                    g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                    g2d.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);

                    g2d.setColor(new Color(0, 0, 0, 150));
                    g2d.fillRect(0, 0, getWidth(), getHeight());
                }
            }
        };
        contentPanel.setLayout(new BorderLayout(20, 20));
        setContentPane(contentPanel);

        // Player name label
        JLabel playerNameLabel = new JLabel("Multiplayer - " + A_HomeUI.playerName, SwingConstants.CENTER);
        playerNameLabel.setForeground(Color.WHITE);
        playerNameLabel.setFont(pixelFont.deriveFont(36f));
        contentPanel.add(playerNameLabel, BorderLayout.NORTH);

        // Main content panel
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.BOTH;

        // Room list panel
        JPanel roomListPanel = new JPanel(new BorderLayout());
        roomListPanel.setOpaque(false);
        roomListPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.WHITE, 2),
            "Available Rooms",
            javax.swing.border.TitledBorder.CENTER,
            javax.swing.border.TitledBorder.DEFAULT_POSITION,
            pixelFont.deriveFont(24f),
            Color.WHITE
        ));

        roomListModel = new DefaultListModel<>();
        // Sample rooms - in real app these would come from server
        roomListModel.addElement("ROOM-ABCDE (2/4 players)");
        roomListModel.addElement("ROOM-FGHIJ (1/4 players)");
        roomListModel.addElement("ROOM-KLMNO (3/4 players)");

        roomList = new JList<>(roomListModel);
        roomList.setFont(pixelFont.deriveFont(20f));
        roomList.setForeground(Color.WHITE);
        roomList.setBackground(new Color(0, 0, 0, 100));
        roomList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        roomList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                setForeground(Color.WHITE);
                setBackground(isSelected ? new Color(50, 150, 250, 150) : new Color(0, 0, 0, 0));
                setFont(pixelFont.deriveFont(20f));
                return this;
            }
        });

        JScrollPane scrollPane = new JScrollPane(roomList);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        roomListPanel.add(scrollPane, BorderLayout.CENTER);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.weighty = 1;
        mainPanel.add(roomListPanel, gbc);

        // Room code input panel
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setOpaque(false);
        GridBagConstraints gbcInput = new GridBagConstraints();
        gbcInput.insets = new Insets(5, 5, 5, 5);
        gbcInput.fill = GridBagConstraints.HORIZONTAL;

        roomCodeField = new JTextField();
        roomCodeField.setFont(pixelFont.deriveFont(20f));
        roomCodeField.setHorizontalAlignment(JTextField.CENTER);
        roomCodeField.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
        roomCodeField.setOpaque(false);
        roomCodeField.setForeground(Color.WHITE);
        roomCodeField.setCaretColor(Color.WHITE);
        roomCodeField.setPreferredSize(new Dimension(200, 40));

        createRoomButton = createStyledButton("CREATE ROOM", 24f, new Color(50, 150, 250));
        joinRoomButton = createStyledButton("JOIN ROOM", 24f, new Color(250, 150, 50));
        backButton = createStyledButton("BACK", 22f, new Color(100, 100, 100));

        gbcInput.gridx = 0;
        gbcInput.gridy = 0;
        gbcInput.gridwidth = 2;
        inputPanel.add(roomCodeField, gbcInput);

        gbcInput.gridy = 1;
        gbcInput.gridwidth = 1;
        inputPanel.add(createRoomButton, gbcInput);

        gbcInput.gridx = 1;
        inputPanel.add(joinRoomButton, gbcInput);

        gbc.gridy = 1;
        gbc.weighty = 0;
        mainPanel.add(inputPanel, gbc);

        contentPanel.add(mainPanel, BorderLayout.CENTER);

        // Bottom panel with back button
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.setOpaque(false);
        bottomPanel.add(backButton);
        contentPanel.add(bottomPanel, BorderLayout.SOUTH);

        setupEventListeners();
    }

    private JButton createStyledButton(String text, float fontSize, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(pixelFont.deriveFont(fontSize));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.WHITE, 2, true),
            BorderFactory.createEmptyBorder(5, 20, 5, 20)
        ));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    private void setupEventListeners() {
        createRoomButton.addActionListener(e -> createRoom());
        joinRoomButton.addActionListener(e -> joinRoom());
        backButton.addActionListener(e -> {
            dispose();
            new B_ModeSelectUI();
        });

        // Double click to join room from list
        roomList.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    String selectedRoom = roomList.getSelectedValue();
                    if (selectedRoom != null) {
                        String roomCode = selectedRoom.split(" ")[0];
                        roomCodeField.setText(roomCode);
                        joinRoom();
                    }
                }
            }
        });
    }

    private void createRoom() {
        String roomCode = generateRoomCode();
        JOptionPane.showMessageDialog(this, 
            "Room Created!\nRoom Code: " + roomCode + "\nShare this code with friends!",
            "Room Created",
            JOptionPane.INFORMATION_MESSAGE);
        
        // In a real app, you would send this to the server
        roomListModel.add(0, roomCode + " (1/4 players)");
        
        // Start game (in real app you'd wait for other players)
        new C1_GameUI(false);
        dispose();
    }

    private void joinRoom() {
        String roomCode = roomCodeField.getText().trim().toUpperCase();
        if (roomCode.isEmpty()) {
            showError("Please enter a room code");
            return;
        }

        // In a real app, you would validate with server
        boolean roomExists = false;
        for (int i = 0; i < roomListModel.size(); i++) {
            if (roomListModel.get(i).startsWith(roomCode)) {
                roomExists = true;
                break;
            }
        }

        if (roomExists) {
            new C1_GameUI(false);
            dispose();
        } else {
            showError("Room not found. Please check the code and try again.");
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, 
            message,
            "Error",
            JOptionPane.ERROR_MESSAGE);
    }

    private String generateRoomCode() {
        String characters = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789"; // Avoid confusing characters
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < 5; i++) {
            code.append(characters.charAt((int)(Math.random() * characters.length())));
        }
        return code.toString();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            A_HomeUI.playerName = "Player1";
            new D_MultiplayerLobbyUI();
        });
    }
}