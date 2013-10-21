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

package org.jreserve.gui.misc.namedcontent.dialog;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.Icon;
import org.jreserve.gui.misc.namedcontent.NamedContent;
import org.jreserve.gui.misc.namedcontent.NamedContentChooserController;
import org.jreserve.gui.misc.utils.widgets.CommonIcons;
import org.openide.loaders.DataFolder;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class TreeFolder implements TreeItem {

    static TreeFolder createFolders(NamedContentChooserController controller) {
        TreeFolder root = new TreeFolder();
        for(NamedContent nc : controller.getRoot().getContents())
            addFolderChildren(root, nc);
        return root;
    }
    
    private static void addFolderChildren(TreeFolder parent, NamedContent nc) {
        if(nc.getLookup().lookup(DataFolder.class) != null) {
            TreeFolder child = (TreeFolder) parent.addItem(nc);
            for(NamedContent childNc : nc.getContents())
                addFolderChildren(child, childNc);
        }
    }
    
    static TreeFolder createRoot(NamedContentChooserController controller) {
        TreeFolder root = new TreeFolder();
        for(NamedContent nc : controller.getRoot().getContents())
            addChildren(root, nc);
        
        root.cleanUp(controller);
        return root;
    }
    
    private static void addChildren(TreeFolder parent, NamedContent nc) {
        TreeItem item = parent.addItem(nc);
        if(item instanceof TreeFolder) {
            TreeFolder tf = (TreeFolder) item;
            for(NamedContent child : nc.getContents())
                addChildren(tf, child);
        }
    }
    
    private String name;
    private TreeItem parent;
    
    private List<TreeFolder> folders = new ArrayList<TreeFolder>();
    private List<TreeFile> files = new ArrayList<TreeFile>();
    
    TreeFolder() {
        name = "";
    }
    
    private TreeFolder(TreeItem parent, String name) {
        this.name = name;
        this.parent = parent;
    }
    
    @Override
    public String getPath() {
        String path = parent==null? "" : parent.getPath();
        if(path.length() > 0)
            path += "/";
        return path+name;
    }
    
    TreeItem addItem(NamedContent nc) {
        if(nc.getLookup().lookup(DataFolder.class) != null) {
            return getFolder(nc.getDisplayName());
        } else {
            TreeFile file = new TreeFile(this, nc);
            files.add(file);
            return file;
        }
    }
    
    private TreeFolder getFolder(String name) {
        for(TreeFolder folder : folders)
            if(name.equals(folder.name))
                return folder;
        
        TreeFolder folder = new TreeFolder(this, name);
        folders.add(folder);
        return folder;
    }
    
    @Override
    public String getName() {
        return name;
    }
    
    @Override
    public Icon getIcon() {
        return CommonIcons.folder();
    }

    @Override
    public List<? extends TreeItem> getChildren() {
        List<TreeItem> children = new ArrayList<TreeItem>(getChildCount());
        children.addAll(folders);
        children.addAll(files);
        return children;
    }

    @Override
    public int getChildCount() {
        return folders.size() + files.size();
    }
    
    @Override
    public boolean containsAcceptable(NamedContentChooserController controller) {
        for(TreeFolder folder : folders)
            if(folder.containsAcceptable(controller))
                return true;
        for(TreeFile file : files)
            if(file.containsAcceptable(controller))
                return true;
        return false;
    }
    
    @Override
    public void cleanUp(NamedContentChooserController controller) {
        Iterator<TreeFile> fileIt = files.iterator();
        while(fileIt.hasNext()) {
            TreeFile file = fileIt.next();
            file.cleanUp(controller);
            if(file.getChildCount()==0 && !file.containsAcceptable(controller))
                fileIt.remove();
        }
        
        Iterator<TreeFolder> fit = folders.iterator();
        while(fit.hasNext()) {
            TreeFolder folder = fit.next();
            folder.cleanUp(controller);
            if(folder.getChildCount() == 0 && !controller.acceptsFolder())
                fit.remove();
        }
    }
    
    @Override
    public String toString() {
        return "TreeFolder ["+name+"]";
    }
}
