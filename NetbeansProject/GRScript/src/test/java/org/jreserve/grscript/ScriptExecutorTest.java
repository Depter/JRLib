/*
 *  Copyright (C) 2013, Peter Decsi.
 * 
 *  This library is free software: you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public 
 *  License as published by the Free Software Foundation, either 
 *  version 3 of the License, or (at your option) any later version.
 * 
 *  This library is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 * 
 *  You should have received a copy of the GNU General Public License
 *  along with this library.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.jreserve.grscript;

import groovy.lang.MissingPropertyException;
import java.io.PrintWriter;
import java.io.StringWriter;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.jreserve.grscript.util.PrintDelegate;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class ScriptExecutorTest {

    private ScriptExecutor engine;
    private StringWriter output;
    
    public ScriptExecutorTest() {
    }

    @Before
    public void setUp() {
        engine = new ScriptExecutor();
        output = new StringWriter();
        engine.setOutput(new PrintWriter(output));
    }

    @Test
    public void testAddFunctionProvider() {
        engine.addFunctionProvider(new PrintDelegate());
        String script = "def data = new double[2]\n"+
                        "printData \"Title\", data";
        engine.runScript(script);
        
        String sep = System.getProperty("line.separator");
        String expected = "Title"+sep+"\t0;\t0"+sep;
        String found = output.toString();
        assertEquals(expected, found);
        
    }

    @Test
    public void testRemoveFunctionProvider() {
        FunctionProvider provider = new PrintDelegate();
        engine.addFunctionProvider(provider);
        String script = "def data = new double[2]\n"+
                        "printData \"Title\", data";
        engine.runScript(script);
        
        engine.removeFunctionProvider(provider);
        try {
            engine.runScript(script);
        } catch (groovy.lang.MissingMethodException ex) {
            return;
        }
        fail("Method should have thrown exception!");
    }

    @Test
    public void testSetOutput() {
        engine.runScript("print \"Hello world!\"");
        assertEquals("Hello world!", output.toString());
    }

    @Test
    public void testSetVariable() {
        engine.setVariable("x", 5);
        engine.runScript("print x");
        assertEquals("5", output.toString());
    }

    @Test
    public void testGetVariable() {
        engine.setVariable("x", 5);
        engine.runScript("x = 10");
        Number n = (Number) engine.getVariable("x");
        assertEquals(10, n.intValue());
    }

    @Test
    public void testRemoveVariable() {
        engine.setVariable("x", 5);
        engine.runScript("x += 10");
        engine.removeVariable("x");
        
        try {
            engine.runScript("x += 10");
        } catch (MissingPropertyException ex) {
            return;
        }
        fail("Method should have thrown exception!");
    }

    @Test
    public void testClearVariables() {
        engine.setVariable("x", 5);
        engine.runScript("x += 10");
        engine.clearVariables();
        
        try {
            engine.runScript("x += 10");
        } catch (MissingPropertyException ex) {
            return;
        }
        fail("Method should have thrown exception!");
    }

    @Test
    public void testRunScript_File() throws Exception {
        java.io.File file = new java.io.File("src/main/resources/org/jreserve/grscript/helloWorld.groovy");
        engine.runScript(file);
        assertEquals("Hello world!", output.toString());
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