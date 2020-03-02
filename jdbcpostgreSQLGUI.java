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
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.SwingConstants;
import java.awt.GridLayout;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Image;

//import java.sql.DriverManager;
/*
Robert lightfoot
CSCE 315
9-25-2019
 */
public class jdbcpostgreSQLGUI {
  static Connection conn;
  static JFrame mainWindow = new JFrame("DataBall FootBase");
  static ResultDisplayPanel table = new ResultDisplayPanel();
  
  public static void main(String args[]) {
    //http://sandsduchon.org/duchon/Musings/a/macButton.html
    try {
      javax.swing.UIManager.setLookAndFeel( javax.swing.UIManager.getCrossPlatformLookAndFeelClassName() );
    } catch (Exception e) {
      e.printStackTrace();
    }

    dbSetupExample my = new dbSetupExample();

    mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    mainWindow.setSize(750,350);

    mainWindow.getContentPane().setBackground(new java.awt.Color(44, 68, 101));

    JLabel title = new JLabel("<html><h1 style=\"font-weight: 500;\">DataBall FootBase</h1></html>");
    title.setHorizontalAlignment(JLabel.CENTER);
    title.setForeground(Color.white);
    mainWindow.add(title, BorderLayout.PAGE_START);
    

    //Building the connection
     conn = null;
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
     }
 
    String options[] = {"Largest Football Stadiums", "10 Most Attended Games", "Mystery Query"};
    
    queryListener qL = new queryListener(mainWindow);
    /*int response = JOptionPane.showOptionDialog(null,"Select what you want to see?",null,JOptionPane.YES_NO_OPTION,JOptionPane.PLAIN_MESSAGE,null,options,options[0]);

     String name = "";
     try{
     //create a statement object
       Statement stmt = conn.createStatement();
       //create an SQL statement
      
      if(response == 0){
      String sqlStatement="SELECT * FROM merged_stadium ORDER BY \"Capacity\" DESC LIMIT 10";
       
      
       //send statement to DBMS
       ResultSet result = stmt.executeQuery(sqlStatement);
       ResultDisplayPanel table = new ResultDisplayPanel();
       table.updateData(result);
       mainWindow.add(table);

       //OUTPUT
       //JOptionPane.showMessageDialog(null,"Lets look at the top 10 Largest Football Stadiums in the World! .");
       name += "Stadium Code\t Name\t Capcacity\n\n";

       while (result.next()) {
        while (result.next()) {
            name+= result.getString("Stadium Code") + " " + result.getString("Name") + " "+ result.getString("Capacity") + "\n";
        }
       }
      }
      else{
        String sqlStatement =  "SELECT stats.\"Attendance\", game.\"Date\", merged_stadium.\"Name\" FROM merged_game_statistics AS stats JOIN merged_game AS game ON stats.\"Game Code\"=game.\"Game Code\" JOIN merged_stadium ON game.\"Stadium Code\"=merged_stadium.\"Stadium Code\" ORDER BY stats.\"Attendance\" DESC LIMIT 10";
        // String sqlStatement = "SELECT COUNT(*) FROM merged_game JOIN merged_stadium ON merged_game.\"Stadium Code\"= merged_stadium.\"Stadium Code\" WHERE \"Name\" = "+ stadium_name+"";
        ResultSet result = stmt.executeQuery(sqlStatement);
        while (result.next()) {
        
          while (result.next()) {
              name+= result.getString("Attendance") + " " + result.getString("Date") + " "+ result.getString("Name") + "\n";
          }
        }
      }
  }
   
  catch (Exception e){
    JOptionPane.showMessageDialog(null,"Error accessing Database.");
  }*/

  //http://www.nullpointer.at/2011/08/21/java-code-snippets-howto-resize-an-imageicon/#comment-11870
  /*ImageIcon logo = new ImageIcon("final.png");
  Image image = logo.getImage(); // transform it 
  Image newimg = image.getScaledInstance(250, 200,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way  
  logo = new ImageIcon(newimg);  // transform it back
  JLabel img = new JLabel();
  img.setIcon(logo);
  mainWindow.add(img, BorderLayout.SOUTH);*/
  mainWindow.setVisible(true);

  /*
  //closing the connection
    try {
      conn.close();
      //JOptionPane.showMessageDialog(null,"Connection Closed.");
    } catch(Exception e) {
      JOptionPane.showMessageDialog(null,"Connection NOT Closed.");
    }//end try catch
  */
  }//end main

  public static String executeQuery(String query){
    String name = "";
    try{
      Statement stmt = conn.createStatement();
      ResultSet result = null;
      
      if(query.equals("Largest Football Stadiums")){
        String sqlStatement="SELECT * FROM merged_stadium ORDER BY \"Capacity\" DESC LIMIT 10";
        result = stmt.executeQuery(sqlStatement);
      }
      else if(query.equals("10 Most Attended Games")){
        String sqlStatement =  "SELECT stats.\"Attendance\", game.\"Date\", merged_stadium.\"Name\" FROM merged_game_statistics AS stats JOIN merged_game AS game ON stats.\"Game Code\"=game.\"Game Code\" JOIN merged_stadium ON game.\"Stadium Code\"=merged_stadium.\"Stadium Code\" ORDER BY stats.\"Attendance\" DESC LIMIT 10";
        // String sqlStatement = "SELECT COUNT(*) FROM merged_game JOIN merged_stadium ON merged_game.\"Stadium Code\"= merged_stadium.\"Stadium Code\" WHERE \"Name\" = "+ stadium_name+"";
        result = stmt.executeQuery(sqlStatement);
      }
      table.updateData(result);
      mainWindow.add(table, BorderLayout.SOUTH);
      mainWindow.validate();
    }
   
    catch (Exception e){
      //JOptionPane.showMessageDialog(null,"Error accessing Database.");
      return "Error accessing Database.";
    }
    return name;
  }

}//end Class
