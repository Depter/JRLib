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

/**
 * This class simply mirrors the fitted {@link LinkRatioSE LinkRatioSE}. 
 * For development periods, where there is no input link-ratio scale NaN
 * is returned.
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class DefaultLinkRatioSEFunction implements LinkRatioSEFunction {
    
    private LinkRatioSE source;
    
    @Override
    public void fit(LinkRatioSE source) {
        this.source = source;
    }
    
    @Override
    public double getValue(int development) {
        return source.getValue(development);
    }
    
    public DefaultLinkRatioSEFunction copy() {
        DefaultLinkRatioSEFunction copy = new DefaultLinkRatioSEFunction();
        copy.source = source;
        return copy;
    }
}
