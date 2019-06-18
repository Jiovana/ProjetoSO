/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projetoso;

import model.HeapP;
import model.Requisicao;

/**
 *
 * @author gomes
 */
public class GarbageCollector extends Thread {

    long id_req = 1;
    int cont = 0;
    
    float limite_cheio;
  

    HeapP heap;

    public GarbageCollector(HeapP heap, float lmc) {
        this.heap = heap;
        this.limite_cheio = lmc;
    }

    @Override
    public void run() {
        this.removeParalelo();
    }

    public void removeParalelo() {
        while (ProjetoSOP.flag) {
            if (heap.verificaLimite(limite_cheio)) {
                heap.removeHeap(limite_cheio);
            }
        }
    }
}


