namespace java chavevalor

struct Aresta {
	1:i32 nomeVertice1,
	2:i32 nomeVertice2,
	3:double peso,
	4:bool direcionada,
	5:string descricao,
	6:i32 nome
}

struct Vertice {
	1:i32 nome,
	2:i32 cor,
	3:string descricao,
	4:double peso
}

struct Grafo {
	1:list<Aresta> arestas,
	2:list<Vertice> vertices
}

exception KeyNotFound
{
	1:string chaveProcurada="thrifty"
}

service ChaveValor
{
	Aresta getAresta(1:i32 nome) throws (1:KeyNotFound knf),
    Vertice getVertice(1:i32 nome) throws (1:KeyNotFound knf),
    bool setAresta(1:i32 nomeVertice1, 2:i32 nomeVertice2, 3:double peso, 4:bool direcionada, 5:string descricao, 6:i32 nome),
	bool setVertice(1:i32 nome, 2:i32 cor, 3:string descricao, 4:double peso),
	bool atualizaAresta(1:i32 nomeVertice1, 2:i32 nomeVertice2, 3:double peso, 4:bool direcionada, 5:string descricao, 6:i32 nome),
	bool atualizaVertice(1:i32 nome, 2:i32 cor, 3:string descricao, 4:double peso),
    void delAresta(1:i32 nome),
	void delVertice(1:i32 nome),
	list<Vertice> listaVerticesGrafo(),
	list<Aresta> listaArestasGrafo(),
	list<Aresta> listaArestasVertice(1:i32 nome),
	list<Vertice> listaVerticesVizinho(1:i32 nome)
	list<Aresta> listaArestasServer()
}  