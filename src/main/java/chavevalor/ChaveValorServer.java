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

    public static void main(String[] args) {
        int porta = Integer.parseInt(args[0]);
        int id = Integer.parseInt(args[1]);
        int serverAmount = Integer.parseInt(args[2]);
        int initialPort = Integer.parseInt(args[3]);

        try {

            //int p = 9090;
//            for(int i = 1; i < 3;i++){
//                //p+=i;
//                System.out.println("for");
//                String[] args0 = new String[]{Integer.toString(i), "localhost", "9091", "localhost", "9092"};
//                MapStateMachine.main(args0);
//            }
            String[] args1 = new String[]{"2", "localhost", "5000", "localhost", "5001", "localhost", "5002"};
            //MapStateMachine.main(args1);
            
            //String[] argsHandler = new String[]{"localhost", "9091", "localhost", "9092"};

            //handler = new ChaveValorHandler(porta, id, serverAmount, initialPort, address);
            handler = new ChaveValorHandler(porta, id, serverAmount, initialPort, args1);
            processor = new ChaveValor.Processor(handler);
            
            System.out.println("ta printando");
            //MapStateMachine.main(args0);
            
            
            //MapStateMachine.main(args1);
            //ChaveValorServerTest cv = new ChaveValorServerTest();
            //System.out.println("depois do construtor");
            //cv.testApp();
            //System.out.println("chamou o testapp");
            TServerTransport serverTransport = new TServerSocket(porta);
            TServer server = new TThreadPoolServer(new TThreadPoolServer.Args(serverTransport).processor(processor));
            server.serve();
            System.out.println("server started");
            
            

        } catch (Exception e) {
            System.out.println("erro ao conectar");
            e.printStackTrace();
        }
    }
}
