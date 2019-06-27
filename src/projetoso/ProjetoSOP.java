/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projetoso;

import java.time.Duration;
import java.time.Instant;
import java.util.Scanner;
//import model.FilaCircularP;
import model.HeapP;

/**
 *
 * @author gomes
 */
public class ProjetoSOP {

    public static int tam_pag; //tamanho da pagina em KB
    public static int min, max;//valores minimo e maximo do tamanho da requisicao em KB
    public static int limite_heap; //limite de capacidade do heap
    public static float limite_cheio; //porcentagem da capacidade do heap considerada cheia para executar garbage collector

    public static int qtd_req; //quantidade de requisições que serão geradas ao todo

    public static HeapP heap;
    public static Instant end;
    public static boolean flag;
    public static Instant start;

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
        //FilaCircularP fila = new FilaCircularP();
        flag = true;

//        Producer pd = new Producer();
//        Consumer cn1 = new Consumer();
//        Cleaner cl = new Cleaner();
//
//        pd.start();
//        cn1.start();
//        cl.start();
//
//        try {
//            pd.join();
//            cn1.join();
//            cl.join();
//        } catch (InterruptedException e) {
//            System.err.println("interrupted exception while finishing threads");
//        }

        System.out.println("FIM=========================");
        System.out.println("Número de requisições atendidas: " + qtd_req);
        System.out.println("Tamanho de pagina em KB:" + tam_pag);
        System.out.println("Nº de páginas no heap: " + limite_heap);
        System.out.println("Intervalo em KB definido: " + min + " a " + max);
        System.out.println("Limite de armazenamento para executar limpeza: " + limite_cheio);
        System.out.println("Tempo necessário para atender às requisições: " + (Duration.between(start, end).toMillis()) + " milisegundos.");
    }

}
