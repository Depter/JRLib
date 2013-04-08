package org.jrlib.linkratio.curve;

import org.jrlib.linkratio.LinkRatio;
import org.jrlib.util.SelectableMethod;

/**
 * Link-ratio curves are able to smooth link-ratios.
 * 
 * @author Peter Decsi
 * @version 1.0
 */
public interface LinkRatioCurve extends SelectableMethod<LinkRatio> {

    @Override
    public LinkRatioCurve copy();
}
