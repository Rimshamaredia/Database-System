import javax.swing.JComboBox;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.sql.ResultSet;
import java.util.Stack;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import java.awt.Color;

public class PanelQ3 extends JPanel implements Savable {

    AutoCompleteBox teams;
    JButton runBtn;
    JLabel question;
    JLabel response;
    String saveString = "No results to display";
  
    public PanelQ3(){
        System.out.println("Connected to database!");

        question = new JLabel("Given a team, find the team with the most rushing yards vs. the given team:");
        response = new JLabel("Run a query to get a response...");
        response.setForeground(Color.BLUE);

        String quer = "SELECT \"Name\" FROM merged_team";
        ResultTable teamNames = MainApplication.dbManager.performQuery(quer);
        String teamStrings[] = new String[teamNames.getNumRows()];
        for(int i = 0; i < teamStrings.length; i++)
        {
            teamStrings[i] = teamNames.getString(i, 0);
        }
        teams = new AutoCompleteBox(teamStrings);
        runBtn = new JButton("GO!");

        this.addListeners();

        this.add(question);
        this.add(teams);
        this.add(runBtn);

        this.add(response);

    }
    public String q3Function(String team){

        try {
            Stack<Integer> st  = new Stack<Integer>();
            String starting = team;
        
            //get the first team code from name
            ResultTable starting_team_name = MainApplication.dbManager.performQuery("SELECT merged_team.\"Team Code\" FROM merged_team WHERE merged_team.\"Name\"= '" + starting +"' ;");
            //get the losing team code from name
            Integer code = starting_team_name.getInteger(0,0);
            //query to get the max yards
            ResultTable get_max_rush_against = MainApplication.dbManager.performQuery("SELECT MAX(final_rush_yards.sum)  FROM final_rush_yards WHERE final_rush_yards.\"Against_Team\" = "+code+";");
            //get the team code
            ResultTable team_code = MainApplication.dbManager.performQuery("SELECT rush_final.\"Team Code\" FROM rush_final WHERE rush_final.\"Visit Team Code\" = "+ code + "AND rush_final.\"sum\" = "+get_max_rush_against.getInteger(0,0)+";");
            //get the team name
            ResultTable team_name = MainApplication.dbManager.performQuery("SELECT merged_team.\"Name\" from merged_team WHERE merged_team.\"Team Code\" = "+team_code.getInteger(0,0)+";");
            //team with most yards against the input team
            //System.out.println(team_name.getString(0,0));
            return team_name.getString(0,0);

        }catch(Exception e){
            return null;
        }
    }

    private void addListeners() {
        runBtn.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                if(e.getActionCommand() == "GO!"){
                    String questionTeam = (String) teams.getSelectedItem();
                    String responseString;
                    String answerString = "";
                    responseString = q3Function(questionTeam);

                    if (responseString != null){
                        answerString = "The team with the most rushing yards against " + questionTeam + " is " + responseString + ".";
                        response.setForeground(Color.decode("#2e994a"));

                    }else {
                        answerString = "The team with the most rushing yards against " + questionTeam + " could not be determined.";
                        response.setForeground(Color.RED);
                    }
                    saveString = answerString;
                    response.setText(answerString);
                }
            }
        });
    }

    @Override
    public String getSaveString(int option) {
        return saveString;
    }
        
    
}
