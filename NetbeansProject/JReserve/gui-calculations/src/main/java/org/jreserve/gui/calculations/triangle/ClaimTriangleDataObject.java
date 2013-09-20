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
package org.jreserve.gui.calculations.triangle;

import java.io.IOException;
import javax.swing.Icon;
import org.jdom2.Element;
import org.jreserve.gui.calculations.api.CalculationEventUtil;
import org.jreserve.gui.misc.audit.api.AuditableMultiview;
import org.jreserve.gui.misc.audit.api.AuditableObject;
import org.jreserve.gui.misc.utils.actions.Deletable;
import org.jreserve.gui.misc.utils.widgets.Displayable;
import org.jreserve.gui.wrapper.jdom.JDomUtil;
import org.netbeans.api.project.FileOwnerQuery;
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
    iconBase = "org/jreserve/gui/calculations/icons/triangle.png",
    displayName = "#LBL_ClaimTriangle_LOADER",
    position = 300
)
@Messages({
    "LBL_ClaimTriangle_LOADER=Claim Triangle"
})
public class ClaimTriangleDataObject extends MultiDataObject {
    
    public final static String MIME_TYPE = "text/jreserve-claimtriangle+xml";
    public final static String EXTENSION = "jct";
    private final static int LKP_VERSION = 1;
    
    private final InstanceContent ic = new InstanceContent();
    private final Lookup lkp;
    private ClaimTriangleCalculation calculation;
    
    public ClaimTriangleDataObject(FileObject pf, MultiFileLoader loader) throws DataObjectExistsException, IOException {
        super(pf, loader);
        registerEditor("text/jreserve-claimtriangle+xml", true);
        
        try {
            Element element = JDomUtil.getRootElement(pf);
            if(isTemplate(element)) {
                super.setTemplate(true);
                lkp = super.getLookup();
            } else {
                super.setTemplate(false);
                lkp = new ProxyLookup(super.getLookup(), new AbstractLookup(ic));
                initLookupContent(element);
            }
        } catch (Exception ex) {
            String msg = "Unable to create calculation!";
            throw new IOException(msg, ex);
        }
    }
    
    private boolean isTemplate(Element element) throws IOException {
        Element auditE = JDomUtil.getExistingChild(element, ClaimTriangleCalculation.AUDIT_ID_ELEMENT);
        String str = auditE.getTextTrim();
        return str != null && str.length() > 0 && str.charAt(0) == '$';
    }
    
    private void initLookupContent(Element element) throws Exception {
        calculation = new ClaimTriangleCalculation(getPrimaryFile(), element);
        ic.add(calculation);
        String path = Displayable.Utils.displayProjectPath(getPrimaryFile());
        calculation.setPath(path);
        
        ic.add(new ClaimTriangleAuditable());
        ic.add(new ClaimTriangleDeletable());
        
        if(lkp.lookup(ClaimTriangleDataObject.class) == null)
            ic.add(this);
    
    }
    
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
        return getPrimaryFile().getName();
    }

    @Override
    protected Node createNodeDelegate() {
        return new ClaimTriangleNode(this);
    }

    @Override
    protected void handleDelete() throws IOException {
        super.handleDelete();
        CalculationEventUtil.fireDeleted(this);
    }

    @Override
    protected DataObject handleCopy(DataFolder df) throws IOException {
        DataObject result = super.handleCopy(df);
        CalculationEventUtil.fireCreated((DataObject) result, this);
        return result;
    }

    @Override
    protected FileObject handleRename(String name) throws IOException {
        String oldPath = calculation.getPath();
        FileObject result = super.handleRename(name);
        String path = Displayable.Utils.displayProjectPath(result);
        calculation.setPath(path);
        CalculationEventUtil.fireRenamed(this, oldPath);
        return result;
    }

    @Override
    protected FileObject handleMove(DataFolder df) throws IOException {
        String oldPath = calculation.getPath();
        FileObject result = super.handleMove(df);
        String path = Displayable.Utils.displayProjectPath(result);
        calculation.setPath(path);
        CalculationEventUtil.fireRenamed(this, oldPath);
        return result;
    }

    @Override
    protected DataObject handleCopyRename(DataFolder df, String name, String ext) throws IOException {
        DataObject result = super.handleCopyRename(df, name, ext);
        CalculationEventUtil.fireCreated((DataObject) result);
        return result;
    }
    
    @MultiViewElement.Registration(
        displayName = "#LBL.ClaimTriangleAudit",
        mimeType = MIME_TYPE,
        persistenceType = TopComponent.PERSISTENCE_NEVER,
        preferredID = "org.jreserve.gui.calculations.triangle.ClaimTriangleDataObject.ClaimTriangleAudit",
        position = 1000
    )
    @NbBundle.Messages("LBL.ClaimTriangleAudit=Audit")
    public static MultiViewElement createAuditElement(Lookup lkp) {
        return new AuditableMultiview(lkp);
    }
    
    private class ClaimTriangleAuditable extends AuditableObject {

        @Override
        public Project getAuditedProject() {
            return FileOwnerQuery.getOwner(getPrimaryFile());
        }

        @Override
        public long getAuditId() {
            return calculation.getAuditId();
        }

        @Override
        public String getAuditName() {
            return calculation.getPath();
        }
    }
    
    private class ClaimTriangleDeletable implements Deletable {
        @Override
        public void delete() throws Exception {
            ClaimTriangleDataObject.this.delete();
        }

        @Override
        public Icon getIcon() {
            return ImageUtilities.loadImageIcon(ClaimTriangleNode.IMG, false);
        }

        @Override
        public String getDisplayName() {
            return calculation.getPath();
        }
    }
}
