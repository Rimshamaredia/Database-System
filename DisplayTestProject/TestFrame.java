/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
//package testproject;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import javax.swing.*;
import java.sql.*;
import java.util.Scanner;

public class TestFrame extends JFrame {
    public TestFrame(){
        
        ResultDisplayPanel resultPanel = new ResultDisplayPanel();
        
        this.setSize(600, 600);
        this.setTitle("SQL Testing Program - Databall Footbase");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
        
        this.setLayout(new GridLayout(1,1));
        
        TestQuerier tq = new TestQuerier();
        ResultSet rs = tq.performQuery();
        
        resultPanel.updateData(rs);
        
        this.add(resultPanel);
        this.pack();
        
        System.out.println("Type something to try null");
        Scanner s = new Scanner(System.in);
        s.next();
        resultPanel.updateData(null);
        this.validate();
        System.out.println("Tried updating with null");
        
    }
    
}