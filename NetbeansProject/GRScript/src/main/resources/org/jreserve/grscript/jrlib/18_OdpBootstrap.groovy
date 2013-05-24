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

//Basic data
paidTriangle = triangle(paidData)
paidLr = linkRatio(data)                    //WeightedAverage
paidLr = smooth(paidLr, 10, "exponential")

//Residual scales
paidResScale = variableScale(paidLr)
paidRes = residuals(paidResScale) {
    exclude(0, 8)
    exclude(8, 0)
}

//Bootstrap
bootstrap = odpBootstrap {
    count 1000
    random "Java", 10   //random(String) , random(Random), DEFAULT = Java
    residuals paidRes
    process "Gamma"     //Default: Gamma, values: [Gamma, Constant]
    segment {
        from(accient:0, development:0)
        to(a:8, d:2)
        from(0, 0)
        to(8, 2)
        cell(0, 0)
        cell(a:0, d:0)
    }
}
