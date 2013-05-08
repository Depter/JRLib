package org.jreserve.grscript.jrlib

String HOME = "C:\\Munka\\Java\\NetbeansWS\\GRScript\\test\\org\\jreserve\\grscript\\input\\"
def paidData = readCsv(HOME+"apc_paid.csv", [columnSeparator:","])
paidData = cummulate(data)

//Basic data
paidTriangle = triangle(paidData)
paidLr = linkRatio(data)                    //WeightedAverage
paidLr = smooth(paidLr, 10, "exponential")  //Exponential tail, length 10
paidLrScales = scale(paidLr)                //Min-Max

//Residuals
paidLrRes = residuals(paidLrScales) {
    exclude(accident:0, development:8)
    exclude(accident:8, development:0)
    adjust()
}

//Bootstap
bootstrap = mackBootstrap {
    count 1000
    random "Java", 10   //random(String) , random(Random), DEFAULT = Java
    residuals paidLrRes
    process "Gamma"     //Default: Gamma, values: [Gamma | Normal | Constant]
    segment {
        from(accient:0, development:0)
        to(a:8, d:2)
        from(0, 0)
        to(8, 2)
        cell(0, 0)
        cell(a:0, d:0)
    }
}