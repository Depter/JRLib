package org.jreserve.grscript.jrlib

String HOME = "C:\\Munka\\Java\\NetbeansWS\\GRScript\\test\\org\\jreserve\\grscript\\input\\"
def paidData = readCsv(HOME+"apc_paid.csv", [columnSeparator:","])
paidData = cummulate(data)

paidTriangle = triangle(paidData)
paidLr = linkRatio(data)   //WeightedAverage
paidLr = smooth(paidLr, 10, "exponential")

//Residuals
paidResiduals = residuals(paidLr)        //Not adjusted
paidResiduals = residuals(paidLr, true)  //adjusted
paidResiduals = adjust(paidResiduals)

paidResiduals = exclude(paidResiduals , [a:0, d:2])
paidResiduals = exclude(paidResiduals , [accident:0, development:2])
paidResiduals = exclude(paidResiduals , 0, 2)
paidResiduals = odpResiduals(paidLr) {
    adjust()
    exclude(0, 0)
    exclude(a:1, d:0)
    exclude(accident:2, development:0)
}
