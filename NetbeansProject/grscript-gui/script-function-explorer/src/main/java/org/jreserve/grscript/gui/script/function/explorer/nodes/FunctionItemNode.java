package org.jreserve.grscript.gui.script.function.explorer.nodes;

import java.awt.Image;
import org.jreserve.grscript.gui.script.function.explorer.FunctionItem;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.util.ImageUtilities;
import org.openide.util.lookup.Lookups;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class FunctionItemNode extends AbstractNode {

    private final static Image FUNCTION_IMG = ImageUtilities.loadImage(FunctionFolderNode.IMG_PATH+"function.png");
    private final static Image PROPERTY_IMG = ImageUtilities.loadImage(FunctionFolderNode.IMG_PATH+"property.png");
    private FunctionItem item;
    
    public FunctionItemNode(FunctionItem item) {
        super(Children.LEAF, Lookups.singleton(item));
        this.item = item;
        setDisplayName(item.getSigniture());
    }
    

    @Override
    public Image getIcon(int type) {
        return item.isProperty()? PROPERTY_IMG : FUNCTION_IMG;
    }
}
