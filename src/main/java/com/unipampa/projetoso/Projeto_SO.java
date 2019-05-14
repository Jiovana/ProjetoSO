/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unipampa.projetoso;

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
public class Projeto_SO {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        int min, max;//valores minimo e maximo do tamanho da requisicao
        int limite_heap; //limite de capacidade do heap
        float limite_cheio; //porcentagem da capacidade do heap considerada cheia para executar garbage collector
        int num_req_aloc; // numero de requisições de alocação atendidas pelo gerenciador
        int tempo; //tempo de duração do programa em segundos

        Scanner in = new Scanner(System.in);

        System.out.println("Projeto SO - Implementação sequencial");
        System.out.println("Regras de execução: ");
        System.out.println("Este programa trabalha usando a medida de um vetor int[256] para formar 1Kbyte. ");
        System.out.println("Informe os valores mínimo e máximo para o tamanho das requisições(em nº de páginas): ");
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
        System.out.println("Informe o tempo de execução do programa, em segundos:");
        tempo = in.nextInt();

        Random rand = new Random();

        FilaCircular filaReq = new FilaCircular(); //buffer circular de requisições 

        Heap heap = new Heap(limite_heap);
        
        int maxx = max*256;//multiplca pra achar valor em 1kbs
        int minn = min*256;

        long id_req = 0;
        int aux = num_req_aloc;
       
        Requisicao rx ;
        Instant end;
        Instant start = Instant.now();       
        do {
            //------inicio gerar requisições
            int numreq = rand.nextInt(50); //num de req geradas por vez
            do { //loop para inserir 'numreq' requisições no buffer, de forma de a quantidade de requisições inseridas é aleatoria
                int tamanho = rand.nextInt((maxx - minn) + 1) + minn;
                Requisicao nova = new Requisicao(tamanho,id_req++);
                filaReq.Inserir(nova);
                numreq--;
            } while (numreq > 0);
            //------fim gerar requisições
            //---- inicio inserção no heap, 1 req por vez dentro do loop.           
            do {
                rx = filaReq.Remover();
                if (rx == null) {
                    break;
                }else{
                    if(heap.isLimt()==true){
                     heap.removeHeap(limite_cheio);
                    } else {
                    heap.insereHeap(rx,limite_cheio);
                    aux--;
                    }
                }
            } while (aux > 0);
            aux = num_req_aloc; //mudar logica de remoção pra ficar certo
            //----- fim inserção no heap           
            //----- inicio 'garbage collector' - elimina 25% do limite máximo estabelecido (possui loop interno)
            //heap.removeHeap(limite_cheio);
            //----- fim 'garbage collector'
            end = Instant.now();
        } while ((Duration.between(start, end).getSeconds()) < tempo);//compara tempo de execução do programa com o tempo definido pelo usuario       
    }
}
