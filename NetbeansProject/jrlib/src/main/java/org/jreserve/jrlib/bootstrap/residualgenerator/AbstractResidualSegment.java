package org.jreserve.jrlib.bootstrap.residualgenerator;

import java.util.List;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class AbstractResidualSegment<V> {

    protected int[][] cells;
    protected int cellCount;
    protected int valueCount;
    
    protected AbstractResidualSegment() {
    }
        
    protected AbstractResidualSegment(int[][] cells) {
        this.cells = cells;
        this.cellCount = cells.length;
    }

        
    public boolean containsCell(int accident, int development) {
        //TODO Binary search
        for(int c = 0; c<cellCount; c++) {
            int[] cell = cells[c];
            if(cell[0] == accident && cell[1] == development)
                return true;
        }
        return false;    
    }
    
    protected void setValues(List<V> values) {
        this.valueCount = values.size();
    }
}
