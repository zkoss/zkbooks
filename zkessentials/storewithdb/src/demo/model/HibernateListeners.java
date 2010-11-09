/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package demo.model;

import org.zkoss.zk.ui.WebApp;
import org.zkoss.zk.ui.util.WebAppCleanup;
import org.zkoss.zk.ui.util.WebAppInit;

/**
 * 
 * @author zkessentials storewithdb
 * 
 *         This class is a listener to initalize Hibernate when the web app is
 *         run and to clean it up when it closes
 * 
 */
public class HibernateListeners implements WebAppInit, WebAppCleanup {

	public void init(WebApp webapp) throws Exception {
		// initialize Hibernate
		StoreHibernateUtil.getSessionFactory();
	}

	public void cleanup(WebApp webapp) throws Exception {
		// Close Hibernate
		StoreHibernateUtil.getSessionFactory().close();
	}

}
