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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.jreserve.gui.misc.namedcontent.dialog.NamedContentChooserPanel;
import org.netbeans.api.project.Project;
import org.openide.loaders.DataFolder;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class NamedContentUtil {
    
    private final static char PATH_SEPARATOR = '/';
    
    public static NamedContent getContent(Project p, String path) {
        for(NamedContentProvider npc : p.getLookup().lookupAll(NamedContentProvider.class)) {
            NamedContent nc = getContent(npc, path);
            if(nc != null)
                return nc;
        }
        return null;
    }
    
    public static NamedContent getContent(NamedContentProvider parent, String path) {
        int index = path.indexOf(PATH_SEPARATOR);
        if(index == 0)
            return getContent(parent, path.substring(1));
        
        if(index < 0)
            return getChild(parent, path);
        
        String name = path.substring(0, index);
        path = path.substring(index+1);
        NamedContent child = getChild(parent, name);
        return child==null? null : getContent(parent, path);
    }
    
    private static NamedContent getChild(NamedContentProvider parent, String name) {
        for(NamedContent child : parent.getContents())
            if(child.getDisplayName().equalsIgnoreCase(name))
                return child;
        return null;
    }
    
    public static <T> T getContent(Project p, String path, Class<T> clazz) {
        for(NamedContentProvider npc : p.getLookup().lookupAll(NamedContentProvider.class)) {
            T result = getContent(npc, path, clazz);
            if(result != null)
                return result;
        }
        return null;
    }
    
    public static <T> T getContent(NamedContentProvider parent, String path, Class<T> clazz) {
        int index = path.indexOf(PATH_SEPARATOR);
        if(index == 0)
            return getContent(parent, path.substring(1), clazz);
        
        if(index < 0) {
            NamedContent child = getChild(parent, path);
            return child==null? null : child.getLookup().lookup(clazz);
        }
        
        String name = path.substring(0, index);
        path = path.substring(index+1);
        NamedContent child = getChild(parent, name);
        return child==null? null : getContent(parent, path, clazz);
    }
    
    
    public static <T> Collection<? extends T> getContents(Project p, String path, Class<T> clazz) {
        for(NamedContentProvider npc : p.getLookup().lookupAll(NamedContentProvider.class)) {
            Collection<? extends T> result = getContents(npc, path, clazz);
            if(result.size() > 0)
                return result;
        }
        return Collections.EMPTY_LIST;
    }
    
    public static <T> Collection<? extends T> getContents(NamedContentProvider parent, String path, Class<T> clazz) {
        int index = path.indexOf(PATH_SEPARATOR);
        if(index == 0)
            return getContents(parent, path.substring(1), clazz);
        
        if(index < 0) {
            NamedContent child = getChild(parent, path);
            return child==null? null : child.getLookup().lookupAll(clazz);
        }
        
        String name = path.substring(0, index);
        path = path.substring(index+1);
        NamedContent child = getChild(parent, name);
        return child==null? Collections.EMPTY_LIST : getContents(parent, path, clazz);
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
    
    public static DataFolder userSelectFolder(NamedContentProvider root) {
        return userSelect(new FolderContentChooserController(root), DataFolder.class);
    }
    
    public static List<DataFolder> userSelectFolders(NamedContentProvider root) {
        return userMultiSelect(new FolderContentChooserController(root), DataFolder.class);
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
