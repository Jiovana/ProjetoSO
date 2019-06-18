/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projetoso;

import java.time.Instant;
import java.util.Random;
import model.FilaCircular;
import model.Requisicao;

/**
 *
 * @author gomes
 */
public class Buffer extends Thread {

  
    Random rand = new Random();
    Requisicao nova;
    int max, min;
    int tam_pag;
    int num_req;
    
    
    public Buffer( int max, int min, int tp, int nr) {     
        this.max = max;
        this.min = min;
        this.tam_pag = tp;
        this.num_req = nr;      
    }

    @Override
    public void run() {
        
        geraReqs();
    }

    public void geraReqs() {
        while (ProjetoSOP.flag) {
            //------inicio gerar requisições
            int numreq = rand.nextInt(num_req); //num de req geradas por vez
            do { //loop para inserir 'numreq' requisições no buffer, de forma de a quantidade de requisições inseridas é aleatoria
                int tamanho = rand.nextInt((max - min) + 1) + min;
                this.nova = new Requisicao(tamanho, (Instant.now().toEpochMilli() + rand.nextInt(Integer.MAX_VALUE)), tam_pag);                
                ProjetoSOP.fila.Inserir(nova);
                numreq--;
            } while (numreq > 0);
            //------fim gerar requisições
        }
    }

}