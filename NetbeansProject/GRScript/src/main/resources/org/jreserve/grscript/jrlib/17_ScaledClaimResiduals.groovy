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
package org.jreserve.grscript.jrlib

String HOME = "C:\\Munka\\Java\\NetbeansWS\\GRScript\\test\\org\\jreserve\\grscript\\input\\"
def paidData = readCsv(HOME+"apc_paid.csv", [columnSeparator:","])
paidData = cummulate(data)

paidTriangle = triangle(paidData)
paidLr = linkRatio(data)   //WeightedAverage
paidLr = smooth(paidLr, 10, "exponential")

//Residual scales
paidResScale = variableScale(paidLr)

//Residuals
paidResiduals = residuals(paidResScale)        //Not adjusted
paidResiduals = residuals(paidResScale, true)  //adjusted
paidResiduals = adjust(paidResiduals)

paidResiduals = exclude(paidResiduals , [a:0, d:2])
paidResiduals = exclude(paidResiduals , [accident:0, development:2])
paidResiduals = exclude(paidResiduals , 0, 2)
paidResiduals = residuals(paidResScale) {
    adjust()
    exclude(0, 0)
    exclude(a:1, d:0)
    exclude(accident:2, development:0)
}
