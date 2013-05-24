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
lrs = linkRatio(data)

lrScales = scale(lrs)
lrScales = scale(lrs, "min-max")
lrScales = scale(lrs, "log-linear")

lrScales = scale(lrs) {
    minMax(0)
    minMax(0..2)
    logLinear(0)
    logLinear(0..2)
    fixed(1, 200.45)
    fixed(1:200.45, 2:122.5)
}