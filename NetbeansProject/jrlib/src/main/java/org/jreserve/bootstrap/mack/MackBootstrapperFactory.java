package org.jreserve.bootstrap.mack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.jreserve.bootstrap.DefaultResidualGenerator;
import org.jreserve.bootstrap.FixedProcessSimulator;
import org.jreserve.bootstrap.ProcessSimulator;
import org.jreserve.bootstrap.Random;
import org.jreserve.estimate.ChainLadderEstimate;
import org.jreserve.linkratio.scale.LinkRatioScale;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class MackBootstrapperFactory {
    
    private Random random;
    private int bootstrapCount;
    private LinkRatioScale scales;
    private MackResidualTriangle resTriangle;
    private List<int[][]> segments = new ArrayList<int[][]>();
    private DefaultResidualGenerator resGenerator;
    private MackPseudoFactorTriangle pseudoFactors;
    private ProcessSimulator simulator;
    
    public MackBootstrapperFactory() {
    }

    
    public MackBootstrapperFactory(LinkRatioScale scales, int n, Random rnd) {
        initLinkRatioScales(scales);
        initBootstrapCount(n);
        initRandom(rnd);
    }
    
    private void initLinkRatioScales(LinkRatioScale scales) {
        this.scales = scales.copy();
        resTriangle = new MackResidualTriangle(scales);
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
    
    public MackBootstrapperFactory setLinkRatios(LinkRatioScale scales) {
        initLinkRatioScales(scales);
        return this;
    }
    
    public MackBootstrapperFactory setBootstrapCount(int bootstrapCount) {
        initBootstrapCount(bootstrapCount);
        return this;
    }
    
    public MackBootstrapperFactory setRandomGenerator(Random rnd) {
        initRandom(rnd);
        return this;
    }
    
    public MackBootstrapperFactory setResidualSegments(List<int[][]> segments) {
        this.segments.clear();
        this.segments.addAll(segments==null? Collections.EMPTY_LIST : segments);
        return this;
    }
    
    private void initPseudoData() {
        if(random == null || resTriangle==null) {
            pseudoFactors = null;
        } else {
            resGenerator = new DefaultResidualGenerator(random);
            resGenerator.initialize(resTriangle, segments);
            pseudoFactors = new MackPseudoFactorTriangle(scales, resGenerator);
        }
    }
    
    public MackBootstrapperFactory useNoProcessVariance() {
        checkSimulatorState();
        simulator  = new FixedProcessSimulator();
        return this;
    }
    
    public MackBootstrapperFactory useGaussianProcessSimulator() {
        checkSimulatorState();
        simulator = new NormalMackProcessSimulator(random, scales);
        return this;
    }
    
    public MackBootstrapperFactory useGammaProcessSimulator() {
        checkSimulatorState();
        simulator = new NormalMackProcessSimulator(random, scales);
        return this;
    }
    
    private void checkSimulatorState() {
        if(random == null)
            throw new IllegalStateException("Random generator is not set!");
        if(scales == null)
            throw new IllegalStateException("Sclaes are not set!");
        if(simulator != null)
            throw new IllegalStateException("Process simulator already set!");
    }
    
    public MackBootstrapper build() {
        initPseudoData();
        checkBuildState();
        ChainLadderEstimate estimate = createEstimate();
        return new MackBootstrapper(estimate, bootstrapCount, simulator);
    }
    
    private void checkBuildState() {
        if(scales == null)
            throw new IllegalStateException("LinkRatioScales are not set!");
        if(bootstrapCount < 1)
            throw new IllegalStateException("Bootstrap count not set!");
        if(simulator == null)
            throw new IllegalStateException("Process simulator not set!");
    }
    
    private ChainLadderEstimate createEstimate() {
        scales.getSourceLinkRatios().setSource(pseudoFactors);
        ChainLadderEstimate estimate = new ChainLadderEstimate(scales.getSourceLinkRatios());
        estimate.detach();
        return estimate;
    }
}
