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

import java.util.Collections;
import java.util.List;
import org.jreserve.gui.misc.namedcontent.NamedContent;
import org.openide.util.Lookup;
import org.openide.util.NbBundle.Messages;
import org.openide.util.lookup.Lookups;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
@Messages({
    "LBL.FactorTriangleContent.Name=Factors"
})
class FactorTriangleContent implements NamedContent {

    private final Lookup lkp;
    
    FactorTriangleContent(FactorTriangleCalculationImpl factors) {
        lkp = Lookups.singleton(factors);
    }
    
    @Override
    public List<NamedContent> getContents() {
        return Collections.EMPTY_LIST;
    }

    @Override
    public String getName() {
        return Bundle.LBL_FactorTriangleContent_Name();
    }

    @Override
    public Lookup getLookup() {
        return lkp;
    }

}
