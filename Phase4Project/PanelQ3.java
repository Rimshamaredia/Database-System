import javax.swing.JPanel;
import java.sql.ResultSet;
import java.util.Stack;
import java.util.ArrayList;
public class PanelQ3 extends JPanel {
  
    public PanelQ3(){
        
    }
        public static void main(String[] args){
            Stack<Integer> st  = new Stack<Integer>();
         
             System.out.println("Connecting to the database...");
             DatabaseManager databaseManager = new DatabaseManager();
             boolean success = databaseManager.connect();
             System.out.println("Finished attempting connecting to database");
             if (!success){
                 System.out.println("Connection to server failed, exiting...");
                 return;
             }
            
             String starting = "LSU";
            
             //get the first team code from name
             ResultTable starting_team_name= databaseManager.performQuery("SELECT merged_team.\"Team Code\" FROM merged_team WHERE merged_team.\"Name\"= '" + starting +"' ;");
             //get the losing team code from name
            // ResultTable losing_code_rt= databaseManager.performQuery("SELECT merged_team.\"Team Code\" FROM merged_team WHERE merged_team.\"Name\"= '" +losing + "' ;");
             Integer code = starting_team_name.getInteger(0,0);
             System.out.println(code);
             //query to get the max yards
              ResultTable get_max_rush_against = databaseManager.performQuery("SELECT MAX(final_rush_yards.sum)  FROM final_rush_yards WHERE final_rush_yards.\"Against_Team\" = "+code+";");
              //get the team code
              ResultTable team_code = databaseManager.performQuery("SELECT rush_final.\"Team Code\" FROM rush_final WHERE rush_final.\"Visit Team Code\" = "+ code + "AND rush_final.\"sum\" = "+get_max_rush_against.getInteger(0,0)+";");
              //get the team name
              ResultTable team_name = databaseManager.performQuery("SELECT merged_team.\"Name\" from merged_team WHERE merged_team.\"Team Code\" = "+team_code.getInteger(0,0)+";");
              //team with most yards against the input team
              System.out.println(team_name.getString(0,0));
             
     
          
         
            databaseManager.closeConnection();
            }
         
        
    
}
