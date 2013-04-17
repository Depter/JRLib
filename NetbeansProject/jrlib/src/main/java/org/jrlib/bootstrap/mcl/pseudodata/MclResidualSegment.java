package org.jrlib.bootstrap.mcl.pseudodata;

import java.util.List;
import org.jrlib.bootstrap.residualgenerator.AbstractResidualSegment;
import org.jrlib.util.random.Random;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class MclResidualSegment extends AbstractResidualSegment<MclResidualCell> {
    
    private final Random rnd;
    private MclResidualCell[] residualCells;
    
    public MclResidualSegment(Random rnd) {
        this.rnd = rnd;
    }
    
    public MclResidualSegment(Random rnd, int[][] cells) {
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
    
    public MclResidualCell getCell() {
        if(valueCount == 0)
            return null;
        if(valueCount == 1)
            return residualCells[0];
        return residualCells[rnd.nextInt(valueCount)];
    }
}
