
package chavevalor;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import java.util.LinkedList;
import java.util.List;

import io.atomix.catalyst.transport.Address;
import io.atomix.catalyst.transport.netty.NettyTransport;
import io.atomix.copycat.client.CopycatClient;
import io.atomix.copycat.server.StateMachine;


public class ChaveValorServerTest 
    extends TestCase
{
    public ChaveValorServerTest( String testName )
    {
        super( testName );
    }

    public static Test suite()
    {
        return new TestSuite( ChaveValorServerTest.class );
    }

    ChaveValorServerTest() {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void testApp()
    {
        System.out.println("chamou o testapp");
    	String[] args0 = new String[]{"1", "localhost","9091", "localhost", "9092"};
    	MapStateMachine.main(args0);
        System.out.println("chamou o 1");
    	String[] args1 = new String[]{"2", "localhost","9091", "localhost", "9092"};
    	MapStateMachine.main(args1);
        System.out.println("chamou o 2");

    	//String[] args2 = new String[]{"2", "127.0.0.1","5000", "127.0.0.1", "5001", "127.0.0.1", "5002"};
    	//MapStateMachine.main(args2);

    	//String[] argsC = new String[]{"127.0.0.1","5000", "127.0.0.1", "5001", "127.0.0.1", "5002"};
    	//GraphClient.main(argsC);
    }    
}
