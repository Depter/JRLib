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
package org.jreserve.grscript.gui.script.functions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jreserve.grscript.gui.script.registration.FunctionProviderAdapterRegistrationProcessor;
import org.openide.cookies.InstanceCookie;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.loaders.DataObject;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class FunctionFolderLoader {
    
    private final static Logger logger = Logger.getLogger(FunctionFolderLoader.class.getName());
    
    private final static Comparator<FileObject> COMPARATOR = new Comparator<FileObject>() {

        @Override
        public int compare(FileObject o1, FileObject o2) {
            int p1 = getPosition(o1);
            int p2 = getPosition(o2);
            int dif = p1 - p2;
            return dif!=0? dif : o1.getName().compareToIgnoreCase(o2.getName());
        }
        
        private int getPosition(FileObject fo) {
            Object p = fo.getAttribute(FunctionProviderAdapterRegistrationProcessor.POSITION);
            if((p instanceof Integer))
                return ((Integer)p).intValue();
            return Integer.MAX_VALUE;
        }
    };
    
    static FunctionFolder getRoot() {
        FileObject fo = FileUtil.getConfigFile(FunctionProviderAdapterRegistrationProcessor.ENTITY_DIRECTORY);
        FunctionFolder root = new FunctionFolder(fo.getName());
        initialize(fo, root);
        return root;
    }
    
    private static void initialize(FileObject dir, FunctionFolder folder) {
        for(FileObject child : Collections.list(dir.getFolders(false))) {
            FunctionFolder f = new FunctionFolder(child.getName());
            folder.addFolder(f);
            initialize(child, f);
        }
        
        ArrayList<? extends FileObject> children = Collections.list(dir.getData(false));
        Collections.sort(children, COMPARATOR);
        for(FileObject child : children)
            loadAdapter(child, folder);
    }
    
    private static void loadAdapter(FileObject fo, FunctionFolder folder) {
        try {
            DataObject dObj = DataObject.find(fo);
            Object instance = dObj.getLookup().lookup(InstanceCookie.class).instanceCreate();
            if(instance instanceof FunctionProviderAdapter)
                folder.addAdapter((FunctionProviderAdapter)instance);
        } catch (Exception ex) {
            String msg = "Unable to load FunctionProviderAdapter from: "+fo.getPath();
            logger.log(Level.WARNING, msg, ex);
        }
    }
    
    private FunctionFolderLoader() {}
}
