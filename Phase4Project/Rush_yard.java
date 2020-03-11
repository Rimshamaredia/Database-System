import java.sql.ResultSet;
import java.util.Stack;
import java.util.ArrayList;;
//import sun.jvm.hotspot.runtime.ResultTypeFinder;

public class Rush_yard {
   
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
         ResultTable get_max_rush_against = databaseManager.performQuery("SELECT MAX(final_rush_yards.sum)  FROM final_rush_yards WHERE final_rush_yards.\"Against_Team\" = "+code+";");
         System.out.println(get_max_rush_against.getInteger(0,0));
     
    
       databaseManager.closeConnection();
    }
}
