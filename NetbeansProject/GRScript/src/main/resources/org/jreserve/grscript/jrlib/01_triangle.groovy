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

String path = "C:\\Munka\\Java\\NetbeansWS\\GRScript\\test\\org\\jreserve\\grscript\\input\\apc_paid.csv"
data = readCsv(path, [columnSeparator:","])

paid = triangle(cummulate(data))
paid = corrigate(paid, [a:0, d:2, value:90000])
paid = smooth(paid) {
    type(name:"Exponential", alpha:0.5)
    cell(a:1, d:1, apply:false)
    cell(a:2, d:1, apply:false)
    cell(a:3, d:1, apply:true)
}

paid = triangle(cummulate(data)) {
    corrigate(0, 2, 90000)
    corrigate(a:0, d:2, value:90000)
    exclude(0, 2)
    smooth {
        type(name:"Exponential", alpha:0.5)
        cell(a:1, d:1, apply:false)
        cell(a:2, d:1, apply:false)
        cell(a:3, d:1, apply:true)
    }
}

paidData "Paid:", paid
