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
import org.jreserve.gui.misc.audit.api.AuditableMultiview;
import org.jreserve.gui.misc.utils.widgets.Displayable;
import org.jreserve.gui.wrapper.jdom.JDomUtil;
import org.netbeans.core.spi.multiview.MultiViewElement;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.MIMEResolver;
import org.openide.loaders.DataObject;
import org.openide.loaders.MultiFileLoader;
import org.openide.nodes.Node;
import org.openide.util.ImageUtilities;
import org.openide.util.Lookup;
import org.openide.util.NbBundle;
import org.openide.util.NbBundle.Messages;
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
public class ClaimTriangleDataObject extends CalculationDataObject {
    
    public final static String MIME_TYPE = "text/jreserve-claimtriangle+xml";
    public final static String EXTENSION = "jct";
    private final static Logger logger = Logger.getLogger(ClaimTriangleDataObject.class.getName());
    private final ClaimTriangleCalculationImpl calculation;
    
    public ClaimTriangleDataObject(FileObject fo, MultiFileLoader loader) throws IOException {
        super(fo, loader);
        super.registerEditor(MIME_TYPE, true);
        
        calculation = loadCalculation();
        super.ic.add(new ClaimTriangleDisplayable());
        super.ic.add(calculation);
        super.setDeleteAllowed(true);
    }
    
    private ClaimTriangleCalculationImpl loadCalculation() throws IOException {
        try {
            Element element = JDomUtil.getRootElement(getPrimaryFile());
            return new ClaimTriangleCalculationImpl(this, element);
        } catch (Exception ex) {
            String msg = "Unable to create calculation from file '%s'!";
            msg = String.format(msg, getPrimaryFile().getPath());
            logger.log(Level.SEVERE, msg, ex);
            throw new IOException(msg, ex);
        }
    }
    
    public Node createNodeDelegate() {
        return new ClaimTriangleNode(this);
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
    
    private class ClaimTriangleDisplayable implements Displayable {

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
