package org.jreserve.grscript.jrlib

setNumberFormat "0.00000"

String HOME = "C:\\Munka\\Java\\NetbeansWS\\GRScript\\test\\org\\jreserve\\grscript\\input\\"
def paidData = readCsv(HOME+"apc_paid.csv", [columnSeparator:","])
def incurredData = readCsv(HOME+"apc_incurred.csv", [columnSeparator:","])

estimate = averageCostEstimate(numberLrs, costLrs)
estimate = estimate(method:"Average-Cost", numbers:numberLrs, costs:costLrs)

estimate = bornhuetterFergusonEstimate(lrs, exposure, lossRatio)
estimate = estimate(method:"Bornhuetter-Ferguson", 
                    linkRatios:lrs, 
                    exposure:exposure, 
                    lossRatio:lossRatio)

estimate = expectedLossRatioEstimate(lrs, exposure, lossRatio)
estimate = estimate(method:"Expected-Loss-Ratio", 
                    linkRatios:lrs, 
                    exposure:exposure, 
                    lossRatio:lossRatio)
                
estimate = capeCodEstimate(lrs, exposure)
estimate = estimate(method:"Cape-Cod", 
                    linkRatios:lrs, 
                    exposure:exposure)

estimate = chainLadderEstimate(lrs)
estimate = estimate(method:"Chain-Ladder", 
                    linkRatios:lrs)

estimate = mackEstimate(lrSE)
estimate = estimate(method:"Mack",
                    linkRatiosSE:lrs)

estimate = munichChainLadderEstimate {
    lrPaid lrPaidRes
    crPaid iPerPRes
    lrIncurred lrIncurredRes
    crIncurred pPerIRes
}
summary(estimate)
