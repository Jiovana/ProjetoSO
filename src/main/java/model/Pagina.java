/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;


/**
 *
 * @author gomes
 */
public class Pagina {

//heap tem páginas com tamanho de 256 integer (1 Kbyte) para dados e mais  1 int pra endereço
    //endereço sera identificador da requisicao
    
    private long endereço;
    private int[] dado = new int[256];
    private boolean ocupado;
    
     public Pagina(long endereço, boolean ocupado) {
        this.endereço = endereço;
        this.ocupado = ocupado;
    }

    public long getEndereço() {
        return endereço;
    }

    public void setEndereço(long endereço) {
        this.endereço = endereço;
    }
 

    public int[] getDado() {
        return dado;
    }

    public void setDado(int[] dado) {
        this.dado = dado;
    }

    public boolean isOcupado() {
        return ocupado;
    }

    public void setOcupado(boolean ocupado) {
        this.ocupado = ocupado;
    }

   
    
    
    
    
    
    
   
    
    


} 
 

