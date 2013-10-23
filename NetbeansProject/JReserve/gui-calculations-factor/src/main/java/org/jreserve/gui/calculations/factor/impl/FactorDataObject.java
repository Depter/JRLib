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
package org.jreserve.gui.calculations.factor.impl;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Icon;
import org.jdom2.Element;
import org.jreserve.gui.calculations.api.CalculationDataObject;
import org.jreserve.gui.misc.eventbus.EventBusManager;
import org.jreserve.gui.misc.utils.widgets.Displayable;
import org.jreserve.gui.wrapper.jdom.JDomUtil;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.MIMEResolver;
import org.openide.loaders.DataObject;
import org.openide.loaders.MultiFileLoader;
import org.openide.nodes.Node;
import org.openide.util.ImageUtilities;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@MIMEResolver.ExtensionRegistration(
    displayName = "Factor",
    mimeType = FactorDataObject.MIME_TYPE,
    extension = FactorDataObject.EXTENSION,
    position = 2200
)
@DataObject.Registration(
    mimeType = FactorDataObject.MIME_TYPE,
    iconBase = "org/jreserve/gui/calculations/factor/factors.png",
    displayName = "#LBL_Factor_LOADER",
    position = 400
)
@Messages({
    "LBL_Factor_LOADER=Factor"
})
public class FactorDataObject extends CalculationDataObject {
    
    public final static String MIME_TYPE = "text/jreserve-factor+xml";
    public final static String EXTENSION = "jfb";
    private final static Logger logger = Logger.getLogger(FactorDataObject.class.getName());
    private final FactorBundleImpl calculation;
    
    public FactorDataObject(FileObject fo, MultiFileLoader loader) throws IOException {
        super(fo, loader);
        super.registerEditor(MIME_TYPE, true);
        
        calculation = loadCalculation();
        EventBusManager.getDefault().subscribe(calculation);
        
        super.ic.add(new FactorDisplayable());
        super.ic.add(calculation);
        super.setDeleteAllowed(true);
    }
    
    private FactorBundleImpl loadCalculation() throws IOException {
        try {
            Element element = JDomUtil.getRootElement(getPrimaryFile());
            return new FactorBundleImpl(this, element);
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
        synchronized(lock) {
            //TODO fire delete SE
            //TODO fire delete scale
            //TODO fire delete linkRatio
        }
        EventBusManager.getDefault().unsubscribe(calculation);
    }
    
    private class FactorDisplayable implements Displayable {

        @Override
        public Icon getIcon() {
            return ImageUtilities.loadImageIcon(FactorNode.IMG, false);
        }

        @Override
        public String getDisplayName() {
            return calculation.getPath();
        }    
    }
}
