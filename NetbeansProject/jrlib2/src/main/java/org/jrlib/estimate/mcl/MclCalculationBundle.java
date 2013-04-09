package org.jrlib.estimate.mcl;

import org.jrlib.AbstractMultiSourceCalculationData;
import org.jrlib.CalculationData;
import org.jrlib.triangle.claim.ClaimTriangle;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class MclCalculationBundle extends AbstractMultiSourceCalculationData<ClaimTriangle> {
    
    public MclCalculationBundle(MclCorrelation paidCorrelation, MclCorrelation incurredCorrelation) {
    }
    
    @Override
    protected void recalculateLayer() {
    }
    
}
//                                 +----------------+
//                                 |   mclBundle    |
//                                 +----------------+
//                               __________|____________
//                              |                        |
//                      +---------------+      +-----------------+
//                      |  pCorrSource  |      |  iCorrSource    |
//                      +---------------+      +-----------------+
//                              |                        |
//                      +---------------+      +-----------------+
//                      |  pCorrSource  |      |  iCorrSource    |
//                      +---------------+      +-----------------+
//           _____________|     |                       |    |________________
//          |                   |                       |                     |
// +----------------+   +---------------+      +-----------------+   +----------------+
// |  pCrResidals   |   |  pLrResidals  |      |  iCrResidals    |   |  iLrResidals   |
// +----------------+   +---------------+      +-----------------+   +----------------+
//          |                   |                       |                     |
// +----------------+   +---------------+      +-----------------+   +----------------+
// |    pCrScale    |   |    pLrScale   |      |     iCrScale    |   |    iLrScale    |
// +----------------+   +---------------+      +-----------------+   +----------------+
//          |                   |                       |                     |
// +----------------+   +---------------+      +-----------------+   +----------------+
// | pCr: ClaimRatio|   | pLr: LossRatio|      | iCr: ClaimRatio |   | iLr: LossRatio |
// +----------------+   +---------------+      +-----------------+   +----------------+
//        |   |                  |                   |        |                |
//        |   |          +---------------+           |        |       +----------------+
//        |   |          | pLr: LossRatio|           |        |       | iLr: LossRatio |
//        |   |          +---------------+           |        |       +----------------+
//        |   |________________|_________________    |        |            /
//        \_________           |         ________\___|        |           /
//                  \          |        /         \           |          /
//                +-----------------------+      +-----------------------+
//                |  paid:ClaimTriangle   |      | inurred:ClaimTriangle |
//                +-----------------------+      +-----------------------+