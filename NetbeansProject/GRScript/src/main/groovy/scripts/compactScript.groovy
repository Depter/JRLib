package scripts

setNumberFormat "0.00000"

String path = "C:\\Munka\\Java\\NetbeansWS\\GRScript\\test\\org\\jreserve\\grscript\\input\\apc_paid.csv"
data = readCsv(path, [columnSeparator:','])

factors = factors(cummulate(data)) {
    exclude(0, 0)
    smooth {
        type(name:"DoubleExponential", alpha:0.5, beta:0.5)
        cell(a:2, d:0, apply:false)
        cell(a:3, d:0, apply:false)
        cell(a:4, d:0, apply:true)
    }
}

lrs = linkRatio(factors) //-> SimpleLinkRatio

printData "LinkRatios:", lrs