package org.zkoss.reference.developer.performance.controlcache;

import org.zkoss.zk.ui.http.SimpleWebApp;
import org.zkoss.zk.ui.util.Configuration;

public class ForceResourcesUpdateOnBuildChangeWebapp extends SimpleWebApp {

    @Override
    public String getBuild() {
        return super.getBuild()+getBuildSuffix();
    }
	
    private String getBuildSuffix(){
    	/* could return a value from configuration, from database, etc. */
    	return "1.1.0";
    }
}
