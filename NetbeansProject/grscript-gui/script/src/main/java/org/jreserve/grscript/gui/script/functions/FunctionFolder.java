package org.jreserve.grscript.gui.script.functions;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class FunctionFolder implements Comparable<FunctionFolder> {
    
    private static FunctionFolder root;
    
    public static FunctionFolder getRoot() {
        if(root == null)
            root = FunctionFolderLoader.getRoot();
        return root;
    }
    
    private String name;
    private Set<FunctionFolder> folders = new TreeSet<FunctionFolder>();
    private List<FunctionProviderAdapter> adapters = new ArrayList<FunctionProviderAdapter>();
    
    FunctionFolder(String name) {
        this.name = name;
    }
    
    public String getName() {
        return name;
    }

    public List<FunctionFolder> getFolders() {
        return new ArrayList<FunctionFolder>(folders);
    }
    
    boolean addFolder(FunctionFolder folder) {
        return folders.add(folder);
    }
    
    public List<FunctionProviderAdapter> getAdapters() {
        return new ArrayList<FunctionProviderAdapter>(adapters);
    }
    
    void addAdapter(FunctionProviderAdapter adapter) {
        adapters.add(adapter);
    }
    
    @Override
    public int compareTo(FunctionFolder o) {
        return name.compareToIgnoreCase(o.name);
    }
}
