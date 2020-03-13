import javax.swing.JComboBox;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.sql.ResultSet;
import java.util.Stack;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PanelQ3 extends JPanel {
    public static DatabaseManager databaseManager = new DatabaseManager();

    JComboBox teams;
    JButton runBtn;
  
    public PanelQ3(){
        try{
            System.out.println("Connecting to the database...");
            databaseManager.connect();
        }
        catch(Exception e){
            System.out.println("Connection to server failed, exiting...");
            return;
        }

        System.out.println("Connected to database!");

        JLabel question = new JLabel("Given a team, find the team with the most rushing yards vs. the given team:");

        String quer = "SELECT \"Name\" FROM merged_team";
        ResultTable teamNames = databaseManager.performQuery(quer);
        String teamStrings[] = new String[teamNames.getNumRows()];
        for(int i = 0; i < teamStrings.length; i++)
        {
            teamStrings[i] = teamNames.getString(i, 0);
        }
        teams = new JComboBox(teamStrings);
        runBtn = new JButton("GO!");

        this.addListeners();

        this.add(question);
        this.add(teams);
        this.add(runBtn);

        databaseManager.closeConnection();
    }
    public String q3Function(String team){
        Stack<Integer> st  = new Stack<Integer>();
        String starting = team;
        
        //get the first team code from name
        ResultTable starting_team_name = databaseManager.performQuery("SELECT merged_team.\"Team Code\" FROM merged_team WHERE merged_team.\"Name\"= '" + starting +"' ;");
        //get the losing team code from name
        Integer code = starting_team_name.getInteger(0,0);
        //query to get the max yards
        ResultTable get_max_rush_against = databaseManager.performQuery("SELECT MAX(final_rush_yards.sum)  FROM final_rush_yards WHERE final_rush_yards.\"Against_Team\" = "+code+";");
        //get the team code
        ResultTable team_code = databaseManager.performQuery("SELECT rush_final.\"Team Code\" FROM rush_final WHERE rush_final.\"Visit Team Code\" = "+ code + "AND rush_final.\"sum\" = "+get_max_rush_against.getInteger(0,0)+";");
        //get the team name
        ResultTable team_name = databaseManager.performQuery("SELECT merged_team.\"Name\" from merged_team WHERE merged_team.\"Team Code\" = "+team_code.getInteger(0,0)+";");
        //team with most yards against the input team
        //System.out.println(team_name.getString(0,0));

        return team_name.getString(0,0);
    }

    private void addListeners() {
        runBtn.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                if(e.getActionCommand() == "GO!"){
                    q3Function((String)teams.getSelectedItem());
                }
            }
        });
    }
         
        
    
}
