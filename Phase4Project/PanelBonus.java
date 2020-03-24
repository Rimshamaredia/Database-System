import java.sql.ResultSet;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import java.awt.Color;

public class PanelBonus extends JPanel implements Savable {

    AutoCompleteBox positions;
    JButton runBtn;
    JLabel question;
    JTextArea response;
    String saveString = "No results to display";
 
    public PanelBonus(){
        System.out.println("Connected to database!");

        question = new JLabel("Find out which home town has the most ");
        response = new JTextArea("Run a query to get a response...");
        response.setEditable(false);
        response.setForeground(Color.BLUE);

        String query = "SELECT DISTINCT \"Position\" FROM merged_player WHERE \"Position\" IS NOT NULL;";
        ResultTable positionNames = MainApplication.dbManager.performQuery(query);
        String positionStrings[] = new String[positionNames.getNumRows() + 1];
	positionStrings[0] = "Players";
        for(int i = 0; i < positionNames.getNumRows(); i++)
        {
            positionStrings[i+1] = positionNames.getString(i, 0);
        }
        positions = new AutoCompleteBox(positionStrings);
        runBtn = new JButton("GO!");

        this.addListeners();

        this.add(question);
        this.add(positions);
        this.add(runBtn);

        this.add(response);

    }

    //Give a position, determine which home town has the greatest number of players that play that position
    public String bonusFunction(String position){
        try {
	    if(position.equals("Players")){
	      //Query regardless of position
	      ResultTable homeTownTable = MainApplication.dbManager.performQuery(
		  "SELECT \"Home Town\",\"Home State\",\"Home Country\", COUNT(*) as \"Count\" FROM merged_player WHERE \"Home Town\" IS NOT NULL GROUP BY \"Home Town\",\"Home State\",\"Home Country\" ORDER BY \"Count\" DESC LIMIT 1;");
	      String town = homeTownTable.getString(0,0);
	      String state = homeTownTable.getString(0,1);
	      String country = homeTownTable.getString(0,2);
	      return town + ", " + state + ", " + country;
	    }
	    else{
	      //Query on a specific position
	      ResultTable homeTownTable = MainApplication.dbManager.performQuery(
		  "SELECT \"Home Town\",\"Home State\",\"Home Country\",\"Position\", COUNT(*) as \"Count\" FROM merged_player WHERE \"Home Town\" IS NOT NULL AND \"Position\"='" + position + "' GROUP BY \"Home Town\",\"Home State\",\"Home Country\",\"Position\" ORDER BY \"Count\" DESC LIMIT 1;");
	      String town = homeTownTable.getString(0,0);
	      String state = homeTownTable.getString(0,1);
	      String country = homeTownTable.getString(0,2);
	      return town + ", " + state + ", " + country;

	    }
        }catch(Exception e){
            return "";
        }
    }

    private void addListeners() {
        runBtn.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                if(e.getActionCommand() == "GO!"){
                    String questionPosition = (String) positions.getSelectedItem();
                    String answerString = "";
                    answerString = bonusFunction(questionPosition);
                    
		    if (!answerString.equals("")){
                        response.setForeground(Color.decode("#2e994a"));
                    }
		    else {
			answerString = "Error occured trying to find this home town";
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
