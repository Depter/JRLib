package org.jreserve.grscript.jrlib

setNumberFormat "0.00000"

String path = "C:\\Munka\\Java\\NetbeansWS\\GRScript\\test\\org\\jreserve\\grscript\\input\\apc_paid.csv"
data = readCsv(path, [columnSeparator:","])
cummulate(data)

//Create from double array
factors = factors(data)

//Create from claim triangle
paid = triangle(data)
factors = factors(paid)

//Builder from claim triangle
factors = factors(paid) {
    exclude(0, 0)
    smooth {
        type(name:"DoubleExponential", alpha:0.5, beta:0.5)
        cell(a:2, d:0, apply:false)
        cell(a:3, d:0, apply:false)
        cell(a:4, d:0, apply:true)
    }
}

//Builder from double[][]
factors = factors(data) {
    exclude(0, 0)
    smooth {
        type(name:"DoubleExponential", alpha:0.5, beta:0.5)
        cell(a:2, d:0, apply:false)
        cell(a:3, d:0, apply:false)
        cell(a:4, d:0, apply:true)
    }
}


//Smooth factors
factors = smooth(factors) {
    type(name:"DoubleExponential", alpha:0.5, beta:0.5)
    cell(a:2, d:0, apply:false)
    cell(a:3, d:0, apply:false)
    cell(a:4, d:0, apply:true)
}

//corrigate factor
factors = corrigate(factors, [a:0, d:2, value:1.1])
factors = corrigate(factors, [accident:0, development:2, value:1.1])
factors = corrigate(factors, 0, 2, 1.1)

//Exclude factor
factors = exclude(factors, [a:0, d:2])
factors = exclude(factors, [accident:0, development:2])
factors = exclude(factors, 0, 2)

printData "Factors:", factors
