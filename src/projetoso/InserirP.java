/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projetoso;

import java.time.Instant;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
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
        System.out.println("rodando inserir");
        insereParalelo();
    }

    public synchronized void insereParalelo() {
        while (true) {
            // synchronized (ProjetoSOP.fila) {
            for (int i = 0; i < this.qtd_req; i++) { //loop até inserir todas as requisições
                while (ProjetoSOP.fila.getTamanhoAtual() == 0) {
                    System.out.println("buffer vazio!");
                    notify();
                    try {
                        System.out.println("inserir esperando...");
                        wait();
                    } catch (InterruptedException ex) {
                        Logger.getLogger(InserirP.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                this.req = ProjetoSOP.fila.Remover(); // e se estiver vaiza aqui ein?
                //if (!heap.ehVazia(this.req)) { // testa se n é vazia
                while (!heap.verificaLimiteTotal(req.getQtdpag())) {
                    notify();
                    try {
                        System.out.println("heap cheio, inserir esperando limpar...");
                        wait();
                    } catch (Exception e) {
                        System.out.println("Wait Failed!");
                    }
                }
                heap.insereHeap(req);//----inserção no heap, 1 req por vez dentro do loop.           
                heap.printLista();
                notifyAll();
                
            }
            ProjetoSOP.flag = false;
            //}
        }
    }
}
