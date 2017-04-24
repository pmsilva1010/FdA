
package fda;

import java.io.*;
import java.util.*;

/**
 *
 * @author Pedro Silva
 */
public class Configs {
    private String origin,destiny;
    private int maxThreads;
    private List extValidas;
    private List <String> seriesValidas;
    
    public Configs(){
        
    }
    
    public void load() throws FileNotFoundException, IOException{
        FileReader frd = new FileReader("configs.txt");
        BufferedReader fr=new BufferedReader(frd);
            
        origin=fr.readLine();
        destiny=fr.readLine();
        maxThreads=Integer.parseInt(fr.readLine());
        extValidas=separaStrings(fr.readLine());
        seriesValidas=separaStrings(fr.readLine());
            
        fr.close();
        frd.close(); 
    }
    
    private List separaStrings(String line){
        List<String> list = new LinkedList(Arrays.asList(line.split(";")));
        
        return list; 
    }
    
    public String getOrigin(){
        return origin;
    }
    
    public String getDestiny(){
        return destiny;
    }
    
    public int getMaxThreads(){
        return maxThreads;
    }
    
    public List getExtValidas(){
        return extValidas;
    }
    
    public List getSeriesValidas(){
        return seriesValidas;
    }
}
