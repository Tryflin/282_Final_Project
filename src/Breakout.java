/*
    Tristian Jurgens
    10/27/2024
    Creating a game of Breakout
 */


import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import javax.swing.*;
import java.util.ArrayList;
import java.util.Random;
import javax.sound.sampled.*;

public class Breakout extends JFrame 
{
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
    
    //Learned from https://www.youtube.com/watch?v=kc3McnaAU8s
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
    
    //Does the painting, which starts the music
    public Breakout() 
    {
        this.setTitle("Breakout");
        this.setSize(win_wid, win_hei);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        blocks = new ArrayList<>();
        blockColors = new ArrayList<>();
        random = new Random();
        
        this.setVisible(true);
        
        musicPlayer = new MusicPlayer();
        musicPlayer.play("Breakout.wav");

        initializeGame();
    }
    
    // Initialize the game for start
    private void initializeGame() 
    {
        Blocks();
        gameOver = false;
        ballMoving = false;
    }
    private void Blocks()
    {
        for (int i = 0; i < 12; i++) 
        {
            CreateBlock(i * 55 + 5, 50, 50, 20, false);  
            CreateBlock(i * 55 + 5, 75, 50, 20, false);
            CreateBlock(i * 55 + 5, 100, 50, 20, false); 
        }
    }
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
    
    private void initializeGame()
    {
        Blocks();
    }
    
    
    
    
    class Ball
    {
        
    }
    
    class Paddle
    {
        
    }
    public static void main(String[] args) 
    {
        Breakout test = new Breakout();
    }
}

