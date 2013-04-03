package org.jreserve.triangle.claim;

import org.jreserve.AbstractMultiSourceCalculationData;
import org.jreserve.triangle.AbstractTriangle;
import org.jreserve.triangle.TriangleOperation;
import org.jreserve.triangle.claim.CompositeClaimTriangle.CompositeClaimTriangleInput;

//TODO add javadoc
/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class CompositeClaimTriangle extends AbstractTriangle<CompositeClaimTriangleInput> implements ClaimTriangle {
    
    private TriangleOperation operation;
    private int accidents;
    private int developments;
    private double[][] values;
    
    public CompositeClaimTriangle(ClaimTriangle primary, ClaimTriangle secondary, TriangleOperation operation) {
        this(new CompositeClaimTriangleInput(primary, secondary), operation);
    }
    
    public CompositeClaimTriangle(CompositeClaimTriangleInput input, TriangleOperation operation) {
        super(input);
        initOperation(operation);
        doRecalculate();
    }
    
    private void initOperation(TriangleOperation operation) {
        if(operation == null)
            throw new NullPointerException("Operation is null!");
        this.operation = operation;
    }

    @Override
    public int getAccidentCount() {
        return accidents;
    }

    @Override
    public int getDevelopmentCount() {
        return developments;
    }

    @Override
    public int getDevelopmentCount(int accident) {
        if(withinBounds(accident))
            return values[accident].length;
        return 0;
    }

    @Override
    public double getValue(int accident, int development) {
        if(withinBounds(accident, development))
            return values[accident][development];
        return Double.NaN;
    }
    
    @Override
    protected void recalculateLayer() {
        doRecalculate();
    }
    
    private void doRecalculate() {
        initAccidents();
        initDevelopments();
        recalculateValues();
    }
    
    private void initAccidents() {
        int a1 =  source.getPrimarySource().getAccidentCount();
        int a2 = source.getSecondarySource().getAccidentCount();
        this.accidents = (a1<a2)? a1 : a2;
    }
    
    private void initDevelopments() {
        int d1 = source.getPrimarySource().getDevelopmentCount();
        int d2 = source.getSecondarySource().getDevelopmentCount();
        this.developments = (d1<d2)? d1 : d2;
    }
    
    private void recalculateValues() {
        values = new double[accidents][];
        for(int a=0; a<accidents; a++)
            values[a] = recalculateAccident(a);
    }
    
    private double[] recalculateAccident(int accident) {
        int devs = getDevelopments(accident);
        double[] result = new double[devs];
        for(int d=0; d<devs; d++)
            result[d] = recalculateCell(accident, d);
        return result;
    }
    
    private int getDevelopments(int accident) {
        int d1 = source.getPrimarySource().getDevelopmentCount(accident);
        int d2 = source.getSecondarySource().getDevelopmentCount(accident);
        return (d1<d2)? d1 : d2;
    }
    
    private double recalculateCell(int accident, int development) {
        double v1 = source.getPrimaryValue(accident, development);
        double v2 = source.getSecondaryValue(accident, development);
        return operation.operate(v1, v2);
    }
    
    @Override
    public CompositeClaimTriangle copy() {
        return new CompositeClaimTriangle(
                source.copy(), 
                operation);
    }
    
    public static class CompositeClaimTriangleInput extends AbstractMultiSourceCalculationData<ClaimTriangle> {

        private ClaimTriangle primary;
        private ClaimTriangle secondary;
        
        public CompositeClaimTriangleInput(ClaimTriangle primary, ClaimTriangle secondary) {
            super(primary, secondary);
            this.primary = primary;
            this.secondary = secondary;
        }
        
        public ClaimTriangle getPrimarySource() {
            return primary;
        }
        
        public double getPrimaryValue(int accident, int development) {
            return primary.getValue(accident, development);
        }
        
        public ClaimTriangle getSecondarySource() {
            return secondary;
        }
        
        public double getSecondaryValue(int accident, int development) {
            return secondary.getValue(accident, development);
        }
        
        @Override
        protected void recalculateLayer() {
        }
        
        private CompositeClaimTriangleInput copy() {
            return new CompositeClaimTriangleInput(
                    primary.copy(), 
                    secondary.copy()
                    );
        }
    }
}
