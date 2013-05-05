package org.jreserve.grscript.jrlib

setNumberFormat "0.00000"

String path = "C:\\Munka\\Java\\NetbeansWS\\GRScript\\test\\org\\jreserve\\grscript\\input\\apc_paid.csv"
data = readCsv(path, [columnSeparator:","])

paid = triangle(cummulate(data))
paid = corrigate(paid, [a:0, d:2, value:90000])
paid = smooth(paid) {
    type(name:"Exponential", alpha:0.5)
    cell(a:1, d:1, apply:false)
    cell(a:2, d:1, apply:false)
    cell(a:3, d:1, apply:true)
}

paid = triangle(cummulate(data)) {
    corrigate(0, 2, 90000)
    corrigate(a:0, d:2, value:90000)
    exclude(0, 2)
    smooth {
        type(name:"Exponential", alpha:0.5)
        cell(a:1, d:1, apply:false)
        cell(a:2, d:1, apply:false)
        cell(a:3, d:1, apply:true)
    }
}

paidData "Paid:", paid
