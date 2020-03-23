import javax.swing.JPanel;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Stack;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import javax.swing.JButton;
import javax.swing.JLabel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Color;

public class PanelQ1 extends JPanel implements Savable {

    AutoCompleteBox teams1;
    AutoCompleteBox teams2;
    JButton runBtn;
    JLabel question;
    JLabel response;
    String saveString = "No results to display";

    public PanelQ1(){
        //System.out.println("Connected to database!");

        question = new JLabel("Given 2 teams, create a victory chain.");
        response = new JLabel("Run a query to get a response...");
        response.setForeground(Color.BLUE);

        String quer = "SELECT \"Name\" FROM merged_team";
        ResultTable teamNames = MainApplication.dbManager.performQuery(quer);
        String teamStrings[] = new String[teamNames.getNumRows()];
        for(int i = 0; i < teamStrings.length; i++)
        {
            teamStrings[i] = teamNames.getString(i, 0);
        }
        teams1 = new AutoCompleteBox(teamStrings);
        teams2 = new AutoCompleteBox(teamStrings);
        runBtn = new JButton("GO!");

        this.addListeners();

        this.add(question);
        this.add(teams1);
        this.add(teams2);
        this.add(runBtn);

        this.add(response);
        
    }

    private void addListeners() {
        runBtn.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                if(e.getActionCommand() == "GO!"){
                    String questionTeam1 = (String) teams1.getSelectedItem();
                    String questionTeam2 = (String) teams2.getSelectedItem();
                    String responseString;
                    String answerString = "";
                    responseString = q1Function(questionTeam1, questionTeam2);

                    if (responseString != "No teams found."){
                        answerString = responseString;
                        response.setForeground(Color.decode("#2e994a"));

                    }else {
                        answerString = "A victory chain between " + questionTeam1 + " and " + questionTeam2 + " could not be determined.";
                        response.setForeground(Color.RED);
                    }
                    saveString = answerString;
                    response.setText(answerString);
                }
            }
        });
    }

    public String bfs(Integer winning_code, Integer losing_code, Connection c) throws SQLException {
        Statement stmt = c.createStatement();
        String ret = "";
        
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
                
                   // Integer game_code = (Integer)game_code;
                   
                    
                    if (losing_team.equals(losing_code)) {
                        for(int i = 0; i < new_node.size(); i++) {
                          
                        
                            ResultSet team_name = stmt.executeQuery("SELECT merged_team.\"Name\"  FROM merged_team WHERE merged_team.\"Team Code\" = "+ new_node.poll()+"; ");
                            team_name.next();
                            ret += team_name.getString(1) + " in ";
                            ResultSet team_date = stmt.executeQuery("SELECT merged_game.\"Game Code\" ,merged_game.\"Date\" FROM merged_game JOIN game_results_final ON game_results_final.game_code= merged_game.\"Game Code\" AND game_results_final.game_code = "+ game_code+ ";");
                            team_date.next();
                            ret += team_date.getString("Date") + " won against->";
                           

                        }
                        ResultSet losing = stmt.executeQuery("SELECT merged_team.\"Name\"  FROM merged_team WHERE merged_team.\"Team Code\" = "+ losing_team+"; ");
                        //ResultSet team_date = stmt.executeQuery("SELECT merged_game.\"Date\" FROM merged_game WHERE merged_game.\"Visit Team Code\"="+ away_team +"AND merged_game.\"Home Team Code\"="+ home_team + ";");
                       
                        losing.next();
                        
                        ret += losing.getString(1);
                        return ret;
                    }
                    if(!visited.contains(losing_team))  {
                        visited.add(losing_team);
                        Queue<Integer> new_stack = new LinkedList<Integer>(new_node);
                        new_stack.add(losing_team);
                        bfs.add(new_stack);
                    }
                }
            }
            return "No teams found.";
    }

    public String q1Function(String team1, String team2){
        String ret = "";
        try {
         Stack<Integer> st = new Stack<Integer>();
  
         System.out.println("Connecting to the database...");
         Class.forName("org.postgresql.Driver");
         Connection conn = DriverManager.getConnection("jdbc:postgresql://csce-315-db.engr.tamu.edu/databallfootbase",
         LoginInfoDB.USERNAME, LoginInfoDB.PASSWORD);
         System.out.println("Finished attempting connecting to database");
         Statement stmt = conn.createStatement();
         String winning = team1;
         String losing = team2;
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
       
       
  
        ret = bfs(winning_code, losing_code, conn);
        //find if team has ever won against it
         
      conn.close();
        
       }catch (Exception e){
        e.printStackTrace();
        ret = "Something went wrong.";
       }
       return ret;
  
      }

    @Override
    public String getSaveString(int option) {
        return saveString;
    }

}
