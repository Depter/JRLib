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
def paidData = readCsv(HOME+"apc_paid.csv", [columnSeparator:","])
def incurredData = readCsv(HOME+"apc_incurred.csv", [columnSeparator:","])

//Triangles
def paid = triangle(cummulate(data))
def incurred = triangle(cummulate(incurredData))

def lrPaid = smooth(linkRatio(paid), 10)
def lrIncurred = smooth(linkRatio(incurred), 10)
def crs = ratios(lrPaid, lrIncurred)
def scales = scale(crs)

res = residuals(scales)        //Not adjusted
res = residuals(scales, true)  //adjusted
res = adjust(rs)           //adjust residuals
res = center(res)              //center the resiuals

res = exclude(scales, [a:0, d:2])
res = exclude(scales, [accident:0, development:2])
res = exclude(scales, 0, 2)

res = residuals(scales) {
    adjust()
    exclude(0, 0)
    exclude(a:1, d:0)
    exclude(accident:2, development:0)
    center()
}