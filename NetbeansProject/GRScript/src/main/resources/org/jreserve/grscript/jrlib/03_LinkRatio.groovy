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
setNumberFormat "0.00000"

String path = "C:\\Munka\\Java\\NetbeansWS\\GRScript\\test\\org\\jreserve\\grscript\\input\\apc_paid.csv"
data = readCsv(path, [columnSeparator:","])
data = cummulate(data)

//From double[][]
lrs = linkRatio(data)

//from claims
paid = triangle(paid)
lrs = linkRatio(paid)

//From factors
factors = factors(data)
lrs = linkRatio(factors)

//Builder syntax
lrs = linkRatio(factors) {
    average(0)          //-> other methods: mack | max | min | weightedAverage
    average(0..2)
    average([0, 2, 3])
    fixed(0, 1.3)
    fixed(0..2, 3.5, 2.5, 1.5)
    fixed([0, 2, 3], 3.5, 2.5, 1.5)
    fixed([0, 2, 3], [3.5, 2.5, 1.5])
}

printData "LinkRatios:", lrs