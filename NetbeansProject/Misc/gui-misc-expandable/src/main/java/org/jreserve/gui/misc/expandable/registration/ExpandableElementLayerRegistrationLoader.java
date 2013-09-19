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
package org.jreserve.gui.misc.expandable.registration;

import java.util.logging.Logger;
import org.jreserve.gui.misc.annotations.LayerRegistrationLoader;
import org.jreserve.gui.misc.expandable.ExpandableElement;
import org.jreserve.gui.misc.expandable.ExpandableElementDescription;
import org.openide.filesystems.FileObject;
import org.openide.util.Lookup;

/**
 *
 * @author Peter Decsi
 */
public class ExpandableElementLayerRegistrationLoader extends LayerRegistrationLoader<ExpandableElementDescription> {

    private final static Logger logger = Logger.getLogger(ExpandableElementLayerRegistrationLoader.class.getName());
    
    private String path;
    private Lookup context; 
    
    public ExpandableElementLayerRegistrationLoader(String mimeType) {
        this(mimeType, null);
    }
    
    public ExpandableElementLayerRegistrationLoader(String mimeType, Lookup context) {
        this.path = String.format(ExpandableElementRegistrationProcessor.PATH, mimeType);
        this.context = context;
    }
    
    @Override
    protected Logger getLogger() {
        return logger;
    }

    @Override
    protected String getDirectory() {
        return path;
    }
    
    @Override
    public ExpandableElementDescription getValue(FileObject file) throws Exception {
        return new FileAdapter(file, context);
    }
}
