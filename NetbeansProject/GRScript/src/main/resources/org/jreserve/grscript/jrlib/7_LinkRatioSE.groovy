package org.jreserve.grscript.jrlib

setNumberFormat "0.00000"

String path = "C:\\Munka\\Java\\NetbeansWS\\GRScript\\test\\org\\jreserve\\grscript\\input\\apc_paid.csv"
data = readCsv(path, [columnSeparator:","])
cummulate(data)
lrs = linkRatio(data)
scales = scale(lrs, "min-max")


lrSE = standardError(scales)              //->LogLin
lrSE = standardError(scales, "LogLin")
lrSE = standardError(scales, "fixed-rate")

lrSE = standardError(scales) {
    logLinear(0)
    logLinear(0..2)
    fixedRate(0)
    fixedRate(0..2)
    fixed(6, 0.02)
    fixed(0:0.02, 1:0.03)
}