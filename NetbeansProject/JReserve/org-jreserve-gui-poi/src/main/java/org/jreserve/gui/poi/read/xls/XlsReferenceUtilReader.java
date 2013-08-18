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
package org.jreserve.gui.poi.read.xls;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import org.apache.poi.hssf.record.BoundSheetRecord;
import org.apache.poi.hssf.record.NameRecord;
import org.apache.poi.hssf.record.Record;
import org.apache.poi.ss.formula.EvaluationWorkbook;
import org.apache.poi.ss.formula.FormulaRenderingWorkbook;
import org.apache.poi.ss.formula.WorkbookDependentFormula;
import org.apache.poi.ss.formula.ptg.AttrPtg;
import org.apache.poi.ss.formula.ptg.MemAreaPtg;
import org.apache.poi.ss.formula.ptg.MemErrPtg;
import org.apache.poi.ss.formula.ptg.MemFuncPtg;
import org.apache.poi.ss.formula.ptg.NamePtg;
import org.apache.poi.ss.formula.ptg.NameXPtg;
import org.apache.poi.ss.formula.ptg.OperationPtg;
import org.apache.poi.ss.formula.ptg.ParenthesisPtg;
import org.apache.poi.ss.formula.ptg.Ptg;
import org.jreserve.gui.poi.read.ReferenceUtil;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class XlsReferenceUtilReader extends XlsReader<ReferenceUtil>{

    private final static short[] RIDS = {
        NameRecord.sid,
        BoundSheetRecord.sid
    };
    private final static String WB_NAME = "wb";
    private final static String RELACE_WB_NAME = "\\[wb\\]";
    
    private final List<String> sheets = new ArrayList<String>();
    private final Map<String, String> names = new HashMap<String, String>();
    private final FormulaRenderer renderer = new FormulaRenderer();
    
    @Override
    protected short[] getInterestingReqordIds() {
        return RIDS;
    }

    @Override
    protected ReferenceUtil getResult() {
        return new ReferenceUtil(sheets, names, true);
    }

    @Override
    protected void recordFound(Record record) {
        switch(record.getSid()) {
            case BoundSheetRecord.sid:
                sheets.add(((BoundSheetRecord)record).getSheetname());
                break;
            case NameRecord.sid:
                NameRecord nr = (NameRecord) record;
                if(isUserDefinedName(nr)) {
                    String name = nr.getNameText();
                    String formula = toFormulaString(nr.getNameDefinition());
                    if(formula != null) {
                        formula = formula.replaceAll(RELACE_WB_NAME, "");
                        names.put(name, formula);
                    }
                }
            default:
                break;
        }
    }
    
    private boolean isUserDefinedName(NameRecord record) {
        return !record.isMacro() && 
               !record.isCommandName() && 
               !record.isBuiltInName() && 
               !record.isFunctionName() && 
               !record.isComplexFunction();
    }
    
    private String toFormulaString(Ptg[] ptgs) {
         if (ptgs == null || ptgs.length == 0)
             return null;
         
         Stack<String> stack = new Stack<String>();
 
         for (int i=0 ; i < ptgs.length; i++) {
            Ptg ptg = ptgs[i];
            if(ptg instanceof MemAreaPtg || ptg instanceof MemFuncPtg || ptg instanceof MemErrPtg)
                continue;
            
            if(ptg instanceof ParenthesisPtg) {
                String contents = stack.pop();
                stack.push ("(" + contents + ")");
                continue;
            }
            
            if(ptg instanceof AttrPtg) {
                AttrPtg attrPtg = ((AttrPtg) ptg);
                if(attrPtg.isOptimizedIf() || 
                   attrPtg.isOptimizedChoose() || 
                   attrPtg.isSkip()) {
                    continue;
                }
                
                if(attrPtg.isSpace() || attrPtg.isSemiVolatile())
                    continue;
                
                if(attrPtg.isSum()) {
                    String[] operands = getOperands(stack, attrPtg.getNumberOfOperands());
                    stack.push(attrPtg.toFormulaString(operands));
                    continue;
                }
                
                throw new RuntimeException("Unexpected tAttr: " + attrPtg.toString());
            }

            if(ptg instanceof WorkbookDependentFormula) {
                WorkbookDependentFormula optg = (WorkbookDependentFormula) ptg;
                stack.push(optg.toFormulaString(renderer));
                continue;
            }
            
            
            if(!(ptg instanceof OperationPtg)) {
                stack.push(ptg.toFormulaString());
                continue;
            }

            OperationPtg o = (OperationPtg) ptg;
            String[] operands = getOperands(stack, o.getNumberOfOperands());
            stack.push(o.toFormulaString(operands));
        }

        if(stack.isEmpty())
            throw new IllegalStateException("Stack underflow");
        
        String result = stack.pop();
        if(!stack.isEmpty())
            throw new IllegalStateException("too much stuff left on the stack");
        
        return result;
    }    
    
    private static String[] getOperands(Stack<String> stack, int nOperands) {
        String[] operands = new String[nOperands];
        for(int j = nOperands-1; j >= 0; j--) {
            if(stack.isEmpty()) {
                String msg = "Too few arguments supplied to operation. Expected (%d) operands but got (%d)!";
                msg = String.format(msg, nOperands, nOperands - j - 1);
                throw new IllegalStateException(msg);
            }
            
            operands[j] = stack.pop();
        }
        return operands;
    }
    
    private class FormulaRenderer implements FormulaRenderingWorkbook {

        @Override
        public EvaluationWorkbook.ExternalSheet getExternalSheet(int i) {
            return new EvaluationWorkbook.ExternalSheet(
                    WB_NAME, getSheetNameByExternSheet(i)
                    );
        }

        @Override
        public String getSheetNameByExternSheet(int i) {
            if(i>=0 && i<sheets.size())
                return sheets.get(i);
            return null;
        }

        @Override
        public String resolveNameXText(NameXPtg nxp) {
            return nxp.toFormulaString();
        }

        @Override
        public String getNameText(NamePtg np) {
            return np.toFormulaString();
        }
    
    }
    
}
