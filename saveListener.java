/*
Nathan Mandell
CSCE 315
*/

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class saveListener implements ActionListener{
  JButton saveBtn;
  JPanel wind;

  public saveListener(JPanel main){
    wind = main;
    saveBtn = new JButton("SAVE");

    saveBtn.setBackground(new Color(133,154,188));
    saveBtn.setOpaque(true);
    saveBtn.setForeground(Color.WHITE);
    saveBtn.setBorderPainted(false);

    saveBtn.addActionListener(this);

    wind.add(saveBtn, BorderLayout.SOUTH);
  }

  public void actionPerformed(ActionEvent e){
    if(!jdbcpostgreSQLGUI.executeSave()){
      System.out.println("Problem writing to csv");
    }
  }

}
