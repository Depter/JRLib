/*
 *  Copyright (C) 2013, Peter Decsi.
 * 
 *  This library is free software: you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public 
 *  License as published by the Free Software Foundation, either 
 *  version 3 of the License, or (at your option) any later version.
 * 
 *  This library is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 * 
 *  You should have received a copy of the GNU General Public License
 *  along with this library.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.jreserve.gui.calculations.claimtriangle;

import org.jreserve.gui.calculations.api.CalculationModifier;
import org.jreserve.gui.data.api.DataSource;
import org.jreserve.jrlib.gui.data.MonthDate;
import org.jreserve.jrlib.gui.data.TriangleGeometry;
import org.jreserve.jrlib.triangle.claim.ClaimTriangle;
import org.jreserve.jrlib.triangle.claim.InputClaimTriangle;
import org.netbeans.api.project.Project;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class EmptyClaimTriangleCalculation implements ClaimTriangleCalculation {

    @Override
    public DataSource getDataSource() {
        return DataSource.EMPTY_TRIANGLE;
    }

    @Override
    public TriangleGeometry getGeometry() {
        MonthDate date = new MonthDate();
        return new TriangleGeometry(date, date, 0, 0);
    }

    @Override
    public int getModificationCount() {
        return 0;
    }

    @Override
    public CalculationModifier<ClaimTriangle> getModificationAt(int index) {
        return null;
    }

    @Override
    public void setModification(int index, CalculationModifier<ClaimTriangle> cm) {
    }

    @Override
    public void addModification(CalculationModifier<ClaimTriangle> cm) {
    }

    @Override
    public void addModification(int index, CalculationModifier<ClaimTriangle> cm) {
    }

    @Override
    public void deleteModification(int index) {
    }

    @Override
    public int indexOfModification(CalculationModifier cm) {
        return -1;
    }

    @Override
    public ClaimTriangle getCalculation(int layer) {
        return new InputClaimTriangle(new double[0][]);
    }

    @Override
    public Project getProject() {
        return null;
    }

    @Override
    public Class<ClaimTriangle> getCalculationClass() {
        return ClaimTriangle.class;
    }

    @Override
    public ClaimTriangle getCalculation() throws Exception {
        return new InputClaimTriangle(new double[0][]);
    }

    @Override
    public String getPath() {
        return null;
    }
    
}
