package org.jreserve.grscript;

import groovy.lang.Binding;
import groovy.lang.ExpandoMetaClass;
import groovy.lang.GroovyShell;
import groovy.lang.Script;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.codehaus.groovy.control.CompilationFailedException;
import org.codehaus.groovy.control.CompilerConfiguration;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class ScriptExecutor {
    
    private List<FunctionProvider> providers = new ArrayList<FunctionProvider>();
    private List<String> classPath = new ArrayList<String>();
    private PrintWriter out;
    private ClassLoader classLoader;
    private Binding binding = new Binding();
    
    private ExpandoMetaClass emc;
    private Class emcClazz;
    
    public ScriptExecutor() {
    }
    
    public void addFunctionProvider(FunctionProvider provider) {
        if(provider == null)
            throw new NullPointerException("Provider is null!");
        providers.add(provider);
    }
    
    public void removeFunctionProvider(FunctionProvider provider) {
        providers.remove(provider);
    }
    
    public void addToClassPath(String path) {
        if(path == null)
            throw new NullPointerException("Path is null!");
        classPath.add(path);
    }
    
    public void removeFromClassPath(String path) {
        classPath.remove(path);
    }
    
    public void setOutput(PrintWriter out) {
        this.out = out;
    }
    
    public void setParentClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }
    
    public void setVariable(String name, Object value) {
        binding.setVariable(name, value);
    }
    
    public void removeVariable(String name) {
        binding.getVariables().remove(name);
    }
    
    public Object getVariable(String name) {
        return binding.getVariable(name);
    }
    
    public void clearVariables() {
        binding.getVariables().clear();
    }
    
    /**
     * Runs the contents of the file as a script, returns the result of the script.
     * 
     * @throws CompilationFailedException
     * @throws IOException 
     * @throws NullPointerException
     */
    public Object runScript(File script) throws CompilationFailedException, IOException {
        Script scr = createShell().parse(script);
        initScript(scr);
        return scr.run();
    }
    
    /**
     * Runs the script, returns the result of the script.
     * 
     * @throws CompilationFailedException
     * @throws IOException 
     * @throws NullPointerException
     */
    public Object runScript(String script) throws CompilationFailedException {
        Script scr = createShell().parse(script);
        initScript(scr);
        return scr.run();
    }
    
    private GroovyShell createShell() {
        CompilerConfiguration config = new CompilerConfiguration();
        setClassPath(config);
        setOutput(config);
        
        return classLoader==null?
            new GroovyShell(binding, config) :
            new GroovyShell(classLoader, binding, config);
    }
    
    private void setClassPath(CompilerConfiguration config) {
        List<String> original = new ArrayList<String>(config.getClasspath());
        for(String path : classPath)
            if(!original.contains(path))
                original.add(path);
        config.setClasspathList(original);
    }
    
    private void setOutput(CompilerConfiguration config) {
        if(out != null)
            config.setOutput(out);
    }
    
    private void initScript(Script script) {
        Class clazz = script.getClass();
        if(emc == null || !emcClazz.equals(clazz)) {
            emcClazz = clazz;
            createEMC();
        }
        script.setMetaClass(emc);
    }
    
    private void createEMC() {
        emc = new ExpandoMetaClass(emcClazz, false);
        for(FunctionProvider provider : providers)
            provider.initFunctions(emc);
        emc.initialize();
    }
}
