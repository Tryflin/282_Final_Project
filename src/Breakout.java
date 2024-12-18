/*
    Tristian Jurgens
    10/27/2024
    Program is designed to simuate a game of Breakout
 */

//all the import statements
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import javax.swing.*;
import java.util.ArrayList;
import java.util.Random;
import javax.sound.sampled.*;

//Pretty much does everything
public class Breakout extends JFrame implements KeyListener 
{    
    //So many Fields
    private boolean gameWon = false;
    private boolean gameOver; 
    private boolean ballMoving;
    private int win_wid = 670;
    private int win_hei = 500;
    private int bottom = 450;
    private int score = 0;
    private int timeRemaining = 180;
    private boolean countdownStarted = false; 
    private ArrayList<Rectangle> blocks; 
    private ArrayList<Color> blockColors; 
    private Random random; 
    private Paddle paddle;
    private Ball ball; 
    private JLabel timerLabel; 
    private JLabel scoreLabel;
    private Timer countdownTimer;
    private MusicPlayer musicPlayer;
    private Timer dialogTimer;
    private ArrayList<PowerUp> powerUps;
    
    
    // Plays music
    //Credit: Music learned from https://www.youtube.com/watch?v=kc3McnaAU8s
    //Music is trimmed from: https://www.youtube.com/watch?v=fYx1inFOUVY using VLC
    public class MusicPlayer 
    {
        private Clip clip;

        public void play(String filepath) 
        {
            try 
            {
                AudioInputStream audioInputStream = 
                    AudioSystem.getAudioInputStream(new File(filepath));
                clip = AudioSystem.getClip();
                clip.open(audioInputStream);
                clip.loop(Clip.LOOP_CONTINUOUSLY); 
                clip.start();
            } 
            catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) 
            {
                e.printStackTrace();
            }
        }

        public void stop() 
        {
            if (clip != null && clip.isRunning()) 
            {
                clip.stop();
            }
        }
    }

    // Constructor
    public Breakout() 
    {
        this.setTitle("Breakout");
        this.setSize(win_wid + 15, win_hei);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.addKeyListener(this);

        blocks = new ArrayList<>();
        blockColors = new ArrayList<>();
        random = new Random();

        initializeGame();
        this.setVisible(true);

        musicPlayer = new MusicPlayer();
        musicPlayer.play("Breakout.wav");
        
        powerUps = new ArrayList<>();

        //This is the checker for updates, A.K.A it checks every couple millaseconds
        //Timer learned from: 
        //https://stackoverflow.com/questions/10032003/how-to-make-a-countdown-timer-in-android
        Timer timer = new Timer(20, e -> 
        {
            if (!gameOver) {
                paddle.update();
                if (ballMoving) {
                    ball.update();
                }
                checkCollisions();

                //This is the collison checker for powerups, If it hits the paddles deminsions
                //It will appy the powerup, if it hits the bottom of the window, it despawns
                for (int i = 0; i < powerUps.size(); i++) 
                {
                    PowerUp p = powerUps.get(i);
                    p.update();

                    if (p.isCaught(paddle)) 
                    {
                        applyPowerUp(p);
                        powerUps.remove(i);
                        i--;
                    } 
                    else if (p.y > win_hei) 
                    { 
                        powerUps.remove(i);
                        i--;
                    }
                }

                repaint();
            }
        });
        timer.start();
    }

    //Powerups where learned from just a google search, 
    //and some help from https://github.com/sibnerian/Breakout/blob/master/src/PowerUp.java
    private void applyPowerUp(PowerUp powerUp) 
    {
        //switch instead of if statement in case i add more
        switch (powerUp.getType()) 
        {
            // Increase paddle width
            case "ExpandPaddle":
                paddle.width += 30; 
                break;
                
                
            // Add extra time, may change for lives if added
            case "ExtraLife":
                timeRemaining += 30; 
                break;
                
            // Double the paddle speed
            case "FastPaddle":
            paddle.setSpeedMultiplier(2); 
            break;
        }
        Timer timer = new Timer(20000, e ->
        {
            switch (powerUp.getType()) 
            {
                case "ExpandPaddle":
                    paddle.width -= 30; 
                    break;
                case "FastPaddle":
                    paddle.setSpeedMultiplier(1); 
                    break;
            }
        });
        timer.setRepeats(false);
        timer.start();
    }

    // Initialize game elements
    private void initializeGame() 
    {
        Words();
        Blocks();
        paddle = new Paddle(300, 400, 85, 15, Color.GRAY);
        ball = new Ball(335, 380, 15, Color.WHITE);
        gameOver = false;
        ballMoving = false;
    }

    // Set up the display words and labels
    private void Words() 
    {
        int font_Size = 16;
        Font bigFont = new Font("Arial", Font.BOLD, font_Size);
        JLabel lab = new JLabel("Breakout");
        lab.setFont(bigFont);
        
        JPanel northPan = new JPanel();         
        northPan.add(lab);
        timerLabel = new JLabel("Time: 3:00"); 
        northPan.add(timerLabel); 
        scoreLabel = new JLabel("Score: 0"); 
        northPan.add(scoreLabel);
        this.add(northPan, BorderLayout.NORTH);
    }

    // Start the countdown timer that appears on the top
    private void startCountdown() 
    {
        countdownTimer = new Timer(1000, e -> 
        {
            if (!gameOver) 
            {
                if (timeRemaining > 0) 
                {
                    timeRemaining--;
                    int minutes = timeRemaining / 60;
                    int seconds = timeRemaining % 60;
                    timerLabel.setText(String.format("Time: %d:%02d", minutes, seconds));
                } 
                else 
                {
                    gameOver = true;
                    timerLabel.setText("Time's Up!");
                    countdownTimer.stop();
                    repaint();
                }
            } 
            else 
            {
                countdownTimer.stop(); 
            }
        });
        countdownTimer.start();
    }

    // Create blocks for the game
    private void Blocks() 
    {
        for (int i = 0; i < 12; i++) 
        {
            CreateBlock(i * 55 + 5, 50, 50, 20, false);  
            CreateBlock(i * 55 + 5, 75, 50, 20, false);
            CreateBlock(i * 55 + 5, 100, 50, 20, false); 
        }

        BlockPanel blockPanel = new BlockPanel();
        this.add(blockPanel, BorderLayout.CENTER);
    }

    // Create an individual block
    private void CreateBlock(int x, int y, int width, int height, boolean randomColor) 
    {
        blocks.add(new Rectangle(x, y, width, height));
        if (randomColor) 
        {
            blockColors.add(generateRandomColor());
        } 
        else 
        {
            blockColors.add(Color.BLUE); 
        }
    }

    // Generate a random color
    private Color generateRandomColor() 
    {
        int r = random.nextInt(256);
        int g = random.nextInt(256);
        int b = random.nextInt(256);
        return new Color(r, g, b); 
    }
    
    //This is what defines the powerup, or what it looks like. 
    class PowerUp 
    {
        private int x, y, size;
        private String type; 
        private Color color;
        private int dy = 2; 
        private long startTime; 
        private final long duration = 20000; 

        public PowerUp(int x, int y, int size, String type, Color color) 
        {
            this.x = x;
            this.y = y;
            this.size = size;
            this.type = type;
            this.color = color;
            this.startTime = System.currentTimeMillis();
        }

        public void draw(Graphics g) 
        {
            g.setColor(color);
            g.fillOval(x, y, size, size);
        }

        public void update()
        {
            y += dy; 
            if (System.currentTimeMillis() - startTime > duration) 
            {             
                powerUps.remove(this); 
            }
        }

        public boolean isCaught(Paddle paddle) 
        {
            return (x + size > paddle.x && x < paddle.x + paddle.width && 
                    y + size > paddle.y && y < paddle.y + paddle.height);
        }

        public String getType() 
        {
            return type;
        }
    }

    // KeyListener methods 
    //KeyListener learned from: 
    //    https://docs.oracle.com/javase/tutorial/uiswing/events/keylistener.html
    @Override
    public void keyTyped(KeyEvent e) 
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void keyPressed(KeyEvent e) 
    {
        if (!countdownStarted) 
        {
            startCountdown(); 
            countdownStarted = true; 
        }

        if (e.getKeyCode() == KeyEvent.VK_LEFT) 
        {
            paddle.moveLeft();
            if (!ballMoving) 
            {
                setRandomBallDirection(); 
            }
            ballMoving = true;
        }
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) 
        {
            paddle.moveRight();
            if (!ballMoving) 
            {
                setRandomBallDirection(); 
            }
            ballMoving = true;
        }
        if (e.getKeyCode() == KeyEvent.VK_UP) 
        {
            ballMoving = true;
            if (!ballMoving) 
            {
                setRandomBallDirection(); 
            }
            ballMoving = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) 
    {
        paddle.stop(); 
    }

    //updated for fix, added a reset for timer/stopping the timerr from being triggered twice
    // Show dialog for game over or win
    private void showTryAgainDialog() 
    {
        if (dialogTimer != null && dialogTimer.isRunning()) 
        {
            dialogTimer.stop(); 
        }
        dialogTimer = new Timer(500, event -> 
        {
            int response = JOptionPane.showOptionDialog(this, gameWon ? 
                "You Win! Try Again?" : "Game Over! Try Again?",
                "Game Over", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,
                null, new String[]{"Yes", "No"}, "Yes");

            if (response == JOptionPane.YES_OPTION) 
            {
                restartGame();
            } 
            else 
            {
                System.exit(0); 
            }
        });
        dialogTimer.setRepeats(false);
        dialogTimer.start();
    }

    // Restart the game
    private void restartGame() 
    {
        gameOver = false;
        gameWon = false;
        timeRemaining = 180;
        score = 0;
        scoreLabel.setText("Score: 0");
        timerLabel.setText("Time: 3:00");
        countdownStarted = false;

        blocks.clear();
        blockColors.clear();
        Blocks(); 

        // Reset the ball position and state
        ball.x = 335;
        ball.y = 380;
        setRandomBallDirection();
        ballMoving = false;

        // Reset paddle position and width
        paddle.x = (win_wid - 85) / 2; 
        paddle.width = 85;            
        paddle.stop(); 
        paddle.resetSpeed();

        // Clear any active power-ups
        powerUps.clear();

        // Restart the music
        musicPlayer.play("Breakout.wav");

        repaint();
    }

    // paints, or adds to the screen
    //Most of the JFrame and how to set it up was learned from PSET5
    private class BlockPanel extends JPanel 
    {
        @Override
        protected void paintComponent(Graphics g) 
        {
            super.paintComponent(g);
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, getWidth(), getHeight());
            for (int i = 0; i < blocks.size(); i++) 
            {
                Rectangle block = blocks.get(i);
                g.setColor(blockColors.get(i));
                g.fillRect(block.x, block.y, block.width, block.height);
            }
            paddle.draw(g);
            ball.draw(g);

            if (powerUps == null) 
            {
                powerUps = new ArrayList<>();
            }
            for (PowerUp p : powerUps) 
            {
                p.draw(g);
            }

            if (gameOver) 
            {
                g.setColor(Color.WHITE);
                g.setFont(new Font("Arial", Font.BOLD, 20));
                if (gameWon) 
                {
                    g.drawString("You Win!", win_wid / 2 - 70, win_hei / 2);
                } 
                else 
                {
                    g.drawString("Game Over", win_wid / 2 - 70, win_hei / 2);
                }
                g.drawString("Score: " + score, win_wid / 2 - 70, win_hei / 2 + 30);
                
                int minutes = timeRemaining / 60;
                int seconds = timeRemaining % 60;
                g.drawString(String.format("Time: %d:%02d", minutes, seconds),
                    win_wid / 2 - 70, win_hei / 2 + 60);

                //fix for try again
                Timer dialogTimer = new Timer(500, event -> showTryAgainDialog());
                dialogTimer.setRepeats(false);
                dialogTimer.start();
            }
        }
    }

    // takes the given deminsions, as well as its speed and color
    class Ball 
    {
        private int x, y, diameter;
        private Color color;
        private int dx, dy;

        public Ball(int x, int y, int diameter, Color color) 
        {
            this.x = x;
            this.y = y;
            this.diameter = diameter;
            this.color = color;
            this.dx = 3; 
            this.dy = -3;
        }

        public void draw(Graphics g) 
        {
            g.setColor(color);
            g.fillOval(x, y, diameter, diameter);
        }

        public void update() 
        {
            x += dx;
            y += dy;

            if (x < 0 || x + diameter > win_wid) 
            {
                dx = -dx;
            }
            if (y < 0) 
            {
                dy = -dy;
            }
            if (y + diameter > bottom) 
            {
                gameOver = true; 
                musicPlayer.stop();
                countdownTimer.stop(); 
            }
        }

        public void setDx(int dx) 
        {
            this.dx = dx; 
        }
    }

    // Paddle with movement options
    class Paddle 
    {
        private int x, y, width, height;
        private Color color;
        private int dx;
        private double speedMultiplier = 1.0; 

        public Paddle(int x, int y, int width, int height, Color color) 
        {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.color = color;
            this.dx = 0;
        }

        // Methods to move the paddle
        public void moveLeft() { dx = -10; }
        public void moveRight() { dx = 10; }
        public void stop() { dx = 0; }

        public void setSpeedMultiplier(double multiplier) 
        {
            this.speedMultiplier = multiplier;
        }
        
        public void update() 
        {
            x += dx * speedMultiplier;  
            if (x < 0) x = 0;
            if (x + width > win_wid) x = win_wid - width; 
        }

        public void resetSpeed() 
        {
            this.speedMultiplier = 1.0; 
        }
        
        public void draw(Graphics g) 
        {
            g.setColor(color);
            g.fillRect(x, y, width, height);
        }
    }

    // Check for collisions
    private void checkCollisions() 
    {
        if (ball.y + ball.diameter >= paddle.y && 
            ball.y + ball.diameter <= paddle.y + paddle.height &&
            ball.x + ball.diameter >= paddle.x && 
            ball.x <= paddle.x + paddle.width) 
        {
            ball.dy = -ball.dy; 
            ball.y = paddle.y - ball.diameter;
            ball.setDx(paddle.dx);
        }

        for (int i = 0; i < blocks.size(); i++) 
        {
            Rectangle block = blocks.get(i);
            if (block.intersects(new Rectangle(ball.x, ball.y, 
                ball.diameter, ball.diameter))) 
            {
                if (ball.x + ball.diameter <= block.x || ball.x >= 
                    block.x + block.width) 
                {
                    ball.dx = -ball.dx; 
                } 
                else
                {
                    ball.dy = -ball.dy; 
                }
                blocks.remove(i);
                blockColors.remove(i);
                
                if (random.nextInt(100) < 20) 
                {
                    String[] types = {"ExpandPaddle", "ExtraLife", "FastPaddle"};
                    String type = types[random.nextInt(types.length)];
                    Color color = type.equals("FastPaddle") ? Color.RED :
                                type.equals("ExpandPaddle") ? Color.GREEN :
                                type.equals("ExtraLife") ? Color.YELLOW : 
                            Color.RED;
                    powerUps.add(new PowerUp(block.x, block.y, 20, type, color));
                }
                
                score += 10; 
                scoreLabel.setText("Score: " + score);
                break;
            }
        }

        if (blocks.isEmpty()) 
        {
            gameOver = true;
            gameWon = true;
            musicPlayer.stop();
            countdownTimer.stop();
        }
    }

    // Set a random direction for the ball
    private void setRandomBallDirection() 
    {
        ball.dx = 4; 
        if (random.nextBoolean()) 
        { 
            ball.dx = -ball.dx; 
        }
        ball.dy =  6; 
    }

    // Main method to start the game
    public static void main(String[] args) 
    {
        Breakout Final = new Breakout();
    }
}
