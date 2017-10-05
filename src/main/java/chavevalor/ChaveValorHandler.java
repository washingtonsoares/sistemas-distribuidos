package chavevalor;

import static java.nio.file.Files.list;
import java.util.ArrayList;
import static java.util.Collections.list;
import org.apache.thrift.TException;
import java.util.HashMap;
import java.util.List;
import chavevalor.*;

public class ChaveValorHandler implements ChaveValor.Iface {

    List<Aresta> listArestas = new ArrayList<>();
    List<Vertice> listVertices = new ArrayList<>();

    Grafo grafo = new Grafo(listArestas, listVertices);

    @Override
    public Aresta getAresta(int key) throws TException {
        for (Aresta a : grafo.arestas) {
            if (a.nome == key) {
                return a;
            }
        }
        throw new KeyNotFound("Não encontrou aresta");
    }

    @Override
    public boolean atualizaAresta(int nomeVertice1, int nomeVertice2, double peso, boolean direcionada, String descricao, int nome) throws TException {
        //Uma aresta não pode ter seus vértices mudados.
        for (Aresta g : grafo.arestas) {
            if (g.nome == nome) {
                synchronized (g) {
                    g.setPeso(peso);
                    g.setDirecionada(direcionada);
                    g.setDescricao(descricao);
                }
                //System.out.println("atualizou aresta");
            }
        }
        //System.out.println("atualizou aresta");

        return true;
    }

    @Override
    public boolean setAresta(int nomeVertice1, int nomeVertice2, double peso,
            boolean direcionada, String descricao, int nome) throws TException {
        boolean vert1 = false;
        boolean vert2 = false;
        boolean arestaJaExiste = false;
        //Uma aresta só pode ser adicionada se ambos os vértices que toca já existirem no grafo.
        for (Vertice v : grafo.vertices) {
            if (v.nome == nomeVertice1) {
                vert1 = true;
            }
            if (v.nome == nomeVertice2) {
                vert2 = true;
            }

        }

        for (Aresta aresta : grafo.arestas) {
            if (aresta.nome == nome) {
                arestaJaExiste = true;
            }
        }

        if (vert1 && vert2 && nomeVertice1 != nomeVertice2 && !arestaJaExiste) {
            Aresta aresta = new Aresta(nomeVertice1, nomeVertice2, peso, direcionada, descricao, nome);
//            aresta.setNomeVertice1(nomeVertice1);
//            aresta.setNomeVertice2(nomeVertice2);
//            aresta.setPeso(peso);
//            aresta.setDirecionada(direcionada);
//            aresta.setDescricao(descricao);
//            aresta.setNome(nome);

            grafo.arestas.add(aresta);

            return true;
        }
        return false;
    }

    @Override
    public void delAresta(int key) throws TException {
        System.out.println("entro no del");

        for (int i = 0; i < grafo.arestas.size(); i++) {
            if (grafo.arestas.get(i).nome == key) {
                synchronized (grafo.arestas.get(i)) {
                    System.out.println("vai remover aresta");
                    grafo.arestas.remove(i);
                    System.out.println("removeu");
                }
            }
        }
    }

    @Override
    public Vertice getVertice(int key) throws TException {
        for (Vertice v : grafo.vertices) {
            if (v.nome == key) {
                return v;
            }
        }
        //System.out.println("nao encontrou");
        //KeyNotFound k = new KeyNotFound("nao encontrou");
        throw new KeyNotFound("Não encontrou vertice");

    }

    @Override
    public boolean atualizaVertice(int nome, int cor, String descricao, double peso) throws TException {
        //O nome de um vértice não pode ser alterado.
        for (Vertice v : grafo.vertices) {
            if (v.nome == nome) {
                synchronized (v) {
                    v.setCor(cor);
                    v.setPeso(peso);
                    v.setDescricao(descricao);
                    System.out.println("atualizou vertice");
                }
            }
        }

        return true;
    }

    @Override
    public boolean setVertice(int nome, int cor, String descricao, double peso) throws TException {
        //Um vértice só pode ser adicionado se não existir outro com o mesmo nome.
        boolean add = true;
        for (Vertice v : grafo.vertices) {
            //System.out.println("v.nome "+v.nome+ " nome "+nome);
            //System.out.println(v.nome == nome);
            if (v.nome == nome) {
                add = false;
                break;
            }
        }
        if (add) {
            System.out.println("add " + nome);
            //List<Aresta> are = new ArrayList<>();
            Vertice vertice = new Vertice(nome, cor, descricao, peso);
//            vertice.setNome(nome);
//            vertice.setCor(cor);
//            vertice.setPeso(peso);
//            vertice.setDescricao(descricao);
            grafo.vertices.add(vertice);
            return true;
        }
        return false;
    }

    @Override
    public void delVertice(int key) throws TException {
        //Um vértice, ao ser removido, implica na remoção de todas as arestas que o tocam.
        for (int i = 0; i < grafo.arestas.size(); i++) {
            if (grafo.arestas.get(i).nomeVertice1 == key || grafo.arestas.get(i).nomeVertice2 == key) {
                synchronized (grafo.arestas.get(i)) {
                    System.out.println("vai remover aresta");
                    grafo.arestas.remove(i);
                    System.out.println("removeu");
                }
            }
        }

        for (int i = 0; i < grafo.vertices.size(); i++) {
            if (grafo.vertices.get(i).nome == key) {
                synchronized (grafo.vertices.get(i)) {
                    System.out.println("vai remover vertice");
                    grafo.vertices.remove(i);
                    System.out.println("removeu");
                }
                break;
            }
        }
        System.out.println("saiu");
    }

    public List<Vertice> listaVerticesGrafo() {
        List<Vertice> list = new ArrayList<>();
        System.out.println("Listando todos os vertices do grafo");
        for (Vertice v : grafo.vertices) {
            list.add(v);

        }
        return list;
    }

    public List<Aresta> listaArestasGrafo() {
        List<Aresta> list = new ArrayList<>();
        //System.out.println("Listando todos as arestas do grafo");
        for (Aresta a : grafo.arestas) {
            list.add(a);

        }
        return list;
    }

    public List<Aresta> listaArestasVertice(int nome) {
        List<Aresta> list = new ArrayList<>();
        //System.out.println("Listando as arestas do vertice "+nome);
        for (Aresta a : grafo.arestas) {
            if (a.nomeVertice1 == nome || a.nomeVertice2 == nome) {
                list.add(a);
            }
        }
        return list;
    }

    public List<Vertice> listaVerticesVizinho(int nome) {

        List<Vertice> list = new ArrayList<>();
        List<Integer> viz = new ArrayList<>();
        System.out.println("Listando nós vizinhos do vertice " + nome);

        for (Aresta a : grafo.arestas) {
            if (a.nomeVertice1 == nome) {
                viz.add(a.nomeVertice2);
            }
            if (a.nomeVertice2 == nome) {
                viz.add(a.nomeVertice1);
            }

        }
        System.out.println(viz.size());
        for (int i = 0; i < viz.size(); i++) {
            System.out.println(viz.get(i));
            for (Vertice v : grafo.vertices) {
                if (viz.get(i) == v.nome) {
                    list.add(v);
                }

            }
        }

        return list;
    }
}
