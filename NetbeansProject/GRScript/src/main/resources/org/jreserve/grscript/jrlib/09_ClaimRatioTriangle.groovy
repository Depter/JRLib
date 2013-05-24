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

setNumberFormat "0.00000"

String HOME = "C:\\Munka\\Java\\NetbeansWS\\GRScript\\test\\org\\jreserve\\grscript\\input\\"
paidData = readCsv(HOME+"apc_paid.csv", [columnSeparator:","])
paid = triangle(cummulate(data))

incurredData = readCsv(HOME+"apc_incurred.csv", [columnSeparator:","])
incurred = triangle(cummulate(incurredData))

pPerI = ratioTriangle(paid, incurred)
pPerI = ratioTriangle(numerator:paid, denominator:incurred)
pPerI = ratioTriangle(num:paid, denom:incurred)
pPerI = ratioTriangle(n:paid, d:incurred)

pPerI = corrigate(pPerI, 0, 2, 0.8)
pPerI = corrigate(pPerI, [a:0, d:2, value:0.8])

pPerI = exclude(0, 2)
pPerI = exclude(a:0, d:2)

pPerI = smooth(pPerI) {
    type(name:"Exponential", alpha:0.5)
    cell(a:1, d:1, apply:false)
    cell(a:2, d:1, apply:false)
    cell(a:3, d:1, apply:true)
}

pPerI = ratioTriangle(paid, incurred) {
    corrigate(0, 2, 0.8)
    corrigate(a:0, d:2, value:0.8)
    exclude(0, 2)
    smooth {
        type(name:"Exponential", alpha:0.5)
        cell(a:1, d:1, apply:false)
        cell(a:2, d:1, apply:false)
        cell(a:3, d:1, apply:true)
    }
}
