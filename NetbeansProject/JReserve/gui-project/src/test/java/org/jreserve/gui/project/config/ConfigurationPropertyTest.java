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
public class ConfigurationPropertyTest {
    
    private final static String NAME = "propName";
    private final static String VALUE = "propValue";
    
    private ConfigurationProperty property;
    
    public ConfigurationPropertyTest() {
    }
    
    @Before
    public void setUp() {
        property = new ConfigurationProperty(NAME, VALUE);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testConstructor_NullName() {
        property = new ConfigurationProperty(null, VALUE);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testConstructor_EmptyName() {
        property = new ConfigurationProperty(" ", VALUE);
    }

    @Test
    public void testGetName() {
        assertEquals(NAME, property.getName());
    }

    @Test
    public void testGetValue() {
        assertEquals(VALUE, property.getValue());
    }

    @Test
    public void testSetValue() {
        property.setValue(NAME);
        assertEquals(NAME, property.getValue());
    }

    /**
     * Test of equals method, of class ConfigurationProperty.
     */
    @Test
    public void testEquals() {
        ConfigurationProperty p1 = new ConfigurationProperty(NAME, VALUE);
        ConfigurationProperty p2 = new ConfigurationProperty(NAME, NAME);
        assertTrue(p1.equals(p2));
        assertTrue(p2.equals(p1));
        
        p2 = null;
        assertFalse(p1.equals(p2));
        
        p1 = new ConfigurationProperty(NAME.toLowerCase(), VALUE);
        p2 = new ConfigurationProperty(NAME.toUpperCase(), VALUE);
        assertFalse(p1.equals(p2));
        assertFalse(p2.equals(p1));
        
        p1 = new ConfigurationProperty(NAME, VALUE);
        p2 = new ConfigurationProperty(NAME+NAME, VALUE);
        assertFalse(p1.equals(p2));
        assertFalse(p2.equals(p1));
    }

    @Test
    public void testToString() {
        String expected = "<property name=\""+NAME+"\">"+VALUE+"</property>";
        assertEquals(expected, property.toString());
    }

    @Test
    public void testIsNull() {
        assertTrue(ConfigurationProperty.isNull(null));
        assertTrue(ConfigurationProperty.isNull(""));
        assertTrue(ConfigurationProperty.isNull(" "));
        assertTrue(ConfigurationProperty.isNull(" \t "));
        assertTrue(ConfigurationProperty.isNull(" \n "));
        assertFalse(ConfigurationProperty.isNull(" a "));
    }
    
    @Test
    public void testXmlMarshalling() throws Exception {
        String expected = "<property name=\""+NAME+"\">"+VALUE+"</property>";
        String found = JAXBUtil.marshall(property);
        assertEquals(expected, found);
    }
    
    @Test
    public void testXmlUnmarshalling() throws Exception {
        String xml = 
            "<property name=\"xmlName\">xmlValue</property>";
        property = JAXBUtil.unmarshall(xml, ConfigurationProperty.class);
        assertEquals("xmlName", property.getName());
        assertEquals("xmlValue", property.getValue());
    }
    
    @Test(expected=IllegalStateException.class)
    public void testXmlUnmarshalling_NoName() throws Throwable {
        try {
            String xml = 
                "<property name=\" \">xmlValue</property>";
            property = JAXBUtil.unmarshall(xml, ConfigurationProperty.class);
        } catch (Exception ex) {
            Throwable t = ex;
            while(t.getCause() != null)
                t = t.getCause();
            throw t;
        }
    }
}