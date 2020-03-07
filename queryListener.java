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
  JPanel panel;
  public queryListener(JPanel main, String options[]){
    queryListDdn = new JComboBox(options);
    runQueryBtn = new JButton("GO!");

    runQueryBtn.setBackground(new Color(133,154,188));
    runQueryBtn.setOpaque(true);
    runQueryBtn.setForeground(Color.WHITE);
    runQueryBtn.setBorderPainted(false);

    queryListDdn.addActionListener(this);
    runQueryBtn.addActionListener(this);

    main.add(queryListDdn);
    main.add(runQueryBtn);
  }

  @Override
  public void actionPerformed(ActionEvent e){
    if(e.getActionCommand() == "GO!"){
      jdbcpostgreSQLGUI.executeQuery((String)queryListDdn.getSelectedItem());
    }
  }

}
