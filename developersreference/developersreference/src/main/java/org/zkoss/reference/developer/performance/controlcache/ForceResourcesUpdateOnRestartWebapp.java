package org.zkoss.reference.developer.performance.controlcache;

import org.zkoss.zk.ui.http.SimpleWebApp;
import org.zkoss.zk.ui.util.Configuration;

public class ForceResourcesUpdateOnRestartWebapp extends SimpleWebApp {

    private String randomizer;
    
    @Override
    public void init(Object context, Configuration config) {
        super.init(context, config);
        randomizer = Long.toHexString(System.currentTimeMillis());
    }
     
    @Override
    public String getBuild() {
        return super.getBuild()+randomizer;
    }
	
}
