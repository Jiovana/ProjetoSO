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
public class Tupla {
    private Long id_req;
    private Integer pont_ult;
    private Integer tamanho;

    public Tupla(Long id_req, Integer pont_ult, Integer tamanho) {
        this.id_req = id_req;
        this.pont_ult = pont_ult;
        this.tamanho = tamanho;
    }

    public Long getId_req() {
        return id_req;
    }

    public void setId_req(Long id_req) {
        this.id_req = id_req;
    }

    public Integer getPont_ult() {
        return pont_ult;
    }

    public void setPont_ult(Integer pont_ult) {
        this.pont_ult = pont_ult;
    }

    public Integer getTamanho() {
        return tamanho;
    }

    public void setTamanho(Integer tamanho) {
        this.tamanho = tamanho;
    }

    @Override
    public String toString() {
        return "Tupla{" + "id_req=" + id_req + ", pont_ult=" + pont_ult + ", tamanho=" + tamanho + '}';
    }
    
    
    
}
