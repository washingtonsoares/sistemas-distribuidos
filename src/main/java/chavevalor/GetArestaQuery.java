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
public class GetArestaQuery implements Query<Aresta> {

    int nome;

    public GetArestaQuery(int nome) {
        this.nome = nome;
    }

}
