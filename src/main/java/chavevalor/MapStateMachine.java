/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chavevalor;

import io.atomix.catalyst.transport.Address;
import io.atomix.catalyst.transport.netty.NettyTransport;
import io.atomix.copycat.server.Commit;
import io.atomix.copycat.server.CopycatServer;
import io.atomix.copycat.server.StateMachine;
import io.atomix.copycat.server.storage.Storage;
import io.atomix.copycat.server.storage.StorageLevel;
import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Tiago
 */
public class MapStateMachine extends StateMachine {

    /**
     * @param args the command line arguments
     *
     */
    Map<Integer, Vertice> vertices = new HashMap<>();
    Map<Integer, Aresta> arestas = new HashMap<>();

    public static void main(String[] args) {
        // TODO code application logic here

        int id = Integer.parseInt(args[0]);
        List<Address> addresses = new LinkedList<>();

        for (int i = 1; i < args.length; i += 2) {
            Address address = new Address(args[i], Integer.parseInt(args[i + 1]));
            addresses.add(address);
        }

        CopycatServer.Builder builder = CopycatServer.builder(addresses.get(id))
                .withStateMachine(MapStateMachine::new)
                .withTransport(NettyTransport.builder()
                        .withThreads(4)
                        .build())
                .withStorage(Storage.builder()
                        .withDirectory(new File("logs_" + id)) //Must be unique
                        .withStorageLevel(StorageLevel.DISK)
                        .build());
        CopycatServer server = builder.build();

        if (id == 0) {
            server.bootstrap().join();
        } else {
            server.join(addresses).join();
        }

    }

    public Boolean setVertice(Commit<SetVerticeCommand> commit) {
        try {
            Vertice vertice = new Vertice(commit.operation().nome, commit.operation().cor,
                    commit.operation().descricao, commit.operation().peso);
            System.out.println("Adicionando");
            return vertices.putIfAbsent(commit.operation().nome, vertice) == null;
        } finally {
            commit.close();
        }
    }

    public Boolean PutVertice(Commit<PutVerticeCommand> commit) {
        try {
            Vertice vertice = new Vertice(commit.operation().nome, commit.operation().cor,
                    commit.operation().descricao, commit.operation().peso);
            System.out.println("Atualizando vertice");
            return vertices.put(commit.operation().nome, vertice) != null;
        } finally {
            commit.close();
        }
    }

    public Vertice getVertice(Commit<GetVerticeQuery> commit) {
        try {
            Vertice result = vertices.get(commit.operation().nome);
            System.out.println("Vertice recuperado: " + result.nome);
            return result;
        } finally {
            commit.close();
        }
    }
    
    public Boolean removeVertice(Commit<RemoveVerticeCommand> commit){
        
         try {
            System.out.println("Vertice sendo deletado");
            return vertices.remove(commit.operation().nome) != null;
        } finally {
            commit.close();
        }
        
    }

    public Boolean setAresta(Commit<SetArestaCommand> commit) {

        try {
            Aresta aresta = new Aresta(commit.operation().nomeVertice1, commit.operation().nomeVertice2, commit.operation().peso,
                    commit.operation().direcionada, commit.operation().descricao, commit.operation().nome);
            System.out.println("Adicionando Aresta");
            return arestas.putIfAbsent(commit.operation().nome, aresta) == null;
        } finally {
            commit.close();
        }
    }

    public Aresta getAresta(Commit<GetArestaQuery> commit) {

        try {
            Aresta result = arestas.get(commit.operation().nome);
            System.out.println("Aresta recuperada: " + result.nome);
            return result;
        } finally {
            commit.close();
        }

    }

    public Boolean putAresta(Commit<PutArestaCommand> commit) {

        try {
            System.out.println("Atualizando Aresta");
            Aresta aresta = new Aresta(commit.operation().nomeVertice1, commit.operation().nomeVertice2, commit.operation().peso,
                    commit.operation().direcionada, commit.operation().descricao, commit.operation().nome);
            return arestas.put(commit.operation().nome, aresta) != null;
        } finally {
            commit.close();
        }

    }
    
    public Boolean removeAresta(Commit<RemoveArestaCommand> commit){
        try{
            System.out.println("Deletando Aresta");
            return arestas.remove(commit.operation().nome) != null;
        } finally {
            commit.close();
        }
    }

}
