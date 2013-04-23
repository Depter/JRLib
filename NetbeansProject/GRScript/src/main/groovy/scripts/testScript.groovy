package scripts

setNumberFormat "0.00000"

String path = "C:\\Munka\\Java\\NetbeansWS\\GRScript\\test\\org\\jreserve\\grscript\\input\\apc_paid.csv"
//data = readCsv(path, [columnSeparator:","])
data = readCsv(path) {
    columnSeparator  ','
//    decimalSeparator '.'
//    rowHeader        false
//    columnHeader     false
//    quotes           false
}

paid = triangle(cummulate(data))
//paid = corrigate(paid, [a:0, d:2, value:90000])
//paid = smooth(paid) {
//    type(name:"Exponential", alpha:0.5)
//    cell(a:1, d:1, apply:false)
//    cell(a:2, d:1, apply:false)
//    cell(a:3, d:1, apply:true)
//}
//
//paid = triangle(data) {
//    corrigate(0, 2, 90000)
//    corrigate(a:0, d:2, value:90000)
//    exclude(0, 2)
//    smooth {
//        type(name:"Exponential", alpha:0.5)
//        cell(a:1, d:1, apply:false)
//        cell(a:2, d:1, apply:false)
//        cell(a:3, d:1, apply:true)
//    }
//}

//factors = factors(data)
//factors = factors(paid)
//factors = smooth(factors) {
//    type(name:"DoubleExponential", alpha:0.5, beta:0.5)
//    cell(a:2, d:0, apply:false)
//    cell(a:3, d:0, apply:false)
//    cell(a:4, d:0, apply:true)
//}

//factors = factors(paid) {
factors = factors(data) {
    exclude(0, 0)
    smooth {
        type(name:"DoubleExponential", alpha:0.5, beta:0.5)
        cell(a:2, d:0, apply:false)
        cell(a:3, d:0, apply:false)
        cell(a:4, d:0, apply:true)
    }
}

printData "Factors:", factors

lrs = linkRatio(factors) //-> SimpleLinkRatio
//lrs = linkRatio(factors) {
//    average(0)
//    average(0..2)
//    average([0, 2, 3])
//    mack
//    max
//    min
//    weightedAverage
//    fixed(0, 1.3)
//    fixed(0..2, 3.5, 2.5, 1.5)
//    fixed([0, 2, 3], 3.5, 2.5, 1.5)
//    fixed([0, 2, 3], [3.5, 2.5, 1.5])
//}

printData "LinkRatios:", lrs