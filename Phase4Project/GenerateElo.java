import java.util.*;
import java.io.*;
import java.math.*;

public class GenerateElo{
 
  final static int k = 40;
  final static int homeBonus = 57;

  public static void main(String args[]) throws FileNotFoundException {
    HashMap<Integer,Double> elos = new HashMap<Integer,Double>(); 
    
    //Find elo ratings without home-field advantage
    generate(elos,homeBonus);

    //assume home-field bonus is between -1000 and +1000
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
    HashMap<Integer, ArrayList<Double>> homeAdvElos = new HashMap<Integer, ArrayList<Double>>();
    generateHomeFieldAdv(homeAdvElos, homeBonus);
    ArrayList<Integer> teamCodes = new ArrayList<Integer>(homeAdvElos.keySet());
    PrintWriter out = new PrintWriter(new File("teamElos.csv"));
    out.printf("Team Code, Elo, Home Elo, Away Elo\n");
    for(int i = 0; i < teamCodes.size(); i++){
      System.out.printf("%d: Home: %.2f Away: %.2f\n", teamCodes.get(i), homeAdvElos.get(teamCodes.get(i)).get(0), 
	  homeAdvElos.get(teamCodes.get(i)).get(1));
      out.printf("%d, %d, %d, %d\n", teamCodes.get(i), Math.round(elos.get(teamCodes.get(i))),
	  Math.round(homeAdvElos.get(teamCodes.get(i)).get(0)), Math.round(homeAdvElos.get(teamCodes.get(i)).get(1)));
    }
    out.close();
  }

  public GenerateElo(){

  }

  public static double generate(HashMap<Integer, Double> teamElos, int homeBonus){
    DatabaseManager db = new DatabaseManager();
    if(!db.connect()){
      System.out.println("Unable to connect to database.");
      //HashMap<Integer, Double> temp = new HashMap<Integer, Double>();
      return -1;
    }
    ResultTable teamCodeTable = db.performQuery("SELECT \"Team Code\",\"Name\" FROM merged_team;");
    //HashMap<Integer, Double> teamElos = new HashMap<Integer, Double>();
    for(int i = 0; i < teamCodeTable.getNumRows(); i++){
      teamElos.put(teamCodeTable.getInteger(i, 0), 1500.0); //1500 is average
    }
    ResultTable table = db.performQuery("SELECT * FROM basic_game_stats order by \"Date\";");
    ArrayList<String> keys = table.getFieldNames();
    double numHomeWins = 0;
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
      double eloChange = eloDelt(teamElos.get(homeCode) + bonus, teamElos.get(visitCode), homeScore > awayScore);
      if(isHome && homeScore > awayScore)
	numHomeWins += 1;
      predHomeWins += eloExpectation(teamElos.get(homeCode) + bonus, teamElos.get(visitCode));
      teamElos.put(homeCode, teamElos.get(homeCode) + eloChange);
      teamElos.put(visitCode, teamElos.get(visitCode) - eloChange);
    }
    System.out.printf("Predicted %.2f home wins, were actually %.2f home wins\n.", predHomeWins, numHomeWins);
    return predHomeWins - numHomeWins;
  }
  
  //teamElos maps team codes to elos for a team, the 0-index for home performance, and the 1-index for away performance
  public static double generateHomeFieldAdv(HashMap<Integer, ArrayList<Double> > teamElos, int homeBonus){
    DatabaseManager db = new DatabaseManager();
    if(!db.connect()){
      System.out.println("Unable to connect to database.");
      //HashMap<Integer, Double> temp = new HashMap<Integer, Double>();
      return -1;
    }
    ResultTable teamCodeTable = db.performQuery("SELECT \"Team Code\",\"Name\" FROM merged_team;");
    //HashMap<Integer, Double> teamElos = new HashMap<Integer, Double>();
    for(int i = 0; i < teamCodeTable.getNumRows(); i++){
      ArrayList<Double> temp = new ArrayList<Double>();
      temp.add(1500.0); temp.add(1500.0);
      teamElos.put(teamCodeTable.getInteger(i, 0), temp); //1500 is average
    }
    ResultTable table = db.performQuery("SELECT * FROM basic_game_stats order by \"Date\";");
    ArrayList<String> keys = table.getFieldNames();
    double numHomeWins = 0;
    double predHomeWins = 0;
    for(int i = 0; i < table.getNumRows(); i++){
      int homeCode, visitCode, homeScore, awayScore;
      homeCode = table.getInteger(i,keys.indexOf("Home Team Code"));
      visitCode = table.getInteger(i,keys.indexOf("Visit Team Code"));
      homeScore = table.getInteger(i,keys.indexOf("Home Score"));
      awayScore = table.getInteger(i,keys.indexOf("Away Score"));
      int isHome = 1;
      if(table.getString(i,keys.indexOf("Site")).equals("TEAM"))
	isHome = 0;
      int bonus = 0;
      if(isHome == 0)
	bonus = homeBonus;
      double eloChange = eloDelt(teamElos.get(homeCode).get(isHome) + bonus, teamElos.get(visitCode).get(1), homeScore > awayScore);
      if(isHome == 0 && homeScore > awayScore)
	numHomeWins += 1;
      predHomeWins += eloExpectation(teamElos.get(homeCode).get(isHome) + bonus, teamElos.get(visitCode).get(1));
      teamElos.get(homeCode).set(isHome, teamElos.get(homeCode).get(isHome) + eloChange);
      teamElos.get(visitCode).set(1, teamElos.get(visitCode).get(1) - eloChange);
    }
    System.out.printf("Predicted %.2f home wins, were actually %.2f home wins\n.", predHomeWins, numHomeWins);
    return predHomeWins - numHomeWins;
  }

  public static double eloDelt(double team1Elo, double team2Elo, boolean team1Win){
    double team1Expected = eloExpectation(team1Elo, team2Elo); 
    if(team1Win){
      return k * (1 - team1Expected);
    }
    else{
      return k * (-team1Expected);
    }
  }

  public static double eloExpectation(double team1Elo, double team2Elo){
    return 1.0/(1 + Math.pow(10, (team2Elo - team1Elo)/400));
  }

}
