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
package org.jreserve.gui.data.editor;

import java.io.Serializable;
import java.util.Collection;
import javax.swing.SwingUtilities;
import org.jreserve.gui.data.api.DataSource;
import org.netbeans.core.api.multiview.MultiViews;
import org.openide.util.Lookup;
import org.openide.util.lookup.Lookups;
import org.openide.windows.Mode;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class EditorUtil {
    
    private final static String EDITOR_MODE = "editor";
    
    public static void openEditor(final DataSource ds) {
        if(SwingUtilities.isEventDispatchThread()) {
            openInEDT(ds);
        } else {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    openInEDT(ds);
                }
            });
        }
    }
    
    private static void openInEDT(DataSource ds) {
        TopComponent tc = getTc(ds);
        if(!tc.isOpened())
            tc.open();
        tc.requestActive();
    }
    
    private static TopComponent getTc(DataSource ds) {
        for(TopComponent tc : TopComponent.getRegistry().getOpened())
            if(isEditorTc(ds, tc))
                return tc;
        return createTc(ds);
    }
    
    private static boolean isEditorTc(DataSource ds, TopComponent tc) {
        Collection<? extends DataSource> all = tc.getLookup().lookupAll(DataSource.class);
        return all.size() == 1 &&
               all.iterator().next() == ds &&
               MultiViews.findMultiViewHandler(tc) != null; //not the ProjectExplorer
    }
    
    private static TopComponent createTc(DataSource ds) {
        TopComponent tc = MultiViews.createMultiView(DataSource.MIME_TYPE, new DataSourceContext(ds));
        Mode editor = WindowManager.getDefault().findMode(EDITOR_MODE);
        if(editor != null)
            editor.dockInto(tc);
        return tc;
    }
    
    private static class DataSourceContext implements Lookup.Provider, Serializable {
        
        private final Lookup lkp;
        
        private DataSourceContext(DataSource ds) {
            lkp = Lookups.singleton(ds);
        }
        
        @Override
        public Lookup getLookup() {
            return lkp;
        }
    }
}
