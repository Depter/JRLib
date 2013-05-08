package org.jreserve.grscript.jrlib

setNumberFormat "0.00000"

String HOME = "C:\\Munka\\Java\\NetbeansWS\\GRScript\\test\\org\\jreserve\\grscript\\input\\"
def paidData = readCsv(HOME+"apc_paid.csv", [columnSeparator:","])
def incurredData = readCsv(HOME+"apc_incurred.csv", [columnSeparator:","])

//Triangles
def paid = triangle(cummulate(data))
def incurred = triangle(cummulate(incurredData))

def lrPaid = smooth(linkRatio(paid), 10)
def lrIncurred = smooth(linkRatio(incurred), 10)
def crs = ratios(lrPaid, lrIncurred)
def scales = scale(crs)

res = residuals(scales)        //Not adjusted
res = residuals(scales, true)  //adjusted
res = adjust(rs)           //adjust residuals

res = exclude(scales, [a:0, d:2])
res = exclude(scales, [accident:0, development:2])
res = exclude(scales, 0, 2)

res = residuals(scales) {
    adjust()
    exclude(0, 0)
    exclude(a:1, d:0)
    exclude(accident:2, development:0)
}