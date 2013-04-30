package org.jreserve.grscript.gui.script.function.explorer.nodes;

import org.jreserve.grscript.gui.script.function.explorer.FunctionItem;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.util.lookup.Lookups;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class FunctionItemNode extends AbstractNode {

    private FunctionItem item;

    public FunctionItemNode(FunctionItem item) {
        super(Children.LEAF, Lookups.singleton(item));
        this.item = item;
        setDisplayName(item.getSigniture());
    }
    
    
}
