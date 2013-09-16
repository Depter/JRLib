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
import org.openide.filesystems.FileObject;
import org.openide.filesystems.MIMEResolver;
import org.openide.loaders.DataObject;
import org.openide.loaders.DataObjectExistsException;
import org.openide.loaders.MultiDataObject;
import org.openide.loaders.MultiFileLoader;
import org.openide.util.NbBundle.Messages;

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
    
    public ClaimTriangleDataObject(FileObject pf, MultiFileLoader loader) throws DataObjectExistsException, IOException {
        super(pf, loader);
        registerEditor("text/jreserve-claimtriangle+xml", true);
    }

    @Override
    protected int associateLookup() {
        return LKP_VERSION;
    }
}
