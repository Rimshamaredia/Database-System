import javax.swing.JPanel;
import javax.swing.JTextArea;

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
import java.math.*;

public class PanelQ5 extends JPanel implements Savable {

    AutoCompleteBox teams1;
    AutoCompleteBox teams2;
    AutoCompleteBox stadiums;
    JButton runBtn;
    JTextArea question;
    JLabel response;
    String saveString = "No results to display";

    public PanelQ5(){
        System.out.println("Connected to database!");

        question = new JTextArea("Given 2 teams and a stadium, return how desirable it would be to advertise during this game on a scale\nfrom 0 to 100.");
        question.setEditable(false);
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
        quer = "SELECT \"Name\" FROM merged_stadium";
        ResultTable stadiumNames = MainApplication.dbManager.performQuery(quer);
        String stadiumStrings[] = new String[stadiumNames.getNumRows()];
        for(int i = 0; i < stadiumStrings.length; i++)
        {
            stadiumStrings[i] = stadiumNames.getString(i, 0);
        }
        stadiums = new AutoCompleteBox(stadiumStrings);
        runBtn = new JButton("GO!");

        this.addListeners();

        this.add(question);
        this.add(teams1);
        this.add(teams2);
	this.add(stadiums);
        this.add(runBtn);

        this.add(response);
        
    }

    private void addListeners() {
        runBtn.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                if(e.getActionCommand() == "GO!"){
                    String questionTeam1 = (String) teams1.getSelectedItem();
                    String questionTeam2 = (String) teams2.getSelectedItem();
                    String questionStadium = (String) stadiums.getSelectedItem();
                    String responseString;
                    String answerString = "";
                    responseString = q5Function(questionTeam1, questionTeam2, questionStadium);

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

    public String q5Function(String team1, String team2, String stadium){
        String ret = "";
        try {
	 ResultTable team1_code_table = MainApplication.dbManager.performQuery(
		  "SELECT merged_team.\"Team Code\",\"Subdivision\" FROM merged_team JOIN merged_conference ON merged_team.\"Conference Code\"=merged_conference.\"Conference Code\" WHERE merged_team.\"Name\"='" + team1 + "';");
         Integer team1Code = team1_code_table.getInteger(0,0);
	 boolean team1FBS = team1_code_table.getString(0,1).equals("FBS");
	 ResultTable team2_code_table = MainApplication.dbManager.performQuery(
		  "SELECT merged_team.\"Team Code\",\"Subdivision\" FROM merged_team JOIN merged_conference ON merged_team.\"Conference Code\"=merged_conference.\"Conference Code\" WHERE merged_team.\"Name\"='" + team2 + "';");
         Integer team2Code = team2_code_table.getInteger(0,0);
	 boolean team2FBS = team2_code_table.getString(0,1).equals("FBS");
	 ResultTable team1Elo_table = MainApplication.dbManager.performQuery(
		"SELECT \"Team Elo\" FROM team_elo_ratings WHERE \"Team Code\"=" + team1Code + ";");
	 Integer team1Elo = team1Elo_table.getInteger(0,0);
	 ResultTable team2Elo_table = MainApplication.dbManager.performQuery(
		"SELECT \"Team Elo\" FROM team_elo_ratings WHERE \"Team Code\"=" + team2Code + ";");
	 Integer team2Elo = team2Elo_table.getInteger(0,0);
	 ResultTable stadiumCapacity_table = MainApplication.dbManager.performQuery(
		"SELECT \"Capacity\" FROM merged_stadium WHERE \"Name\"='" + stadium +"';");
	 Integer stadiumCapacity = stadiumCapacity_table.getInteger(0,0);
	
	 //Value between 0 and 1
	 double evenness = 2 * Math.min(GenerateElo.eloExpectation(team1Elo, team2Elo), GenerateElo.eloExpectation(team2Elo, team1Elo) );
	 //Value between 0 and 1
	 //Ratings *almost* never exceed 2000
	 double quality = (team1Elo + team2Elo) / 4000.0;
	 double subdivisionBonus = 0;
	 if(team1FBS)
	   subdivisionBonus += .5;
	 if(team2FBS)
	   subdivisionBonus += .5;
	 //System.out.printf("Quality: %.2f\nEvenness: %.2f\nSubBonus: %.2f\nCap: %d", quality, evenness, subdivisionBonus, stadiumCapacity);
	 double desirability = (evenness * quality * subdivisionBonus * stadiumCapacity) / 1000;
	 ret = String.format("%.2f", desirability);
        
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
