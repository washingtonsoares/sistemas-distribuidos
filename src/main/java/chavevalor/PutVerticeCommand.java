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
public class PutVerticeCommand implements Command<Boolean>{
    
    public int nome;
    public int cor;
    public String descricao;
    public double peso;

    public PutVerticeCommand(int nome, int cor, String descricao, double peso) {
        this.nome = nome;
        this.cor = cor;
        this.descricao = descricao;
        this.peso = peso;
    }
    
}
