/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package demo.model;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * @author zkessentials storewithdb
 * 
 *         Hibernate Utility class with a convenient method to get Session
 *         Factory object.
 * 
 */
public class StoreHibernateUtil {
	private static final SessionFactory sessionFactory;

	static {
		try {

			// Create the SessionFactory from standard (hibernate.cfg.xml)
			// config file.
			sessionFactory = new Configuration().configure()
					.buildSessionFactory();
		} catch (Throwable ex) {
			// Log the exception.
			System.err.println("Initial SessionFactory creation failed." + ex);
			throw new ExceptionInInitializerError(ex);
		}
	}

	public static SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public static Session openSession() {
		return sessionFactory.openSession();
	}
}
