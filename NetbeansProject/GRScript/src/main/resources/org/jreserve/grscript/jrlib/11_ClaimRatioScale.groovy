package org.jreserve.grscript.jrlib

setNumberFormat "0.00000"

String HOME = "C:\\Munka\\Java\\NetbeansWS\\GRScript\\test\\org\\jreserve\\grscript\\input\\"
paidData = readCsv(HOME+"apc_paid.csv", [columnSeparator:","])
incurredData = readCsv(HOME+"apc_incurred.csv", [columnSeparator:","])

//Triangles
paid = triangle(cummulate(data))
incurred = triangle(cummulate(incurredData))

def lrPaid = smooth(linkRatio(paid), 10)
def lrIncurred = smooth(linkRatio(incurred), 10)

crs = ratios(lrPaid, lrIncurred)

crScales = scale(crs)
crScales = scale(crs, "min-max")
crScales = scale(crs, "log-linear")

crScales = scale(crs) {
    minMax(0)
    minMax(0..2)
    logLinear(0)
    logLinear(0..2)
    fixed(1, 200.45)
    fixed(1:200.45, 2:122.5)
}