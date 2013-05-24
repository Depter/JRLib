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
factors = factors(paid)

test = testCalendarEffect(paid)
test = testCalendarEffect(paid, 0.5)
test = testCalendarEffect(factors)
test = testCalendarEffect(factors, 0.5)

test = testUncorrelatedDevFactors(paid)
test = testUncorrelatedDevFactors(paid, 0.5)
test = testUncorrelatedDevFactors(factors)
test = testUncorrelatedDevFactors(factors, 0.5)

summary(test)