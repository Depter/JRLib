package org.jreserve.triangle.claim;

import org.jreserve.triangle.AbstractTriangle;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class InputTriangle extends AbstractTriangle<ClaimTriangle> implements ClaimTriangle {

    private double[][] data;
    private int accidents;
    private int developments;
    
    public InputTriangle(double[][] data) {
        if(data == null || data.length == 0) {
            fillEmptyData();
        } else {
            fillData(data);
        }
    }
    
    protected void setData(double[][] data) {
        if(data == null || data.length == 0) {
            fillEmptyData();
        } else {
            fillData(data);
        }
        fireChange();
    }
    
    private void fillEmptyData() {
        accidents = 0;
        developments = 0;
    }
    
    private void fillData(double[][] data) {
        accidents = data.length;
        this.data = new double[accidents][];
        for(int a=0; a<accidents; a++)
            copyDevelopment(a, data[a]);
    }
    
    private void copyDevelopment(int accident, double[] data) {
        int devs = setDevelopments(data);
        this.data[accident] = new double[devs];
        System.arraycopy(data, 0, this.data[accident], 0, devs);
    }
    
    private int setDevelopments(double[] data) {
        int devs = data.length;
        if(devs > developments) 
            developments = devs;
        return devs;
    }
    
    @Override
    protected void recalculateLayer() {
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
            return data[accident].length;
        return 0;
    }
    
    @Override
    public double getValue(int accident, int development) {
        if(withinBounds(accident, development))
            return data[accident][development];
        return Double.NaN;
    }
    
    @Override
    public InputTriangle copy() {
        return new InputTriangle(data);
    }
}
