package org.jreserve.grscript

import org.jreserve.grscript.input.CsvReaderDelegate
import org.jreserve.grscript.jrlib.*

/**
 *
 * @author Peter Decsi
 */
class ScriptExecutor {
	
    private final static FunctionProvider[] FUNCTION_PROVIDERS = [
        new PrintDelegate(),
        new CsvReaderDelegate(),
        new TriangleUtilDelegate(),
        new ClaimTriangleDelegate(),
        new FactorTriangleDelegate(),
        new LinkRatioDelegate()
    ]

    private final static String SCRIPT_HOME = "C:\\Munka\\Java\\NetbeansWS\\GRScript\\src\\scripts\\"
    
    static void main(String[] args) {
        runScript(new File(SCRIPT_HOME+"testScript.groovy"))
    }
    
    static void runScript(File script) {
        runScript(script.text)
    }
    
    static void runScript(String script) {
        Script s = new GroovyShell().parse(script);
        s.metaClass = createEMC(s.class)
        s.run()
    }
    
    private static ExpandoMetaClass createEMC(Class clazz) {
        ExpandoMetaClass emc = new ExpandoMetaClass(clazz, false)
        FUNCTION_PROVIDERS.each(){it.initFunctions(emc)}
        emc.initialize()
        return emc
    }
}

