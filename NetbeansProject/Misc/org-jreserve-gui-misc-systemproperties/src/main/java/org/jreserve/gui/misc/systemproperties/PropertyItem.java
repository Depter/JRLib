/*
 *  Copyright (C) 2013, Peter Decsi.
 * 
 *  This library is free software: you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public 
 *  License as published by the Free Software Foundation, either 
 *  version 3 of the License, or (at your option) any later version.
 * 
 *  This library is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 * 
 *  You should have received a copy of the GNU General Public License
 *  along with this library.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.jreserve.gui.misc.systemproperties;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class PropertyItem implements Comparable<PropertyItem>{
    
    static PropertyItem createRoot() {
        PropertyItem root = new PropertyItem(null, "root");
        for(Object key : System.getProperties().keySet())
            createItem(root, asPath(key));
        return root;
    }
    
    private static List<String> asPath(Object key) {
        String str = key==null? "null" : key.toString();
        return new ArrayList<String>(Arrays.asList(str.split("\\.")));
    }
    
    private static void createItem(PropertyItem root, List<String> path) {
        if(path.isEmpty())
            return;
        
        String name = path.remove(0);
        for(PropertyItem item : root.children) {
            if(item.name.equalsIgnoreCase(name))
                createItem(item, path);
        }
        
        String strRoot = root.root;
        strRoot = strRoot==null? name : strRoot+"."+name;
        PropertyItem item = new PropertyItem(strRoot, name);
        root.children.add(item);
        
        createItem(item, path);
    }
    
    private String root;
    private String name;
    Set<PropertyItem> children = new TreeSet<PropertyItem>();

    public PropertyItem(String root, String name) {
        this.root = root;
        this.name = name;
    }
    
    public String getName() {
        return name;
    }
    
    public String getRoot() {
        return root;
    }
    
    public Object getValue() {
        Object value = root==null? null : System.getProperty(root);
        return value;
        //return root==null? null : System.getProperty(root);
    }

    public Set<PropertyItem> getChildren() {
        return children;
    }
    
    @Override
    public int compareTo(PropertyItem o) {
        if(root == null)
            return o.root==null? 0 : -1;
        return o.root==null? 1 : root.compareToIgnoreCase(o.root);
    }
    
    @Override
    public String toString() {
        return name;
    }
}
