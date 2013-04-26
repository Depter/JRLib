
def name='AA461472'

println "Hello $name!"
Exponential
InversePower
Power
Weibull



UserInput


TailFitting {
  int methodType
  int[] excluded
  int[] applied
}
rSquare


smooth(linkRatio, 8, type="exponential")
smooth(linkRatio, 8) {
  exponential(0)
  exponential(0..2)
  exponential([0, 2, 3])

  exponential(developments:[3, 4, 5] exclude:[0..2])

}