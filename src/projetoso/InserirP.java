/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projetoso;

import java.time.Instant;
import java.util.Random;
import model.FilaCircular;
import model.Heap;
import model.HeapP;
import model.Requisicao;

/**
 *
 * @author gomes
 */
public class InserirP extends Thread {

    long id_req = 1;
    int cont = 0;
    Requisicao req;
    int qtd_req;
    HeapP heap;

    public InserirP(HeapP heap, int qtd_req) {
        this.heap = heap;
        this.qtd_req = qtd_req;
    }

    @Override
    public void run() {
        this.insereParalelo();
    }

    public void insereParalelo() {
        for (int i = 0; i < this.qtd_req; i++) { //loop até inserir todas as requisições
            this.req = ProjetoSOP.fila.Remover(); // e se estiver vaiza aqui ein?
            if (!heap.ehVazia(this.req)) { // testa se n é vazia
                if (heap.verificaLimiteTotal(req.getQtdpag())) { //testa o limite para ver se precisa limpar
                    heap.insereHeap(req);//----inserção no heap, 1 req por vez dentro do loop.           
                    heap.printLista();
                }
            }
        }
        ProjetoSOP.flag = false;
    }
}
