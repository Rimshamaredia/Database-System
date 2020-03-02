/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
//package testproject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 *
 * @author John Marrs
 */
public class TestQuerier {
    
    public ResultSet performQuery(){
        ResultSet finalResult = null;
        
        //dbSetup hides my username and password
        TestDBSetupExample my = new TestDBSetupExample();
        //Building the connection
        Connection conn = null;
        try {
            Class.forName("org.postgresql.Driver");
            conn = DriverManager.getConnection("jdbc:postgresql://csce-315-db.engr.tamu.edu/databallfootbase",
            my.user, my.pswd);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        }//end try catch
        System.out.println("Opened database successfully");
     
        String name = "";
    
        try{
            //create a statement object
            Statement stmt = conn.createStatement();
            //create an SQL statement
            // String sqlStatement = "SELECT * FROM merged_stadium
            //ORDER BY \"Capacity\" DESC
            // LIMIT 10";
            // String sqlStatement = "SELECT \"Name\" FROM merged_conference";
            String sqlStatement = "SELECT * FROM merged_player";
     
            //send statement to DBMS
            finalResult = stmt.executeQuery(sqlStatement);
        } catch(Exception e){
            e.printStackTrace();
        }
 
        return finalResult;
    }
    
}
