package org.jreserve.jrlib.bootstrap.mcl.pseudodata;

import java.util.List;
import org.jreserve.jrlib.bootstrap.residualgenerator.AbstractResidualSegment;
import org.jreserve.jrlib.util.random.Random;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class MclResidualSegment extends AbstractResidualSegment<MclResidualCell> {
    
    private final Random rnd;
    private MclResidualCell[] residualCells;
    
    MclResidualSegment(Random rnd) {
        this.rnd = rnd;
    }
    
    MclResidualSegment(Random rnd, int[][] cells) {
        super(cells);
        this.rnd = rnd;
    }
    
    @Override
    protected void setValues(List<MclResidualCell> values) {
        super.setValues(values);
        this.residualCells = new MclResidualCell[valueCount];
        for(int i=0; i<valueCount; i++)
            this.residualCells[i] = values.get(i);
    }
    
    MclResidualCell getCell() {
        if(valueCount == 0)
            return null;
        if(valueCount == 1)
            return residualCells[0];
        return residualCells[rnd.nextInt(valueCount)];
    }
}
