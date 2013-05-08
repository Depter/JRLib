package org.jreserve.grscript.jrlib

String HOME = "C:\\Munka\\Java\\NetbeansWS\\GRScript\\test\\org\\jreserve\\grscript\\input\\"
def paidData = readCsv(HOME+"apc_paid.csv", [columnSeparator:","])
paidData = cummulate(data)

paidTriangle = triangle(paidData)
paidLr = linkRatio(data)   //WeightedAverage
paidLr = smooth(paidLr, 10, "exponential")

//Residuals
paidResiduals = residuals(paidLr) {
    exclude(accident:0, development:8)
    exclude(accident:8, development:0)
    adjust()
}

//Residual scales
paidResScale = constantScale(paidLr)
paidResScale = variableScale(paidLr)

paidResScale = constantScale(paidResiduals)
paidResScale = constantScale(paidResiduals, scale)  //fixed user input scale
paidResScale = variableScale(paidResiduals)

paidResScale = variableScale(paidResiduals) {
    fixed(1, 200.45)
    fixed(1:200.45, 2:122.5)
}
