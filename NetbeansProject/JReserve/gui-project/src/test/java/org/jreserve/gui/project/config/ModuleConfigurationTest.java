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
 * @author Peter Decsi
 * @version 1.0
 */
public class ModuleConfigurationTest {
    
    private final static String OWNER_ID = "owner.id";
    private final static String N1 = "prop.1";
    private final static String V1 = "value 1";
    private final static String N2 = "prop.2";
    private final static String V2 = "value 2";
    
    private ModuleConfiguration mc;
    
    @Before
    public void setUp() {
        mc = new ModuleConfiguration(OWNER_ID);
        mc.setProperty(N1, V1);
        mc.setProperty(N2, V2);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testConstructor_NullName() {
        mc = new ModuleConfiguration(null);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testConstructor_EmptyName() {
        mc = new ModuleConfiguration(" ");
    }

    @Test
    public void testGetOwnerId() {
        assertEquals(OWNER_ID, mc.getOwnerId());
    }

    @Test
    public void testHasProperty() {
        assertTrue(mc.hasProperty(N1));
        assertTrue(mc.hasProperty(N2));
        
        assertFalse(mc.hasProperty(null));
        assertFalse(mc.hasProperty("prop.3"));
    }

    @Test
    public void testGetProperty() {
        assertEquals(V1, mc.getProperty(N1));
        assertEquals(V2, mc.getProperty(N2));
        
        assertEquals(null, mc.getProperty(null));
        assertEquals(null, mc.getProperty("prop.3"));
    }

    @Test
    public void testSetProperty() {
        mc.setProperty(N1, "value 3");
        assertTrue(mc.hasProperty(N1));
        assertEquals("value 3", mc.getProperty(N1));
        
        mc.setProperty(N1, null);
        assertFalse(mc.hasProperty(N1));
        assertEquals(null, mc.getProperty(N1));
        
        mc.setProperty("prop.3", null);
        assertFalse(mc.hasProperty("prop.3"));
        assertEquals(null, mc.getProperty("prop.3"));
        
        mc.setProperty("prop.3", "value 3");
        assertTrue(mc.hasProperty("prop.3"));
        assertEquals("value 3", mc.getProperty("prop.3"));
    }
    
    @Test
    public void testXmlMarshalling() throws Exception {
        String expected =
            "<configuration ownerId=\""+OWNER_ID+"\">" + 
                "<property name=\""+N1+"\">"+V1+"</property>" + 
                "<property name=\""+N2+"\">"+V2+"</property>" + 
            "</configuration>";
        String found = JAXBUtil.marshall(mc);
        assertEquals(expected, found);
    }
    
    @Test
    public void testXmlUnmarshalling() throws Exception {
        String xml =
            "<configuration ownerId=\"xml.owner.id\">" + 
                "<property name=\"xml.prop.1\">xml value 1</property>" + 
                "<property name=\"xml.prop.2\">xml value 2</property>" + 
            "</configuration>";
        
        mc = JAXBUtil.unmarshall(xml, ModuleConfiguration.class);
        assertEquals("xml.owner.id", mc.getOwnerId());
        assertEquals("xml value 1", mc.getProperty("xml.prop.1"));
        assertEquals("xml value 2", mc.getProperty("xml.prop.2"));
    }
    
    @Test(expected=IllegalStateException.class)
    public void testXmlUnmarshalling_NoOwnerId() throws Throwable {
        try {
            String xml = "<configuration ownerId=\" \"/>";
            mc = JAXBUtil.unmarshall(xml, ModuleConfiguration.class);
        } catch (Exception ex) {
            Throwable t = ex;
            while(t.getCause() != null)
                t = t.getCause();
            throw t;
        }
    }
}