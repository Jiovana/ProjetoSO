/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

/**
 *
 * @author gomes
 */
public class Heap {

    //memoria com paginas de tamanho 1kbyte 
    private Pagina[] heap; // heap é um vetor de paginas de tamanho informado pelo user
    int cont_pag_livre; //conta quantas paginas tem livres no heap
    private Queue<Long> ids = new LinkedList<>();
    

    public Heap(int limite_heap) {
        this.heap = new Pagina[limite_heap];
        for (int i = 0; i < heap.length; i++) {
            this.heap[i] = new Pagina(0, false);
        }
        this.cont_pag_livre = heap.length;
    }

    public boolean verificaLimite(float limite_cheio) {
        float limite = (heap.length * (limite_cheio / 100));
        if ((heap.length - cont_pag_livre) >= (int) limite) {
            System.out.println("Precisa limpar");
            return true;
        } else {
            return false;
        }
    }

    /**
     *
     * @param req insereHeap insere uma requisição com x paginas e a insere no
     * vetor heap se o heap estiver a metade ou menos de pags ocupadas, a
     * posição de inserção é aleatória, se não, a posição sera encontrada por
     * busca sequencial por meio do identificador de ocupado para achar paginas
     * livres.
     */
    public void insereHeap(Requisicao req) {
        int qtdpag = req.getQtdpag();
        System.out.print("---> Requisição nº " + req.getId());
        System.out.println(" com " + req.getQtdpag() + " paginas <---");
        Random rand = new Random();
        int pos;
        if (cont_pag_livre >= qtdpag) { //se tenho paginas livres o suficiente para a requisicao
            do {
//                if (cont_pag_livre >= (heap.length*0.6)) { //se tem menos de 50% do heap ocupado, insere de forma aleatoria
                pos = rand.nextInt(this.heap.length); //sortear uma posição do vetor e checar se ela está livre
                if (qtdpag > 0 && this.heap[pos].isOcupado() == false) { //se ainda tem pag pra inserir e posicao livre
                    this.heap[pos] = req.getPaginas()[qtdpag - 1]; // [e assim para ser decrescente como no if externo
                    this.heap[pos].setOcupado(true);

                    cont_pag_livre--;
                    qtdpag--;
                    System.out.println("pagina da requisicao inserida!");
                }
//                } else { //se está mais de 50% ocupado, procura proxima sequencial
//                    for (int i = 0; i < this.heap.length; i++) {
//                        if (qtdpag > 0 && this.heap[i].isOcupado() == false) {
//                            this.heap[i] = req.getPaginas()[qtdpag - 1];
//                            this.heap[i].setOcupado(true);
//                           
//                            cont_pag_livre--;
//                            qtdpag--;
//                            System.out.println("pagina da requisicao inserida!");
//                        }
//                    }
//                }
            } while (qtdpag > 0);
            ids.add(req.getPaginas()[0].getEndereço());
            ids.add((long) req.getQtdpag());
            System.out.println("A requisição foi inserida com sucesso");
        } else {
            System.out.println("Não há espaço disponível para inserir essa requisição!");
        }
    }

    /**
     *
     * @param limite_cheio é o limite em % de quando o heap é considerado cheio
     * removeHeap vai remover 25% da quantidade limite usando os ids contidos na
     * fila ids, de forma que são excluídas as páginas com ids mais velhos.
     */
    public void removeHeap(float limite_cheio) {
        long id, qtd;
        int aux;
        float limite = (heap.length * (limite_cheio / 100));
        System.out.println("Limite do heap: " + limite);
        if ((heap.length - cont_pag_livre) >= (int) limite) {
            int qtd_retirar = (int) (limite * 0.5); //qtd necessaria de paginas para retirar e chegar a 25% abaixo do limite estabelecido
            System.out.println("Quantidade necessaria para retirar: " + qtd_retirar);
            int qtd_retirada = 0;
            do { //vai remover ids até alcançar a quantidade de paginas necessaria
                if (ids.isEmpty())
                    break;
                aux = 0;
                id = ids.remove(); //primeira remoção id//ATENÇAO: as paginas que sobrarem quando chegar no limite de remoção perderão referencia para pilha de exclusão! Consequencia: ocupando espaço no heap para sempre!               
                qtd = ids.remove(); //segunda remoção quantidade de pag SOLUCIONADO TALVEZ
                for (Pagina heap1 : heap) {
                    //percorre heap procurando id
                    if (heap1.getEndereço() == id) {//se acha id na posição, apaga conteudo da pag e diz q esta livre                     
                        heap1.setDado(null);
                        heap1.setOcupado(false);
                        heap1.setEndereço(-1);
                        cont_pag_livre++;
                        aux++;
                        System.out.println("Página removida do heap.");
                    }
                    if (aux == qtd)//chegou no limite de pags da requisicao, pode sair
                        break;
                }
                qtd_retirada += aux;
                System.out.println("Quantidade retirada:" + qtd_retirada);
                if (qtd_retirada >= qtd_retirar)
                    break;
            } while (qtd_retirada <= qtd_retirar);
        } else {
            System.out.println("---> Nada a ser removido!!");
        }
    }

    public void printEstado() {
        System.out.println("===================================================");
        for (Pagina heap1 : heap) {
            String linha = "";
            for (int cont = 0; cont < 25; cont++) {
                if (heap1.isOcupado()) 
                    linha = linha + "[ X ]";
                else 
                    linha = linha + "[ - ]";                
            }
            System.out.println(linha);
        }
        System.out.println("===================================================");
    }
}
