package org.jrlib.bootstrap.residualgenerator;

import java.util.List;
import org.jrlib.util.random.Random;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class DoubleResidualSegment extends AbstractResidualSegment<Double> {
    
    private final Random rnd;
    private double[] residuals;
    
    public DoubleResidualSegment(Random rnd) {
        this.rnd = rnd;
    }
    
    public DoubleResidualSegment(Random rnd, int[][] cells) {
        super(cells);
        this.rnd = rnd;
    }
    
    @Override
    protected void setValues(List<Double> values) {
        super.setValues(values);
        this.residuals = new double[valueCount];
        for(int i=0; i<valueCount; i++)
            this.residuals[i] = values.get(i);
    }
    
    public double getResidual() {
        if(valueCount == 0)
            return Double.NaN;
        if(valueCount == 1)
            return residuals[0];
        return residuals[rnd.nextInt(valueCount)];
    }
}
