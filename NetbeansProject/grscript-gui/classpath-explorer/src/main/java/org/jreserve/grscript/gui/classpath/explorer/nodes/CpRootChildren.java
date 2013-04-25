package org.jreserve.grscript.gui.classpath.explorer.nodes;

import java.util.Arrays;
import java.util.List;
import org.openide.nodes.ChildFactory;
import org.openide.nodes.Node;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class CpRootChildren extends ChildFactory<CpRootType> {

    @Override
    protected boolean createKeys(List<CpRootType> list) {
        list.addAll(Arrays.asList(CpRootType.values()));
        return true;
    }

    @Override
    protected Node createNodeForKey(CpRootType key) {
        return new CpCategoryRootNode(key);
    }
}
