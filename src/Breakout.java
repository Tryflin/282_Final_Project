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
    private int win_wid = 670;
    private int win_hei = 500;
    private Ball ball;
    private Paddle paddle;
    private Random random;
    private ArrayList<Rectangle> blocks; 
    private ArrayList<Color> blockColors; 
    
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
    
    
    public Breakout() 
    {
        this.setTitle("Breakout");
        this.setSize(win_wid, win_hei);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        
        
        this.setVisible(true);
    }
    private void Blocks()
    {
        
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
