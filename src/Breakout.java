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
