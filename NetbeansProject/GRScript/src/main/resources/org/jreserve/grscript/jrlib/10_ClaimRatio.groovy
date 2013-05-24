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
incurredData = readCsv(HOME+"apc_incurred.csv", [columnSeparator:","])

//Triangles
paid = triangle(cummulate(data))
incurred = triangle(cummulate(incurredData))
pPerI = ratios(paid, incurred)

cr = ratios(pPerI)
cr = ratios(paid, incurred)
cr = ratios(numerator:paid, denominator:incurred)

def lrPaid = smooth(linkRatio(paid), 10)
def lrIncurred = smooth(linkRatio(incurred), 10)

cr = ratios(lrPaid, lrIncurred)
cr = ratios(pPerI, lrPaid, lrIncurred)
cr = ratios(pPerI, 10, lrPaid, lrIncurred)

cr = ratios(lrPaid, lrIncurred) {
    lrExtrapolation(lrPaid, lrIncurred, 8, 9)
    fixed(8, 0.9)
    fixed(8:0.9, 9:1)
}

cr = ratios(paid, incurred, 10) {
    lrExtrapolation(lrPaid, lrIncurred, 8, 9)
    fixed(8, 0.9)
    fixed(8:0.9, 9:1)
}

cr = ratios(pPerI, 10) {
    lrExtrapolation(lrPaid, lrIncurred, 7, 8)
    lrExtrapolation(lrPaid, lrIncurred, 2..4)
    fixed(8, 0.9)
    fixed(8:0.9, 9:1)
}