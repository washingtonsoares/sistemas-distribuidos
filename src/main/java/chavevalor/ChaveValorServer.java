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
    int porta = getPorta(args);
    try {
      handler = new ChaveValorHandler();
      processor = new ChaveValor.Processor(handler);
      System.out.println("porta "+ porta);

      TServerTransport serverTransport = new TServerSocket(porta);
      TServer server = new TThreadPoolServer(new TThreadPoolServer.Args(serverTransport).processor(processor));

      System.out.println("Starting the multi server...");
      server.serve();

    } catch (Exception x) 
    {
      x.printStackTrace();
    }
  }

  static int getPorta(String[] args) {
      final Integer PORTA_PADRAO = 9000;

      if(args != null && args.length > 0) {
          return Integer.parseInt(args[0]);
      }
      return  PORTA_PADRAO;
  }
}
