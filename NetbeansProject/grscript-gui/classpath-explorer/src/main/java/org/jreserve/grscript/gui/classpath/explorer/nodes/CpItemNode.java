package org.jreserve.grscript.gui.classpath.explorer.nodes;

import java.awt.Image;
import org.jreserve.grscript.gui.classpath.registry.ClassPathItem;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.util.ImageUtilities;
import org.openide.util.lookup.Lookups;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class CpItemNode extends AbstractNode {
    
    final static String IMG_PATH = "org/jreserve/grscript/gui/classpath/explorer/nodes/";
    private final static Image JAR_IMG = ImageUtilities.loadImage(IMG_PATH+"jar.png");
    private final static Image CLASS_IMG = ImageUtilities.loadImage(IMG_PATH+"class.png");

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

    @Override
    public Image getIcon(int type) {
        switch(item.getType()) {
            case JAR: return JAR_IMG;
            case CLASS: return CLASS_IMG;
            default: return super.getIcon(type);
        }
    }
}
