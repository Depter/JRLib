
String path = "C:\\Munka\\Java\\NetbeansWS\\GRScript\\test\\org\\jreserve\\grscript\\input\\apc_paid.csv"
//data = readCsv(path, [columnSeparator:","])
data = readCsv(path) {
    columnSeparator  ','
//    decimalSeparator '.'
//    rowHeader        false
//    columnHeader     false
//    quotes           false
}
