
import java.sql.ResultSet;
import java.util.Stack;
import java.util.ArrayList;;
//import sun.jvm.hotspot.runtime.ResultTypeFinder;

public class TestingProgram {
   
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
       
        String winning = "LSU";
        String losing = "TAMU";
        //get the winning team code from name
        ResultTable winning_code_rt= databaseManager.performQuery("SELECT merged_team.\"Team Code\" FROM merged_team WHERE merged_team.\"Name\"= '" + winning +"' ;");
        //get the losing team code from name
        ResultTable losing_code_rt= databaseManager.performQuery("SELECT merged_team.\"Team Code\" FROM merged_team WHERE merged_team.\"Name\"= '" +losing + "' ;");
        Integer winning_code = winning_code_rt.getInteger(0,0);
        Integer losing_code = losing_code_rt.getInteger(0,0);
        //find if team has ever won against it
        ResultTable is_winning = databaseManager.performQuery("SELECT game_results_final.\"winning_team_code\", game_results_final.\"losing_team_code\", game_results_final.\"game_code\" FROM game_results_final WHERE game_results_final.\"winning_team_code\" = " + winning_code + "AND game_results_final.\"losing_team_code\" = "+losing_code+";");
      
       if(is_winning.getNumRows()==0){
           //list of all losing teams
        ResultTable stack = databaseManager.performQuery("SELECT game_results_final.\"losing_team_code\", game_results_final.\"game_code\" FROM game_results_final WHERE game_results_final.\"winning_team_code\" =" + winning_code + ";");
        Integer i = 0;
        //store it in stack
       while(i<stack.getNumRows()){
           Integer losing_team = stack.getInteger(0,i);
           st.push(losing_team);
           i++;
          
       }
       while(!st.empty()){
           Integer losing_team = st.pop();
           //from one losing team to destination team
           ResultTable temp = databaseManager.performQuery("SELECT game_results_final.\"winning_team_code\", game_results_final.\"losing_team_code\", game_results_final.\"game_code\" FROM game_results_final WHERE game_results_final.\"winning_team_code\" = " + losing_team + "AND game_results_final.\"losing_team_code\" = "+losing_code+";");
          // if(temp.getNumRows()==1){
              if(temp.getInteger(0,0)==losing_code){
              System.out.println("Winning");
            //System.out.println(temp + " is won against " + losing);
            databaseManager.closeConnection();
           }
           
        }
      
       
       }else{
        System.out.println(winning + " is won against " + losing);
       }
    
       databaseManager.closeConnection();
    }
}
