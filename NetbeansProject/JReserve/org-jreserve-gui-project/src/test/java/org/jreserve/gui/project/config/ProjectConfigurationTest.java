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
package org.jreserve.gui.project.config;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Peter  Deics
 * @version 1.0
 */
public class ProjectConfigurationTest {
    
    private final static String M1 = "module.1";
    private final static String M2 = "module.2";
    private final static String P = "property";
    private final static String V = "value";
    
    private ProjectConfiguration pc;
    
    @Before
    public void setUp() {
        pc = new ProjectConfiguration();
        pc.getConfigurator(M1).setProperty(P, V);
        pc.getConfigurator(M2).setProperty(P, V);
    }

    @Test
    public void testHasConfigurator() {
        assertTrue(pc.hasConfigurator(M1));
        assertTrue(pc.hasConfigurator(M2));
        assertFalse(pc.hasConfigurator("bela"));
    }

    @Test
    public void testGetConfigurator() {
        assertTrue(pc.getConfigurator(M1).hasProperty(P));
        assertTrue(pc.getConfigurator(M2).hasProperty(P));
        
        assertFalse(pc.hasConfigurator("bela"));
        assertFalse(pc.getConfigurator("bela").hasProperty(P));
        assertTrue(pc.hasConfigurator("bela"));
    }
    
    @Test
    public void testXmlMarshalling() throws Exception {
        String expected =
            "<project>"+
                "<configurations>"                                      +
                    "<configuration ownerId=\""+M1+"\">"                + 
                        "<property name=\""+P+"\">"+V+"</property>"     + 
                    "</configuration>"                                  +
                    "<configuration ownerId=\""+M2+"\">"                + 
                        "<property name=\""+P+"\">"+V+"</property>"     + 
                    "</configuration>"                                  +
                "</configurations>"                                     +
            "</project>"                                                ;
        String found = JAXBUtil.marshall(pc);
        assertEquals(expected, found);
    }
    
    @Test
    public void testXmlUnmarshalling() throws Exception {
        String xml =
            "<project>"+
                "<configurations>"                                      +
                    "<configuration ownerId=\""+M1+"\">"                + 
                        "<property name=\""+P+"\">"+V+"</property>"     + 
                    "</configuration>"                                  +
                    "<configuration ownerId=\""+M2+"\">"                + 
                        "<property name=\""+P+"\">"+V+"</property>"     + 
                    "</configuration>"                                  +
                "</configurations>"                                     +
            "</project>"                                                ;
        
        pc = JAXBUtil.unmarshall(xml, ProjectConfiguration.class);
        assertEquals(V, pc.getConfigurator(M1).getProperty(P));
        assertEquals(V, pc.getConfigurator(M2).getProperty(P));
    }
}