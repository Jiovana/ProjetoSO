/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;

/**
 *
 * @author gomes
 */
public class HeapP {

    //memoria com paginas de tamanho 1kbyte 
    static volatile Pagina[] heap; // heap é um vetor de paginas de tamanho informado pelo user
    static volatile int cont_pag_livre; //conta quantas paginas tem livres no heap
    static volatile List<Tupla> lista;

    public HeapP(int limite_heap, int tam_pag) {
        heap = new Pagina[limite_heap];
        for (int i = 0; i < heap.length; i++) {
            heap[i] = new Pagina(Integer.MIN_VALUE, false, tam_pag, Integer.MIN_VALUE);
        }
        cont_pag_livre = heap.length;

        lista = Collections.synchronizedList(new ArrayList<>());
    }

    public synchronized void incContPagLivre() {
        cont_pag_livre++;
    }

    public synchronized boolean verificaLimite(float limite_cheio) {
        float limite = (heap.length * (limite_cheio / 100));
        if ((heap.length - cont_pag_livre) >= (int) limite) {
            System.out.println("Precisa limpar");
            return true;
        } else {
            return false;
        }
    }

    public synchronized boolean ehVazia(Requisicao req) {
        if (req == null) {
            notify();
            try {
                System.out.println("entrou em ehvazia");
                wait();
            } catch (Exception e) {
                System.out.println("Wait Failed!");
            }
            return true;
        } else {
            return false;
        }

    }

    public synchronized boolean verificaLimiteTotal(int qtd_pag) {
        if (qtd_pag < cont_pag_livre) { //então tem paginas livres o suficiente para requisiçao
            cont_pag_livre -= qtd_pag;
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
        int qtdpag = req.getQtdpag(); //pega quantidade de paginas da requisição
        System.out.print("---> Requisição nº " + req.getId());
        System.out.println(" com " + req.getQtdpag() + " paginas <---");
        Random rand = new Random();
        int pos = -1;
        int id_ant = -1;// posicao no heap da pagina anterior a essa atual, se -1 significa q esta na primeira pagina
        Tupla tp;
        //if (cont_pag_livre >= qtdpag) { //se tenho paginas livres o suficiente para a requisicao
        while (qtdpag > 0) {
            pos = rand.nextInt(heap.length); //sortear uma posição do vetor e checar se ela está livre
            synchronized (heap[pos]) {
                if (qtdpag > 0 && heap[pos].isOcupado() == false) { //se ainda tem pag pra inserir e posicao livre
                    heap[pos].setEndereço(req.getId()); // coloca id da requisição dentro da pagina
                    heap[pos].setOcupado(true); // diz que pagina esta ocupada
                    heap[pos].setId_ant(id_ant); // coloca ponteiro para pagina inserida antes dessa
                    qtdpag--;
                    id_ant = pos;//define o id da anterior como a posicao atual para a proxima pagina usar
                    System.out.println("pagina da requisicao inserida!");
                }
            }
        }
        tp = new Tupla(req.getId(), pos, req.getTamKB());
        //synchronized (lista) {
            lista.add(tp); //insere na lista o id, o ponteiro p ultima pag, e o tam em KB da req.
            lista.sort(Collections.reverseOrder(Comparator.comparing(t -> t.getTamanho()))); //ordena em ordem decrescente pelo tamanho
        //}
        System.out.println("A requisição foi inserida com sucesso");
    }

    /**
     *
     * @param limite_cheio é o limite em % de quando o heap é considerado cheio
     * removeHeap vai remover 25% da quantidade limite usando os ids contidos na
     * fila ids, de forma que são excluídas as páginas com ids mais velhos.
     */
    public void removeHeap(float limite_cheio) {
        Tupla tp;
        int cont_pag, pont, ant;
        float limitesup = (heap.length * (limite_cheio / 100));
        float limiteinf = (float) (limitesup * 0.5);
        System.out.println("Limite inferior do heap: " + limiteinf);
        int qtd_retirar = (int) (limitesup - limiteinf); //qtd necessaria de paginas para retirar e chegar a 25% abaixo do limite estabelecido
        System.out.println("Quantidade necessaria para retirar: " + qtd_retirar);
        int qtd_retirada = 0;
        do { //vai remover pagina até alcançar a quantidade de paginas necessaria
            if (lista.isEmpty()) {
                break;
            }
            cont_pag = 0; //contador de paginas retiradas da requisicao atual
            ant = 0; //armazena ponteiro para pagina anterior
           // synchronized (lista) {
                tp = lista.remove(0);//remove primeira posição da lista de req, ou seja, a req inserida de maior tamanho
            //}
            pont = tp.getPont_ult(); // pega ponteiro para ultima pagina inserida dessa requisicao              
            while (ant != -1) { //remove até tirar todas pags da requisição da memoria
                synchronized (this.heap[pont]) {
                    if (heap[pont].getEndereço() == tp.getId_req()) { //testa se o endereço daquela posicao é realmente o da req selecionada
                        System.out.println("Endereços iguais: a posicao esta correta!");
                        ant = heap[pont].getId_ant();//pega endereço para pag anterior                 
                        heap[pont].setEndereço(-1);//seta endereço da pag para invalido
                        heap[pont].setOcupado(false); //seta q n esta ocupado
                        heap[pont].setId_ant(-1); //seta ponteiro como invalido   
                        pont = ant; //define q ponteiro atual é igual ao anterior, para prox vez ir para trás
                        //cont_pag_livre++;
                        this.incContPagLivre();
                        cont_pag++;
                        System.out.println("Página da req" + tp.getId_req() + " removida do heap.");
                    } else {
                        System.out.println("Erro: posicao errada salva!!");
                    }
                }
            }
            qtd_retirada += cont_pag; //incrementa quantidade total retirada    
            System.out.println("Quantidade retirada:" + qtd_retirada);
            if (qtd_retirada >= qtd_retirar) {
                break;
            }
        } while (qtd_retirada <= qtd_retirar); //compara se já retirou o suficiente
    }

    public void printEstado() {
        int cont = 0;
        System.out.println("===================================================");
        for (Pagina heap1 : heap) {
            String linha = "";
            if (heap1.isOcupado()) {
                linha = cont + "[ X ]";
            } else {
                linha = cont + "[ - ]";
            }
            System.out.println(linha);
            cont++;
        }
        System.out.println("===================================================");
    }

    public void printLista() {
        Object[] tp = lista.toArray();
        for (int i = 0; i < tp.length; i++) {
            System.out.println(tp[i]);
        }
    }
}
