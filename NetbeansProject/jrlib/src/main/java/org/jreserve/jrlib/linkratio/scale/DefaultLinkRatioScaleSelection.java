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
package org.jreserve.jrlib.linkratio.scale;

import org.jreserve.jrlib.linkratio.LinkRatio;
import org.jreserve.jrlib.scale.*;
import org.jreserve.jrlib.triangle.claim.ClaimTriangle;
import org.jreserve.jrlib.triangle.factor.FactorTriangle;

/**
 *
 * @see DefaultScaleSelection
 * @author Peter Decsi
 * @version 1.0
 */
public class DefaultLinkRatioScaleSelection extends DefaultScaleSelection<LinkRatioScaleInput> implements LinkRatioScaleSelection {

    private LinkRatioScaleInput sourceInput;
    
    public DefaultLinkRatioScaleSelection(LinkRatio source) {
        this(new LinkRatioScaleInput(source));
    }
    
    public DefaultLinkRatioScaleSelection(LinkRatio source, ScaleEstimator<LinkRatioScaleInput> method) {
        this(new LinkRatioScaleInput(source), method);
    }

    public DefaultLinkRatioScaleSelection(LinkRatioScaleInput source) {
        this(source, new DefaultScaleEstimator<LinkRatioScaleInput>());
    }
    
    public DefaultLinkRatioScaleSelection(LinkRatioScaleInput source, ScaleEstimator<LinkRatioScaleInput> method) {
        this(new DefaultScaleCalculator<LinkRatioScaleInput>(source), method);
    }

    public DefaultLinkRatioScaleSelection(Scale<LinkRatioScaleInput> source) {
        this(source, new DefaultScaleEstimator<LinkRatioScaleInput>());
    }

    public DefaultLinkRatioScaleSelection(Scale<LinkRatioScaleInput> source, ScaleEstimator<LinkRatioScaleInput> method) {
        super(source, method);
        this.sourceInput = source.getSourceInput();
    }
    
    private DefaultLinkRatioScaleSelection(DefaultLinkRatioScaleSelection original) {
        super(original);
        this.sourceInput = super.source.getSourceInput();
    }
    
    @Override
    public LinkRatioScaleInput getSourceInput() {
        return sourceInput;
    }
    
    @Override
    public LinkRatio getSourceLinkRatios() {
        return sourceInput.getSourceLinkRatios();
    }

    @Override
    public FactorTriangle getSourceFactors() {
        return sourceInput.getSourceFactors();
    }

    @Override
    public ClaimTriangle getSourceTriangle() {
        return sourceInput.getSourceTriangle();
    }
}
