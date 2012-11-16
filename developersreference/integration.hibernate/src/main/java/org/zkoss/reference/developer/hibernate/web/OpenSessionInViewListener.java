package org.zkoss.reference.developer.hibernate.web;
import java.util.List;

import org.hibernate.StaleObjectStateException;
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

	public void cleanup(Execution exec, Execution parent, List errs) {
		if (parent == null) { //the root execution of a servlet request
			try {
				if (errs == null || errs.isEmpty()) {
					log.debug("Committing the database transaction: "+exec);
					HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().commit();
				} else {
					final Throwable ex = (Throwable) errs.get(0);
					if (ex instanceof StaleObjectStateException) {
						handleStaleObjectStateException(exec, (StaleObjectStateException)ex);
					} else {
						handleOtherException(exec, ex);
					}
				}
			}finally{
				//commit() and rollback() both close the current session
			}
		}
	}

	/**
	 * <p>Default StaleObjectStateException handler. This implementation
	 * does not implement optimistic concurrency control! It simply rollback 
	 * the transaction.</p>
	 * 
	 * <p>Application developer might want to extends this class and override 
	 * this method to do other things like compensate for any permanent changes 
	 * during the conversation, and finally restart business conversation. 
	 * Or maybe give the user of the application a chance to merge some of his 
	 * work with fresh data... what can be done here depends on the applications 
	 * design.</p>
	 *
	 * @param exec the exection to clean up.
	 * @param ex the StaleObjectStateException being thrown (and not handled) during the execution
	 */			
	protected void handleStaleObjectStateException(Execution exec, StaleObjectStateException ex) {
		log.error("This listener does not implement optimistic concurrency control!");
		rollback(exec, ex);
	}

	/**
	 * <p>Default other exception (other than StaleObjectStateException) handler. 
	 * This implementation simply rollback the transaction.</p>
	 * 
	 * <p>Application developer might want to extends this class and override 
	 * this method to do other things like compensate for any permanent changes 
	 * during the conversation, and finally restart business conversation... 
	 * what can be done here depends on the applications design.</p>
	 *
	 * @param exec the execution to clean up.
	 * @param ex the Throwable other than StaleObjectStateException being thrown (and not handled) during the execution
	 */			
	protected void handleOtherException(Execution exec, Throwable ex) {
		// Rollback only
		ex.printStackTrace();
		rollback(exec, ex);
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

