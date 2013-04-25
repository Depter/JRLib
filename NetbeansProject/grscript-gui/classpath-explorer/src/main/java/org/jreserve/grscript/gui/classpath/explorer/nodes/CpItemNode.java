package org.jreserve.grscript.gui.classpath.explorer.nodes;

import org.jreserve.grscript.gui.classpath.registry.ClassPathItem;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.util.lookup.Lookups;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class CpItemNode extends AbstractNode {

    private ClassPathItem item;
    
    public CpItemNode(ClassPathItem item) {
        super(Children.LEAF, Lookups.singleton(item));
        this.item = item;
        initDisplayName();
        super.setShortDescription(item.getPath());
    }

    private void initDisplayName() {
        String path = item.getPath();
        setShortDescription(path);
        setDisplayName(getName(path));
    }
    
    private String getName(String path) {
        return new java.io.File(path).getName();
    }
}
