package org.jreserve.grscript.gui.classpath;

import org.jreserve.grscript.gui.classpath.registry.ClassPathItem;
import org.jreserve.grscript.gui.eventbus.EventBusRegistry;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class ClassPathEvent {
    
    public static void publishAdd(ClassPathItem item) {
        ClassPathEvent evt = new ClassPathEvent(item, Type.ADDED);
        EventBusRegistry.getDefault().publishEvent(evt);
    }
    
    public static void publishDelete(ClassPathItem item) {
        ClassPathEvent evt = new ClassPathEvent(item, Type.DELETED);
        EventBusRegistry.getDefault().publishEvent(evt);
    }
    
    private final ClassPathItem item;
    private final Type type;
    
    public ClassPathEvent(ClassPathItem item, Type type) {
        if(item == null)
            throw new NullPointerException("ClassPathItem is null!");
        this.item = item;
        
        if(type == null)
            throw new NullPointerException("Type is null!");
        this.type = type;
    }
    
    public ClassPathItem getItem() {
        return item;
    }
    
    public Type getType() {
        return type;
    }
    
    @Override
    public boolean equals(Object o) {
        return (o instanceof ClassPathEvent) &&
               equals((ClassPathEvent)o);
    }
    
    public boolean equals(ClassPathEvent evt) {
        return type.equals(evt.type) &&
               item.equals(evt.item);
    }
    
    @Override
    public int hashCode() {
        return 17 * type.hashCode() + item.hashCode();
    }
    
    public static enum Type {
        ADDED,
        DELETED;
    }
}
