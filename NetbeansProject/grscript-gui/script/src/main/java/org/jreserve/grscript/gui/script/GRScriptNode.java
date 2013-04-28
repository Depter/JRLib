package org.jreserve.grscript.gui.script;

import java.io.IOException;
import org.openide.loaders.DataNode;
import org.openide.nodes.Children;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class GRScriptNode extends DataNode {

    public GRScriptNode(GRscriptDataObject obj) {
        super(obj, Children.LEAF);
    }

    @Override
    public boolean canDestroy() {
        return super.getDataObject().isDeleteAllowed();
    }

    @Override
    public void destroy() throws IOException {
        super.destroy(); //To change body of generated methods, choose Tools | Templates.
        super.getDataObject().delete();
    }
    
}
