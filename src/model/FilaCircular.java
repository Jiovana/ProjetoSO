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
public class FilaCircular {
    private int ini, fim;    
    private final Requisicao[] FILA;
    private final int tamanho = 500;
    private int tamanhoAtual;
    
    public FilaCircular() {
        tamanhoAtual = 0;
        ini = tamanhoAtual;
        fim = tamanhoAtual;
        FILA = new Requisicao [tamanho];      
    } 
   
    public  boolean Inserir(Requisicao nova){
        if (tamanhoAtual < tamanho){ //tem espaço
            FILA[fim] = nova;
            fim = (fim+1)%tamanho;
            tamanhoAtual++;        
            //System.out.println("Requisicao inserida no buffer");
            return true;
        }else{
            //System.out.println("Sem espaço no buffer de requisições");
            return false;
        }       
   }     
   public  Requisicao Remover(){
       if(tamanhoAtual > 0){
           Requisicao req = FILA[ini];
           ini=(ini+1)%tamanho;
           tamanhoAtual--;
           return req;
       }else{
           //System.out.println("Buffer vazio");
           return null;
       }      
   }  

    public int getIni() {
        return ini;
    }

    public void setIni(int ini) {
        this.ini = ini;
    }

    public int getFim() {
        return fim;
    }

    public void setFim(int fim) {
        this.fim = fim;
    }

    public int getTamanhoAtual() {
        return tamanhoAtual;
    }

    public void setTamanhoAtual(int tamanhoAtual) {
        this.tamanhoAtual = tamanhoAtual;
    }
   
   
   
}
