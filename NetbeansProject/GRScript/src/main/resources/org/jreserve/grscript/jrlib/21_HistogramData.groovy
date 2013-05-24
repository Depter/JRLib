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
paidLr = linkRatio(paidData)                    //WeightedAverage
paidLr = smooth(paidLr, 10, "exponential")  //Exponential tail, length 10
paidLrScales = scale(paidLr)                //Min-Max

//Residuals
paidLrRes = residuals(paidLrScales) {
    exclude(accident:0, development:6)
    adjust()
    center()
}

//Bootstap
bootstrap = mackBootstrap {
    count 1000
    random "Java", 10
    residuals paidLrRes
    process "Gamma" 
}
bootstrap.run()

histData = histogram(bootstrap.getReserves(), 50)
histData = histogram(bootstrap.getReserves(), 1850000, 60000)
histData = histogram(bootstrap.getReserves()) {
    intervals 50
    first 1850000
    step 60000
}
