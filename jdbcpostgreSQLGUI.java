import java.sql.*;
import javax.swing.JOptionPane;
//import java.sql.DriverManager;
/*
Robert lightfoot
CSCE 315
9-25-2019
 */
public class jdbcpostgreSQLGUI {
  public static void main(String args[]) {
    dbSetupExample my = new dbSetupExample();
    //Building the connection
     Connection conn = null;
     try {
        Class.forName("org.postgresql.Driver");
        conn = DriverManager.getConnection("jdbc:postgresql://csce-315-db.engr.tamu.edu/databallfootbase",
           my.user, my.pswd);
     } catch (Exception e) {
        e.printStackTrace();
        System.err.println(e.getClass().getName()+": "+e.getMessage());
        System.exit(0);
     }//end try catch
     JOptionPane.showMessageDialog(null,"Opened database successfully");
     String name = "";
     try{
     //create a statement object
       Statement stmt = conn.createStatement();
       //create an SQL statement
      
      // String sqlStatement = "SELECT merged_team.\"Name\", COUNT(*) FROM merged_game JOIN merged_team ON merged_game.\"Visit Team Code\"=merged_team.\"Team Code\" GROUP BY merged_team.\"Name\" ORDER BY COUNT(*) DESC LIMIT 10;
       String sqlStatement="SELECT * FROM merged_stadium ORDER BY \"Capacity\" DESC LIMIT 10";

      
       //send statement to DBMS
       ResultSet result = stmt.executeQuery(sqlStatement);

       //OUTPUT
       JOptionPane.showMessageDialog(null,"Lets look at the top 10 Largest Football Stadiums in the World! .");
       //System.out.println("______________________________________");
       while (result.next()) {
        // System.out.println("Stadium Code  Name  City\n");
        while (result.next()) {
            //name += result.getString("Home_Town") +"\n";
            name+= result.getString("Stadium Code") + " " + result.getString("Name") + " "+ result.getString("City") + "\n";
           //name += result.getString("") + " " + result.getString("count") + "\n";
          System.out.println(name);
        }
       }
   } catch (Exception e){
     JOptionPane.showMessageDialog(null,"Error accessing Database.");
   }
   JOptionPane.showMessageDialog(null,name);
    //closing the connection
    try {
      conn.close();
      JOptionPane.showMessageDialog(null,"Connection Closed.");
    } catch(Exception e) {
      JOptionPane.showMessageDialog(null,"Connection NOT Closed.");
    }//end try catch
  }//end main
}//end Class