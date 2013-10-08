/*
 * Copyright (c) 2010 Chris Böhme - Pinkmatter Solutions. All Rights Reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  o Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 *  o Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 *  o Neither the name of Chris Böhme, Pinkmatter Solutions, nor the names of
 *    any contributors may be used to endorse or promote products derived
 *    from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS;
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE
 * OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.jreserve.gui.misc.flamingo.modules;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JSeparator;
import org.openide.cookies.InstanceCookie;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.loaders.DataObject;

/**
 *
 * @author Chris
 */
public class ActionItems {

    private ActionItems() {
    }

    public static List<? extends ActionItem> forPath(String path) {
        List<ActionItem> actions = new ArrayList<ActionItem>();
        Map<String, FileObject> foMap = createFileObjectMap(path);
        Map<String, ActionItem> actionMap = new TreeMap<String, ActionItem>();
        
        Set<Item> items = getItemsForPath(foMap.values());
//        Collection<? extends Lookup.Item<Object>> items =
//                Lookups.forPath(path).lookupResult(Object.class).allItems();
        for (Item item : items) {
            connectToParent(item, path, actionMap, foMap);
            String rootItem = getRootName(makeRelative(item.getId(), path));
            ActionItem actionItem = actionMap.get(rootItem);
            if (!actions.contains(actionItem)) {
                actions.add(actionItem);
            }
        }

        return actions;
    }
    
    private static Set<Item> getItemsForPath(Collection<FileObject> files) {
        Set<Item> result = new LinkedHashSet<Item>();
        for(FileObject file : files) {
            Object o = loadObject(file);
            if(o != null)
                result.add(new Item(o, getId(file)));
        }
        return result;
    }
    
    private static Object loadObject(FileObject file) {
        if(file.isFolder())
            return null;
        try {
            DataObject dobj = DataObject.find(file);
            InstanceCookie ic = dobj.getLookup().lookup(InstanceCookie.class);
            return ic==null? null : ic.instanceCreate();
        } catch (Exception ex) {
            return null;
        }
    }
    
    private static String getId(FileObject file) {
        FileObject parent = file.getParent();
        if(parent == null)
            return file.getName();
        return parent.getPath()+"/"+file.getName();
    }

    private static Map<String, FileObject> createFileObjectMap(String rootPath) {
        FileObject root = FileUtil.getConfigRoot();
        FileObject folder = root.getFileObject(rootPath);
        Map<String, FileObject> map = new LinkedHashMap<String, FileObject>();
        if (folder != null) {
            for (FileObject fo : FileUtil.getOrder(Arrays.asList(folder.getChildren()), true)) {
                addToMap("", fo, map);
            }
        }
        return map;
    }

    private static void addToMap(String path, FileObject folder, Map<String, FileObject> map) {
        String absolutePath = "";
        if (path.length() > 0) {
            absolutePath += path + "/";
        }
        absolutePath += folder.getName();
        map.put(absolutePath, folder);
        for (FileObject fo : FileUtil.getOrder(Arrays.asList(folder.getChildren()), true)) {
            addToMap(absolutePath, fo, map);
        }
    }

    private static void connectToParent(
            Item item, 
            String rootPath,
            Map<String, ActionItem> actionMap, 
            Map<String, FileObject> foMap) {
        
        String name = makeRelative(item.getId(), rootPath);
        ActionItem action = getOrCreateActionItem(item, name, actionMap, foMap);
        if (action != null) {
            String parentName = getParentName(name);
            if (parentName != null) {
                ActionItem parent = getOrCreateFolderItem(parentName, actionMap, foMap);
                parent.addChild(action);
            }
        }

    }

    private static String getRootName(String name) {
        int index = name.indexOf('/');
        if (index > 0) {
            return name.substring(0, index);
        } else {
            return name;
        }

    }

    private static String getParentName(String name) {
        int index = name.lastIndexOf('/');
        if (index > 0) {
            String result = name.substring(0, index);
            return result;
        } else {
            return null;
        }
    }

    private static ActionItem getOrCreateFolderItem(String name, Map<String, ActionItem> actionMap,
            Map<String, FileObject> foMap) {
        ActionItem item = actionMap.get(name);
        if (item == null) {
            item = new ActionItem.Compound();
            actionMap.put(name, item);
            addProperties(item, foMap.get(name));
            item.setText(foMap.get(name).getName());
            String parentName = getParentName(name);
            if (parentName != null) {
                ActionItem parent = getOrCreateFolderItem(parentName, actionMap, foMap);
                parent.addChild(item);
            }
        }
        return item;
    }

    private static ActionItem getOrCreateActionItem(
            Item item, 
            String name,
            Map<String, ActionItem> actionMap,
            Map<String, FileObject> foMap) {
        ActionItem actionItem = actionMap.get(name);
        if (actionItem == null) {
            if (Action.class.isAssignableFrom(item.getType())) {
                Action instance = (Action) item.getInstance();
                if (instance != null) {
                    actionItem = ActionItem.leaf((Action) instance);
                }
            } else if (JSeparator.class.isAssignableFrom(item.getType())) {
                actionItem = ActionItem.separator();
                actionItem.setText(foMap.get(name).getName());
            } else if (JComponent.class.isAssignableFrom(item.getType())) {
                JComponent instance = (JComponent) item.getInstance();
                if (instance != null) {
                    actionItem = ActionItem.component((JComponent) instance);
                }
            } else {
                System.out.println("Unknown item: " + item.getType());
            }
            if (actionItem != null) {
                addProperties(actionItem, foMap.get(name));
                actionMap.put(name, actionItem);
            }
        }
        return actionItem;
    }

    private static void addProperties(ActionItem action, FileObject fo) {
        Enumeration<String> attrs = fo.getAttributes();
        while (attrs.hasMoreElements()) {
            String attr = attrs.nextElement();
            if (!"originalFile".equals(attr) && !"position".equals(attr)) {
                Object value = fo.getAttribute(attr);
                action.putValue(attr, value);
            }
        }
    }

    private static String makeRelative(String name, String rootPath) {
        String result = name.substring(rootPath.length(), name.length());
        if (result.startsWith("/")) {
            result = result.substring(1, result.length());
        }
        return result;
    }
    
    private static class Item {
        
        private final String id;
        private final Object obj;
        
        private Item(Object obj, String id) {
            this.id = id;
            this.obj = obj;
        }
        
        String getId() {
            return id;
        }
        
        Object getInstance() {
            return obj;
        }
        
        Class<?> getType() {
            return obj.getClass();
        }
        
        @Override
        public boolean equals(Object o) {
            return (o instanceof Item) &&
                   id.equals(((Item)o).id);
        }
        
        @Override
        public int hashCode() {
            return id.hashCode();
        }
        
        @Override
        public String toString() {
            return id;
        }
    }
}
