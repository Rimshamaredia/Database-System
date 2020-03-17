import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Stack;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
//import sun.jvm.hotspot.runtime.ResultTypeFinder;

public class Query1 {
    public static void bfs(Integer winning_code, Integer losing_code, Connection c) throws SQLException {
        Statement stmt = c.createStatement();
        ResultSet is_winning = stmt.executeQuery(
                "SELECT game_results_final.\"winning_team_code\", game_results_final.\"losing_team_code\", game_results_final.\"game_code\" FROM game_results_final WHERE game_results_final.\"winning_team_code\" = "
                        + winning_code + "AND game_results_final.\"losing_team_code\" = " + losing_code + ";");

            // list of all losing teams
            ResultSet stack = stmt.executeQuery(
                    "SELECT game_results_final.\"losing_team_code\", game_results_final.\"game_code\" FROM game_results_final WHERE game_results_final.\"winning_team_code\" ="
                            + winning_code + ";");
            // store it in Queue
            Queue<Queue<Integer>> bfs = new LinkedList<>();
            //for storing the visited nodes
            HashSet<Integer> visited = new HashSet<Integer>();
            Queue<Integer> initQ = new LinkedList<>();
            initQ.add(winning_code);
            bfs.add(initQ);
            visited.add(winning_code);
            while (!bfs.isEmpty()) {
                Queue<Integer> new_node = bfs.poll();
                Integer team_code = ((LinkedList<Integer>) new_node).getLast();
               //get all the losing team names
                ResultSet losing_teams = stmt.executeQuery(
                        "SELECT game_results_final.\"losing_team_code\", game_results_final.\"game_code\" FROM game_results_final WHERE game_results_final.\"winning_team_code\" ="
                                + team_code + ";");
               
                while(losing_teams.next()) {
                    Integer losing_team = losing_teams.getInt(1);
                    Long game_code = losing_teams.getLong("game_code");
                   
                   
                    
                    if (losing_team.equals(losing_code)) {
                        for(int i = 0; i < new_node.size(); i++) {
                          
                        
                            ResultSet team_name = stmt.executeQuery("SELECT merged_team.\"Name\"  FROM merged_team WHERE merged_team.\"Team Code\" = "+ new_node.poll()+"; ");
                           
                            team_name.next();
                           ResultSet team_date = stmt.executeQuery("SELECT 'Game_Dates.\"Date\"' FROM \"Game_Dates\" WHERE 'Game_Dates.\"Game Code\"' ="+ game_code+ ";");
                            //System.out.print(team_name.getString(1) + "in" + team_date.getString("Date")+"won against"+"->");
                            System.out.print(team_name.getString(1)+"->");

                        }
                        ResultSet losing = stmt.executeQuery("SELECT merged_team.\"Name\"  FROM merged_team WHERE merged_team.\"Team Code\" = "+ losing_team+"; ");
                        //ResultSet team_date = stmt.executeQuery("SELECT merged_game.\"Date\" FROM merged_game WHERE merged_game.\"Visit Team Code\"="+ away_team +"AND merged_game.\"Home Team Code\"="+ home_team + ";");
                       
                        losing.next();
                        
                        System.out.print(losing.getString(1) +"\n");
                        return;
                    }
                    if(!visited.contains(losing_team))  {
                        visited.add(losing_team);
                        Queue<Integer> new_stack = new LinkedList<Integer>(new_node);
                        new_stack.add(losing_team);
                        bfs.add(new_stack);
                    }
                }
            }
            System.out.println("no teams found");
            return;
    }

    public static void main(String[] args) throws SQLException {
       Stack<Integer> st = new Stack<Integer>();

       System.out.println("Connecting to the database...");
       Connection conn = DriverManager.getConnection("jdbc:postgresql://csce-315-db.engr.tamu.edu/databallfootbase",
       LoginInfoDB.USERNAME, LoginInfoDB.PASSWORD);
       System.out.println("Finished attempting connecting to database");
       Statement stmt = conn.createStatement();
       String winning = "Texas A&M";
       String losing = "Auburn";
       // get the winning team code from name
       ResultSet winning_code_rt = stmt.executeQuery(
               "SELECT merged_team.\"Team Code\" FROM merged_team WHERE merged_team.\"Name\"= '" + winning + "' ;");
       // get the losing team code from name
       winning_code_rt.next();
       Integer winning_code = winning_code_rt.getInt(1);
       ResultSet losing_code_rt = stmt.executeQuery(
               "SELECT merged_team.\"Team Code\" FROM merged_team WHERE merged_team.\"Name\"= '" + losing + "' ;");
    losing_code_rt.next();
       Integer losing_code = losing_code_rt.getInt(1);
     
     

       bfs(winning_code, losing_code, conn);
        //find if team has ever won against it
       
    
       conn.close();
    }
}
