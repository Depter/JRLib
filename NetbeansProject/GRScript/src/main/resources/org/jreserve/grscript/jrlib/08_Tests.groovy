package org.jreserve.grscript.jrlib

setNumberFormat "0.00000"

String path = "C:\\Munka\\Java\\NetbeansWS\\GRScript\\test\\org\\jreserve\\grscript\\input\\apc_paid.csv"
data = readCsv(path, [columnSeparator:","])
paid = triangle(cummulate(data))
factors = factors(paid)

test = testCalendarEffect(paid)
test = testCalendarEffect(paid, 0.5)
test = testCalendarEffect(factors)
test = testCalendarEffect(factors, 0.5)

test = testUncorrelatedDevFactors(paid)
test = testUncorrelatedDevFactors(paid, 0.5)
test = testUncorrelatedDevFactors(factors)
test = testUncorrelatedDevFactors(factors, 0.5)

summary(test)