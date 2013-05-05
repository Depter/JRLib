setNumberFormat "0.00000"

String path = "C:\\Munka\\Java\\NetbeansWS\\GRScript\\test\\org\\jreserve\\grscript\\input\\apc_paid.csv"
data = readCsv(path, [columnSeparator:","])
data = cummulate(data)

//From double[][]
lrs = linkRatio(data)

//from claims
paid = triangle(paid)
lrs = linkRatio(paid)

//From factors
factors = factors(data)
lrs = linkRatio(factors)

//Builder syntax
lrs = linkRatio(factors) {
    average(0)          //-> other methods: mack | max | min | weightedAverage
    average(0..2)
    average([0, 2, 3])
    fixed(0, 1.3)
    fixed(0..2, 3.5, 2.5, 1.5)
    fixed([0, 2, 3], 3.5, 2.5, 1.5)
    fixed([0, 2, 3], [3.5, 2.5, 1.5])
}

printData "LinkRatios:", lrs