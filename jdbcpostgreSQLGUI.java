import java.sql.*;
import java.io.*;
import javax.swing.JFrame;
import java.awt.Color;
import javax.swing.JLabel;
import java.awt.BorderLayout;

/*
Robert lightfoot
CSCE 315
9-25-2019
 */
public class jdbcpostgreSQLGUI {
  static Connection conn;
  static JFrame mainWindow = new JFrame("DataBall FootBase");
  static ResultDisplayPanel table = new ResultDisplayPanel();
  static String csvContents;
  static ResultSet result;
  static String lastQuery;

  public static void main(String args[]) {
    dbSetupExample my = new dbSetupExample();

    mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    mainWindow.setResizable(false);
    mainWindow.setSize(750,550);

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
    
    queryListener qL = new queryListener(mainWindow);
    saveListener sL = new saveListener(mainWindow);

  //http://www.nullpointer.at/2011/08/21/java-code-snippets-howto-resize-an-imageicon/#comment-11870
  /*ImageIcon logo = new ImageIcon("final.png");
  Image image = logo.getImage(); // transform it 
  Image newimg = image.getScaledInstance(250, 200,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way  
  logo = new ImageIcon(newimg);  // transform it back
  JLabel img = new JLabel();
  img.setIcon(logo);
  mainWindow.add(img, BorderLayout.SOUTH);*/
  mainWindow.setVisible(true);

  }//end main

  public static void executeQuery(String query){
    try{
      Statement stmt = conn.createStatement();
      String sqlStatement = "";

      if(query.equals("Largest Football Stadiums")){
        sqlStatement="SELECT * FROM merged_stadium ORDER BY \"Capacity\" DESC LIMIT 10";
      }
      else if(query.equals("10 Worst Kickoffs")){
        sqlStatement =  "SELECT DISTINCT merged_player.\"First Name\", merged_player.\"Last Name\", merged_kickoff_return.\"Yards\" FROM merged_player JOIN merged_kickoff_return ON merged_player.\"Player Code\"=merged_kickoff_return.\"Player Code\" ORDER BY merged_kickoff_return.\"Yards\" LIMIT 10;";
      }
      else if(query.equals("10 Most Attended Games"))
      {
        //I know this line is long, and I am sorry, but it has to be this way.
        sqlStatement = "SELECT stats.\"Attendance\", game.\"Date\", merged_stadium.\"Name\", visitteam.\"Name\" as \"Visit Team Name\", hometeam.\"Name\" as \"Home Team Name\" FROM merged_game_statistics AS stats JOIN merged_game AS game ON stats.\"Game Code\"=game.\"Game Code\" JOIN merged_stadium ON game.\"Stadium Code\"=merged_stadium.\"Stadium Code\" JOIN merged_team as visitteam ON game.\"Visit Team Code\"=visitteam.\"Team Code\" JOIN merged_team as hometeam ON game.\"Home Team Code\"=hometeam.\"Team Code\" ORDER BY stats.\"Attendance\" DESC LIMIT 10;";
      }
      result = stmt.executeQuery(sqlStatement);
      csvContents = table.updateData(result);
      mainWindow.add(table, BorderLayout.SOUTH);
      mainWindow.validate();
      lastQuery = query;
    }
   
    catch (Exception e){
      System.out.println("Error Accessing Database");
      return;
    }
  }
  
  public static boolean executeSave(){
    try{
      FileWriter out = new FileWriter(lastQuery.replaceAll(" ", "_") + ".csv");
      out.write(csvContents);
      out.close();
    }
    catch(Exception e){
      System.out.println(e);
      return false;
    }
    return true;
  }
}//end Class




