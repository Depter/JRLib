setNumberFormat "0.00000"

String path = "C:\\Munka\\Java\\NetbeansWS\\GRScript\\test\\org\\jreserve\\grscript\\input\\apc_paid.csv"
data = readCsv(path, [columnSeparator:","])
data = cummulate(data)
linkRatio = linkRatio(data)   //WeightedAverage

//Exponential
//InversePower
//Power
//Weibull
//UserInput

curve = smooth(linkRatio, 10)               //Default: type=Exponential
curve = smooth(linkRatio, 10, "exponential")
curve = smooth(linkRatio, 10, "exponential", 0..2)   //exclude first 3 elements from fit
curve = smooth(linkRatio, 10, "exponential", 0)      //exclude first elemnt for fit

curve = smoothAll(linkRatio, 10)               //Default: type=Exponential
curve = smoothAll(linkRatio, 10, "exponential")  //Use for all elements
curve = smoothAll(linkRatio, 10, "exponential", 0..2)   //exclude first 3 elements from fit
curve = smoothAll(linkRatio, 10, "exponential", 0)      //exclude first elemnt for fit

curve = smooth(linkRatio, 10) {
    exponential {
        apply(8)
        apply(8..9)
        exclude(0)
        exclude(0..2)
    }
    fixed {
        cell(1, 1.02)
    }
}

rSquare(curve)