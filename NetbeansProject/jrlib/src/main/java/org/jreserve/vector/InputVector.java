package org.jreserve.vector;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class InputVector extends AbstractVector implements Vector {

    private double[] data;
    private int length;
    
    public InputVector(double[] data) {
        if(data == null || data.length == 0) {
            fillEmptyData();
        } else {
            fillData(data);
        }
    }
    
    protected void setData(double[] data) {
        if(data == null || data.length == 0) {
            fillEmptyData();
        } else {
            fillData(data);
        }
        fireChange();
    }
    
    private void fillEmptyData() {
        length = 0;
    }
    
    private void fillData(double[] data) {
        length = data.length;
        this.data = new double[length];
        System.arraycopy(data, 0, this.data, 0, length);
    }
    
    @Override
    protected void recalculateLayer() {
    }

    @Override
    public int getLength() {
        return length;
    }
    
    @Override
    public double getValue(int index) {
        if(withinBound(index))
            return data[index];
        return Double.NaN;
    }
    
    public InputVector copy() {
        return new InputVector(data);
    }
}
