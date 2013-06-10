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
package org.jreserve.jrlib.linkratio.standarderror;

import org.jreserve.jrlib.linkratio.LinkRatio;
import org.jreserve.jrlib.linkratio.scale.LinkRatioScale;
import org.jreserve.jrlib.linkratio.scale.LinkRatioScaleInput;
import org.jreserve.jrlib.triangle.claim.ClaimTriangle;
import org.jreserve.jrlib.triangle.factor.FactorTriangle;
import org.jreserve.jrlib.util.method.AbstractSimpleMethodSelection;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class SimpleLinkRatioSE 
    extends AbstractSimpleMethodSelection<LinkRatioSE, LinkRatioSEFunction> 
    implements LinkRatioSE {

    
    public SimpleLinkRatioSE(LinkRatioScale scale) {
        this(scale, new LogLinearRatioSEFunction());
    }
    
    public SimpleLinkRatioSE(LinkRatioScale scales, LinkRatioSEFunction estimator) {
        super(new LinkRatioSECalculator(scales), new DefaultLinkRatioSEFunction(), estimator);
    }

    @Override
    public LinkRatioScale getSourceLRScales() {
        return source.getSourceLRScales();
    }

    @Override
    public LinkRatioScaleInput getSourceLrScaleInput() {
        return source.getSourceLrScaleInput();
    }

    @Override
    public LinkRatio getSourceLinkRatios() {
        return source.getSourceLinkRatios();
    }

    @Override
    public FactorTriangle getSourceFactors() {
        return source.getSourceFactors();
    }

    @Override
    public ClaimTriangle getSourceTriangle() {
        return source.getSourceTriangle();
    }
    
    @Override
    protected void initCalculation() {
        length = source.getLength();
    }
}
