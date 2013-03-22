package org.jreserve.bootstrap.odp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.jreserve.bootstrap.DefaultResidualGenerator;
import org.jreserve.bootstrap.ProcessSimulator;
import org.jreserve.bootstrap.Random;
import org.jreserve.estimate.ChainLadderEstimate;
import org.jreserve.estimate.Estimate;
import org.jreserve.linkratio.LinkRatio;
import org.jreserve.triangle.claim.CummulatedClaimTriangle;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class ODPBootstrapFactory {

    private Random random;
    private int bootstrapCount;
    private LinkRatio lrs;
    private PearsonResidualClaimTriangle resTriangle;
    
    private ODPScaledResidualTriangle odpResiduals;
    private List<int[][]> segments = new ArrayList<int[][]>();
    private DefaultResidualGenerator resGenerator;
    private ODPPseudoTriangle pseudoTriangle;
    private ProcessSimulator simulator;
    
    public ODPBootstrapFactory() {
    }
    
    public ODPBootstrapFactory(LinkRatio lrs, int n, Random rnd) {
        initLinkRatios(lrs);
        initBootstrapCount(n);
        initRandom(rnd);
    }
    
    private void initLinkRatios(LinkRatio lrs) {
        this.lrs = lrs.copy();
        resTriangle = new PearsonResidualClaimTriangle(lrs);
    }
    
    private void initBootstrapCount(int n) {
        if(n < 1)
            throw new IllegalArgumentException("BootstrapCount is less then 1! "+bootstrapCount);
        this.bootstrapCount = n;
    }
    
    private void initRandom(Random rnd) {
        if(rnd == null)
            throw new NullPointerException("Random can not be null!");
        this.random = rnd;
    }
    
    public ODPBootstrapFactory setLinkRatios(LinkRatio lrs) {
        initLinkRatios(lrs);
        return this;
    }
    
    public ODPBootstrapFactory setBootstrapCount(int bootstrapCount) {
        initBootstrapCount(bootstrapCount);
        return this;
    }
    
    public ODPBootstrapFactory setRandomGenerator(Random rnd) {
        initRandom(rnd);
        return this;
    }
    
    public ODPBootstrapFactory setResidualSegments(List<int[][]> segments) {
        this.segments.clear();
        this.segments.addAll(segments==null? Collections.EMPTY_LIST : segments);
        return this;
    }
    
    public ODPBootstrapFactory useConstantScaledResiduals() {
        checkResidualsState();
        this.odpResiduals = new ConstantScaleODPResidualTriangle(resTriangle);
        initPseudoData();
        return this;
    }
    
    private void checkResidualsState() {
        if(lrs == null)
            throw new IllegalStateException("LinkRatio is not set!");
        if(simulator != null)
            throw new IllegalStateException("Process simulator is already set!");
    }
    
    private void initPseudoData() {
        resGenerator = new DefaultResidualGenerator(random);
        resGenerator.initialize(odpResiduals, segments);
        pseudoTriangle = new ODPPseudoTriangle(resGenerator, odpResiduals);
    }
    
    public ODPBootstrapFactory useVariableScaledResiduals() {
        checkResidualsState();
        this.odpResiduals = new VariableScaleODPResidualTriangle(odpResiduals);
        initPseudoData();
        return this;
    }
    
    public ODPBootstrapFactory useResidualProcessSimulator() {
        checkSimulatorState();
        simulator = new ResidualODPEstimateSimulator(resGenerator, odpResiduals);
        return this;
    }
    
    private void checkSimulatorState() {
        if(odpResiduals == null || resGenerator == null)
            throw new IllegalStateException("Residuals are not set!");
        if(simulator != null)
            throw new IllegalStateException("Process simulator already set!");
    }
    
    public ODPBootstrapFactory useGammaProcessSimulator() {
        checkSimulatorState();
        simulator = new GammaODPProcessSimulator(random, odpResiduals);
        return this;
    }
    
    public ODPBootstrapper build() {
        checkBuildState();
        Estimate estimate = createEstimate();
        return new ODPBootstrapper(estimate, bootstrapCount, simulator);
    }
    
    private void checkBuildState() {
        if(lrs == null)
            throw new IllegalStateException("LinkRatios are not set!");
        if(bootstrapCount < 1)
            throw new IllegalStateException("Bootstrap count not set!");
        if(simulator == null)
            throw new IllegalStateException("Process simulator not set!");
    }
    
    private Estimate createEstimate() {
        lrs.getSourceFactors().setSource(new CummulatedClaimTriangle(pseudoTriangle));
        ChainLadderEstimate estimate = new ChainLadderEstimate(lrs);
        estimate.detach();
        return estimate;
    }
}
