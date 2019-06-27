/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projetoso;

import java.time.Duration;
import java.time.Instant;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.HeapP;
import model.Requisicao;

/**
 *
 * @author gomes
 */
public class PC {

    private FilaCircularP fila;
    private Semaphore mutex;
    private Semaphore empty;
    private Semaphore full;

    private final int TAMANHO = 500;

    private volatile boolean flag;
    private static int tam_pag; //tamanho da pagina em KB
    private static int min, max;//valores minimo e maximo do tamanho da requisicao em KB
    private static int limite_heap; //limite de capacidade do heap
    private static float limite_cheio; //porcentagem da capacidade do heap considerada cheia para executar garbage collector

    private static int qtd_req; //quantidade de requisições que serão geradas ao todo

    public static HeapP heap;
    public static Instant end;
    public static Instant start;

    public class FilaCircularP {

        private int ini, fim;
        private Requisicao[] fila;

        private int tamanhoAtual;

        public FilaCircularP() {
            tamanhoAtual = 0;
            ini = tamanhoAtual;
            fim = tamanhoAtual;
            fila = new Requisicao[TAMANHO];
        }

        public void Inserir(Requisicao nova) {
            fila[fim] = nova;
            fim = (fim + 1) % TAMANHO;
            tamanhoAtual++;
        }

        public Requisicao Remover() {
            Requisicao req = fila[ini];
            ini = (ini + 1) % TAMANHO;
            tamanhoAtual--;
            return req;
        }
    }

    public class Producer extends Thread {

        @Override
        public void run() {
            System.out.println("Buffer rodando...");
            Random rand = new Random();
            Requisicao nova;
            int tam;
            while (flag) {
                tam = rand.nextInt((max - min) + 1) + min;
                nova = new Requisicao(tam, (Instant.now().toEpochMilli() + rand.nextInt(Integer.MAX_VALUE)), tam_pag);
                try {
                    empty.acquire();
                    mutex.acquire();

                    fila.Inserir(nova);

                    mutex.release();
                    full.release();

                } catch (Exception ex) {
                    Logger.getLogger(FilaCircularP.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            System.out.println("Buffer encerrado!!");
        }
    }

    public class Consumer extends Thread {
        public Consumer(){
            this.setName("Consumer");
        }
        @Override
        public void run() {
            System.out.println("Inserir rodando...");
            boolean aux;
            Requisicao req = null;
            int i = 0;
            start = Instant.now();
            while (i < qtd_req) {
                try {
                   
                    full.acquire();
                    mutex.acquire();

                    req = fila.Remover();

                    mutex.release();
                    empty.release();
                    
                    while(!heap.verificaLimiteTotal(req.getQtdpag())){                  
                      // wait();    
                    }
                    heap.insereHeap(req);                                                        
                    i++;  

                } catch (Exception ex) {
                    Logger.getLogger(PC.FilaCircularP.class
                            .getName()).log(Level.SEVERE, null, ex);
                }
//                aux = false;
//                while (!aux) {
                    
                //}
            }
            end = Instant.now();
            flag = false;
            empty.release();
            System.out.println("-----> Acabou requisições");
        }
    }

    public class Cleaner extends Thread {

        @Override
        public void run() {
            System.out.println("Remover rodando...");
            boolean aux = false;
            while (flag) {
                while (!heap.verificaLimite(limite_cheio)) {
                    if (!flag) {
                        aux = true;
                        break;
                    } else {
                        //do nothing
                        
                    }
                }
                if (aux) {
                    break;
                }
                heap.removeHeap(limite_cheio);
            }
            System.out.println("Remove encerrado!!");
        }
    }

    private void execute(Consumer con, Cleaner cl, Producer pr) {
        con.start();
        cl.start();
        pr.start();
        try {
            con.join();
            pr.join();
            cl.join();
        } catch (InterruptedException e) {
            System.err.println("interrupted exception while finishing threads");
        }

        System.out.println("FIM=========================");
        System.out.println("Número de requisições atendidas: " + qtd_req);
        System.out.println("Tamanho de pagina em KB:" + tam_pag);
        System.out.println("Nº de páginas no heap: " + limite_heap);
        System.out.println("Intervalo em KB definido: " + min + " a " + max);
        System.out.println("Limite de armazenamento para executar limpeza: " + limite_cheio);
        System.out.println("Tempo necessário para atender às requisições: " + (Duration.between(start, end).toMillis()) + " milisegundos.");

    }

    public PC() {
        fila = new FilaCircularP();
        empty = new Semaphore(TAMANHO);//producer sleeping
        mutex = new Semaphore(1);
        full = new Semaphore(0); //consumer sleeping
        flag = true;

        Consumer con = new Consumer();
        Cleaner cl = new Cleaner();
        Producer pr = new Producer();

        execute(con, cl, pr);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);

        System.out.println("Projeto SO - Implementação paralela");
        System.out.println("Informa o tamanho de página em KB:");
        tam_pag = in.nextInt();
        System.out.println("Informe os valores mínimo e máximo para o tamanho das requisições(em KB):");
        min = in.nextInt();
        max = in.nextInt();
        System.out.println("Informe o limite de capacidade do heap em paginas:");
        limite_heap = in.nextInt();
        System.out.println("Informe o percentual limite para o heap estar cheio: ");
        limite_cheio = in.nextFloat();
        System.out.println("Informe a quantidade de requisições a serem geradas(total):");
        qtd_req = in.nextInt();

        heap = new HeapP(limite_heap, tam_pag);

        PC pc = new PC();
    }
}
