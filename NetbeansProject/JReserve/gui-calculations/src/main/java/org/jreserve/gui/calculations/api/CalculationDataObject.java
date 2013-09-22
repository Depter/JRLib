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
import org.jdom2.Element;
import org.jreserve.gui.misc.audit.event.AuditedObject;
import org.jreserve.gui.misc.utils.actions.Deletable;
import org.jreserve.gui.misc.utils.widgets.Displayable;
import org.jreserve.gui.wrapper.jdom.JDomUtil;
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
public abstract class CalculationDataObject<C extends CalculationProvider> extends MultiDataObject {
    
    private final static Logger logger = Logger.getLogger(CalculationDataObject.class.getName());
    private final static int LKP_VERSION = 1;
    
    protected final InstanceContent ic = new InstanceContent();
    private final Lookup lkp;
    protected final C calculation;
    protected final CalculationEventUtil2 evtUtil;
    
    protected CalculationDataObject(FileObject fo, MultiFileLoader loader) throws DataObjectExistsException, IOException {
        super(fo, loader);
        registerEditor(getMimeType(), true);
        
        try {
            Element root = JDomUtil.getRootElement(fo);
            calculation = createCalculationProvider(root);
            ic.add(calculation);
        
            ic.add(new CalculationDeletable());
        
            lkp = new ProxyLookup(super.getLookup(), new AbstractLookup(ic));
            if(lkp.lookup(CalculationDataObject.class) == null)
                ic.add(this);
            
            AuditedObject auditedObject = createAuditedObject();
            ic.add(auditedObject);
            evtUtil = new CalculationEventUtil2(auditedObject, calculation);
        } catch (Exception ex) {
            String msg = "Unable to create calculation from file '%s'!";
            msg = String.format(msg, fo.getPath());
            logger.log(Level.SEVERE, msg, ex);
            throw new IOException(msg, ex);
        }
    }

    protected abstract String getMimeType();
    
    protected abstract C createCalculationProvider(Element root) throws Exception;
    
    protected abstract AuditedObject createAuditedObject();
    
    protected abstract Displayable createDisplayable();
    
    @Override
    protected int associateLookup() {
        return LKP_VERSION;
    }
    
    @Override
    public Lookup getLookup() {
        return lkp;
    }

    @Override
    public String getName() {
        synchronized(calculation) {
            return getPrimaryFile().getName();
        }
    }

    @Override
    protected void handleDelete() throws IOException {
        synchronized(calculation) {
            super.handleDelete();
            evtUtil.flushAuditCache();
            evtUtil.fireDeleted();
        }
    }

    @Override
    protected DataObject handleCopy(DataFolder df) throws IOException {
        synchronized(calculation) {
            if(isModified())
                saveCalculation();
            CalculationDataObject result = (CalculationDataObject) super.handleCopy(df);
            result.evtUtil.fireCreated();
            return result;
        }
    }
    
    private void saveCalculation() throws IOException {
        try {
            Element e = calculationToXml();
            JDomUtil.save(getPrimaryFile(), e);
            evtUtil.flushAuditCache();
            setModified(false);
        } catch (IOException ex) {
            logger.log(Level.SEVERE, "", ex);
            throw ex;
        }
    } 
    
    protected abstract Element calculationToXml();

    @Override
    protected FileObject handleRename(String name) throws IOException {
        synchronized(calculation) {
            if(isModified())
                saveCalculation();
            
            String oldPath = calculation.getPath();
            FileObject result = super.handleRename(name);
            String path = Displayable.Utils.displayProjectPath(result);
            calculation.setPath(path);
            CalculationEventUtil.fireRenamed(this, oldPath);
            return result;
        }
    }
    
    private class CalculationDeletable extends Deletable.DisplayableDeletable {
        private CalculationDeletable() {
            super(createDisplayable());
        }
        
        @Override
        public void delete() throws Exception {
            CalculationDataObject.this.delete();
        }
    }
}
