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
package org.jreserve.gui.calculations.claimtriangle.impl;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Icon;
import org.jdom2.Element;
import org.jreserve.gui.calculations.api.CalculationDataObject;
import org.jreserve.gui.calculations.api.CalculationEventUtil;
import org.jreserve.gui.calculations.api.CalculationProvider;
import org.jreserve.gui.misc.audit.api.AuditableMultiview;
import org.jreserve.gui.misc.audit.event.AuditedObject;
import org.jreserve.gui.misc.eventbus.EventBusManager;
import org.jreserve.gui.misc.utils.actions.AbstractDisplayableSavable;
import org.jreserve.gui.misc.utils.actions.Deletable;
import org.jreserve.gui.misc.utils.widgets.Displayable;
import org.jreserve.gui.wrapper.jdom.JDomUtil;
import org.netbeans.api.actions.Savable;
import org.netbeans.api.project.Project;
import org.netbeans.core.spi.multiview.MultiViewElement;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.MIMEResolver;
import org.openide.loaders.DataFolder;
import org.openide.loaders.DataObject;
import org.openide.loaders.DataObjectExistsException;
import org.openide.loaders.MultiDataObject;
import org.openide.loaders.MultiFileLoader;
import org.openide.nodes.Node;
import org.openide.util.ImageUtilities;
import org.openide.util.Lookup;
import org.openide.util.NbBundle;
import org.openide.util.NbBundle.Messages;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;
import org.openide.util.lookup.ProxyLookup;
import org.openide.windows.TopComponent;

@MIMEResolver.ExtensionRegistration(
    displayName = "Claim Triangle",
    mimeType = ClaimTriangleDataObject.MIME_TYPE,
    extension = ClaimTriangleDataObject.EXTENSION,
    position = 2000
)
@DataObject.Registration(
    mimeType = ClaimTriangleDataObject.MIME_TYPE,
    iconBase = "org/jreserve/gui/calculations/claimtriangle/triangle.png",
    displayName = "#LBL_ClaimTriangle_LOADER",
    position = 300
)
@Messages({
    "LBL_ClaimTriangle_LOADER=Claim Triangle"
})
public class ClaimTriangleDataObject extends CalculationDataObject<ClaimTriangleCalculationImpl> {
    
    public final static String MIME_TYPE = "text/jreserve-claimtriangle+xml";
    public final static String EXTENSION = "jct";
    private final static Logger logger = Logger.getLogger(ClaimTriangleDataObject.class.getName());
    
    public ClaimTriangleDataObject(FileObject fo, MultiFileLoader loader) throws IOException {
        super(fo, loader);
        
        try {
            Element element = JDomUtil.getRootElement(fo);
            ClaimTriangleCalculationImpl calculation = new ClaimTriangleCalculationImpl(this, element);
            calculation.setPath(getPath(fo));
            super.registerCalculation(calculation, calculation);
            super.setDeleteAllowed(true);
        } catch (Exception ex) {
            String msg = "Unable to create calculation from file '%s'!";
            msg = String.format(msg, fo.getPath());
            logger.log(Level.SEVERE, msg, ex);
            throw new IOException(msg, ex);
        }
    }

    @Override
    protected Element toXml(ClaimTriangleCalculationImpl calculation) {
        return calculation.toXml();
    }

    @Override
    protected void renameCalculation(FileObject newFile) {
        String path = getPath(newFile);
        getCalculation().setPath(path);
    }

    @Override
    protected Displayable createDisplayable() {
        return new ClaimTriangleDisplayable();
    }
    
//    
//    private final static Logger logger = Logger.getLogger(ClaimTriangleDataObject.class.getName());
//    private final static int LKP_VERSION = 1;
//    
//    private final InstanceContent ic = new InstanceContent();
//    private final Lookup lkp;
//    private final ClaimTriangleCalculationImpl calculation;
//    
//    public ClaimTriangleDataObject(FileObject pf, MultiFileLoader loader) throws DataObjectExistsException, IOException {
//        super(pf, loader);
//        registerEditor(MIME_TYPE, true);
//        
//        try {
//            Element element = JDomUtil.getRootElement(pf);
//            calculation = new ClaimTriangleCalculationImpl(this, element);
//            ic.add(calculation);
//            String path = Displayable.Utils.displayProjectPath(getPrimaryFile());
//            calculation.setPath(path);
//        
//            ic.add(new ClaimTriangleDeletable());
//        
//            lkp = new ProxyLookup(super.getLookup(), new AbstractLookup(ic));
//            if(lkp.lookup(ClaimTriangleDataObject.class) == null)
//                ic.add(this);
//        } catch (Exception ex) {
//            String msg = "Unable to create calculation from file '%s'!";
//            msg = String.format(msg, pf.getPath());
//            logger.log(Level.SEVERE, msg, ex);
//            throw new IOException(msg, ex);
//        }
//    }
//    
//    @Override
//    protected int associateLookup() {
//        return LKP_VERSION;
//    }
//    
//    @Override
//    public Lookup getLookup() {
//        return lkp;
//    }
//
//    @Override
//    public String getName() {
//        synchronized(calculation) {
//            return getPrimaryFile().getName();
//        }
//    }
//
//    @Override
//    protected Node createNodeDelegate() {
//        return new ClaimTriangleNode(this);
//    }
//
//    @Override
//    protected void handleDelete() throws IOException {
//        synchronized(calculation) {
//            super.handleDelete();
//            CalculationEventUtil.fireDeleted(this);
//            EventBusManager.getDefault().unsubscribe(calculation);
//        }
//    }
//
//    @Override
//    protected DataObject handleCopy(DataFolder df) throws IOException {
//        synchronized(calculation) {
//            if(isModified())
//                saveCalculation();
//            DataObject result = super.handleCopy(df);
//            CalculationEventUtil.fireCreated((DataObject) result, this);
//            return result;
//        }
//    }
//    
//    private void saveCalculation() throws IOException {
//        try {
//            Element e = calculation.toXml();
//            JDomUtil.save(getPrimaryFile(), e);
//            setModified(false);
//        } catch (IOException ex) {
//            logger.log(Level.SEVERE, "", ex);
//            throw ex;
//        }
//    } 
//
//    @Override
//    protected FileObject handleRename(String name) throws IOException {
//        synchronized(calculation) {
//            if(isModified())
//                saveCalculation();
//            
//            String oldPath = calculation.getPath();
//            FileObject result = super.handleRename(name);
//            String path = Displayable.Utils.displayProjectPath(result);
//            calculation.setPath(path);
//            CalculationEventUtil.fireRenamed(this, oldPath);
//            return result;
//        }
//    }
//
//    @Override
//    protected FileObject handleMove(DataFolder df) throws IOException {
//        synchronized(calculation) {
//            if(isModified())
//                saveCalculation();
//            
//            String oldPath = calculation.getPath();
//            FileObject result = super.handleMove(df);
//            String path = Displayable.Utils.displayProjectPath(result);
//            calculation.setPath(path);
//            CalculationEventUtil.fireRenamed(this, oldPath);
//            return result;
//        }
//    }
//
//    @Override
//    public void setModified(boolean modified) {
//        synchronized(calculation) {
//            if(modified)
//                addSavable();
//            else 
//                removeSavable();
//            super.setModified(modified);
//        }
//    }
//    
//    private void addSavable() {
//        if(getLookup().lookup(Savable.class) == null)
//            ic.add(new ClaimTriangleSavable());
//    }
//    
//    private void removeSavable() {
//        Savable savable = getLookup().lookup(Savable.class);
//        if(savable != null)
//            ic.remove(savable);
//    }
//    
//    @Override
//    public boolean isModified() {
//        synchronized(calculation) {
//            return super.isModified();
//        }
//    }
//    
//    @Override
//    protected DataObject handleCopyRename(DataFolder df, String name, String ext) throws IOException {
//        synchronized(calculation) {
//            if(isModified())
//                saveCalculation();
//            
//            DataObject result = super.handleCopyRename(df, name, ext);
//            CalculationEventUtil.fireCreated((DataObject) result);
//            return result;
//        }
//    }
//    
//    @MultiViewElement.Registration(
//        displayName = "#LBL.ClaimTriangleAudit",
//        mimeType = MIME_TYPE,
//        persistenceType = TopComponent.PERSISTENCE_NEVER,
//        preferredID = "org.jreserve.gui.calculations.triangle.ClaimTriangleDataObject.ClaimTriangleAudit",
//        position = 1000
//    )
//    @NbBundle.Messages("LBL.ClaimTriangleAudit=Audit")
//    public static MultiViewElement createAuditElement(Lookup lkp) {
//        return new AuditableMultiview(lkp);
//    }
//    
    private class ClaimTriangleDisplayable implements Displayable {

        @Override
        public Icon getIcon() {
            return ImageUtilities.loadImageIcon(ClaimTriangleNode.IMG, false);
        }

        @Override
        public String getDisplayName() {
            return getCalculation().getPath();
        }    
    }
//    private class ClaimTriangleDeletable extends Deletable.DisplayableDeletable {
//        
//        private ClaimTriangleDeletable() {
//            super(new ClaimTriangleDisplayable());
//        }
//        
//        @Override
//        public void delete() throws Exception {
//            ClaimTriangleDataObject.this.delete();
//        }
//    }
//    
//    private class ClaimTriangleSavable extends AbstractDisplayableSavable {
//
//        public ClaimTriangleSavable() {
//            super(new ClaimTriangleDisplayable());
//            register();
//        }
//
//        @Override
//        protected void handleSave() throws IOException {
//            synchronized(calculation) {
//                saveCalculation();
//            }
//        }
//
//        @Override
//        public boolean equals(Object obj) {
//            return this == obj;
//        }
//
//        @Override
//        public int hashCode() {
//            return ClaimTriangleDataObject.this.hashCode();
//        }
//    }
}
