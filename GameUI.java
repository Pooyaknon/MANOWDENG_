import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;

public class GameUI extends JFrame {

    private int playerLives = 3;
    private int playerScore = 0;
    private boolean isSinglePlayer;
    private Timer gameTimer;
    private Ball ball;
    private Paddle paddle;
    private ArrayList<Block> blocks;
    private JLabel scoreLabel;
    private JLabel livesLabel;
    private JLabel playerNameLabel;

    private VoiceClient voiceClient;
    private boolean isMicrophoneOn = false;
    private JButton micButton;

    public GameUI(boolean isSinglePlayer) {
        this.isSinglePlayer = isSinglePlayer;
        setTitle("MANOWDENG - Game");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // ตั้ง Layout
        setLayout(new BorderLayout());

        // ส่วนบน - ชื่อผู้เล่น, คะแนน, ชีวิต
        JPanel topPanel = new JPanel();
        playerNameLabel = new JLabel("Player: " + HomeUI.playerName);
        scoreLabel = new JLabel("Score: " + playerScore);
        livesLabel = new JLabel("Lives: " + playerLives);
        topPanel.add(playerNameLabel);
        topPanel.add(scoreLabel);
        topPanel.add(livesLabel);
        add(topPanel, BorderLayout.NORTH);

        // ส่วนกลาง - พื้นที่เล่นเกม
        GamePanel gamePanel = new GamePanel();
        add(gamePanel, BorderLayout.CENTER);

        // ปุ่ม Pause
        JButton pauseButton = new JButton("Pause");
        pauseButton.addActionListener(e -> pauseGame());
        add(pauseButton, BorderLayout.SOUTH);

        // ปุ่มเปิด/ปิดไมโครโฟน
        micButton = new JButton("Turn Mic On");
        micButton.addActionListener(e -> toggleMicrophone());
        add(micButton, BorderLayout.SOUTH);

        // เชื่อมต่อกับ VoiceServer
        try {
            voiceClient = new VoiceClient("server_ip_here", 5000);  // ใช้ IP ของเซิร์ฟเวอร์
        } catch (Exception e) {
            e.printStackTrace();
        }

        setVisible(true);

        // เริ่มเกม
        initializeGame();
    }

    // Initialize game elements
    private void initializeGame() {
        ball = new Ball();
        paddle = new Paddle();
        blocks = new ArrayList<>();
        for (int i = 0; i < 20; i++) {  // สร้างบล็อก 20 ตัว
            blocks.add(new Block(i * 60 + 50, 50));
        }

        // Timer เพื่อทำให้เกมเคลื่อนไหว
        gameTimer = new Timer(10, e -> gameLoop());
        gameTimer.start();

        // KeyListener สำหรับการควบคุม paddle
        this.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                    paddle.moveLeft();
                }
                if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    paddle.moveRight();
                }
            }
        });
        this.setFocusable(true);
    }

    // Game loop สำหรับ Single Player และ Multiplayer
    private void gameLoop() {
        ball.move();
        ball.checkCollision(paddle, blocks);
        checkGameOver();

        scoreLabel.setText("Score: " + playerScore);
        livesLabel.setText("Lives: " + playerLives);
        repaint();  // อัพเดตหน้าจอ
    }

    // ตรวจสอบว่าเกมจบหรือไม่
    private void checkGameOver() {
        if (playerLives <= 0) {
            gameTimer.stop();
            JOptionPane.showMessageDialog(this, "Game Over! Final Score: " + playerScore);
            dispose();
            new HomeUI();  // กลับไปหน้าแรก
        }
    }

    // Pause game
    private void pauseGame() {
        gameTimer.stop();
        int choice = JOptionPane.showOptionDialog(this, "Game Paused", "Pause",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null,
                new Object[] {"Resume", "Home"}, null);
        if (choice == 0) {
            gameTimer.start();
        } else {
            dispose();
            new HomeUI();  // กลับหน้าแรก
        }
    }

    // Toggle microphone on/off
    private void toggleMicrophone() {
        if (isMicrophoneOn) {
            voiceClient.stopRecording();
            micButton.setText("Turn Mic On");
        } else {
            voiceClient.startRecording();
            micButton.setText("Turn Mic Off");
        }
        isMicrophoneOn = !isMicrophoneOn;
    }

    // ตัวเกม Panel สำหรับวาดลูกบอล, paddle, และบล็อก
    private class GamePanel extends JPanel {
        public GamePanel() {
            setPreferredSize(new Dimension(800, 500));
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            // วาดลูกบอล
            ball.draw(g);

            // วาดแท่น paddle
            paddle.draw(g);

            // วาดบล็อก
            for (Block block : blocks) {
                block.draw(g);
            }
        }
    }

    // Class สำหรับลูกบอล
    class Ball {
        private int x = 400, y = 300, dx = 2, dy = -2, diameter = 20;

        public void move() {
            if (x <= 0 || x >= getWidth() - diameter) dx = -dx;
            if (y <= 0) dy = -dy;
            if (y >= getHeight()) {  // ลูกบอลตก
                playerLives--;
                x = 400;
                y = 300;
            }
            x += dx;
            y += dy;
        }

        public void checkCollision(Paddle paddle, ArrayList<Block> blocks) {
            // ตรวจสอบการชนกับ paddle
            if (new Rectangle(x, y, diameter, diameter).intersects(paddle.getBounds())) {
                dy = -dy;
            }

            // ตรวจสอบการชนกับบล็อก
            for (int i = 0; i < blocks.size(); i++) {
                Block block = blocks.get(i);
                if (new Rectangle(x, y, diameter, diameter).intersects(block.getBounds())) {
                    blocks.remove(i);
                    playerScore++;
                    dy = -dy;
                    break;
                }
            }
        }

        public void draw(Graphics g) {
            g.setColor(Color.RED);
            g.fillOval(x, y, diameter, diameter);
        }
    }

    // Class สำหรับแท่น paddle
    class Paddle {
        private int x = 350, y = 450, width = 100, height = 10;

        public void moveLeft() {
            if (x > 0) x -= 10;
        }

        public void moveRight() {
            if (x < getWidth() - width) x += 10;
        }

        public Rectangle getBounds() {
            return new Rectangle(x, y, width, height);
        }

        public void draw(Graphics g) {
            g.setColor(Color.BLUE);
            g.fillRect(x, y, width, height);
        }
    }

    // Class สำหรับบล็อก
    class Block {
        private int x, y, width = 60, height = 20;

        public Block(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public Rectangle getBounds() {
            return new Rectangle(x, y, width, height);
        }

        public void draw(Graphics g) {
            g.setColor(Color.GREEN);
            g.fillRect(x, y, width, height);
        }
    }

    // สำหรับทดสอบ run
    public static void main(String[] args) {
        new GameUI(true);  // Test Single Player
    }
}