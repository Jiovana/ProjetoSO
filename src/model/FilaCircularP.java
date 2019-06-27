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
public class FilaCircularP {

    private  int ini, fim;
    private static volatile Requisicao[] fila;
    private final int tamanho = 500;
    private volatile int tamanhoAtual;

    public FilaCircularP() {
        tamanhoAtual = 0;
        ini = tamanhoAtual;
        fim = tamanhoAtual;
        fila = new Requisicao[tamanho];
        for (int i = 0; i < fila.length; i++) {
            fila[i] = new Requisicao(-1, -1, -1);
        }
    }

    public boolean Inserir(Requisicao nova, boolean flag) {
        //if (flag) {
            // try {
            //synchronized (fila[fim]) {
                if (tamanhoAtual < tamanho) { //n tem espaço                   
                    fila[fim] = nova;
                    fim = (fim + 1) % tamanho;
                    tamanhoAtual++;
                    //notify(); //notifica consumidor para acordar 
                    return true;
                }
            //}
//            } catch (InterruptedException exception) {
//                exception.printStackTrace();
//            }
        //}
        return false;
    }

    public Requisicao Remover() {
        Requisicao req = null;
        //try {
            //synchronized (fila[ini]) {
                if (tamanhoAtual > 0) {
                    req = fila[ini];
                    ini = (ini + 1) % tamanho;
                    tamanhoAtual--;
                   // notify();
                }
           // }
//        } catch (InterruptedException exception) {
//            exception.printStackTrace();
//        }
        return req;
    }

    public int getIni() {
        return ini;
    }

    public void setIni(int ini) {
        this.ini = ini;
    }

    public int getFim() {
        return fim;
    }

    public void setFim(int fim) {
        this.fim = fim;
    }

    public synchronized int getTamanhoAtual() {
        return tamanhoAtual;
    }

    public void setTamanhoAtual(int tamanhoAtual) {
        this.tamanhoAtual = tamanhoAtual;
    }
}
