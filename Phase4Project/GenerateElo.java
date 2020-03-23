import java.util.*;
import java.io.*;
import java.math.*;

public class GenerateElo{
  
  //k is a constant used when updating the elo ratings of teams
  final static int k = 40;
  //homeBonus is added to a team's elo rating when they play a home game
  //homeBonus was calculated so that the model accurately predicts the number of home wins
  final static int homeBonus = 57;

  public static void main(String args[]) throws FileNotFoundException {
    //HashMap of teamCodes to their elo ratings
    HashMap<Integer,Double> elos = new HashMap<Integer,Double>(); 
    
    generate(elos,homeBonus);
    
    //The following code finds which homeBonus accurately predicts the number of home wins using binary search
    /*
    int lower = -1000;
    int upper = 1000;
    //binary search
    while(lower < upper){
      System.out.printf("Lower: %d\tUpper: %d\n", lower, upper);
      int mid = (lower + upper)/2;
      double homeWinDiff = generate(elos, mid);
      if(homeWinDiff < 0){
	lower = mid + 1;
      }
      else{
	upper = mid - 1;
      }
    }
    System.out.printf("Home field advantage is %d\n", lower);
    ArrayList<Integer> teamCodes = new ArrayList<Integer>(elos.keySet());
    for(int i = 0; i < teamCodes.size(); i++){
      System.out.printf("%d:\t%.2f\n", teamCodes.get(i), elos.get(teamCodes.get(i)));
    }
    */
    //HashMap of teamCodes to an ArrayList with two entries:
    //--homeEloRating: the team's elo rating for when they play home games
    //--awayEloRating: the team's elo rating for when they play away games
    HashMap<Integer, ArrayList<Double>> homeAdvElos = new HashMap<Integer, ArrayList<Double>>();
    generateHomeFieldAdv(homeAdvElos, homeBonus);
    ArrayList<Integer> teamCodes = new ArrayList<Integer>(homeAdvElos.keySet());
    //Write Team Codes, Standard Elo Rating, Home Elo Rating, and Away Elo Rating to teamElos.csv
    PrintWriter out = new PrintWriter(new File("teamElos.csv"));
    out.printf("Team Code, Elo, Home Elo, Away Elo\n");
    for(int i = 0; i < teamCodes.size(); i++){
      out.printf("%d, %d, %d, %d\n", teamCodes.get(i), Math.round(elos.get(teamCodes.get(i))),
	  Math.round(homeAdvElos.get(teamCodes.get(i)).get(0)), Math.round(homeAdvElos.get(teamCodes.get(i)).get(1)));
    }
    out.close();
  }

  public GenerateElo(){}

  /*
   *generate() produces elo ratings for all teams in the database accessed by the DatabaseManager using all games
   *accessed similarly with a give home-field advantage.
   *Arguments:
   *--teamElos: HashMap from Team Codes to their elo ratings. Should be passed in empty and will be populated by the method
   *--homeBonus: integer representing the average home-field advantage for a team
   *Returns:
   *--homeWinDifferential: double representing the difference in how many home wins were predicted and how many occurred
   */
  public static double generate(HashMap<Integer, Double> teamElos, int homeBonus){
    //Connect to database
    DatabaseManager db = new DatabaseManager();
    if(!db.connect()){
      System.out.println("Unable to connect to database.");
      return -1;
    }
    //Query database for all team codes
    ResultTable teamCodeTable = db.performQuery("SELECT \"Team Code\",\"Name\" FROM merged_team;");
    for(int i = 0; i < teamCodeTable.getNumRows(); i++){
      //1500 is the average team rating, so all teams will start with that rating
      teamElos.put(teamCodeTable.getInteger(i, 0), 1500.0);
    }
    ResultTable table = db.performQuery("SELECT * FROM basic_game_stats order by \"Date\";");
    ArrayList<String> keys = table.getFieldNames();
    //Number of actual home wins
    double numHomeWins = 0;
    //Number of predicted home wins
    double predHomeWins = 0;
    for(int i = 0; i < table.getNumRows(); i++){
      int homeCode, visitCode, homeScore, awayScore;
      homeCode = table.getInteger(i,keys.indexOf("Home Team Code"));
      visitCode = table.getInteger(i,keys.indexOf("Visit Team Code"));
      homeScore = table.getInteger(i,keys.indexOf("Home Score"));
      awayScore = table.getInteger(i,keys.indexOf("Away Score"));
      boolean isHome = table.getString(i,keys.indexOf("Site")).equals("TEAM");
      int bonus = 0;
      if(isHome)
	bonus = homeBonus;
      //Calculate how both teams' elo ratings should change based on the game's outcome
      double eloChange = eloDelt(teamElos.get(homeCode) + bonus, teamElos.get(visitCode), homeScore > awayScore);
      if(isHome && homeScore > awayScore)
	numHomeWins += 1;
      predHomeWins += eloExpectation(teamElos.get(homeCode) + bonus, teamElos.get(visitCode));
      //Update both teams' elo ratings
      teamElos.put(homeCode, teamElos.get(homeCode) + eloChange);
      teamElos.put(visitCode, teamElos.get(visitCode) - eloChange);
    }
    return predHomeWins - numHomeWins;
  }
  
  /*
   *generateHomeFieldAdv() produces elo ratings for all home and away teams in the database accessed by the DatabaseManager using all games
   *accessed similarly with a give home-field advantage.
   *Arguments:
   *--teamElos: HashMap from Team Codes to their elo ratings at home and away. Should be passed in empty and will be populated by the method
   *--homeBonus: integer representing the average home-field advantage for a team
   *Returns:
   *--homeWinDifferential: double representing the difference in how many home wins were predicted and how many occurred
   */
  public static double generateHomeFieldAdv(HashMap<Integer, ArrayList<Double> > teamElos, int homeBonus){
    //Connect to database
    DatabaseManager db = new DatabaseManager();
    if(!db.connect()){
      System.out.println("Unable to connect to database.");
      //HashMap<Integer, Double> temp = new HashMap<Integer, Double>();
      return -1;
    }
    //Query database for all team codes
    ResultTable teamCodeTable = db.performQuery("SELECT \"Team Code\",\"Name\" FROM merged_team;");
    for(int i = 0; i < teamCodeTable.getNumRows(); i++){
      ArrayList<Double> temp = new ArrayList<Double>();
      //1500 is the average team rating, so all teams will start with that rating
      //First element of temp is the home rating, second is the away rating
      temp.add(1500.0); temp.add(1500.0);
      teamElos.put(teamCodeTable.getInteger(i, 0), temp); 
    }
    ResultTable table = db.performQuery("SELECT * FROM basic_game_stats order by \"Date\";");
    ArrayList<String> keys = table.getFieldNames();
    //Number of actual home wins
    double numHomeWins = 0;
    //Number of predicted home wins
    double predHomeWins = 0;
    for(int i = 0; i < table.getNumRows(); i++){
      int homeCode, visitCode, homeScore, awayScore;
      homeCode = table.getInteger(i,keys.indexOf("Home Team Code"));
      visitCode = table.getInteger(i,keys.indexOf("Visit Team Code"));
      homeScore = table.getInteger(i,keys.indexOf("Home Score"));
      awayScore = table.getInteger(i,keys.indexOf("Away Score"));
      //isHome == 1 for neutral site games, isHome == 0 for home games
      int isHome = 1;
      if(table.getString(i,keys.indexOf("Site")).equals("TEAM"))
	isHome = 0;
      int bonus = 0;
      if(isHome == 0)
	bonus = homeBonus;
      //Calculate how both teams' elo ratings should change based on the game's outcome
      double eloChange = eloDelt(teamElos.get(homeCode).get(isHome) + bonus, teamElos.get(visitCode).get(1), homeScore > awayScore);
      if(isHome == 0 && homeScore > awayScore)
	numHomeWins += 1;
      predHomeWins += eloExpectation(teamElos.get(homeCode).get(isHome) + bonus, teamElos.get(visitCode).get(1));
      //Update both teams' elo ratings
      teamElos.get(homeCode).set(isHome, teamElos.get(homeCode).get(isHome) + eloChange);
      teamElos.get(visitCode).set(1, teamElos.get(visitCode).get(1) - eloChange);
    }
    return predHomeWins - numHomeWins;
  }

  //Elo rating scheme is implement as described by Arpad Elo in:
  //Elo, Arpad (1978). The Rating of Chessplayers, Past and Present. Arco. ISBN 978-0-668-04721-0.
  
  /*eloDelt() determines how much each teams' elo rating should change after they finish a game against the other
   *Arguments:
   *--team1Elo: team1's Elo rating
   *--team2Elo: team2's Elo rating
   *--team1Win: true if team1 won the game
   *Returns:
   *--delt: double that represents how many points the winning team's score should gain and how many points the losing team should lose
   */
  public static double eloDelt(double team1Elo, double team2Elo, boolean team1Win){
    double team1Expected = eloExpectation(team1Elo, team2Elo); 
    if(team1Win){
      return k * (1 - team1Expected);
    }
    else{
      return k * (-team1Expected);
    }
  }

  //eloExpectation() returns the probability that a team with a rating of team1Elo will win a game against a team with a rating of team2Elo
  public static double eloExpectation(double team1Elo, double team2Elo){
    return 1.0/(1 + Math.pow(10, (team2Elo - team1Elo)/400));
  }

}
