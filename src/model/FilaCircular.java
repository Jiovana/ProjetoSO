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
    static volatile int ini, fim;    
    static volatile Requisicao[] fila;
    private final int tamanho = 500;
    static volatile int tamanhoAtual;
    
    public FilaCircular() {
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
           return req;
       }else{
           System.out.println("Buffer vazio");
           return null;
       }      
   }    
}
