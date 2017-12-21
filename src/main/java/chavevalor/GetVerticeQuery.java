/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chavevalor;

import io.atomix.copycat.Query;

/**
 *
 * @author Tiago
 */
public class GetVerticeQuery implements Query<Vertice>{
    
    int nome;

    public GetVerticeQuery(int nome) {
        this.nome = nome;
    }
    
}
