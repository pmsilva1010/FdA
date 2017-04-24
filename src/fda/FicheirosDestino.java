
package fda;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Pedro Silva
 */
public class FicheirosDestino {
    private String dest;
    private List extValidas;
    private File directory;
    private File[] flist;
    private ArrayList <Ficheiros> fDisco;
    
    public FicheirosDestino(String dest, List extValidas){
        this.dest=dest;
        this.extValidas=extValidas;
        fDisco=new ArrayList<>();
        directory=new File(dest);
        flist=directory.listFiles();
        
        getFdisco();
    }
    
    private void getFdisco(){
        Ficheiros aux;
        for(int i=0;i<flist.length;i++){
            if(flist[i].isFile()){
                aux=new Ficheiros(flist[i],flist[i].getName());
                fDisco.add(aux);
            }
            else if(flist[i].isDirectory()){
                getFile(flist[i].getAbsolutePath(),flist[i].getName(),"");
            }
        }
    }
    
    private void getFile(String caminho, String nome, String season){
        File directory=new File(caminho);
        File[] fList = directory.listFiles();
        String aux;
        
        for(int i=0;i<fList.length;i++){
            if(fList[i].isFile()){
                aux=fList[i].getName().substring(fList[i].getName().lastIndexOf(".")+1);
                if(extValidas.contains(aux)){
                    fDisco.add(new Ficheiros(fList[i],nome,season));
                }
            }
            else if(fList[i].isDirectory()){
                //Para ir buscar a season no disco
                if(fList[i].getName().contains("Season")){
                    season=fList[i].getName().substring(fList[i].getName().lastIndexOf(" ")+1);
                }
                //end
                getFile(fList[i].getAbsolutePath(), nome, season);
            }
        }
    }
    
    public ArrayList<Ficheiros> getDisco(){
        return fDisco;
    }
}
