
package fda;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Pedro Silva
 */
public class FicheirosOrigem {
    private String origin;
    private List <String> seriesValidas;
    private List extValidas;
    private File directory;
    private File[] flist;
    private ArrayList <Ficheiros> fValidos;
    
    public FicheirosOrigem(String origin, List <String> seriesValidas, List extValidas){
        this.origin=origin;
        this.seriesValidas=seriesValidas;
        this.extValidas=extValidas;
        fValidos=new ArrayList<>();
        directory=new File(origin);
        flist=directory.listFiles();
        
        checkSeries();
        checkExt();
    }
    
    /*
     Serve para ver se a serie é valida
    */
    private void checkSeries(){
        Ficheiros aux;
        String aux2;
        
        for(int i=0;i<flist.length;i++){
            aux2=checkFile(flist[i]);
            if(aux2!= null){
                aux=new Ficheiros(flist[i],aux2);
                fValidos.add(aux);
            }
        }
    }
    
    /*
     Auxiliar para comparar os nomes dos ficheiro com as series
    */
    private String checkFile(File fich){
        String nomeFich=fich.getName().toLowerCase();
        String seriePontos, serieEspacos;
  
        for(int i=0;i<seriesValidas.size();i++){
            seriePontos=seriesValidas.get(i).toLowerCase();
            serieEspacos=seriePontos.replace(".", " ");
            
            if((nomeFich.contains(seriePontos)) || (nomeFich.contains(serieEspacos))){
                return seriesValidas.get(i).replace(".", " ");
            }
        }
        return null;
    }
    
    private void checkExt(){
        ArrayList<Ficheiros> eliminar=new ArrayList<>();
        for(Ficheiros fich:fValidos){
            if(fich.ficheiro.isFile()){
                if(extValidas.contains(fich.ext)){
                    //Its valid
                }
                else
                    eliminar.add(fich);
            }
            else if(fich.ficheiro.isDirectory()){
                fich.getFile(fich.path, extValidas);
            }
        }
        fValidos.removeAll(eliminar);
    }
    
    public ArrayList<Ficheiros> getValidos(){
        return fValidos;
    }
}
