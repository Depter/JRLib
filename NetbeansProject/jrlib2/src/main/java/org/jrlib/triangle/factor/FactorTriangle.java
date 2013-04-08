package org.jrlib.triangle.factor;

import org.jrlib.Copyable;
import org.jrlib.MutableSource;
import org.jrlib.triangle.Triangle;
import org.jrlib.triangle.claim.ClaimTriangle;

/**
 * A factor triangle calculates the individual development factors from a
 * given {@link ClaimTriangle ClaimTriangle}. 
 * 
 * The formula used is `f(a,d) = c(a,d+1) / c(a,d)`, where `c(a,d)` is the
 * value from the input claim triangle for accident period <i>a</i> and
 * development period <i>d</i>. From this follows, that a FactorTriangle
 * has one less development period for evry accident period. If the last
 * accident period contained only one cell, then this instance contains one
 * less accident periods.
 * 
 * @author Peter Decsi
 * @version 1.0
 */
public interface FactorTriangle extends Triangle, MutableSource<ClaimTriangle>, Copyable<FactorTriangle> {

    /**
     * Returns the {@link Triangle Triangle} containing the
     * input claims for the calculations.
     */
    public ClaimTriangle getSourceTriangle();
}
