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
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.FilaCircularP;
import model.HeapP;
import model.Requisicao;

/**
 *
 * @author gomes
 */
public class ProjetoSOP {

    static FilaCircularP fila = new FilaCircularP();
    static int tam_pag; //tamanho da pagina em KB
    static int min, max;//valores minimo e maximo do tamanho da requisicao em KB
    static int limite_heap; //limite de capacidade do heap
    static float limite_cheio; //porcentagem da capacidade do heap considerada cheia para executar garbage collector

    static int qtd_req; //quantidade de requisições que serão geradas ao todo

    static HeapP heap;
    static Instant end;

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
        if (min <= 0) {
            while (min <= 0) {
                System.out.println("por favor informe um valor maior que zero para o minimo!!:");
                min = in.nextInt();
            }
        }
        max = in.nextInt();
        if (max > Integer.MAX_VALUE) {
            while (max > Integer.MAX_VALUE) {
                System.out.println("por favor informe um valor menor que o limite para int!!:");
                max = in.nextInt();
            }
        }
        System.out.println("Informe o limite de capacidade do heap em paginas:");
        limite_heap = in.nextInt();
        System.out.println("Informe o percentual limite para o heap estar cheio: ");
        limite_cheio = in.nextFloat();

        System.out.println("Informe a quantidade de requisições a serem geradas(total):");
        qtd_req = in.nextInt();

        heap = new HeapP(limite_heap, tam_pag);

        final PC pc = new PC();

        Thread buf1 = new Thread(() -> {
            pc.produce();
        });
//        Thread buf2 = new Thread(() -> {
//            pc.produce();
//        });

        Thread ins = new Thread(() -> {
            pc.consume();
        });

        Thread gb = new Thread(() -> {
            pc.remove();
        });

        Instant start = Instant.now();
        buf1.start();
        //buf2.start();
        ins.start();
        gb.start();

        try {
            buf1.join();
            //buf2.join();
            gb.join();
            ins.join();
            System.out.println("FIM=========================");
            System.out.println("Número de requisições atendidas: " + qtd_req);
            System.out.println("Tamanho de pagina em KB:" + tam_pag);
            System.out.println("Nº de páginas no heap: " + limite_heap);
            System.out.println("Intervalo em KB definido: " + min + " a " + max);
            System.out.println("Limite de armazenamento para executar limpeza: " + limite_cheio);
            System.out.println("Tempo necessário para atender às requisições: " + (Duration.between(start, end).toMillis()) + " milisegundos.");
        } catch (InterruptedException ex) {
            Logger.getLogger(ProjetoSOP.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static class PC {

        volatile boolean flag = true;
        Lock lock = new ReentrantLock();
        Condition podeBuffar = lock.newCondition();
        Condition podeInserir = lock.newCondition();
        Random rand = new Random();
        Requisicao nova, req;

        public void produce() {
            System.out.println("Buffer rodando...");
            boolean aux = false;
            while (flag) {
                int tamanho = rand.nextInt((ProjetoSOP.max - ProjetoSOP.min) + 1) + ProjetoSOP.min;
                this.nova = new Requisicao(tamanho, (Instant.now().toEpochMilli() + rand.nextInt(Integer.MAX_VALUE)), ProjetoSOP.tam_pag);
                //try {
//                    // synchronized (this) {
//                    while (ProjetoSOP.fila.getTamanhoAtual() == 500) {
//                        // System.out.println("Buffer cheio. Esperando consumidor");
//                        if (!flag) {
//                            aux = true;
//                            break;
//                        } else {
//                            wait(); //espera enquanto buffer estiver cheio
//                        }
//                    }
                // System.out.println("Buffer produziu req: " + nova.getId());
                while (!aux) {
                    if (!flag) {
                        break;
                    }
                    aux = ProjetoSOP.fila.Inserir(nova, flag);
                }
                //notify(); //notifica consumidor para acordar 
                if (!flag) {
                    break;
                }
//                } catch (InterruptedException exception) {
//                    exception.printStackTrace();
//                }
            }
            System.out.println("Buffer encerrado!!");
        }

        public void consume() {
            System.out.println("Inserir rodando...");
            boolean aux = false;
            int i = 0;
            while (i < ProjetoSOP.qtd_req) {
                //try {
//                    while (ProjetoSOP.fila.getTamanhoAtual() == 0) {
//                        //System.out.println("Buffer vazio. Esperando produtor");
//                        wait();
//                    }
                while (req == null) {
                    req = ProjetoSOP.fila.Remover(); // e se estiver vazia aqui ein?
                }
                //System.out.println("Consumidor pegou req: " + req.getId()); 
                aux = false;
                while (!aux) {
                    if (ProjetoSOP.heap.verificaLimiteTotal(req.getQtdpag())) {
                        // System.out.println("Heap Cheio. Esperando para inserir");
                        ProjetoSOP.heap.insereHeap(req);//----inserção no heap, 1 req por vez dentro do loop.                                                         
                        //ProjetoSOP.heap.printLista();
                        i++;
                        aux = true;
                    }
                }
                //System.out.println("Requisicao numero: " + i);
                //notify();
                //     }
//                } catch (InterruptedException exception) {
//                    exception.printStackTrace();
//                }
            }
            flag = false; //diz a main que acabou as requisições
            System.out.println("-----> Acabou requisições");
            end = Instant.now();
        }

        public void remove() {
            System.out.println("Remover rodando...");
            boolean aux = false;
            while (flag) {
                //lock.lock();
                //try {
                while (!ProjetoSOP.heap.verificaLimite(ProjetoSOP.limite_cheio)) {
                    if (!flag) {
                        aux = true;
                        break;
                    } else {
                        // System.out.println("Esperando para limpar");
                        // podeLimpar.await();
                    }
                }
                if (aux) {
                    break;
                }
                //System.out.println("Removendo...");
                ProjetoSOP.heap.removeHeap(ProjetoSOP.limite_cheio);
//                podeInserir.signal();
                //}
//                } catch (InterruptedException exception) {
//                    exception.printStackTrace();
//                } finally {
//                    lock.unlock(); // unlock this object
//                } // end finally

            }
            System.out.println("Remove encerrado!!");
        }

    }
}
