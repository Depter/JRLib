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
linkRatio = linkRatio(data)   //WeightedAverage

//Exponential
//InversePower
//Power
//Weibull
//UserInput

curve = smooth(linkRatio, 10)               //Default: type=Exponential
curve = smooth(linkRatio, 10, "exponential")
curve = smooth(linkRatio, 10, "exponential", 0..2)   //exclude first 3 elements from fit
curve = smooth(linkRatio, 10, "exponential", 0)      //exclude first elemnt for fit

curve = smoothAll(linkRatio, 10)               //Default: type=Exponential
curve = smoothAll(linkRatio, 10, "exponential")  //Use for all elements
curve = smoothAll(linkRatio, 10, "exponential", 0..2)   //exclude first 3 elements from fit
curve = smoothAll(linkRatio, 10, "exponential", 0)      //exclude first elemnt for fit

curve = smooth(linkRatio, 10) {
    exponential {
        apply(8)
        apply(8..9)
        exclude(0)
        exclude(0..2)
    }
    fixed {
        cell(1, 1.02)
    }
}

rSquare(curve)