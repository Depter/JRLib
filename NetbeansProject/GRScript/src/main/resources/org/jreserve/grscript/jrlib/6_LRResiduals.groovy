package org.jreserve.grscript.jrlib

setNumberFormat "0.00000"

String path = "C:\\Munka\\Java\\NetbeansWS\\GRScript\\test\\org\\jreserve\\grscript\\input\\apc_paid.csv"
data = readCsv(path, [columnSeparator:","])
cummulate(data)
lrs = linkRatio(data)

res = residuals(lrs)        //Not adjusted
res = residuals(lrs, true)  //adjusted
res = adjust(res)           //adjust residuals

res = exclude(res, [a:0, d:2])
res = exclude(res, [accident:0, development:2])
res = exclude(res, 0, 2)

res = residuals(lrs) {
    adjust()
    exclude(0, 0)
    exclude(a:1, d:0)
    exclude(accident:2, development:0)
}