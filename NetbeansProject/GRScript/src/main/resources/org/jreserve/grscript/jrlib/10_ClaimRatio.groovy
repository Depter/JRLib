package org.jreserve.grscript.jrlib

setNumberFormat "0.00000"

String HOME = "C:\\Munka\\Java\\NetbeansWS\\GRScript\\test\\org\\jreserve\\grscript\\input\\"
paidData = readCsv(HOME+"apc_paid.csv", [columnSeparator:","])
incurredData = readCsv(HOME+"apc_incurred.csv", [columnSeparator:","])

//Triangles
paid = triangle(cummulate(data))
incurred = triangle(cummulate(incurredData))
pPerI = ratios(paid, incurred)

cr = ratios(pPerI)
cr = ratios(paid, incurred)
cr = ratios(numerator:paid, denominator:incurred)

def lrPaid = smooth(linkRatio(paid), 10)
def lrIncurred = smooth(linkRatio(incurred), 10)

cr = ratios(lrPaid, lrIncurred)
cr = ratios(pPerI, lrPaid, lrIncurred)
cr = ratios(pPerI, 10, lrPaid, lrIncurred)

cr = ratios(lrPaid, lrIncurred) {
    lrExtrapolation(lrPaid, lrIncurred, 8, 9)
    fixed(8, 0.9)
    fixed(8:0.9, 9:1)
}

cr = ratios(paid, incurred, 10) {
    lrExtrapolation(lrPaid, lrIncurred, 8, 9)
    fixed(8, 0.9)
    fixed(8:0.9, 9:1)
}

cr = ratios(pPerI, 10) {
    lrExtrapolation(lrPaid, lrIncurred, 7, 8)
    lrExtrapolation(lrPaid, lrIncurred, 2..4)
    fixed(8, 0.9)
    fixed(8:0.9, 9:1)
}