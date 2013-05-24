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
package org.jreserve.grscript.gui.script.executor;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import org.jreserve.grscript.ScriptExecutor;
import org.jreserve.grscript.gui.classpath.ClassPathUtil;
import org.jreserve.grscript.gui.classpath.registry.ClassPathItem;
import org.jreserve.grscript.gui.script.functions.FunctionProviderAdapter;
import org.jreserve.grscript.gui.script.GRScriptDataObject;
import org.jreserve.grscript.gui.script.functions.FunctionFolder;
import org.netbeans.api.actions.Savable;
import org.openide.filesystems.FileUtil;
import org.openide.util.RequestProcessor;
import org.openide.windows.IOProvider;
import org.openide.windows.InputOutput;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public class GRScriptExecutor {
    
    private final static Object LOCK = new Object();
    private static List<FunctionProviderAdapter> adapters;
    private final static RequestProcessor RP = new RequestProcessor(GRScriptExecutor.class);
    
    public static void executeScript(GRScriptDataObject obj) throws IOException {
        if(obj.isModified()) {
            Savable s = obj.getLookup().lookup(Savable.class);
            if(s != null)
                s.save();
        }
        new ScriptTask(FileUtil.toFile(obj.getPrimaryFile())).execute();
    }
    
    private static List<FunctionProviderAdapter> getAdapters() {
        synchronized(LOCK) {
            if(adapters == null)
                initAdapters();
            return adapters;
        }
    }
    
    private static void initAdapters() {
        synchronized(FunctionFolder.class) {
            adapters = new ArrayList<FunctionProviderAdapter>();
            FunctionFolder root = FunctionFolder.getRoot();
            addAdapters(root);
        }
        
    }
    
    private static void addAdapters(FunctionFolder folder) {
        for(FunctionFolder child : folder.getFolders())
            addAdapters(child);
        adapters.addAll(folder.getAdapters());
    }
    
    private static class ScriptTask implements Runnable {
    
        private final File file;
        
        private ScriptTask(File file) {
            this.file = file;
        }

        void execute() {
            RP.create(this).schedule(0);
        }
        
        @Override
        public void run() {
            InputOutput io = null;
            try {
                long begin = System.currentTimeMillis();
                io = createIO();
                ScriptExecutor executor = createExecutor();
                executor.setOutput(io.getOut());
                executor.runScript(file);
                printFinnished(io, begin);
            } catch (Exception ex) {
                printStackTrace(ex, io!=null? io.getErr() : new PrintWriter(System.err));
            }
        }
        
        private InputOutput createIO() throws IOException {
            InputOutput io = IOProvider.getDefault().getIO(file.getAbsolutePath(), false);
            io.getOut().reset();
            io.select();
            return io;
        }
        
        private ScriptExecutor createExecutor() {
            ScriptExecutor executor = new ScriptExecutor();
            addClasspathElements(executor);
            addFunctionProviders(executor);
            return executor;
        }
        
        private void addClasspathElements(ScriptExecutor executor) {
            for(ClassPathItem item : ClassPathUtil.getRegistryItems())
                executor.addToClassPath(item.getPath());
        }
        
        private void addFunctionProviders(ScriptExecutor executor) {
            for(FunctionProviderAdapter adapter : getAdapters())
                executor.addFunctionProvider(adapter.getFunctionProvider());
        }
        
        private void printFinnished(InputOutput io, long begin) {
            if(io != null) {
                double duration = System.currentTimeMillis() - begin;
                duration /= 1000d;
                io.getOut().printf("Finnished in %.3f seconds.", duration);
            }
        }
        
        private void printStackTrace(Exception ex, PrintWriter writer) {
            writer.println(ex.getLocalizedMessage());
            for(StackTraceElement element : ex.getStackTrace())
                printStackTrace(element, writer);
        }

        private void printStackTrace(StackTraceElement element, PrintWriter writer) {
            writer.printf("\t%s.%s(%s:%d)%n",
                    element.getClassName(),
                    element.getMethodName(),
                    element.getFileName(),
                    element.getLineNumber()
            );
        }
    }
}
