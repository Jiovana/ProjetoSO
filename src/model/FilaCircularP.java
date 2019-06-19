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
public class FilaCircularP {
    private  int ini, fim;    
    private  Requisicao[] fila;
    private final int tamanho = 500;
    private volatile int tamanhoAtual;
    
    public FilaCircularP() {
        tamanhoAtual = 0;
        ini = tamanhoAtual;
        fim = tamanhoAtual;
        fila = new Requisicao [tamanho];      
    }      
   
    public synchronized boolean Inserir(Requisicao nova){
        if (tamanhoAtual < tamanho){ //tem espaço
            fila[fim] = nova;
            fim = (fim+1)%tamanho;
            tamanhoAtual++;        
            System.out.println("Tamanho atual do buffer: " +tamanhoAtual);
            System.out.println("Requisicao inserida no buffer");
            return true;
        }else{
            System.out.println("Sem espaço no buffer de requisições");
            return false;
        }       
   }     
   public  synchronized Requisicao Remover(){
       if(tamanhoAtual > 0){
           Requisicao req = fila[ini];
           ini=(ini+1)%tamanho;
           tamanhoAtual--;
           System.out.println("Tamanho atual do buffer: " +tamanhoAtual);
           System.out.println("Requisicao removida do buffer");
           return req;
       }else{
           System.out.println("Buffer vazio");
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

    public synchronized int getTamanhoAtual() {
        return tamanhoAtual;
    }

    public void setTamanhoAtual(int tamanhoAtual) {
        this.tamanhoAtual = tamanhoAtual;
    }
}
