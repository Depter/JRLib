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

String path = "C:\\Munka\\Java\\NetbeansWS\\GRScript\\test\\org\\jreserve\\grscript\\input\\apc_exposure.csv"
data = readCsv(path, [columnSeparator:","])
data = data[0]

exposure = vector(5, 11.5)
exposure = vector(data)
exposure = corrigate(exposure, 1, 90000)
exposure = corrigate(exposure, [1:90000, 2:50000])

exposure = exclude(exposure, 2)
exposure = exclude(exposure, 2, 3)
exposure = exclude(exposure, 2..4)

exposure = smooth(exposure) {
    type(name:"Exponential", alpha:0.5)
    cells(1:false, 2:false, 3:true)
}

exposure = vector(data) {
    corrigate(0, 90000)
    corrigate(0:90000, 1:50000)
    exclude(0, 2)
    exclude(0..2)
    smooth {
        type(name:"Exponential", alpha:0.5)
        cells(1:false, 2:false, 3:true)
    }
}

paidData "Exposure:", exposure
