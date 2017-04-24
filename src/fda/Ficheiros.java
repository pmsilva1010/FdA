
package fda;

import java.io.File;
import java.util.*;

/**
 *
 * @author Pedro Silva
 */
public class Ficheiros {
    public File ficheiro;
    public String path, ext, nome, season;
    
    public Ficheiros(File ficheiro, String nome){
        this.ficheiro=ficheiro;
        path=ficheiro.getAbsolutePath();
        ext=ficheiro.getName().substring(ficheiro.getName().lastIndexOf(".")+1);
        this.nome=nome;
        season=getSeason();
    }
    
    public Ficheiros(File ficheiro, String nome, String season){
        this.ficheiro=ficheiro;
        path=ficheiro.getAbsolutePath();
        ext=ficheiro.getName().substring(ficheiro.getName().lastIndexOf(".")+1);
        this.nome=nome;
        this.season=season;
    }
    
    public void getFile(String caminho, List extValidas){
        if(ficheiro.isDirectory()){
            File directory=new File(caminho);
            File[] fList = directory.listFiles();
            String aux;
            
            for(int i=0;i<fList.length;i++){
                if(fList[i].isFile()){
                    aux=fList[i].getName().substring(fList[i].getName().lastIndexOf(".")+1);
                    if(extValidas.contains(aux)){
                        ficheiro=fList[i];
                        path=fList[i].getAbsolutePath();
                        ext=aux;
                    }
                }
                else if(fList[i].isDirectory()){
                    getFile(fList[i].getAbsolutePath(), extValidas);
                }
            }
        }
    }
    
    private String getSeason(){
        int tamanho=ficheiro.getName().indexOf(nome.replace(" ","."))+nome.length();
        
        if(ficheiro.getName().charAt(tamanho+1)=='S'){
            return ficheiro.getName().substring(tamanho+2, tamanho+4);
            
        }
        else if(ficheiro.getName().charAt(tamanho+6)=='S'){
            return ficheiro.getName().substring(tamanho+7, tamanho+9);
        }
        else{
            tamanho=ficheiro.getName().indexOf(nome)+nome.length();
            if(ficheiro.getName().charAt(tamanho+1)=='S'){
                return ficheiro.getName().substring(tamanho+2, tamanho+4);
            }
            else
                return ficheiro.getName().substring(tamanho+7, tamanho+9);  
        }       
    }
    
    public String toString(){
        return "Ficheiro: "+ficheiro.getName()+"   |Season: "+season+"|   |Nome: "+nome+"|   |Extensão: "+ext+"|\n";
    }
    
}
