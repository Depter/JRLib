setNumberFormat "0.00000"

String path = "C:\\Munka\\Java\\NetbeansWS\\GRScript\\test\\org\\jreserve\\grscript\\input\\apc_paid.csv"
data = readCsv(path, [columnSeparator:","])
data = cummulate(data)
lrs = linkRatio(data)

lrScales = scale(lrs)
lrScales = scale(lrs, "min-max")
lrScales = scale(lrs, "log-linear")

lrScales = scale(lrs) {
    minMax(0)
    minMax(0..2)
    logLinear(0)
    logLinear(0..2)
    fixed {
        cell(1, 200.45)
        cell(2, 122.5)
    }
}