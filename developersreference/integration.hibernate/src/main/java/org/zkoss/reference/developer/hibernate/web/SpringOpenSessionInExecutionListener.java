package org.zkoss.reference.developer.hibernate.web;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import org.hibernate.FlushMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.StaleObjectStateException;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.orm.hibernate3.SessionHolder;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
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
 *		&lt;listener-class>org.zkoss.reference.developer.hibernate.web.SpringOpenSessionInExecutionListener&lt;/listener-class>
 *	&lt;/listener>
 * </code></pre>
 * </p>
 * <p>Applicable to Hibernate version 3.2.ga or later</p>
 */
public class SpringOpenSessionInExecutionListener implements ExecutionInit, ExecutionCleanup {
	private static final Log log = Log.lookup(SpringOpenSessionInExecutionListener.class);
	public static final String DEFAULT_SESSION_FACTORY_BEAN_NAME = "sessionFactory";
	
	private String sessionFactoryBeanName = DEFAULT_SESSION_FACTORY_BEAN_NAME;
	boolean participate = false; //TODO threadlocal
	SessionFactory sessionFactory = null;
	
	public void init(Execution exec, Execution parent) {
		if (parent == null) { //the root execution of a servlet request
			SessionFactory sessionFactory = lookupSessionFactory(exec);
			// single session mode
			if (TransactionSynchronizationManager.hasResource(sessionFactory)) {
				// Do not modify the Session: just set the participate flag.
				participate = true;
			}
			else {
				Session session = getSession(sessionFactory);
				session.beginTransaction();
				TransactionSynchronizationManager.bindResource(sessionFactory, new SessionHolder(session));
			}
		}
	}

	public void cleanup(Execution exec, Execution parent, List errs) {
		if (parent == null) { //the root execution of a servlet request
			try {
				if (!participate) {
					SessionFactory sessionFactory = lookupSessionFactory(exec);
					SessionHolder sessionHolder =
							(SessionHolder) TransactionSynchronizationManager.unbindResource(sessionFactory);
					Session session = sessionHolder.getSession();
					if (errs == null || errs.isEmpty()) {
						log.debug("Committing the database transaction: "+exec);
						// single session mode
						//						closeSession(sessionHolder.getSession(), sessionFactory);
						session.getTransaction().commit();
					} else {
						//rollback
						final Throwable ex = (Throwable) errs.get(0);
						rollback(sessionHolder, ex);
					}
				}
			}finally{
				//commit() and rollback() both close the current session
			}
		}
	}

	

	/**
	 * rollback the current session.
	 *
	 * @param exec the execution to clean up.
	 * @param ex the StaleObjectStateException being thrown (and not handled) during the execution
	 */	
	private void rollback(SessionHolder sessionHolder, Throwable ex) {
		Session session = sessionHolder.getSession();
		if (session.getTransaction().isActive()){
			session.getTransaction().rollback();
		}
	}
	
	protected String getSessionFactoryBeanName() {
		return this.sessionFactoryBeanName;
	}
	
	private ServletContext getServletContext(Execution exec){
		return ((HttpSession)exec.getSession().getNativeSession()).getServletContext();
	}
	/**
	 * Look up the SessionFactory that this filter should use.
	 * <p>The default implementation looks for a bean with the specified name
	 * in Spring's root application context.
	 * @return the SessionFactory to use
	 * @see #getSessionFactoryBeanName
	 */
	protected SessionFactory lookupSessionFactory(Execution exec) {
		WebApplicationContext wac = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext(exec));
		return wac.getBean(getSessionFactoryBeanName(), SessionFactory.class);
	}
	
	protected Session getSession(SessionFactory sessionFactory) throws DataAccessResourceFailureException {
		Session session = SessionFactoryUtils.getSession(sessionFactory, true);
		FlushMode flushMode = getFlushMode();
		if (flushMode != null) {
			session.setFlushMode(flushMode);
		}
		return session;
	}	
	
	protected FlushMode getFlushMode() {
		return FlushMode.MANUAL;
	}
	
	protected void closeSession(Session session, SessionFactory sessionFactory) {
		SessionFactoryUtils.closeSession(session);
	}
}

