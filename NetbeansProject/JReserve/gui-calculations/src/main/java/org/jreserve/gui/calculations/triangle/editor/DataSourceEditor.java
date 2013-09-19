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
package org.jreserve.gui.calculations.triangle.editor;

import java.awt.Component;
import org.jreserve.gui.calculations.triangle.ClaimTriangleDataObject;
import org.jreserve.gui.misc.expandable.AbstractExpandableElement;
import org.jreserve.gui.misc.expandable.ExpandableElement;
import org.openide.util.Lookup;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@ExpandableElement.Registration(
    displayName = "#LBL.DataSourceEditor.Title",
    mimeType = ClaimTriangleDataObject.MIME_TYPE,
    position = 100,
    prefferedID = "org.jreserve.gui.calculations.triangle.editor.DataSourceEditor",
    background = "646464",
    iconBase = "org/jreserve/gui/data/icons/database.png"
)
@Messages({
    "LBL.DataSourceEditor.Title=Input Data"
})
public class DataSourceEditor extends AbstractExpandableElement {

    private DataSourceEditorPanel panel;

    public DataSourceEditor() {
    }
    
    public DataSourceEditor(Lookup context) {
    }
    
    @Override
    protected Component createVisualComponent() {
        panel = new DataSourceEditorPanel();
        return panel;
    }

    @Override
    protected boolean openMaximized() {
        return false;
    }
    
    
}
