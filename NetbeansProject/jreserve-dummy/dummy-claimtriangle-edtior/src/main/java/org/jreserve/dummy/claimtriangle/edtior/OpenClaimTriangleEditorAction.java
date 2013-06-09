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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;
import org.netbeans.core.api.multiview.MultiViews;
import org.openide.util.Lookup;
import org.openide.util.NbBundle.Messages;
import org.openide.windows.TopComponent;

//@ActionID(
//    category = "Edit",
//    id = "org.jreserve.dummy.claimtriangle.edtior.OpenClaimTriangleEditorAction"
//)
//@ActionRegistration(
//    displayName = "#CTL_OpenClaimTriangleEditorAction"//,
////    iconBase = "org/jreserve/dummy/projecttree/resources/triangle.png"    
//)
//@ActionReferences({
//    @ActionReference(path = "Menu/Window" /*, position = 333 */),
//    @ActionReference(path = "Ribbon/TaskPanes/Windows/JReserve", position = 100)
//})
@Messages("CTL_OpenClaimTriangleEditorAction=Claim Triangle")
public final class OpenClaimTriangleEditorAction implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        TopComponent tc = MultiViews.createMultiView(TriangleEditorMultiview.MIME_TYPE, new Context());
        tc.open();
        tc.requestActive();
    }
    
    private static class Context implements Serializable, Lookup.Provider {
        @Override
        public Lookup getLookup() {
            return Lookup.EMPTY;
        }
    
    }
}
