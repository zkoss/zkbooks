package org.zkoss.reference.developer.hibernate.web;
import java.util.List;

import org.zkoss.reference.developer.hibernate.dao.HibernateUtil;
import org.zkoss.util.logging.Log;
import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.util.ExecutionCleanup;
import org.zkoss.zk.ui.util.ExecutionInit;

/**
 * Listener to initialize and cleanup the Hibernate session automatically, implement
 * the Hibernate's "Open Session In View" pattern without JTA support. 
 *
 * <p>In WEB-INF/zk.xml, add following lines:
 * <pre><code>
 * 	&lt;listener>
 *		&lt;description>Hibernate "OpenSessionInView" Listener&lt;/description>
 *		&lt;listener-class>org.zkoss.reference.developer.hibernate.web.OpenSessionInViewListener&lt;/listener-class>
 *	&lt;/listener>
 * </code></pre>
 * </p>
 * <p>Applicable to Hibernate version 3.2.ga or later</p>
 */
public class OpenSessionInViewListener implements ExecutionInit, ExecutionCleanup {
	private static final Log log = Log.lookup(OpenSessionInViewListener.class);

	public void init(Execution exec, Execution parent) {
		if (parent == null) { //the root execution of a servlet request
			log.debug("Starting a database transaction: "+exec);
			HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();
		}
	}

	/*
	 * http://docs.jboss.org/hibernate/core/3.6/reference/en-US/html_single/#tutorial-firstapp-workingpersistence
	 * A org.hibernate.Session begins when the first call to getCurrentSession() is made for 
	 * the current thread. It is then bound by Hibernate to the current thread. When 
	 * the transaction ends, either through commit or rollback, Hibernate automatically unbinds
	 *  the org.hibernate.Session from the thread and closes it for you.
	 */
	public void cleanup(Execution exec, Execution parent, List errs) {
		if (parent == null) { //the root execution of a servlet request
			if (errs == null || errs.isEmpty()) {
				log.debug("Committing the database transaction: "+exec);
				HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().commit();
			} else {
				final Throwable ex = (Throwable) errs.get(0);
				rollback(exec, ex);
			}
		}
	}

	/**
	 * rollback the current session.
	 *
	 * @param exec the execution to clean up.
	 * @param ex the StaleObjectStateException being thrown (and not handled) during the execution
	 */	
	private void rollback(Execution exec, Throwable ex) {
		try {
			if (HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().isActive()) {
				log.debug("Trying to rollback database transaction after exception:"+ex);
				HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().rollback();
			}
		} catch (Throwable rbEx) {
			log.error("Could not rollback transaction after exception! Original Exception:\n"+ex, rbEx);
		}
	}
}

