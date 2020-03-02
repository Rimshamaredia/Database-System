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
  String options[] = {"Largest Football Stadiums", "10 Most Attended Games", "Mystery Query"};
  JFrame mainWindow;
  public queryListener(JFrame main){
    mainWindow = main;
    queryListDdn = new JComboBox(options);
    runQueryBtn = new JButton("GO!");

    runQueryBtn.setBackground(new Color(133,154,188));
    runQueryBtn.setOpaque(true);
    runQueryBtn.setForeground(Color.WHITE);
    queryListDdn.addActionListener(this);
    runQueryBtn.addActionListener(this);

    mainWindow.add(queryListDdn, BorderLayout.CENTER);
    mainWindow.add(runQueryBtn, BorderLayout.EAST);
  }

  public void actionPerformed(ActionEvent e){
    if(e.getActionCommand() == "GO!"){

      jdbcpostgreSQLGUI.executeQuery((String)queryListDdn.getSelectedItem());
    }
    /*
    System.out.println(e.getActionCommand());
    System.out.println(e.getModifiers());
    System.out.println(e.paramString());
    */
  }

}
