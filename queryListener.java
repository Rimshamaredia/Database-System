/*
Nathan Mandell
CSCE 315
*/

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class queryListener implements ActionListener{
  JComboBox queryListDdn;
  JButton runQueryBtn;
  JButton saveBtn;
  String options[] = {"Largest Football Stadiums", "10 Worst Kickoffs", "10 Most Attended Games"};
  JFrame mainWindow;
  public queryListener(JFrame main){
    mainWindow = main;
    queryListDdn = new JComboBox(options);
    runQueryBtn = new JButton("GO!");
    saveBtn = new JButton("SAVE");

    runQueryBtn.setBackground(new Color(133,154,188));
    runQueryBtn.setOpaque(true);
    runQueryBtn.setForeground(Color.WHITE);
    runQueryBtn.setBorderPainted(false);

    saveBtn.setBackground(new Color(133,154,188));
    saveBtn.setOpaque(true);
    saveBtn.setForeground(Color.WHITE);
    saveBtn.setBorderPainted(false);

    queryListDdn.addActionListener(this);
    runQueryBtn.addActionListener(this);
    saveBtn.addActionListener(this);

    mainWindow.add(queryListDdn, BorderLayout.CENTER);
    mainWindow.add(runQueryBtn, BorderLayout.EAST);
    mainWindow.add(saveBtn, BorderLayout.SOUTH);
  }

  public void actionPerformed(ActionEvent e){
    if(e.getActionCommand() == "GO!"){
      jdbcpostgreSQLGUI.executeQuery((String)queryListDdn.getSelectedItem());
    }
  }

}
