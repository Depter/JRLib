package org.jreserve.grscript.jrlib

String HOME = "C:\\Munka\\Java\\NetbeansWS\\GRScript\\test\\org\\jreserve\\grscript\\input\\"
def paidData = readCsv(HOME+"apc_paid.csv", [columnSeparator:","])
paidData = cummulate(data)

//Basic data
paidTriangle = triangle(paidData)
paidLr = linkRatio(data)   //WeightedAverage
paidLr = smooth(paidLr, 10, "exponential")

//Residuals
paidResiduals = odpResiduals(paidLr) {
    exclude(accident:0, development:8)
    exclude(accident:8, development:0)
    adjust()
}

//Residual scales
paidResScale = variableScale(paidResiduals)

//Bootstrap
bootstrap = odpBootstrap {
    count 1000
    linkRatio paidLr
    random "Java", 10   //random(String) , random(Random), DEFAULT = Java
    scale "Constant"    //Constant | Variable            , DEFAULT = "Constant"
    scales paidResScale
    segment {
        from(accient:0, development:0)
        to(a:8, d:2)
        from(0, 0)
        to(8, 2)
        cell(0, 0)
        cell(a:0, d:0)
    }
}
