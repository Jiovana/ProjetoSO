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
import model.FilaCircular;
import model.Heap;
import model.Requisicao;

/**
 *
 * @author gomes
 */
public class ProjetoSO {

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

        System.out.println("Projeto SO - Implementação sequencial");
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

        Random rand = new Random();

        FilaCircular filaReq = new FilaCircular(); //buffer circular de requisições 

        Heap heap = new Heap(limite_heap,tam_pag);

        
        long id_req = 1;
        int aux = num_req_aloc;
        int cont = 0;
        Requisicao rx;
        Requisicao nova;
        Instant end;
        Instant start = Instant.now();
        do {
            //------inicio gerar requisições
            int numreq = rand.nextInt(num_req); //num de req geradas por vez
            do { //loop para inserir 'numreq' requisições no buffer, de forma de a quantidade de requisições inseridas é aleatoria
                int tamanho = rand.nextInt((max - min) + 1) + min;
                nova = new Requisicao(tamanho, (Instant.now().toEpochMilli()+rand.nextInt(Integer.MAX_VALUE)),tam_pag);
                filaReq.Inserir(nova);
                numreq--;
            } while (numreq > 0);
            //------fim gerar requisições
            do { //loop de inserir e remover
                rx = filaReq.Remover();
                if (rx == null) {
                    break;
                }else if(id_req >= qtd_req){//tentativa de fazer com que pare de executar ao atingir o limite de requisições informado
                    break; //mas sempre acaba passando 
                } else {
                    if (!heap.verificaLimite(limite_cheio)) { //testa o limite para ver se precisa limpar
                        heap.insereHeap(rx);//----inserção no heap, 1 req por vez dentro do loop.           
                        heap.printLista();
                        aux--;
                        cont++;
                    } else {//'garbage collector' - elimina 25% do limite máximo estabelecido (possui loop interno)          
                        heap.removeHeap(limite_cheio);
                    }
                }
            } while (aux > 0);
            aux = num_req_aloc;
           
            //heap.printEstado();           
        } while (cont <= qtd_req);//compara tempo de execução do programa com o tempo definido pelo usuario       
        end = Instant.now();
        
        System.out.println("FIM=========================");
        System.out.println("Número de requisições atendidas: " +cont); 
        System.out.println("Tamanho de pagina em KB:" +tam_pag);
        System.out.println("Nº de páginas no heap: " +limite_heap);
        System.out.println("Nº de requisições atendidas por ciclo: " +num_req_aloc);
        System.out.println("Nº de requisições geradas no buffer por ciclo: " +num_req);
        System.out.println("Intervalo em KB definido: " +min +" a " +max);
        System.out.println("Limite de armazenamento para executar limpeza: " +limite_cheio);
        System.out.println("Tempo necessário para atender às requisições: " +(Duration.between(start, end).toMillis()) +" milisegundos." );
    }
   
}
    

