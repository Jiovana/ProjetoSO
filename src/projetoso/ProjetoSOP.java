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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import model.FilaCircular;
import model.Heap;
import model.HeapP;
import model.Requisicao;

/**
 *
 * @author gomes
 */
public class ProjetoSOP {

    static volatile boolean flag = true;
    static volatile FilaCircular fila = new FilaCircular();

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        int tam_pag; //tamanho da pagina em KB
        int min, max;//valores minimo e maximo do tamanho da requisicao em KB
        int limite_heap; //limite de capacidade do heap
        float limite_cheio; //porcentagem da capacidade do heap considerada cheia para executar garbage collector
        int num_req_aloc; // numero de requisições de alocação atendidas pelo gerenciador
        int qtd_req; //quantidade de requisições que serão geradas ao todo
        int num_req;//numero de requisições geradas por ciclo

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
        System.out.println("Informe o número de requisições a serem atendidas pelo gerenciador de memória: ");
        num_req_aloc = in.nextInt();
        System.out.println("Informe o número de requisições geradas por ciclo para o buffer ");
        num_req = in.nextInt();
        System.out.println("Informe a quantidade de requisições a serem geradas(total):");
        qtd_req = in.nextInt();

        HeapP heap = new HeapP(limite_heap, tam_pag);

        Instant end;

        Buffer buf = new Buffer(max, min, tam_pag, num_req);
        InserirP ins = new InserirP(heap, qtd_req);
        GarbageCollector gb = new GarbageCollector(heap, limite_cheio);

        Instant start = Instant.now();
        buf.start();
        ins.start();
        gb.start();
        end = Instant.now();
        
//        for (int i = 0; i < qtd_req; i++) {
//            System.out.println("Requisição"+i);
//            Thread t = new InserirP(heap, filaReq.Remover(),qtd_req );
//            exec.execute(t);
//        }
//        exec.shutdown();       
//        while (!exec.isTerminated()) {
//        }
//        if (exec.isTerminated()) {
//        	 System.out.println("Finished all threads");
//        }
        
        System.out.println("FIM=========================");
        System.out.println("Número de requisições atendidas: " + qtd_req);
        System.out.println("Tamanho de pagina em KB:" + tam_pag);
        System.out.println("Nº de páginas no heap: " + limite_heap);
        System.out.println("Nº de requisições atendidas por ciclo: " + num_req_aloc);
        System.out.println("Nº de requisições geradas no buffer por ciclo: " + num_req);
        System.out.println("Intervalo em KB definido: " + min + " a " + max);
        System.out.println("Limite de armazenamento para executar limpeza: " + limite_cheio);
        System.out.println("Tempo necessário para atender às requisições: " + (Duration.between(start, end).getSeconds()) + " segundos.");

    }

}
