
import java.io.PrintWriter;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author johnw
 */
public class FileSaver {
    public static boolean saveFile(String path, String content){
       boolean result = false;
       
       try {
           PrintWriter outWriter = new PrintWriter(path);
           outWriter.print(content);
           outWriter.close();
           result = true;
       } catch (Exception e){
           
       }
       
       return result;
    }
}
