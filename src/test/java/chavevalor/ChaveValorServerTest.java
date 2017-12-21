/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chavevalor;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Tiago
 */
public class ChaveValorServerTest {
    
    public ChaveValorServerTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of main method, of class ChaveValorServer.
     */
    @org.junit.Test
    public void testMain() {
        System.out.println("main");
        String[] args = null;
        ChaveValorServer.main(args);
        System.out.println("chegou");
        String[] args0 = new String[]{"1", "localhost", "9091", "localhost", "9092"};
        MapStateMachine.main(args0);
        System.out.println("started primeiro");
        String[] args1 = new String[]{"2", "localhost", "9091", "localhost", "9092"};
        MapStateMachine.main(args1);
        System.out.println("started segundo");
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
