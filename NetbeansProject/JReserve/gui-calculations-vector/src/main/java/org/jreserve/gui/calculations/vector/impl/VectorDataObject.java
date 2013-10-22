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
package org.jreserve.gui.calculations.vector.impl;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Icon;
import org.jdom2.Element;
import org.jreserve.gui.calculations.api.CalculationDataObject;
import org.jreserve.gui.misc.audit.api.AuditableMultiview;
import org.jreserve.gui.misc.eventbus.EventBusManager;
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

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@MIMEResolver.ExtensionRegistration(
    displayName = "Vector",
    mimeType = VectorDataObject.MIME_TYPE,
    extension = VectorDataObject.EXTENSION,
    position = 2100
)
@DataObject.Registration(
    mimeType = VectorDataObject.MIME_TYPE,
    iconBase = "org/jreserve/gui/calculations/vector/vector.png",
    displayName = "#LBL_Vector_LOADER",
    position = 400
)
@Messages({
    "LBL_Vector_LOADER=Vector"
})
public class VectorDataObject extends CalculationDataObject {
    
    public final static String MIME_TYPE = "text/jreserve-vector+xml";
    public final static String EXTENSION = "jcv";
    private final static Logger logger = Logger.getLogger(VectorDataObject.class.getName());
    private final VectorCalculationImpl calculation;
    
    public VectorDataObject(FileObject fo, MultiFileLoader loader) throws IOException {
        super(fo, loader);
        super.registerEditor(MIME_TYPE, true);
        
        calculation = loadCalculation();
        EventBusManager.getDefault().subscribe(calculation);
        
        super.ic.add(new VectorDisplayable());
        super.ic.add(calculation);
        super.setDeleteAllowed(true);
    }
    
    private VectorCalculationImpl loadCalculation() throws IOException {
        try {
            Element element = JDomUtil.getRootElement(getPrimaryFile());
            return new VectorCalculationImpl(this, element);
        } catch (Exception ex) {
            String msg = "Unable to create calculation from file '%s'!";
            msg = String.format(msg, getPrimaryFile().getPath());
            logger.log(Level.SEVERE, msg, ex);
            throw new IOException(msg, ex);
        }
    }
    
    public Node createNodeDelegate() {
        return new VectorNode(this);
    }

    @Override
    protected void handleDelete() throws IOException {
        super.handleDelete();
        EventBusManager.getDefault().unsubscribe(calculation);
    }
    
    @MultiViewElement.Registration(
        displayName = "#LBL.VectorAudit",
        mimeType = MIME_TYPE,
        persistenceType = TopComponent.PERSISTENCE_NEVER,
        preferredID = "org.jreserve.gui.calculations.vector.VectorDataObject.VectorAudit",
        position = 1000
    )
    @NbBundle.Messages("LBL.VectorAudit=Audit")
    public static MultiViewElement createAuditElement(Lookup lkp) {
        return new AuditableMultiview(lkp);
    }
    
    private class VectorDisplayable implements Displayable {

        @Override
        public Icon getIcon() {
            return ImageUtilities.loadImageIcon(VectorNode.IMG, false);
        }

        @Override
        public String getDisplayName() {
            return calculation.getPath();
        }    
    }
}
