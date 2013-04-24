package org.jreserve.grscript.gui.classpath.registry;

import org.jreserve.grscript.gui.classpath.registry.ClassPathItem;
import org.jreserve.grscript.gui.classpath.registry.ClassPathItemType;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class ClassPathItemTest {
    
    private final static String PATH = "C:\\Test\\Test.jar";
    private final static ClassPathItemType TYPE = ClassPathItemType.JAR;
    
    private ClassPathItem item;
    
    public ClassPathItemTest() {
    }
    
    @Before
    public void setUp() {
        item = new ClassPathItem(TYPE, PATH);
    }

    @Test(expected = NullPointerException.class)
    public void testConstructor_NullType() {
        item = new ClassPathItem(null, PATH);
    }

    @Test(expected = NullPointerException.class)
    public void testConstructor_NullPath() {
        item = new ClassPathItem(TYPE, null);
    }
    
    @Test
    public void testGetPath() {
        assertEquals(PATH, item.getPath());
    }

    @Test
    public void testGetType() {
        assertEquals(TYPE, item.getType());
    }

    @Test
    public void testCompareTo() {
        ClassPathItem i2 = new ClassPathItem(ClassPathItemType.JAR, PATH);
        assertEquals(0, item.compareTo(i2));
        assertEquals(0, i2.compareTo(item));
        
        i2 = new ClassPathItem(ClassPathItemType.CLASS, PATH);
        assertTrue(i2.compareTo(item) < 0);
        assertTrue(item.compareTo(i2) > 0);
        
        i2 = new ClassPathItem(ClassPathItemType.JAR, "C:\\Test\\Test2.jar");
        assertTrue(i2.compareTo(item) > 0);
        assertTrue(item.compareTo(i2) < 0);
        
        i2 = new ClassPathItem(ClassPathItemType.CLASS, "C:\\Test\\Test2.jar");
        assertTrue(i2.compareTo(item) < 0);
        assertTrue(item.compareTo(i2) > 0);
    }

    @Test
    public void testEquals() {
        ClassPathItem i2 = null;
        assertFalse(item.equals(i2));
        
        i2 = new ClassPathItem(ClassPathItemType.JAR, PATH);
        assertTrue(item.equals(i2));
        assertTrue(i2.equals(item));
        
        i2 = new ClassPathItem(ClassPathItemType.CLASS, PATH);
        assertFalse(item.equals(i2));
        assertFalse(i2.equals(item));
        
        i2 = new ClassPathItem(ClassPathItemType.JAR, "C:\\Test2.jar");
        assertFalse(item.equals(i2));
        assertFalse(i2.equals(item));
        
        i2 = new ClassPathItem(ClassPathItemType.CLASS, "C:\\Test2.jar");
        assertFalse(item.equals(i2));
        assertFalse(i2.equals(item));
    }

    @Test
    public void testHashCode() {
        ClassPathItem i2 = new ClassPathItem(ClassPathItemType.JAR, PATH);
        assertTrue(item.hashCode() == i2.hashCode());
        
        i2 = new ClassPathItem(ClassPathItemType.CLASS, PATH);
        assertFalse(item.hashCode() == i2.hashCode());
        
        i2 = new ClassPathItem(ClassPathItemType.JAR, "C:\\Test2.jar");
        assertFalse(item.hashCode() == i2.hashCode());
        
        i2 = new ClassPathItem(ClassPathItemType.CLASS, "C:\\Test2.jar");
        assertFalse(item.hashCode() == i2.hashCode());
    }

    @Test
    public void testToString() {
        String expected = "ClassPathItem [JAR; "+PATH+"]";
        assertEquals(expected, item.toString());
    }
}