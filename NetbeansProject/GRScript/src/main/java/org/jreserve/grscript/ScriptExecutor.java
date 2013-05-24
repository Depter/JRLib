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
package org.jreserve.grscript;

import groovy.lang.Binding;
import groovy.lang.ExpandoMetaClass;
import groovy.lang.GroovyShell;
import groovy.lang.Script;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
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
            binding.setProperty("out", new PrintWriter(out));
            //config.setOutput(out);
    }
    
    private void initScript(Script script) {
        Class clazz = script.getClass();
        
        emc = new ExpandoMetaClass(clazz, false);
        for(FunctionProvider provider : providers)
            provider.initFunctions(script, emc);
        emc.initialize();
        
        script.setMetaClass(emc);
    }
}
