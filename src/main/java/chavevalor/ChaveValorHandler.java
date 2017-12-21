package chavevalor;

import java.lang.reflect.Array;
import java.util.ArrayList;

import org.apache.thrift.TException;

import java.util.Arrays;
import java.util.List;

import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;

import io.atomix.catalyst.transport.Address;
import io.atomix.catalyst.transport.netty.NettyTransport;
import io.atomix.copycat.client.CopycatClient;
import java.util.LinkedList;
import java.util.concurrent.CompletableFuture;

public class ChaveValorHandler implements ChaveValor.Iface {

    List<Aresta> listArestas = new ArrayList<>();
    List<Vertice> listVertices = new ArrayList<>();

    Grafo grafo = new Grafo(listArestas, listVertices);

    private int id;
    private int serverAmount;
    private int port;
    private int initialPort;
    //private String[] address;
    List<Address> addresses = new LinkedList<>();

    public ChaveValorHandler(int port, int id, int serverAmount, int initialPort, String[] address) {
        this.port = port;
        this.id = id;
        this.serverAmount = serverAmount;
        this.initialPort = initialPort;
        //this.address = address;
        for (int i = 1; i < address.length; i += 2) {
            Address ad = new Address(address[i], Integer.parseInt(address[i + 1]));
            addresses.add(ad);
        }
    }

//    public List<Address> getAddresses(String[] ad) {
//        for (int i = 1; i < address.length; i += 2) {
//            Address ad = new Address(address[i], Integer.parseInt(address[i + 1]));
//            addresses.add(ad);
//        }
//    }
    @Override
    public Aresta getAresta(int key) throws TException {
        int server = key % this.getServerAmount();
        int port = this.getInitialPort() + server;

        if (server != this.getId()) {
            TTransport transport = new TSocket("localhost", port);
            TProtocol protocol = new TBinaryProtocol(transport);
            ChaveValor.Client client = new ChaveValor.Client(protocol);
            transport.open();
            Aresta aresta = client.getAresta(key);
            transport.close();

            return aresta;
        } else {
            for (Aresta a : grafo.arestas) {
                if (a.nome == key) {
                    return a;
                }
            }
            throw new KeyNotFound("Não encontrou aresta");
        }
    }

    @Override
    public boolean atualizaAresta(int nomeVertice1, int nomeVertice2, double peso, boolean direcionada, String descricao, int nome) throws TException {
        int server = nome % this.getServerAmount();
        int port = this.getInitialPort() + server;

        if (server != this.getId()) {
            TTransport transport = new TSocket("localhost", port);
            TProtocol protocol = new TBinaryProtocol(transport);
            ChaveValor.Client client = new ChaveValor.Client(protocol);
            transport.open();
            boolean updated = client.atualizaAresta(nomeVertice1, nomeVertice2, peso, direcionada, descricao, nome);
            transport.close();

            return updated;
        } else {
            for (Aresta g : grafo.arestas) {
                if (g.nome == nome) {
                    synchronized (g) {
                        g.setPeso(peso);
                        g.setDirecionada(direcionada);
                        g.setDescricao(descricao);
                    }
                }
            }

            return true;
        }

    }

    @Override
    public boolean setAresta(int nomeVertice1, int nomeVertice2, double peso,
            boolean direcionada, String descricao, int nome) throws TException {

        int server = nome % this.getServerAmount();
        int port = this.getInitialPort() + server;

        System.out.println("setAresta");
        System.out.println("server " + server + " porta " + port);
        System.out.println("ID " + this.getId());

        if (server != this.getId()) {
            TTransport transport = new TSocket("localhost", port);
            TProtocol protocol = new TBinaryProtocol(transport);
            ChaveValor.Client client = new ChaveValor.Client(protocol);
            transport.open();
            boolean created = client.setAresta(nomeVertice1, nomeVertice2, peso, direcionada, descricao, nome);
            transport.close();

            return created;

        } else {
            boolean arestaJaExiste = false;

            try {
                if (nomeVertice1 == nomeVertice2) {
                    return false;
                }
                getVertice(nomeVertice1);
                getVertice(nomeVertice2);

                for (Aresta aresta : grafo.arestas) {
                    if (aresta.nome == nome || (aresta.nomeVertice1 == nomeVertice1 && aresta.nomeVertice2 == nomeVertice2)) {
                        arestaJaExiste = true;
                    }
                }
                if (!arestaJaExiste) {
                    Aresta aresta = new Aresta(nomeVertice1, nomeVertice2, peso, direcionada, descricao, nome);
                    grafo.arestas.add(aresta);
                    return true;
                }

                return false;
            } catch (KeyNotFound e) {
                System.out.println("Vértice inexistente");
                return false;
            }
        }
    }

    @Override
    public void delAresta(int key) throws TException {
        int server = key % this.getServerAmount();
        int port = this.getInitialPort() + server;

        if (server != this.getId()) {
            TTransport transport = new TSocket("localhost", port);
            TProtocol protocol = new TBinaryProtocol(transport);
            ChaveValor.Client client = new ChaveValor.Client(protocol);
            transport.open();
            client.delAresta(key);
            transport.close();
        } else {
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

    }

    @Override
    public Vertice getVertice(int key) throws TException {
        int server = key % this.getServerAmount();
        int port = this.getInitialPort() + server;

        if (server != this.getId()) {
            TTransport transport = new TSocket("localhost", port);
            TProtocol protocol = new TBinaryProtocol(transport);
            ChaveValor.Client client = new ChaveValor.Client(protocol);
            transport.open();
            Vertice vertice = client.getVertice(key);
            transport.close();

            return vertice;
        } else {
            CopycatClient.Builder builder = CopycatClient.builder()
                    .withTransport(NettyTransport.builder()
                            .withThreads(4)
                            .build());
            CopycatClient client = builder.build();

            CompletableFuture<CopycatClient> future = client.connect(addresses);
            future.join();

            try {
                System.out.println("1: ");
                Vertice v = client.submit(new GetVerticeQuery(key)).get();
                return v;
            } catch (Exception e) {
                System.out.println("Commands may have failed.");
                e.printStackTrace();
            }

//            for (Vertice v : grafo.vertices) {
//                if (v.nome == key) {
//                    return v;
//                }
//            }
            throw new KeyNotFound("Não encontrou vertice");
        }

    }

    @Override
    public boolean atualizaVertice(int nome, int cor, String descricao, double peso) throws TException {
        int server = nome % this.getServerAmount();
        int port = this.getInitialPort() + server;

        if (server != this.getId()) {
            TTransport transport = new TSocket("localhost", port);
            TProtocol protocol = new TBinaryProtocol(transport);
            ChaveValor.Client client = new ChaveValor.Client(protocol);
            transport.open();

            boolean updated = client.atualizaVertice(nome, cor, descricao, peso);

            transport.close();

            return updated;
        } else {
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
    }

    @Override
    public boolean setVertice(int nome, int cor, String descricao, double peso) throws TException {
        int server = nome % this.getServerAmount();
        int port = this.getInitialPort() + server;

        if (server != this.getId()) {
            System.out.println("entrou no if, server:"+ server);
            TTransport transport = new TSocket("localhost", port);
            TProtocol protocol = new TBinaryProtocol(transport);
            ChaveValor.Client client = new ChaveValor.Client(protocol);
            transport.open();

            boolean created = client.setVertice(nome, cor, descricao, peso);

            transport.close();

            return created;
        } else {

            for (Vertice v : grafo.vertices) {
                if (v.nome == nome) {
                    return false;
                }
            }
            System.out.println("entrou no copycat");
            CopycatClient.Builder builder = CopycatClient.builder()
                    .withTransport(NettyTransport.builder()
                            .withThreads(3)
                            .build());
            System.out.println("chegou aqui");
            CopycatClient client = builder.build();

            CompletableFuture<CopycatClient> future = client.connect(addresses);
            future.join();

            CompletableFuture[] futures = new CompletableFuture[1];

            try {
                futures[0] = client.submit(new SetVerticeCommand(nome, cor, descricao, peso));
                //futures[1] = client.submit(new AddVertexCommand(2,2, "world! Hello"));
                //futures[2] = client.submit(new AddEdgeCommand(1,2, "Edge"));

                CompletableFuture.allOf(futures).thenRun(() -> System.out.println("Commands completed!"));

            } catch (Exception e) {
                System.out.println("Commands may have failed.");
                e.printStackTrace();
            }

            System.out.println("add " + nome);
            Vertice vertice = new Vertice(nome, cor, descricao, peso);
            grafo.vertices.add(vertice);
            return true;
        }

    }

    @Override
    public void delVertice(int key) throws TException {

        int server = key % this.getServerAmount();
        int port = this.getInitialPort() + server;

        if (server != this.getId()) {
            TTransport transport = new TSocket("localhost", port);
            TProtocol protocol = new TBinaryProtocol(transport);
            ChaveValor.Client client = new ChaveValor.Client(protocol);
            transport.open();

            client.delVertice(key);

            transport.close();
        } else {
            List<Aresta> todasArestas = listaArestasGrafo();
            //Um vértice, ao ser removido, implica na remoção de todas as arestas que o tocam.
            for (int i = 0; i < todasArestas.size(); i++) {
                if (todasArestas.get(i).nomeVertice1 == key || todasArestas.get(i).nomeVertice2 == key) {
                    System.out.println("vai remover aresta");
                    delAresta(todasArestas.get(i).getNome());
                    System.out.println("removeu");
                }
            }

            for (int i = 0; i < grafo.vertices.size(); i++) {
                System.out.println("NOME VERTICE " + grafo.vertices.get(i).nome);
                if (grafo.vertices.get(i).nome == key) {
                    synchronized (grafo.vertices.get(i)) {
                        System.out.println("vai remover vertice");
                        grafo.vertices.remove(i);
                        System.out.println("removeu");
                    }
                    break;
                }
            }
        }

    }

    @Override
    public List<Vertice> listVerticesServer() throws TException {
        List<Vertice> todosVertices = new ArrayList<>();

        for (int i = 0; i < this.getServerAmount(); i++) {
            List<Vertice> verticesServer;

            if (this.getId() == i) {
                verticesServer = listaVerticesGrafo();
            } else {
                TTransport transport = new TSocket("localhost", this.getInitialPort() + i);
                TProtocol protocol = new TBinaryProtocol(transport);
                ChaveValor.Client client = new ChaveValor.Client(protocol);
                transport.open();
                verticesServer = client.listaVerticesGrafo();

                transport.close();
            }
            todosVertices.addAll(verticesServer);
        }

        return todosVertices;

    }

    public List<Vertice> listaVerticesGrafo() {
        List<Vertice> list = new ArrayList<>();
        System.out.println("Listando todos os vertices do grafo");
        for (Vertice v : grafo.vertices) {
            list.add(v);

        }
        return list;
    }

    public List<Aresta> listaArestasGrafo() throws TException {
        List<Aresta> todasArestas = new ArrayList<>();

        for (int i = 0; i < this.getServerAmount(); i++) {
            List<Aresta> arestasServer;

            if (this.getId() == i) {
                arestasServer = listaArestasServer();
            } else {
                TTransport transport = new TSocket("localhost", this.getInitialPort() + i);
                TProtocol protocol = new TBinaryProtocol(transport);
                ChaveValor.Client client = new ChaveValor.Client(protocol);
                transport.open();
                arestasServer = client.listaArestasServer();

                transport.close();
            }
            todasArestas.addAll(arestasServer);
        }

        return todasArestas;

    }

    public List<Aresta> listaArestasServer() {
        List<Aresta> list = new ArrayList<>();
        for (Aresta a : grafo.arestas) {
            list.add(a);

        }
        return list;
    }

    public List<Aresta> listaArestasVertice(int nome) throws TException {
        List<Aresta> todasArestas = listaArestasGrafo();
        System.out.println("TODAS ARESTAS " + Arrays.toString(new List[]{todasArestas}));

        List<Aresta> arestas = new ArrayList<>();

        for (Aresta a : todasArestas) {
            if (a.nomeVertice1 == nome || a.nomeVertice2 == nome) {
                arestas.add(a);
            }
        }

        return arestas;
    }

    public List<Vertice> listaVerticesVizinho(int nome) throws TException {
        List<Aresta> todasArestas = listaArestasGrafo();
        List<Vertice> verticeViz = new ArrayList<>();
        System.out.println("Listando nós vizinhos do vertice " + nome);

        for (Aresta a : todasArestas) {
            if (a.nomeVertice1 == nome) {
                try {
                    Vertice v = new Vertice();
                    v = getVertice(a.nomeVertice2);
                    verticeViz.add(v);
                } catch (KeyNotFound k) {
                    System.out.println(k.chaveProcurada);
                }
            }
            if (a.nomeVertice2 == nome) {
                try {
                    Vertice v = new Vertice();
                    v = getVertice(a.nomeVertice1);
                    verticeViz.add(v);
                } catch (KeyNotFound k) {
                    System.out.println(k.chaveProcurada);
                }
            }
        }
        return verticeViz;
    }

    @Override
    public Grafo getGrafo() throws TException {
        return null;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getServerAmount() {
        return serverAmount;
    }

    public void setServerAmount(int serverAmount) {
        this.serverAmount = serverAmount;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getInitialPort() {
        return initialPort;
    }

    public void setInitialPort(int initialPort) {
        this.initialPort = initialPort;
    }

    public void menorCaminho(int v1, int destino) throws TException {
        List<Aresta> todasArestas = new ArrayList<>();
        List<Vertice> todosVertice = new ArrayList<>();
        double dist[] = null;
        boolean visitado[] = null;
        todasArestas = listaArestasGrafo();
        todosVertice = listaVerticesGrafo();

        for (int i = 0; i < todosVertice.size(); i++) {
            dist[i] = Double.MAX_VALUE;
            visitado[i] = false;
        }
        Vertice v = new Vertice();
        v = getVertice(v1);
        int ind = todosVertice.indexOf(v);
        dist[ind] = 0;
    }
}
