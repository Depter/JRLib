/*
 *  Copyright (C) 2013, Peter Decsi.
 * 
 *  This library is free software: you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public 
 *  License as published by the Free Software Foundation, either 
 *  version 3 of the License, or (at your option) any later version.
 * 
 *  This library is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 * 
 *  You should have received a copy of the GNU General Public License
 *  along with this library.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.jreserve.jrlib.estimate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.jreserve.jrlib.AbstractMultiSourceCalculationData;
import org.jreserve.jrlib.CalculationData;
import org.jreserve.jrlib.CalculationState;
import org.jreserve.jrlib.triangle.Cell;
import org.jreserve.jrlib.triangle.TriangleUtil;
import org.jreserve.jrlib.bootstrap.StaticProcessSimulator;
import org.jreserve.jrlib.bootstrap.ProcessSimulator;

/**
 * The CompositeEstimate class allowes to combine the result of multiple
 * estimation methods, thus enabling to use a different estimate for evry 
 * accident period.
 * 
 * The composite estimate have as many accident periods as input estimates, 
 * and the number of development periods is equal of the number of development
 * periods of the shortest input estimate.
 * 
 * @author Peter Decsi
 * @version 1.0
 */
public class CompositeEstimate 
    extends AbstractMultiSourceCalculationData<Estimate> 
    implements Estimate {
    
    private static Estimate[] getUniqueEstimates(Estimate[] estimates) {
        List<Estimate> result = new ArrayList<Estimate>(estimates.length);
        for(Estimate estimate: estimates)
            if(!contains(result, estimate))
                result.add(estimate);
        return result.toArray(new Estimate[result.size()]);
    }
    
    private static boolean contains(List<Estimate> estimates, Estimate estimate) {
        if(estimate == null)
            throw new NullPointerException("Can not add null estimates!");
        for(Estimate e : estimates)
            if(e == estimate)
                return true;
        return false;
    }
    
    private Estimate[] estimates;
    private int accidents;
    private int developments;
    private double[][] values;
    
    /**
     * Creates a composite estimate from the given estimates.
     * 
     * @throws NullPointerException if 'estimates' or one of its elements is null.
     */
    public CompositeEstimate(Collection<Estimate> estimates) {
        this(estimates.toArray(new Estimate[estimates.size()]));
    }
    
    /**
     * Creates a composite estimate from the given estimates.
     * 
     * @throws NullPointerException if 'estimates' or one of its elements is null.
     */
    public CompositeEstimate(Estimate... estimates) {
        super(getUniqueEstimates(estimates));
        copyEstimates(estimates);
        doRecalculate();
    }
    
    private void copyEstimates(Estimate[] estimates) {
        this.accidents = estimates.length;
        this.estimates = new Estimate[accidents];
        System.arraycopy(estimates, 0, this.estimates, 0, accidents);
    }

    /**
     * Returns the Estimate used to calculate the values for the given 
     * accident period, or 'null' it 'accident' falls outside the bounds 
     * '[0; {@link #getAccidentCount() getAccidentCount()}['.
     */
    public Estimate getEstimate(int accident) {
        return withinBounds(accident)?
                estimates[accident] :
                null;
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
    public int getObservedDevelopmentCount(int accident) {
        return withinBounds(accident)?
                estimates[accident].getObservedDevelopmentCount(accident) :
                0;
    }
    
    private boolean withinBounds(int accident) {
        return 0 <= accident && accident < accidents;
    }
    
    @Override
    public double getValue(Cell cell) {
        return getValue(cell.getAccident(), cell.getDevelopment());
    }

    @Override
    public double getValue(int accident, int development) {
        return withinBounds(accident, development)?
                values[accident][development] :
                Double.NaN;
    }
    
    private boolean withinBounds(int accident, int development) {
        return withinBounds(accident) &&
               0 <= development && development < developments;
    }

    @Override
    public double[][] toArray() {
        return TriangleUtil.copy(values);
    }

    @Override
    public double getReserve(int accident) {
        int dLast = getObservedDevelopmentCount(accident)-1;
        double lastObserved = getValue(accident, dLast);
        double lastEstimated = getValue(accident, developments-1);
        return lastEstimated - lastObserved;
    }

    @Override
    public double getReserve() {
        double sum = 0d;
        for(int a=0; a<accidents; a++)
            sum += getReserve(a);
        return sum;
    }

    @Override
    public double[] toArrayReserve() {
        double[] result = new double[accidents];
        for(int a=0; a<accidents; a++)
            result[a] = getReserve(a);
        return result;
    }

    @Override
    protected void recalculateLayer() {
        doRecalculate();
    }

    private void doRecalculate() {
        if(sources == null) {
            accidents = 0;
            developments = 0;
        } else {
            recalculateDevelopments();
        }
        values = new double[accidents][developments];
        recalculateValues();
    }
    
    private void recalculateDevelopments() {
        developments = -1;
        for(Estimate e : sources) {
            int d = e==null? 0 : e.getDevelopmentCount();
            if(developments==-1 || developments>d)
                developments = d;
        }
    }
    
    /**
     * Recalculates the values for all cells.
     */
    protected void recalculateValues() {
        for(int a=0; a<accidents; a++)
            for(int d=0; d<developments; d++)
                values[a][d] = recalculateValue(a, d);
    }

    /**
     * Recalculates the value for the given cell.
     */
    protected double recalculateValue(int accident, int development) {
        return estimates[accident].getValue(accident, development);
    }
}
