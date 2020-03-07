
import java.sql.ResultSet;

public class TestingProgram {
    public static void main(String[] args){
        System.out.println("Connecting to the database...");
        DatabaseManager databaseManager = new DatabaseManager();
        boolean success = databaseManager.connect();
        System.out.println("Finished attempting connecting to database");
        if (!success){
            System.out.println("Connection to server failed, exiting...");
            return;
        }
      
        ResultTable rt = databaseManager.performQuery("SELECT * FROM merged_player LIMIT 10;");
        
        System.out.println(rt.getString(2, rt.getFieldIndex("Player Code")));
        System.out.println(rt.toString());
        databaseManager.closeConnection();
    }
}
