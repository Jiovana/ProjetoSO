/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author gomes
 */
public class Requisicao {

    private long id;
    private int qtdpag; //quantidade de paginas calculada de acordo com limite   
    private int tamKB;

    //tamanho limite do vetor objeto
    public Requisicao(int tamanho, long id, int tam_pag) {
        this.id = id;     
        this.tamKB = tamanho;
       
        this.qtdpag = tamanho/tam_pag;//vai calcular quantas paginas vai precisar de acordo com o tamanho de pag
        if(tamanho%tam_pag!=0) //se o tamanho total for impar vai dar valor com virgula e tem q add + 1 pagina
            this.qtdpag++;
        else if(this.qtdpag < 1) //se ele for mto pequeno tem q deixar 1 pagina pelo menos
            this.qtdpag = 1;
    }

    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }

    public int getQtdpag() {
        return qtdpag;
    }
    public void setQtdpag(int qtdpag) {
        this.qtdpag = qtdpag;
    }

    public int getTamKB() {
        return tamKB;
    }

    public void setTamKB(int tamKB) {
        this.tamKB = tamKB;
    }
    
}
