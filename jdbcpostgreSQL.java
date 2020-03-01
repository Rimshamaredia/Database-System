import java.sql.*;
//import javax.swing.JOptionPane;

/*
Robert lightfoot
CSCE 315
9-25-2019 Original
2/7/2020 Update for AWS
 */
public class jdbcpostgreSQL {
  public static void main(String args[]) {
    //dbSetup hides my username and password
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
     System.out.println("Opened database successfully");
     
     String name = "";
    
     try{
     //create a statement object
       Statement stmt = conn.createStatement();
       //create an SQL statement
      // String sqlStatement = "SELECT * FROM merged_stadium
       //ORDER BY \"Capacity\" DESC
      // LIMIT 10";
     // String sqlStatement = "SELECT \"Name\" FROM merged_conference";
     String sqlStatement = "SELECT merged_team.\"Name\", COUNT(*) FROM merged_game
     JOIN merged_team ON merged_game.\"Visit Team Code\"=merged_team.\"Team Code\"
     GROUP BY merged_team.\"Name\"
     ORDER BY COUNT(*) DESC
     LIMIT 10";
     
       //send statement to DBMS
       ResultSet result = stmt.executeQuery(sqlStatement);

       //OUTPUT
      // System.out.println("Customer Last names from the Database.");
       System.out.println("______________________________________");
       while (result.next()) {
          // name += result.getString("Name") +" ";
          name += result.getString("Name") + " " + result.getString("count") + "\n";
           //count += result.getString("count") 
         System.out.println(name);
       }
   } catch (Exception e){
     System.out.println("Error accessing Database.");
   }
    //closing the connection
    try {
      conn.close();
      System.out.println("Connection Closed.");
    } catch(Exception e) {
      System.out.println("Connection NOT Closed.");
    }//end try catch
  }//end main
}//end Class