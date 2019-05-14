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
    private  Requisicao[] fila;
    private final int tamanho = 500;
    private int tamanhoAtual;
    public FilaCircular() {
        this.tamanhoAtual = 0;
        this.ini = tamanhoAtual;
        this.fim = tamanhoAtual;
        this.fila = new Requisicao [tamanho];      
    } 
   public boolean Inserir(Requisicao nova){
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
   public  Requisicao Remover(){
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
