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

//Residuals
paidResiduals = residuals(paidLr) {
    exclude(accident:0, development:8)
    exclude(accident:8, development:0)
    adjust()
}

//Residual scales
paidResScale = constantScale(paidLr)
paidResScale = variableScale(paidLr)

paidResScale = constantScale(paidResiduals)
paidResScale = constantScale(paidResiduals, scale)  //fixed user input scale
paidResScale = variableScale(paidResiduals)

paidResScale = variableScale(paidResiduals) {
    fixed(1, 200.45)
    fixed(1:200.45, 2:122.5)
}
