/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chavevalor;

import io.atomix.copycat.Command;

/**
 *
 * @author Tiago
 */
public class SetVerticeCommand implements Command<Boolean> {

    public int nome;
    public int cor;
    public String descricao;
    public double peso;

    public SetVerticeCommand(int nome, int cor, String descricao, double peso) {
        this.nome = nome;
        this.cor = cor;
        this.descricao = descricao;
        this.peso = peso;
    }

    public int getNome() {
        return nome;
    }

    public void setNome(int nome) {
        this.nome = nome;
    }

    public int getCor() {
        return cor;
    }

    public void setCor(int cor) {
        this.cor = cor;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public double getPeso() {
        return peso;
    }

    public void setPeso(double peso) {
        this.peso = peso;
    }
    
    

}
