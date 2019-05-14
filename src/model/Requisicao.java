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
    private int[] objeto; //dado dentro da pagina
    private int qtdpag; //quantidade de paginas calculada de acordo com limite   
    private Pagina[] paginas;

    //tamanho limite do vetor objeto
    public Requisicao(int tamanho, long id) {
        this.id = id;
        this.objeto = new int[256]; //tamanho de  1 pagina
        for (int i = 0; i < 256; i++){ //preenchendo vetor objeto com quantidade fixa de info pq n faz diferença
            this.objeto[i] = i;
        }
        this.qtdpag = tamanho;
        this.paginas = new Pagina[qtdpag]; //vetor de paginas com tamanho de acordo com quantidade de paginas        
        for (int i = 0; i < qtdpag; i++) { //vai preencher as paginas da requisicao com o endereço da req e dado objeto
            this.paginas[i] = new Pagina(this.id, false);
            this.paginas[i].setDado(objeto); //colocando dado dentro da pagina i          
        }
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

    public Pagina[] getPaginas() {
        return paginas;
    }
    public void setPaginas(Pagina[] paginas) {
        this.paginas = paginas;
    }

    public int[] getObjeto() {
        return objeto;
    }
    public void setObjeto(int[] objeto) {
        this.objeto = objeto;
    }
}
