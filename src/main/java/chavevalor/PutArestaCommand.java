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
public class PutArestaCommand implements Command<Boolean>{
    
    public int nomeVertice1;
    public int nomeVertice2;
    public double peso;
    public boolean direcionada;
    public String descricao;
    public int nome;

    public PutArestaCommand(int nomeVertice1, int nomeVertice2, double peso, boolean direcionada, String descricao, int nome) {
        this.nomeVertice1 = nomeVertice1;
        this.nomeVertice2 = nomeVertice2;
        this.peso = peso;
        this.direcionada = direcionada;
        this.descricao = descricao;
        this.nome = nome;
    }
    
}
