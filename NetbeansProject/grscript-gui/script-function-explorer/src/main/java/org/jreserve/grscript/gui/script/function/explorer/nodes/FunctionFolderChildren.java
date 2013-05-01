package org.jreserve.grscript.gui.script.function.explorer.nodes;

import java.util.List;
import org.jreserve.grscript.gui.script.functions.FunctionProviderAdapter;
import org.jreserve.grscript.gui.script.function.explorer.FunctionItem;
import org.jreserve.grscript.gui.script.functions.FunctionFolder;
import org.openide.nodes.ChildFactory;
import org.openide.nodes.Node;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class FunctionFolderChildren extends ChildFactory<Object> {

    private FunctionFolder folder;

    public FunctionFolderChildren(FunctionFolder folder) {
        this.folder = folder;
    }
    
    @Override
    protected boolean createKeys(List<Object> list) {
        list.addAll(folder.getFolders());
        addProperties(list);
        addFunctions(list);
        return true;
    }
    
    private void addProperties(List<Object> list) {
        for(FunctionProviderAdapter adapter : folder.getAdapters())
            for(String signiture : adapter.getPropertyNames())
                list.add(new FunctionItem(adapter, signiture, true));
    }
    
    private void addFunctions(List<Object> list) {
        for(FunctionProviderAdapter adapter : folder.getAdapters())
            for(String signiture : adapter.getFunctionSignitures())
                list.add(new FunctionItem(adapter, signiture, false));
    }

    @Override
    protected Node createNodeForKey(Object key) {
        if(key instanceof FunctionFolder)
            return new FunctionFolderNode((FunctionFolder)key);
        else
            return new FunctionItemNode((FunctionItem)key);
    }    
}
