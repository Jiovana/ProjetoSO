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
    private int[] dado;
    private boolean ocupado;
    private int id_ant;
    
     public Pagina(long endereço, boolean ocupado, int tamanho, int id_ant) {
        this.endereço = endereço;
        this.ocupado = ocupado;
        this.id_ant = id_ant;
        int tam_dado = tamanho*256; //(256 = 1KB)
        this.dado = new int[tam_dado]; //tamanho da area de dados da pagina
        for (int i = 0; i < tam_dado; i++){ //preenchendo vetor objeto com quantidade fixa de info pq n faz diferença
            this.dado[i] = i;
        }
       
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

    public int getId_ant() {
        return id_ant;
    }

    public void setId_ant(int id_ant) {
        this.id_ant = id_ant;
    }

} 
 

