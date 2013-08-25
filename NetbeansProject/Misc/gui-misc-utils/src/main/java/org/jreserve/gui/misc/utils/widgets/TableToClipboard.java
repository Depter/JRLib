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

package org.jreserve.gui.misc.utils.widgets;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.TableModel;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class TableToClipboard {
    
    public static interface Renderer {
        
        public String toString(Object value);
    }
    
    public static class DefaultRenderer implements Renderer {
        @Override
        public String toString(Object value) {
            return value==null? "" : value.toString();
        }
    }
    
    private final static Logger logger = Logger.getLogger(TableToClipboard.class.getName());
    private final static Renderer DEFAULT_RENDERER = new DefaultRenderer();
    
    private Map<Class, Renderer> defaultRenderers = new HashMap<Class, Renderer>();
    
    public void setRenderer(Class clazz, Renderer renderer) {
        if(renderer == null)
            defaultRenderers.remove(clazz);
        else
            defaultRenderers.put(clazz, renderer);
    }
    
    public Renderer getRenderer(Class clazz) {
        return defaultRenderers.get(clazz);
    }
    
    private int cCount;
    private int rCount;
    private Renderer[] renderers;
    private StringBuilder sb;
    
    public void toClipboard(TableModel model, boolean withHeader) {
        initState(model);
        buildString(model, withHeader);
        putToClipboard();
        clearState();
    }
    
    private void initState(TableModel model) {
        cCount = model.getColumnCount();
        rCount = model.getRowCount();
        sb = new StringBuilder();
        renderers = new Renderer[cCount];
        for(int c=0; c<cCount; c++)
            renderers[c] = getNonNullRenderer(model.getColumnClass(c));
    }
    
    private Renderer getNonNullRenderer(Class clazz) {
        Renderer r = defaultRenderers.get(clazz);
        return r==null? DEFAULT_RENDERER : r;
    }
    
    private void buildString(TableModel model, boolean withHeader) {
        if(withHeader)
            buildHeader(model);
        for(int r=0; r<rCount; r++) {
            if(sb.length() > 0) sb.append('\n');
            for(int c=0; c<cCount; c++) {
                if(c>0) sb.append('\t');
                sb.append(renderers[c].toString(model.getValueAt(r, c)));
            }
        }
    }
    
    private void buildHeader(TableModel model) {
        for(int c=0; c<cCount; c++) {
            if(c > 0)
                sb.append('\t');
            sb.append(model.getColumnName(c));
        }
    }
    
    private void putToClipboard() {
        try {
            StringSelection sel = new StringSelection(sb.toString());
            Clipboard cb = Toolkit.getDefaultToolkit().getSystemClipboard();
            cb.setContents(sel, null);
        } catch (Exception ex) {
            logger.log(Level.WARNING, "Unable to put text on system clipboard!", ex);
        }
    }
    
    private void clearState() {
        cCount = 0;
        rCount = 0;
        sb = null;
        renderers = null;
    }
}
