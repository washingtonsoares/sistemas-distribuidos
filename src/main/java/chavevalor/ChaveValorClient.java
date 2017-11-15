package chavevalor;

import java.util.ArrayList;
import java.util.List;
import org.apache.thrift.TException;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import chavevalor.Grafo;
import chavevalor.*;
import java.util.Scanner;

public class ChaveValorClient {

    public static void main(String[] args) {
        
        try {
            Integer porta = 4050;
            String host = "localhost";
            TTransport transport = new TSocket(host, porta);
            transport.open();

            TProtocol protocol = new TBinaryProtocol(transport);
            ChaveValor.Client client = new ChaveValor.Client(protocol);
            int operacao = 10;
            do {

                Scanner ler = new Scanner(System.in);
                System.out.println("Adicionar vertice:           1");
                System.out.println("Adicionar aresta:            2");
                System.out.println("Buscar vertice:              3");
                System.out.println("Buscar aresta:               4");
                System.out.println("Atualizar vertice:           5");
                System.out.println("Atualizar aresta:            6");
                System.out.println("Deletar vertice:             7");
                System.out.println("Deletar aresta:              8");
                System.out.println("Listar vertices do grafo:    9");
                System.out.println("Listar arestas do grafo:    10");
                System.out.println("Listar arestas do vertice:  11");
                System.out.println("Listar vizinhos do vertice: 12");
                //System.out.println();
                //System.out.println("Pra sair digite: 0");
                System.out.println();
                System.out.println("Digite o numero da operacao ou 0 pra sair");

                operacao = ler.nextInt();

                switch (operacao) {
                    case 1: {
                        int nome;
                        int cor;
                        String descricao;
                        double peso;
                        System.out.println("Digite o nome do vertice");
                        nome = ler.nextInt();
                        ler.nextLine();
                        System.out.println("Digite a cor do vertice");
                        cor = ler.nextInt();
                        ler.nextLine();
                        System.out.println("Digite a descricao do vertice");
                        descricao = ler.nextLine();
                        System.out.println("Digite o peso do vertice");
                        peso = ler.nextDouble();
                        ler.nextLine();

                        boolean result = client.setVertice(nome, cor, descricao, peso);
                        System.out.println();
                        if (result) {
                            System.out.println("Cadastro realizado com sucesso");
                        } else {
                            System.out.println("Erro ao cadastrar");
                        }
                        System.out.println();
                        break;
                    }

                    case 2: {
                        int nomeVertice1;
                        int nomeVertice2;
                        String descricao;
                        double peso;
                        boolean direcionada = true;
                        int nome;
                        int controle = 0;
                        System.out.println("Digite o nome do vertice 1");
                        nomeVertice1 = ler.nextInt();
                        ler.nextLine();
                        System.out.println("Digite a nome do vertice 2");
                        nomeVertice2 = ler.nextInt();
                        ler.nextLine();
                        System.out.println("Digite a descricao da aresta");
                        descricao = ler.nextLine();
                        System.out.println("Digite o peso da aresta");
                        peso = ler.nextDouble();
                        ler.nextLine();
                        do {
                            System.out.println("Se for direcionada digite   1");
                            System.out.println("Se for bidirecionada digite 2");
                            controle = ler.nextInt();
                            //do{
                            if (controle == 1) {
                                direcionada = true;
                            }
                            if (controle == 2) {
                                direcionada = false;
                            }
                            if (controle != 1 && controle != 2) {
                                System.out.println("Opcao invalida");
                            }
                            ler.nextLine();
                        } while (controle != 1 && controle != 2);
                        //ler.nextLine();
                        System.out.println("Digite o nome da aresta");
                        nome = ler.nextInt();
                        ler.nextLine();

                        boolean result = client.setAresta(nomeVertice1, nomeVertice2, peso, direcionada, descricao, nome);
                        System.out.println();
                        if (result) {
                            System.out.println("Cadastro realizado com sucesso");
                        } else {
                            System.out.println("Erro ao cadastrar");
                        }
                        System.out.println();
                        break;
                    }
                    case 3: {
                        int nome;

                        System.out.println("Digite o nome do vertice");
                        nome = ler.nextInt();
                        ler.nextLine();
                        System.out.println();
                        try {
                            Vertice vertice = client.getVertice(nome);
                            System.out.println("Vértice encontrado: ");
                            System.out.println("Nome: " + vertice.getNome() );
                            System.out.println("Peso: " + vertice.getPeso() );
                            System.out.println("Descricao: " + vertice.getDescricao());
                            System.out.println("Cor: " + vertice.getCor());
                        } catch (KeyNotFound k) {
                            System.out.println(k.chaveProcurada);
                        }
                        System.out.println();
                        break;
                    }
                    case 4: {
                        int nome;

                        System.out.println("Digite o nome da aresta");
                        nome = ler.nextInt();
                        ler.nextLine();
                        System.out.println();
                        try {
                            Aresta aresta = client.getAresta(nome);
                            System.out.println("Aresta encontrada: ");
                            System.out.println("Nome: " + aresta.getNome() );
                            System.out.println("Peso: " + aresta.getPeso() );
                            System.out.println("Vértice1 :" + aresta.getNomeVertice1());
                            System.out.println("Vértice2 :" + aresta.getNomeVertice2());
                        } catch (KeyNotFound k) {
                            System.out.println(k.chaveProcurada);
                        }
                        System.out.println();
                        break;
                    }
                    case 5: {
                        int nome;
                        int cor;
                        String descricao;
                        double peso;
                        System.out.println("Digite o nome do vertice");
                        nome = ler.nextInt();
                        ler.nextLine();
                        System.out.println("Digite a cor do vertice");
                        cor = ler.nextInt();
                        ler.nextLine();
                        System.out.println("Digite a descricao do vertice");
                        descricao = ler.nextLine();
                        System.out.println("Digite o peso do vertice");
                        peso = ler.nextDouble();
                        ler.nextLine();
                        System.out.println();
                        boolean con = client.atualizaVertice(nome, cor, descricao, peso);
                        if (con) {
                            System.out.println("Atualizado com sucesso");
                        } else {
                            System.out.println("Erro ao atualizar");
                        }
                        System.out.println();
                        break;
                    }
                    case 6: {
                        int nomeVertice1;
                        int nomeVertice2;
                        String descricao;
                        double peso;
                        boolean direcionada = true;
                        int nome;
                        int controle = 0;
                        System.out.println("Digite o nome do vertice 1");
                        nomeVertice1 = ler.nextInt();
                        ler.nextLine();
                        System.out.println("Digite a nome do vertice 2");
                        nomeVertice2 = ler.nextInt();
                        ler.nextLine();
                        System.out.println("Digite a descricao da aresta");
                        descricao = ler.nextLine();
                        System.out.println("Digite o peso da aresta");
                        peso = ler.nextDouble();
                        ler.nextLine();
                        do {
                            System.out.println("Se for direcionada digite   1");
                            System.out.println("Se for bidirecionada digite 2");
                            controle = ler.nextInt();
                            //do{
                            if (controle == 1) {
                                direcionada = true;
                            }
                            if (controle == 2) {
                                direcionada = false;
                            }
                            if (controle != 1 && controle != 2) {
                                System.out.println("Opcao invalida");
                            }
                            ler.nextLine();
                        } while (controle != 1 && controle != 2);
                        //ler.nextLine();
                        System.out.println("Digite o nome da aresta");
                        nome = ler.nextInt();
                        ler.nextLine();
                        System.out.println();
                        boolean con = client.atualizaAresta(nomeVertice1, nomeVertice2, peso, direcionada, descricao, nome);
                        if (con) {
                            System.out.println("Atualizado com sucesso");
                        } else {
                            System.out.println("Erro ao atualizar");
                        }
                        System.out.println();
                        break;
                    }
                    case 7: {
                        System.out.println("Qual vértice deseja remover?");
                        Integer vertice = ler.nextInt();
                        ler.nextLine();
                        System.out.println();
                        client.delVertice(vertice);
                        System.out.println("Vértice removido com sucesso");
                        System.out.println();
                        break;
                    }

                    case 8: {
                        System.out.println("Qual aresta deseja remover?");
                        Integer aresta = ler.nextInt();
                        ler.nextLine();
                        System.out.println();
                        client.delAresta(aresta);
                        System.out.println("Aresta removida com sucesso");
                        System.out.println();
                        break;
                    }

                    case 9: {
                        System.out.println("Listando vértices do grafo");
                        System.out.println();
                        List<Vertice> vertices = client.listVerticesServer();
                        for (Vertice ve : vertices) {
                            System.out.println("Nome " + ve.nome + " cor " + ve.cor);
                        }
                        if(vertices.size() == 0) {
                            System.out.println("Nenhum vértice encontrado");
                        }
                        System.out.println();
                        break;
                    }
                    case 10: {
                        System.out.println("Listando arestas do grafo");
                        System.out.println();
                        List<Aresta> arestas = client.listaArestasGrafo();
                        for (Aresta ar : arestas) {
                            System.out.println("Aresta " + ar.nome + " vertice1 " + ar.nomeVertice1 + " vertice2 " + ar.nomeVertice2);
                        }
                        if(arestas.size() == 0) {
                            System.out.println("Nenhuma aresta encontrada");
                        }
                        System.out.println();
                        break;
                    }
                    case 11: {
                        System.out.println("Qual vértices deseja listar as arestas ?");
                        Integer vertice = ler.nextInt();
                        ler.nextLine();
                        System.out.println("Lista de arestas no vertice " + vertice);
                        System.out.println();
                        List<Aresta> arestas = client.listaArestasVertice(vertice);
                        for (Aresta ar : arestas) {
                            System.out.println("Aresta " + ar.nome + " vertice1 " + ar.nomeVertice1 + " vertice2 " + ar.nomeVertice2);
                        }

                        if (arestas.size() < 1) {
                            System.out.println("Vertice nao possui arestas ou vertice nao encontrado");
                        }
                        System.out.println();
                        break;
                    }
                    case 12: {
                        System.out.println("Qual vértices deseja listar os vizinhos ?");
                        Integer vertice = ler.nextInt();
                        ler.nextLine();
                        List<Vertice> ver = client.listaVerticesVizinho(vertice);
                        System.out.println();
                        System.out.println("Lista de vizinho do vertice " + vertice);
                        for (Vertice ve : ver) {
                            System.out.println("Nome " + ve.nome + " cor " + ve.cor);
                        }
                        if (ver.size() < 1) {
                            System.out.println("Vertice nao possue visizinhos");
                        }
                        System.out.println();
                        break;
                    }
                    default: {
                        break;
                    }
                }

            } while (operacao != 0);

            transport.close();
        } catch (KeyNotFound k) {
            System.out.println(k.chaveProcurada);

        } catch (TException x) {
            x.printStackTrace();
        }
    }
}
