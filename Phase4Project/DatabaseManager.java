import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
public class DatabaseManager {
    Connection conn;
    public DatabaseManager(){
 
    }
    
    public ResultTable performQuery(String query){
        ResultTable response = null;
        try {
            Statement stmt = conn.createStatement();
            response = new ResultTable(stmt.executeQuery(query));
        } catch (Exception e){
            e.printStackTrace();
        }
        return response;
    }
    public boolean connect() {
        try {
            Class.forName("org.postgresql.Driver");
            conn = DriverManager.getConnection("jdbc:postgresql://csce-315-db.engr.tamu.edu/databallfootbase",
            LoginInfoDB.USERNAME, LoginInfoDB.PASSWORD);
            return true;
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
    // closeConnection returns true on a success and false on failure.
    public boolean closeConnection(){
        try {
            conn.close();
            return true;
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
}
