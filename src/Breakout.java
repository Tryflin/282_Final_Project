/*
    Tristian Jurgens
    10/27/2024
    Creating a game of Breakout
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;

public class Breakout extends JFrame 
{
    private int win_wid = 650;
    private int win_hei = 240;
    
    ArrayList<String> text = new ArrayList<>();
    
    public Breakout() 
    {
        this.setTitle("Test");
        this.setSize(win_wid, win_hei);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        
        this.setVisible(true);
    }
    public static void main(String[] args) 
    {
        System.out.println("Test");
        System.out.println("Test 2");
    }
}
