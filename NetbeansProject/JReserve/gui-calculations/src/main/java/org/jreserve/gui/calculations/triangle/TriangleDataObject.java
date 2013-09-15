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
import org.netbeans.core.spi.multiview.MultiViewElement;
import org.netbeans.core.spi.multiview.text.MultiViewEditorElement;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.MIMEResolver;
import org.openide.loaders.DataObject;
import org.openide.loaders.DataObjectExistsException;
import org.openide.loaders.MultiDataObject;
import org.openide.loaders.MultiFileLoader;
import org.openide.util.Lookup;
import org.openide.util.NbBundle.Messages;
import org.openide.windows.TopComponent;

@Messages({
    "LBL_Triangle_LOADER=Files of Triangle"
})
@MIMEResolver.ExtensionRegistration(
    displayName = "#LBL_Triangle_LOADER",
    mimeType = "text/jreserve-triangle+xml",
    position = 2000,
    extension = {"triangle"}
)
@DataObject.Registration(
    mimeType = "text/jreserve-triangle+xml",
    iconBase = "org/jreserve/gui/calculations/icons/triangle.png",
    displayName = "#LBL_Triangle_LOADER",
    position = 300
)
@ActionReferences({
    @ActionReference(
        path = "Loaders/text/jreserve-triangle+xml/Actions",
        id = @ActionID(category = "System", id = "org.openide.actions.OpenAction"),
        position = 100, separatorAfter = 200),
    @ActionReference(
        path = "Loaders/text/jreserve-triangle+xml/Actions",
        id = @ActionID(category = "Edit", id = "org.openide.actions.CutAction"),
        position = 300),
    @ActionReference(
        path = "Loaders/text/jreserve-triangle+xml/Actions",
        id = @ActionID(category = "Edit", id = "org.openide.actions.CopyAction"),
        position = 400, separatorAfter = 500),
    @ActionReference(
        path = "Loaders/text/jreserve-triangle+xml/Actions",
        id = @ActionID(category = "Edit", id = "org.openide.actions.DeleteAction"),
        position = 600),
    @ActionReference(
        path = "Loaders/text/jreserve-triangle+xml/Actions",
        id = @ActionID(category = "System", id = "org.openide.actions.RenameAction"),
        position = 700, separatorAfter = 800),
    @ActionReference(
        path = "Loaders/text/jreserve-triangle+xml/Actions",
        id = @ActionID(category = "System", id = "org.openide.actions.SaveAsTemplateAction"),
        position = 900, separatorAfter = 1000),
    @ActionReference(
        path = "Loaders/text/jreserve-triangle+xml/Actions",
        id = @ActionID(category = "System", id = "org.openide.actions.FileSystemAction"),
        position = 1100, separatorAfter = 1200),
    @ActionReference(
        path = "Loaders/text/jreserve-triangle+xml/Actions",
        id = @ActionID(category = "System", id = "org.openide.actions.ToolsAction"),
        position = 1300),
    @ActionReference(
        path = "Loaders/text/jreserve-triangle+xml/Actions",
        id = @ActionID(category = "System", id = "org.openide.actions.PropertiesAction"),
        position = 1400)
})
public class TriangleDataObject extends MultiDataObject {
    
    private final static int LKP_VERSION = 1;
    
    public TriangleDataObject(FileObject pf, MultiFileLoader loader) throws DataObjectExistsException, IOException {
        super(pf, loader);
        registerEditor("text/jreserve-triangle+xml", true);
    }

    @Override
    protected int associateLookup() {
        return LKP_VERSION;
    }

    @MultiViewElement.Registration(
        displayName = "#LBL_Triangle_EDITOR",
        iconBase = "org/jreserve/gui/calculations/icons/triangle.png",
        mimeType = "text/jreserve-triangle+xml",
        persistenceType = TopComponent.PERSISTENCE_ONLY_OPENED,
        preferredID = "Triangle",
        position = 1000)
    @Messages("LBL_Triangle_EDITOR=Source")
    public static MultiViewEditorElement createEditor(Lookup lkp) {
        return new MultiViewEditorElement(lkp);
    }
}
