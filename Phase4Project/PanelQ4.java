import java.sql.ResultSet;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import java.awt.Color;

public class PanelQ4 extends JPanel implements Savable {

    AutoCompleteBox teams;
    JButton runBtn;
    JLabel question;
    JTextArea response;
    String saveString = "No results to display";
  
    public PanelQ4(){
        System.out.println("Connected to database!");

        question = new JLabel("Given a team, determine its home-field advantage.");
        response = new JTextArea("Run a query to get a response...");
        response.setEditable(false);
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
    public ArrayList<Integer> q4Function(String team){
	ArrayList<Integer> elos = new ArrayList<Integer>();
        try {
            //get the first team code from name
            ResultTable teamCodeTable = MainApplication.dbManager.performQuery("SELECT merged_team.\"Team Code\" FROM merged_team WHERE merged_team.\"Name\"= '" + team +"' ;");
            //get the losing team code from name
            Integer teamCode = teamCodeTable.getInteger(0,0);
	    //Get regular elo, home elo, and away elo
            ResultTable teamElos = MainApplication.dbManager.performQuery("SELECT \"Team Elo\", \"Home Elo\", \"Away Elo\" FROM team_elo_ratings where \"Team Code\"="+teamCode+";");
	    elos.add(teamElos.getInteger(0,1));
	    elos.add(teamElos.getInteger(0,2));
	    return elos;
        }catch(Exception e){
            return elos;
        }
    }

    private void addListeners() {
        runBtn.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                if(e.getActionCommand() == "GO!"){
                    String questionTeam = (String) teams.getSelectedItem();
                    ArrayList<Integer> teamElos;
                    String answerString = "";
                    teamElos = q4Function(questionTeam);

                    if (teamElos.size() != 0){
			int homeElo = teamElos.get(0);
			int awayElo = teamElos.get(1);
			answerString = String.format("%s is %d Elo points better at home than away.\nThis means that %s has a %.2f%% chance of beating an average team at a neutral site,\nwhile they have a %.2f%% chance of beating an average team at home.", questionTeam,
			    GenerateElo.homeBonus + homeElo - awayElo, questionTeam, GenerateElo.eloExpectation(awayElo, 1500)*100,
			    GenerateElo.eloExpectation(homeElo + GenerateElo.homeBonus, 1500)*100);
                        response.setForeground(Color.decode("#2e994a"));

                    }else {
			answerString = "The home field advantage for " + questionTeam + " could not be computed.";
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
