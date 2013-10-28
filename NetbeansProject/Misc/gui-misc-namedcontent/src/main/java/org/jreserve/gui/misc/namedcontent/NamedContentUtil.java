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

package org.jreserve.gui.misc.namedcontent;

import java.beans.BeanInfo;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.swing.Icon;
import org.jreserve.gui.misc.namedcontent.dialog.NamedContentChooserPanel;
import org.jreserve.gui.misc.utils.widgets.CommonIcons;
import org.jreserve.gui.misc.utils.widgets.Displayable;
import org.jreserve.gui.misc.utils.widgets.EmptyIcon;
import org.netbeans.api.project.FileOwnerQuery;
import org.netbeans.api.project.Project;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.loaders.DataFolder;
import org.openide.loaders.DataObject;
import org.openide.nodes.Node;
import org.openide.util.ImageUtilities;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class NamedContentUtil {
    
    private final static char PATH_SEPARATOR = '/';
    
    public static FileObject getContentRoot(final FileObject file) {
        synchronized(file) {
            Project p = FileOwnerQuery.getOwner(file);
            if(p == null)
                return null;
            
            for(NamedContentProvider ncp : p.getLookup().lookupAll(NamedContentProvider.class)) {
                FileObject root = ncp.getRoot();
                if(root.equals(file) || FileUtil.isParentOf(root, file))
                    return root;
            }
            
            return null;
        }
    }
    
    public static NamedContent getContent(Project p, String path) {
        for(NamedContentProvider npc : p.getLookup().lookupAll(NamedContentProvider.class)) {
            NamedContent nc = getContent(npc, path);
            if(nc != null)
                return nc;
        }
        return null;
    }
    
    public static NamedContent getContent(NamedContentProvider ncp, String path) {
        int index = path.indexOf(PATH_SEPARATOR);
        if(index == 0)
            return getContent(ncp, path.substring(1));
        
        String name;
        if(index > 0) {
            name = path.substring(0, index);
            path = path.substring(index+1);
        } else {
            name = path;
        }
        
        for(NamedContent nc : ncp.getContents())
            if(name.equals(nc.getName()))
                return index<0? nc : getContent(nc, path);
        
        return null;
    }
    
    public static NamedContent getContent(NamedContent nc, String path) {
        int index = path.indexOf(PATH_SEPARATOR);
        if(index == 0)
            return getContent(nc, path.substring(1));
        
        String name;
        if(index < 0) {
            name = path;
            path = null;
        } else {
            name = path.substring(0, index);
            path = path.substring(index+1);
        }
        
        NamedContent child = getChild(nc, name);
        if(child == null)
            return null;
        
        return path==null? child : getContent(child, path);
    }
    
    public static NamedContent getChild(NamedContent parent, String name) {
        for(NamedContent child : parent.getContents())
            if(child.getName().equals(name))
                return child;
        return null;
    }
    
    public static <T> T getContent(Project p, String path, Class<T> clazz) {
        NamedContent nc = getContent(p, path);
        return nc==null? null : nc.getLookup().lookup(clazz);
    }
    
    public static <T> T getContent(NamedContentProvider ncp, String path, Class<T> clazz) { 
        NamedContent nc = getContent(ncp, path);
        return nc==null? null : nc.getLookup().lookup(clazz);
    }
    
    public static <T> T getContent(NamedContent nc, String path, Class<T> clazz) { 
        nc = getContent(nc, path);
        return nc==null? null : nc.getLookup().lookup(clazz);
    }
    
    public static <T> Collection<? extends T> getContents(Project p, String path, Class<T> clazz) {
        NamedContent nc = getContent(p, path);
        return nc==null? null : nc.getLookup().lookupAll(clazz);
    }
    
    public static <T> Collection<? extends T> getContents(NamedContentProvider ncp, String path, Class<T> clazz) { 
        NamedContent nc = getContent(ncp, path);
        return nc==null? null : nc.getLookup().lookupAll(clazz);
    }
    
    public static <T> Collection<? extends T> getContents(NamedContent nc, String path, Class<T> clazz) { 
        nc = getContent(nc, path);
        return nc==null? null : nc.getLookup().lookupAll(clazz);
    }
    
    public static NamedContent userSelect(NamedContentChooserController controller) {
        List<NamedContent> selection = NamedContentChooserPanel.selectContent(controller, false);
        return selection.isEmpty()? null : selection.get(0);
    }
    
    public static List<NamedContent> userMultiSelect(NamedContentChooserController controller) {
        return NamedContentChooserPanel.selectContent(controller, true);
    }
    
    public static <T> T userSelect(NamedContentChooserController controller, Class<T> clazz) {
        NamedContent nc = userSelect(controller);
        return nc==null? null : nc.getLookup().lookup(clazz);
    }
    
    public static <T> List<T> userMultiSelect(NamedContentChooserController controller, Class<T> clazz) {
        List<NamedContent> ncs = userMultiSelect(controller);
        return convert(ncs, clazz);
    }
    
    public static <T> T userSelect(NamedContentProvider root, Class<T> clazz, String title) {
        NamedContentChooserController controller = new DefaultContentChooserController(root, clazz, title);
        return userSelect(controller, clazz);
    }
    
    public static <T> List<T> userMultiSelect(NamedContentProvider root, Class<T> clazz, String title) {
        NamedContentChooserController controller = new DefaultContentChooserController(root, clazz, title);
        return userMultiSelect(controller, clazz);
    }
    
    private static <T> List<T> convert(List<NamedContent> contents, Class<T> clazz) {
        List<T> result = new ArrayList<T>(contents.size());
        for(NamedContent nc : contents) {
            T t = nc.getLookup().lookup(clazz);
            if(t != null)
                result.add(t);
        }
        return result;
    }
    
    public static String userSelectFolder(NamedContentProvider root) {
        NamedContentChooserController controller = new FolderContentChooserController(root);
        List<String> folders = NamedContentChooserPanel.selectFolder(controller, false);
        return folders.isEmpty()? null : folders.get(0);
    }
    
    public static List<String> userSelectFolders(NamedContentProvider root) {
        NamedContentChooserController controller = new FolderContentChooserController(root);
        return NamedContentChooserPanel.selectFolder(controller, true);
    }
    
    public static ProjectContentProvider createContentProvider(Project project) {
        return new ProjectContentProviderImpl(project);
    }
    
    public static ProjectContentProvider getContentProvider(Project project) {
        ProjectContentProvider pcp = project.getLookup().lookup(ProjectContentProvider.class);
        if(pcp == null)
            return new ProjectContentProviderImpl(project);
        return pcp;
    }
    
    public static Icon getIcon(NamedContent nc) {
        Displayable d = nc.getLookup().lookup(Displayable.class);
        if(d != null)
            return d.getIcon();
        
        DataObject obj = nc.getLookup().lookup(DataObject.class);
        if(obj == null)
            return EmptyIcon.EMPTY_16;
        
        if(obj instanceof DataFolder)
            return CommonIcons.folder();
        
        d = obj.getLookup().lookup(Displayable.class);
        if(d != null)
            return d.getIcon();
        
        Node n = obj.getNodeDelegate();
        return ImageUtilities.image2Icon(n.getIcon(BeanInfo.ICON_COLOR_16x16));
    }
    
//  TODO
//  userSelectOne(Project p, Class<T> clazz): T
//  userSelectMultiple(Project p, Class<T> clazz): List<T>

    private NamedContentUtil() {}

    private static class ProjectContentProviderImpl implements ProjectContentProvider {

        private Project project;
        
        private ProjectContentProviderImpl(Project project) {
            this.project = project;
        }
        
        @Override
        public NamedContent getContent(String path) {
            return NamedContentUtil.getContent(project, path);
        }

        @Override
        public <T> T getContent(String path, Class<T> clazz) {
            return NamedContentUtil.getContent(project, path, clazz);
        }

        @Override
        public <T> Collection<? extends T> getContents(String path, Class<T> clazz) {
            return NamedContentUtil.getContents(project, path, clazz);
        }
    
    }
}
