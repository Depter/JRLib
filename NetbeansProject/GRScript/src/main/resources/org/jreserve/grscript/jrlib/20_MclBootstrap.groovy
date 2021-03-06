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
def incurredData = readCsv(HOME+"apc_incurred.csv", [columnSeparator:","])
paidData = cummulate(paidData)
incurredData = cummulate(incurredData)

//////////////////////////////////////////
//              BASIC DATA              //
//////////////////////////////////////////
paidTriangle = triangle(paidData)
paidLr = linkRatio(paidTriangle)            //WeightedAverage
paidLr = smooth(paidLr, 10, "exponential")  //Exponential tail, length 10
paidLrScales = scale(paidLr)                //Min-Max

incurredTriangle = triangle(paidData)
incurredLr = linkRatio(incurredTriangle)        //WeightedAverage
incurredLr = smooth(paidLr, 10, "exponential")  //Exponential tail, length 10
incurredLrScales = scale(paidLr)                //Min-Max


//////////////////////////////////////////////
//          LINK-RATIO RESIDUALS            //
//////////////////////////////////////////////
paidLrRes = residuals(paidLrScales) {
    exclude(accident:0, development:6)
    exclude(accident:6, development:0)
    adjust()
}

incurredLrRes = residuals(incurredLrScales) {
    exclude(accident:0, development:6)
    exclude(accident:6, development:0)
    adjust()
}


//////////////////////////////////////////////
//         CLAIM-RATIO RESIDUALS            //
//////////////////////////////////////////////
crsPperI = ratios(lrPaid, lrIncurred)
scalesPperI = scale(crsPperI)
pPerIRes = residuals(scalesPperI, true)

crsIperP = ratios(lrIncurred, lrPaid)
scalesIperP = scale(crsIperP)
IperPRes = residuals(scalesIperP, true)


//////////////////////////////////////////////
//         CLAIM-RATIO RESIDUALS            //
//////////////////////////////////////////////
bootstrap = mackBootstrap {
    //Bootstrap count
    count 1000
    //Random generator
    random "Java", 10   //random(String) , random(Random), DEFAULT = Java
    
    //Residuals
    residuals {
        paidLr paidLrRes
        paidRatio iPerPRes
        incurredLr incurredLrRes
        incurredRatio pPerIRes
    }
    
    //Process error
    process {
        paid "Gamma"        //Default: Gamma, values: [Gamma | Weighted-Gamma | Normal | Weighted-Normal | Constant]
        incurred "Gamma"    //Default: Gamma, values: [Gamma | Weighted-Gamma | Normal | Weighted-Normal | Constant]
    }
    process paid:"Gamma", incurred:"Gamma" //Gamma, values: [Gamma | Weighted-Gamma | Normal | Weighted-Normal | Constant]
    
    //Segments
    segment {
        from(accient:0, development:0)
        to(a:8, d:2)
        from(0, 0)
        to(8, 2)
        cell(0, 0)
        cell(a:0, d:0)
    }
}