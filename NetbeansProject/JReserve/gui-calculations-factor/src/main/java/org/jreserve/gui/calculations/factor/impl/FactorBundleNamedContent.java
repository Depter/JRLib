/*
 * Copyright (C) 2013, Peter Decsi.
 *
 * This library is free software: you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public 
 * License as published by the Free Software Foundation, either 
 * version 3 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this library.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.jreserve.gui.calculations.factor.impl;

import java.util.ArrayList;
import java.util.List;
import org.jreserve.gui.calculations.factor.impl.linkratio.LinkRatioNamedContent;
import org.jreserve.gui.misc.namedcontent.NamedContent;
import org.jreserve.gui.misc.namedcontent.NamedDataObjectContent;
import org.openide.loaders.DataObject;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class FactorBundleNamedContent extends NamedDataObjectContent {

    private final List<NamedContent> children = new ArrayList<NamedContent>();
    
    FactorBundleNamedContent(FactorDataObject obj) {
        super(obj);
        
        FactorBundleImpl bundle = obj.getLookup().lookup(FactorBundleImpl.class);
        children.add(new FactorTriangleContent(bundle.getFactors()));
        children.add(new LinkRatioNamedContent(bundle.getLinkRatio()));
    }
    
    @Override
    public List<NamedContent> getContents() {
        return new ArrayList<NamedContent>(children);
    }
}
