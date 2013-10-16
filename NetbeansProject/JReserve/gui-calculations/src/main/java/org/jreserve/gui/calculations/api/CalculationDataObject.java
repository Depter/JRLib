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

package org.jreserve.gui.calculations.api;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Icon;
import org.jdom2.Element;
import org.jreserve.gui.misc.utils.actions.AbstractDisplayableSavable;
import org.jreserve.gui.misc.utils.actions.deletable.DataObjectDeletable;
import org.jreserve.gui.misc.utils.actions.deletable.Deletable;
import org.jreserve.gui.misc.utils.widgets.Displayable;
import org.jreserve.gui.wrapper.jdom.JDomUtil;
import org.netbeans.api.actions.Savable;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataFolder;
import org.openide.loaders.DataObject;
import org.openide.loaders.DataObjectExistsException;
import org.openide.loaders.MultiDataObject;
import org.openide.loaders.MultiFileLoader;
import org.openide.util.Lookup;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;
import org.openide.util.lookup.ProxyLookup;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public abstract class CalculationDataObject extends MultiDataObject {

    private final static Logger logger = Logger.getLogger(CalculationDataObject.class.getName());
    private final static int LKP_VERSION = 1;
    
    protected final Object lock = new Object();
    protected final InstanceContent ic = new InstanceContent();
    private final Lookup lkp;
    
    public CalculationDataObject(FileObject fo, MultiFileLoader loader) throws DataObjectExistsException {
        super(fo, loader);
        lkp = new ProxyLookup(super.getLookup(), new AbstractLookup(ic));
    }
    
    protected static String getPath(final FileObject file) {
        synchronized(file) {
            return Displayable.Utils.displayProjectPath(file);
        }
    }
    
    @Override
    protected int associateLookup() {
        return LKP_VERSION;
    }
    
    @Override
    public Lookup getLookup() {
        return lkp;
    }

    protected final Object getThreadLock() {
        return lock;
    }
    
    @Override
    public String getName() {
        synchronized(lock) {
            return getPrimaryFile().getName();
        }
    }

    @Override
    protected void handleDelete() throws IOException {
        synchronized(lock) {
            super.handleDelete();
            getCalculation().events.fireDeleted();
        }
    }
    
    private AbstractCalculationProvider getCalculation() {
        AbstractCalculationProvider calculation = getLookup().lookup(AbstractCalculationProvider.class);
        if(calculation == null) {
            String msg = String.format("No instance of '%s' found in the lookup!", AbstractCalculationProvider.class.getName());
            logger.log(Level.SEVERE, msg);
            throw new IllegalStateException(msg);
        }
        return calculation;
    }

    @Override
    protected DataObject handleCopy(DataFolder df) throws IOException {
        synchronized(lock) {
            if(isModified())
                saveCalculation();
            CalculationDataObject result = (CalculationDataObject) super.handleCopy(df);
            result.getCalculation().events.fireCreated();
            return result;
        }
    }
    
    private void saveCalculation() throws IOException {
        FileObject pf = getPrimaryFile();
        try {
            AbstractCalculationProvider calculation = getCalculation();
            Element e = calculation.toXml();
            JDomUtil.save(pf, e);
            calculation.events.flushAuditCache();
            calculation.events.fireSave();
            setModified(false);
        } catch (IOException ex) {
            String msg = String.format("Unabel to save calculation to file ''%s''.", pf.getPath());
            logger.log(Level.SEVERE, msg, ex);
            throw ex;
        }
    } 

    @Override
    protected FileObject handleRename(String name) throws IOException {
        synchronized(lock) {
            if(isModified())
                saveCalculation();
            
            FileObject result = super.handleRename(name);
            getCalculation().setPath(getPath(result));
            return result;
        }
    }

    @Override
    protected FileObject handleMove(DataFolder df) throws IOException {
        synchronized(lock) {
            if(isModified())
                saveCalculation();
            
            FileObject result = super.handleMove(df);
            getCalculation().setPath(getPath(result));
            return result;
        }
    }

    @Override
    public void setModified(boolean modified) {
        synchronized(lock) {
            if(modified)
                addSavable();
            else 
                removeSavable();
            super.setModified(modified);
        }
    }
    
    private void addSavable() {
        if(getLookup().lookup(Savable.class) == null)
            ic.add(new ClaimTriangleSavable());
    }
    
    private void removeSavable() {
        Savable savable = getLookup().lookup(Savable.class);
        if(savable != null)
            ic.remove(savable);
    }
    
    protected Displayable getDisplayable() {
        synchronized(lock) {
            Displayable result = getLookup().lookup(Displayable.class);
            if(result == null)
                result = new Displayable.Node(getNodeDelegate());
            return result;
        }
    }
    
    @Override
    public boolean isModified() {
        synchronized(lock) {
            return super.isModified();
        }
    }
    
    @Override
    public boolean isDeleteAllowed() {
        synchronized(lock) {
            return lkp.lookup(Deletable.class) != null;
        }
    }
    
    protected void setDeleteAllowed(boolean allowed) {
        synchronized(lock) {
            Deletable deletable = lkp.lookup(Deletable.class);
            if(allowed && deletable == null) {
                ic.add(new CalculationDeletable());
            } else if(!allowed && deletable != null) {
                ic.remove(deletable);
            }
        }
    }
    
    @Override
    protected DataObject handleCopyRename(DataFolder df, String name, String ext) throws IOException {
        synchronized(lock) {
            if(isModified())
                saveCalculation();

            CalculationDataObject result = (CalculationDataObject) super.handleCopyRename(df, name, ext);
            result.getCalculation().events.fireCreated();
            return result;
        }
    }
    
    private class CalculationDeletable extends DataObjectDeletable {
        
        private Displayable displayable;
        
        private CalculationDeletable() {
            super(CalculationDataObject.this);
            displayable = getDisplayable();
        }

        @Override
        public Icon getIcon() {
            return displayable.getIcon();
        }

        @Override
        public String getDisplayName() {
            return displayable.getDisplayName();
        }
    }
    
    private class ClaimTriangleSavable extends AbstractDisplayableSavable {

        public ClaimTriangleSavable() {
            super(getDisplayable());
            register();
        }

        @Override
        protected void handleSave() throws IOException {
            synchronized(lock) {
                saveCalculation();
            }
        }

        @Override
        public boolean equals(Object obj) {
            return this == obj;
        }

        @Override
        public int hashCode() {
            return CalculationDataObject.this.hashCode();
        }
    }
}
