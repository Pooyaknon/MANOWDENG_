import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Random;
import java.util.List;

public class C2_MultiplayerGameUI extends JFrame {

    private boolean isSinglePlayer;
    private JPanel gamePanel;
    private JLabel scoreLabel, livesLabel, playerNameLabel;
    private int score;
    private int lives = 3;
    private Timer gameTimer;
    private int paddleX;
    private int ballX, ballY, ballDX, ballDY;
    private ArrayList<Block> blocks;
    private boolean isPaused = false;
    private Random random = new Random();
    private JButton pauseButton, homeButton, resumeButton, soundButton, scoreBoardButton;
    private JPanel buttonPanel;
    private ScoreBoardDialog scoreBoardDialog;
    private boolean isSoundOn = true; // For toggling sound
    private List<String> players;
    private ObjectOutputStream outputStream;
    private String playerName;

    public C2_MultiplayerGameUI(boolean isSinglePlayer, List<String> players, ObjectOutputStream out) {
        this.isSinglePlayer = isSinglePlayer;
        this.players = players;
        this.outputStream = out;
        this.playerName = A_HomeUI.playerName;
        setTitle("MANOWDENG - " + (isSinglePlayer ? "Single Player" : "Multiplayer"));
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    
        initializeGame();
        setupUI();
        startGame();
    
        setVisible(true);
    }

    public C2_MultiplayerGameUI(boolean isSinglePlayer) {
        this(isSinglePlayer, new ArrayList<>(), null);
    }

    private void initializeGame() {
        this.score = 0;
        this.lives = 3;
        this.paddleX = 350;
        this.ballX = 400;
        this.ballY = 300;
        this.ballDX = 2 + random.nextInt(2);
        this.ballDY = -2 - random.nextInt(2);
        
        // Initialize blocks
        blocks = new ArrayList<>();
        createBlocks();
    }

    private void createBlocks() {
        int rows = 3;
        int cols = 10;
        int blockWidth = 70;
        int blockHeight = 20;
        int blockPadding = 5;
        
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                blocks.add(new Block(
                    j * (blockWidth + blockPadding) + 30,
                    i * (blockHeight + blockPadding) + 50,
                    blockWidth,
                    blockHeight,
                    new Color(random.nextInt(200) + 55, random.nextInt(200) + 55, random.nextInt(200) + 55)
                ));
            }
        }
    }

    private void setupUI() {
        // Main game panel
        gamePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawGame(g);
            }
        };
        gamePanel.setBackground(Color.BLACK);
        gamePanel.setLayout(new BorderLayout());
        
        // Top panel with game info
        JPanel infoPanel = new JPanel(new GridLayout(1, 3));
        infoPanel.setBackground(new Color(30, 30, 30));
        
        playerNameLabel = new JLabel(A_HomeUI.playerName, SwingConstants.CENTER);
        playerNameLabel.setForeground(Color.WHITE);
        playerNameLabel.setFont(new Font("Arial", Font.BOLD, 20));
        
        scoreLabel = new JLabel("Score: " + score, SwingConstants.CENTER);
        scoreLabel.setForeground(Color.WHITE);
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 20));
        
        livesLabel = new JLabel("Lives: " + lives, SwingConstants.CENTER);
        livesLabel.setForeground(Color.WHITE);
        livesLabel.setFont(new Font("Arial", Font.BOLD, 20));
        
        infoPanel.add(playerNameLabel);
        infoPanel.add(scoreLabel);
        infoPanel.add(livesLabel);
        
        // Bottom panel with buttons
        buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(30, 30, 30));
        
        pauseButton = new JButton("Pause");
        pauseButton.setFocusable(false);
        pauseButton.addActionListener(e -> togglePause());
        
        homeButton = new JButton("Home");
        homeButton.setFocusable(false);
        homeButton.addActionListener(e -> returnToHome());
        
        resumeButton = new JButton("Resume");
        resumeButton.setFocusable(false);
        resumeButton.addActionListener(e -> togglePause());
        resumeButton.setVisible(false);

        soundButton = new JButton("Sound: On");
        soundButton.setFocusable(false);
        soundButton.addActionListener(e -> toggleSound());

        buttonPanel.add(pauseButton);
        buttonPanel.add(homeButton);
        buttonPanel.add(resumeButton);
        buttonPanel.add(soundButton);

        gamePanel.add(infoPanel, BorderLayout.NORTH);
        gamePanel.add(buttonPanel, BorderLayout.SOUTH);
        
        setContentPane(gamePanel);
        setupKeyListeners();
    }

    private void drawGame(Graphics g) {
        if (!isPaused) {
            // Draw paddle
            g.setColor(Color.BLUE);
            g.fillRect(paddleX, getHeight() - 80, 100, 15);
            
            // Draw ball
            g.setColor(Color.RED);
            g.fillOval(ballX, ballY, 20, 20);
            
            // Draw blocks
            for (Block block : blocks) {
                block.draw(g);
            }
            
            // Check collisions
            checkCollisions();
            
            // Update positions
            ballX += ballDX;
            ballY += ballDY;
            
            // Check if all blocks are cleared
            if (blocks.isEmpty()) {
                createBlocks();
                ballDY = Math.abs(ballDY); // Ensure ball moves downward
            }
        } else {
            // Draw pause overlay
            g.setColor(new Color(0, 0, 0, 150));
            g.fillRect(0, 0, getWidth(), getHeight());
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 48));
            String pauseText = "PAUSED";
            int textWidth = g.getFontMetrics().stringWidth(pauseText);
            g.drawString(pauseText, (getWidth() - textWidth)/2, getHeight()/2);
        }
    }

    private void checkCollisions() {
        // Wall collisions
        if (ballX <= 0 || ballX >= getWidth() - 20) ballDX = -ballDX;
        if (ballY <= 0) ballDY = -ballDY;
        
        // Paddle collision
        if (ballY >= getHeight() - 95 && ballY <= getHeight() - 80 &&
            ballX >= paddleX && ballX <= paddleX + 100) {
            ballDY = -ballDY;
            // Add some angle variation based on where ball hits paddle
            int hitPosition = ballX - (paddleX + 50);
            ballDX += hitPosition / 20;
        }
        
        // Block collisions
        for (int i = 0; i < blocks.size(); i++) {
            Block block = blocks.get(i);
            if (new Rectangle(ballX, ballY, 20, 20).intersects(block.getBounds())) {
                blocks.remove(i);
                score += 10;
                scoreLabel.setText("Score: " + score);
                ballDY = -ballDY;
                break;
            }
        }
        
        // Bottom collision (ball fell)
        if (ballY >= getHeight()) {
            loseLife();
        }
    }

    private void loseLife() {
        lives--;
        livesLabel.setText("Lives: " + lives);
        
        if (lives <= 0) {
            gameOver();
        } else {
            // Reset ball and paddle position
            ballX = 400;
            ballY = 300;
            ballDX = 2 + random.nextInt(2);
            ballDY = -2 - random.nextInt(2);
            paddleX = 350;
        }
    }

    private void setupKeyListeners() {
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (!isPaused) {
                    if ((e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A) && paddleX > 0) {
                        paddleX -= 20;
                    } else if ((e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D) && paddleX < getWidth() - 100) {
                        paddleX += 20;
                    }
                }
            }
        });
        setFocusable(true);
    }

    private void startGame() {
        gameTimer = new Timer(10, e -> gamePanel.repaint());
        gameTimer.start();
    }

    private void togglePause() {
        isPaused = !isPaused;
        pauseButton.setVisible(!isPaused);
        resumeButton.setVisible(isPaused);
        if (isPaused) {
            gameTimer.stop();
        } else {
            gameTimer.start();
        }
        requestFocus();
    }

    private void gameOver() {
        gameTimer.stop();
        
        if (!isSinglePlayer) {
            // Multiplayer: ส่งคะแนนไปหา Host
            try {
                java.util.Map<String, Integer> myScore = new java.util.HashMap<>();
                myScore.put(playerName, score);
                outputStream.writeObject(myScore);
                outputStream.flush();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    
        Object[] options = {"Play Again", "Home", "Scoreboard"};
        int choice = JOptionPane.showOptionDialog(this,
                "Game Over! Your score: " + score,
                "Game Over",
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                options,
                options[0]);
    
        if (choice == JOptionPane.YES_OPTION) {
            initializeGame();
            gameTimer.start();
        } else if (choice == JOptionPane.NO_OPTION) {
            returnToHome();
        } else if (choice == JOptionPane.CANCEL_OPTION) {
            showScoreboard();
        } else {
            returnToHome();
        }
    }    
    

    public void returnToHome() {
        if (scoreBoardDialog != null && scoreBoardDialog.isVisible()) {
            scoreBoardDialog.dispose();
        }
        dispose();
        new A_HomeUI();
    }
    
    

    private void toggleSound() {
        isSoundOn = !isSoundOn;
        soundButton.setText("Sound: " + (isSoundOn ? "On" : "Off"));
        // Handle actual sound toggling here
    }

    private void showScoreboard() {
        java.util.Map<String, Integer> scoreData = new java.util.HashMap<>();
        scoreData.put(A_HomeUI.playerName, score);
    
        scoreBoardDialog = new ScoreBoardDialog(this, scoreData);
    }
    
    
    
    
    

    class Block {
        private int x, y, width, height;
        private Color color;
        
        public Block(int x, int y, int width, int height, Color color) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.color = color;
        }
        
        public Rectangle getBounds() {
            return new Rectangle(x, y, width, height);
        }
        
        public void draw(Graphics g) {
            g.setColor(color);
            g.fillRect(x, y, width, height);
            g.setColor(color.darker());
            g.drawRect(x, y, width, height);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            A_HomeUI.playerName = "Player1";
            new C2_MultiplayerGameUI(true);
        });
    }
}
