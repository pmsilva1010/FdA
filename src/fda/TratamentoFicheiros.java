
package fda;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;
import java.util.zip.ZipInputStream;

/**
 *
 * @author Pedro Silva
 */
public class TratamentoFicheiros {
    private ArrayList<Ficheiros> fValidos, fDisco;
    private FdAgui gui;
    
    public TratamentoFicheiros(ArrayList<Ficheiros> fValidos, ArrayList<Ficheiros> fDisco, FdAgui gui) throws IOException, InterruptedException{
        this.fValidos=fValidos;
        this.fDisco=fDisco;
        this.gui=gui;
        
        delEquals();
    }
    
    /*
     Serve para remover os ficheiros que ja existem no disco externo
    */
    private void delEquals() throws IOException, InterruptedException{
        ArrayList<Ficheiros> eliminar=new ArrayList<>();
        for(Ficheiros fich:fValidos){
            if(fich.ext.equals("rar")){
                if(processaRaR(fich)==1){
                    eliminar.add(fich);
                }
            }
            else if(fich.ext.equals("zip")){
                if(processaZip(fich)==1)
                    eliminar.add(fich);
            }
            else{
                if(processaRaw(fich)==1){
                    eliminar.add(fich);
                }
            }
        }
        fValidos.removeAll(eliminar);
    }
    
    /*
     Auxiliar para verificar se o ficheiro dentro do rar ja existe
    */
    private int processaRaR(Ficheiros serie) throws IOException, InterruptedException{
        String comand="C:/Program Files/WinRAR/unrar";
        String flag="lb";
        String path=serie.path;
        
        String[] nome=new String[]{comand,flag,path};
        
        Process p=Runtime.getRuntime().exec(nome);
        
        StreamGobbler errorGobbler = new StreamGobbler(p.getErrorStream(), "ERROR", gui);            
        StreamGobbler outputGobbler = new StreamGobbler(p.getInputStream(), "OUTPUT", gui);
        //kick them off
        errorGobbler.start();
        outputGobbler.start();
            
        p.waitFor();
        
        String name=outputGobbler.name;
        
        for(Ficheiros fich:fDisco){
            if(name.equals(fich.ficheiro.getName())){
                return 1;
            }
        }
        return 0;
    }
    
    /*
     Auxiliar para verifica se o ficheiro dentro do zip ja existe
    */
    private int processaZip(Ficheiros serie) throws FileNotFoundException, IOException{
        ZipInputStream zip = new ZipInputStream(new FileInputStream(serie.path));
        
        String name=zip.getNextEntry().getName();
        zip.close();
        
        for(Ficheiros fich:fDisco){
            if(name.equals(fich.ficheiro.getName())){
                return 1;
            }
        }
        return 0;
    }
    
    /*
     Auxiliar para verificar se ficheiros puros ja existem
    */
    private int processaRaw(Ficheiros serie){
        for(Ficheiros fich:fDisco){
            if(serie.ficheiro.getName().equals(fich.ficheiro.getName())){
                return 1;
            }
        }
        return 0;
    }
    
    public ArrayList<Ficheiros> getValidos(){
        return fValidos;
    }
    
    public void trataFicheiros(String dest, int maxThreads) throws InterruptedException{
        Semaphore sem=new Semaphore(maxThreads);
        Semaphore mutex=new Semaphore(1);
        Mythread temp=null;
        
        while(fValidos.isEmpty()!=true){
            mutex.acquire();
            Ficheiros fich=fValidos.get(0);
            fValidos.remove(fValidos.get(0));
            mutex.release();
            sem.acquire();
            temp=new Mythread(fich, dest, sem, gui);
            temp.start(); 
        }
        sem.acquire(maxThreads);
    }
}
