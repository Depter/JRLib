package org.jreserve.grscript.gui.classpath.explorer.nodes;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.jreserve.grscript.gui.classpath.ClassPathEvent;
import org.jreserve.grscript.gui.classpath.ClassPathUtil;
import org.openide.nodes.ChildFactory;
import org.jreserve.grscript.gui.classpath.registry.ClassPathItem;
import org.jreserve.grscript.gui.eventbus.EventBusListener;
import org.jreserve.grscript.gui.eventbus.EventBusRegistry;
import org.openide.nodes.Node;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
class CpChildFactory extends ChildFactory<ClassPathItem>{

    private CpRootType type;
    private RegistryListener listener;
    
    public CpChildFactory(CpRootType type) {
        this.type = type;
        if(CpRootType.CUSTOM == type)
            initListener();
    }
    
    private void initListener() {
        listener = new RegistryListener();
        EventBusRegistry.getDefault().subscribe(ClassPathEvent.class, listener);
    }
    
    @Override
    protected boolean createKeys(List<ClassPathItem> list) {
        list.addAll(getItems());
        Collections.sort(list);
        return true;
    }
    
    private List<ClassPathItem> getItems() {
        switch(type) {
            case PLATFORM:
                return ClassPathUtil.getPlatformClassPath();
            case MODULES:
                return ClassPathUtil.getModulesClassPath();
            case CUSTOM:
                return ClassPathUtil.getRegistryItems();
            default:
                throw new IllegalStateException("Unknon type: "+type);
        }
    }

    @Override
    protected Node createNodeForKey(ClassPathItem key) {
        return new CpItemNode(key);
    }
    
    private class RegistryListener implements EventBusListener<ClassPathEvent> {

        @Override
        public void published(Collection<ClassPathEvent> instances) {
            CpChildFactory.this.refresh(true);
        }
    }
}
