package chavevalor;

import java.util.Scanner;
import org.apache.thrift.server.TServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;
import org.apache.thrift.server.TServer.Args;
import org.apache.thrift.server.TSimpleServer;
import org.apache.thrift.server.TThreadPoolServer;

// Generated code

//import java.util.HashMap;

public class ChaveValorServer {

  public static ChaveValorHandler handler;

  public static ChaveValor.Processor processor;

  public static void main(String [] args) {
    int porta = Integer.parseInt(args[0]);
    int id = Integer.parseInt(args[1]);
    int serverAmount = Integer.parseInt(args[2]);
    int initialPort = Integer.parseInt(args[3]);

    try {
      handler = new ChaveValorHandler(porta, id, serverAmount, initialPort);
      processor = new ChaveValor.Processor(handler);

      TServerTransport serverTransport = new TServerSocket(porta);
      TServer server = new TThreadPoolServer(new TThreadPoolServer.Args(serverTransport).processor(processor));
      server.serve();

    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
