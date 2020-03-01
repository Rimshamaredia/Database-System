import java.sql.*;
import javax.swing.JOptionPane;
import javax.swing.JFrame;
import java.awt.Color;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.math.BigInteger;
import javax.swing.JPanel;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.SwingConstants;
import java.awt.GridLayout;
import java.awt.BorderLayout;

//import java.sql.DriverManager;
/*
Robert lightfoot
CSCE 315
9-25-2019
 */
public class jdbcpostgreSQLGUI implements ActionListener {
  public static void main(String args[]) {
    dbSetupExample my = new dbSetupExample();

    JFrame mainWindow = new JFrame("DataBall FootBase");
    mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    mainWindow.setSize(1000,400);

    mainWindow.getContentPane().setBackground(new java.awt.Color(44, 68, 101));

    JTextField title = new JTextField("DataBall FootBase");
    mainWindow.add(title);

    //Building the connection
     Connection conn = null;
     try 
     {
        Class.forName("org.postgresql.Driver");
        conn = DriverManager.getConnection("jdbc:postgresql://csce-315-db.engr.tamu.edu/databallfootbase",
        my.user, my.pswd);
     } 
     catch (Exception e) 
     {
        e.printStackTrace();
        System.err.println(e.getClass().getName()+": "+e.getMessage());
        System.exit(0);
     }//end try catch
     //JOptionPane.showMessageDialog(null,"Opened database successfully");
    
    String[] options = new String[] {"Largest Football Stadiums", "10 Most Attended Games", "Mystery Query"};

    //https://docs.oracle.com/javase/tutorial/uiswing/components/combobox.html
    JComboBox querylist = new JComboBox(options);
    querylist.addActionListener(querylist);

    mainWindow.add(querylist);

    int response = JOptionPane.showOptionDialog(null,"Select what you want to see?",null,JOptionPane.YES_NO_OPTION,JOptionPane.PLAIN_MESSAGE,null,options,options[0]);

     String name = "";
     try{
     //create a statement object
       Statement stmt = conn.createStatement();
       //create an SQL statement
      
      if(response == 0){
      String sqlStatement="SELECT * FROM merged_stadium ORDER BY \"Capacity\" DESC LIMIT 10";
       
      
       //send statement to DBMS
       ResultSet result = stmt.executeQuery(sqlStatement);

       //OUTPUT
       JOptionPane.showMessageDialog(null,"Lets look at the top 10 Largest Football Stadiums in the World! .");
       name += "Stadium Code\t Name\t Capcacity\n\n";
       //System.out.println("______________________________________");
       while (result.next()) {
        // System.out.println("Stadium Code  Name  City\n");
        while (result.next()) {
            //name += result.getString("Home_Town") +"\n";
            name+= result.getString("Stadium Code") + " " + result.getString("Name") + " "+ result.getString("Capacity") + "\n";
           //name += result.getString("") + " " + result.getString("count") + "\n";
          System.out.println(name);
        }
       }
      }
      else{
        String sqlStatement =  "SELECT stats.\"Attendance\", game.\"Date\", merged_stadium.\"Name\" FROM merged_game_statistics AS stats JOIN merged_game AS game ON stats.\"Game Code\"=game.\"Game Code\" JOIN merged_stadium ON game.\"Stadium Code\"=merged_stadium.\"Stadium Code\" ORDER BY stats.\"Attendance\" DESC LIMIT 10";
        // String sqlStatement = "SELECT COUNT(*) FROM merged_game JOIN merged_stadium ON merged_game.\"Stadium Code\"= merged_stadium.\"Stadium Code\" WHERE \"Name\" = "+ stadium_name+"";
        ResultSet result = stmt.executeQuery(sqlStatement);
        while (result.next()) {
          // System.out.println("Stadium Code  Name  City\n");
        
          while (result.next()) {
              //name += result.getString("Home_Town") +"\n";
              name+= result.getString("Attendance") + " " + result.getString("Date") + " "+ result.getString("Name") + "\n";
             //name += result.getString("") + " " + result.getString("count") + "\n";
            System.out.println(name);
          }
        }
      }
  }
   
  catch (Exception e){
    JOptionPane.showMessageDialog(null,"Error accessing Database.");
  }

  JOptionPane.showMessageDialog(null,name);
  mainWindow.setVisible(true);

  //closing the connection
    try {
      conn.close();
      JOptionPane.showMessageDialog(null,"Connection Closed.");
    } catch(Exception e) {
      JOptionPane.showMessageDialog(null,"Connection NOT Closed.");
    }//end try catch
  }//end main

  public void actionPerformed(ActionEvent e) {
    JComboBox cb = (JComboBox)e.getSource();
    String queryChoice = (String)cb.getSelectedItem();
    switch (queryChoice) {
      case "Largest Football Stadiums":
        System.out.println("Howdy");
        break;
      case "Top 10 Most Attended Games":
        System.out.println("Test");
        break;
      case "Mystery Query":
        System.out.println("Hello");
        break;
    }
  }
}//end Class