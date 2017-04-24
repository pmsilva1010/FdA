/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fda;

import java.io.*;

public class StreamGobbler extends Thread{
    InputStream is;
    String type;
    public String name;
    private FdAgui gui;
    
    public StreamGobbler(InputStream is, String type, FdAgui gui){
        this.is = is;
        this.type = type;
        this.gui=gui;
    }
    
    public void run(){
        try{
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line=null;
            while((line=br.readLine())!=null){
                gui.setMsg(type + ">" + line+"\n"); //escreve na area de texto da gui original
                name=line;
            }
        } 
        catch (IOException ioe){
            gui.setMsg("Um erro ocorreu com o Winrar!\n"); //escreve na area de texto da gui original
        }
    }
}
