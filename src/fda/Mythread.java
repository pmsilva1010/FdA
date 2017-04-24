
package fda;

import java.io.*;
import java.util.concurrent.Semaphore;

/**
 *
 * @author Pedro Silva
 */
public class Mythread extends Thread {
    Ficheiros fich;
    String dest;
    Semaphore sem;
    FdAgui gui;
    
    public Mythread(Ficheiros fich, String dest, Semaphore sem, FdAgui gui){
        this.fich=fich;
        this.dest=dest;
        this.sem=sem;
        this.gui=gui;
    }
    
    public void run(){
        try {
            copiaFicheiro(fich,dest);
            sem.release();
        } catch (IOException | InterruptedException ex) {
            gui.setMsg("ERROR - Ao copiar o ficheiro "+fich.nome+"!\n");
        }
    }
    
    /*
     Auxiliar para copiar um ficheiro para o disco externo
    */
    private void copiaFicheiro(Ficheiros fich, String dest) throws IOException, InterruptedException{
        if(fich.ext.equals("rar")){
            copiaRaR(fich,dest);
        }
        else if(fich.ext.equals("zip")){
            copiaZip(fich,dest);
        }
        else{
            copiaRaw(fich,dest);
        }
    }
    
    /*
     Auxiliar para copiar ficheiros RAR
    */
    private void copiaRaR(Ficheiros serie,String dest) throws IOException, InterruptedException{
        String comand="C:/Program Files/WinRAR/unrar";
        String flag="e";
        String path=serie.path;
        String destiny=dest+"/"+serie.nome+"/Season "+serie.season;
        
        mkdir(destiny);
        
        String[] extrair=new String[]{comand,flag,path,destiny};
        
        gui.setMsg("START Rar - "+serie.ficheiro.getName()+"\n");
        Process p=Runtime.getRuntime().exec(extrair);
        
        StreamGobbler errorGobbler = new StreamGobbler(p.getErrorStream(), "ERROR", gui);            
        //StreamGobbler outputGobbler = new StreamGobbler(p.getInputStream(), "OUTPUT", gui);       
        // kick them off
        errorGobbler.start();
        //outputGobbler.start();
            
        p.waitFor();
        gui.setMsg("SUCCESS - "+serie.ficheiro.getName()+" *UnRar*\n");
    }
    
    /*
     Auxiliar para copiar ficheiros ZIP
    */
    private void copiaZip(Ficheiros serie, String dest) throws IOException{
        String source=serie.path;
        String destiny=dest+"/"+serie.nome+"/Season "+serie.season;
        
        mkdir(destiny);
     
        UnzipUtility unzipper = new UnzipUtility();
        try {
            gui.setMsg("START Zip - "+serie.ficheiro.getName()+"\n");
            unzipper.unzip(source, destiny);
            gui.setMsg("SUCCESS - "+serie.ficheiro.getName()+" *UnZipado*\n");
        } catch (Exception ex) {
            // some errors occurred
            gui.setMsg("ERROR Zip - A unzipar o ficheiro "+serie.ficheiro.getName()+"!\n");
        }
    }
    
    /*
     Auxiliar para copiar ficheiros puros
    */
    private void copiaRaw(Ficheiros serie,String dest) throws FileNotFoundException, IOException{
        File origin=new File(serie.path);
        File destino=new File(dest+"/"+serie.nome+"/Season "+serie.season+"/"+serie.ficheiro.getName());
        
        mkdir(dest+"/"+serie.nome+"/Season "+serie.season);
        
        InputStream is;
        OutputStream os;
        
        is = new FileInputStream(origin);
        os = new FileOutputStream(destino);
        
        gui.setMsg("START cpy - "+serie.ficheiro.getName()+"\n");
        
        byte[] buffer = new byte[1024];
        int length;
        while ((length = is.read(buffer)) > 0) {
            os.write(buffer, 0, length);
        }
        
        is.close();
        os.close();
        
        gui.setMsg("SUCCESS - "+serie.ficheiro.getName()+" *COPIADO*\n");
    }
    
    /*
     Auxiliar para verificar e criar directorias inexistentes
    */
    private void mkdir(String dest){
        File destiny=new File(dest);
        if(destiny.exists()==false){
            destiny.mkdirs();
        }
    }
}
