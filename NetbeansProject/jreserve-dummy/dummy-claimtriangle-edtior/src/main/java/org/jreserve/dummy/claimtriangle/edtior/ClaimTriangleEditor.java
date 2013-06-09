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
package org.jreserve.dummy.claimtriangle.edtior;

import java.awt.BorderLayout;
import org.jreserve.gui.misc.expandable.ExpandableContainerHandler;
import org.jreserve.gui.misc.expandable.ExpandableFactory;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.util.Lookup;
import org.openide.util.NbBundle.Messages;
import org.openide.util.lookup.Lookups;
import org.openide.windows.TopComponent;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
//@TopComponent.Description(
//    preferredID = "ClaimTriangleEditorTc",
//    persistenceType = TopComponent.PERSISTENCE_NEVER, 
//    iconBase = "org/jreserve/dummy/projecttree/resources/triangle.png"
//)
//@TopComponent.Registration(
//    mode="editor", 
//    openAtStartup = false
//)
//@ActionID(
//    category = "Window", 
//    id = "org.jreserve.dummy.claimtriangle.edtior.ClaimTriangleEditor"
//)
//@ActionReferences({
//    @ActionReference(path = "Menu/Window" /*, position = 333 */),
//    @ActionReference(path = "Ribbon/TaskPanes/Windows/JReserve", position = 100)
//})
//@TopComponent.OpenActionRegistration(
//    displayName = "#CTL.ClaimTriangleEditorAction",
//    preferredID = "ClaimTriangleEditor"
//)
@Messages({
    "CTL.ClaimTriangleEditorAction=Claim Triangle",
    "CTL.ClaimTriangleEditor.Title=Edit Triangle"
})
public class ClaimTriangleEditor extends TopComponent {
    
    private final ExpandableContainerHandler handler;
    
    public ClaimTriangleEditor() {
        setLayout(new BorderLayout());
        Lookup lkp = Lookups.singleton(TriangleFactory.createTriangle());
        handler = ExpandableFactory.createScrollPanel(TriangleEditorMultiview.MIME_TYPE, lkp);
        add(handler.getComponent(), BorderLayout.CENTER);
        setDisplayName("APC Paid");
    }
    
    @Override
    public Lookup getLookup() {
        Lookup lkp = handler.getLookup();
        return handler.getLookup();
    }
}
