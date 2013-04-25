package org.jreserve.grscript;

import java.io.PrintWriter;
import java.io.StringWriter;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class ScriptExecutorTest {

    private ScriptExecutor engine;
    
    public ScriptExecutorTest() {
    }

    @Before
    public void setUp() {
        engine = new ScriptExecutor();
    }

    @Test
    public void testAddFunctionProvider() {
        fail("The test case is a prototype.");
    }

    @Test
    public void testRemoveFunctionProvider() {
        fail("The test case is a prototype.");
    }

    @Test
    public void testAddToClassPath() {
        fail("The test case is a prototype.");
    }

    @Test
    public void testRemoveFromClassPath() {
        fail("The test case is a prototype.");
    }

    @Test
    public void testSetOutput() {
        StringWriter out = new StringWriter();
        engine.setOutput(new PrintWriter(out));
        engine.runScript("print \"Hello world!\"");
        assertEquals("Hello world!", out.toString());
    }

    @Test
    public void testSetParentClassLoader() {
        fail("The test case is a prototype.");
    }

    @Test
    public void testSetVariable() {
        fail("The test case is a prototype.");
    }

    @Test
    public void testRemoveVariable() {
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetVariable() {
        fail("The test case is a prototype.");
    }

    @Test
    public void testClearVariables() {
        fail("The test case is a prototype.");
    }

    @Test
    public void testRunScript_File() throws Exception {
        fail("The test case is a prototype.");
    }

    @Test
    public void testRunScript_String() {
        int x = 5;
        engine.setVariable("x", x);
        engine.runScript("x = 10");
        Number n = (Number) engine.getVariable("x");
        assertEquals(10, n.intValue());
    }

}