package org.zkoss.reference.developer.hibernate.dao;

import java.sql.SQLException;
import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.zkoss.reference.developer.hibernate.domain.Order;

/**
 * Follow Hibernate reference manual's suggestion (http://docs.jboss.org/hibernate/core/3.6/reference/en-US/html_single/#tutorial-webapp-servlet)
 * , implement session-per-request pattern.   
 * HibernateSessionRequestFilter open and begin transaction. 
 * sessionFactory.getCurrentSession() helps us to get the session under current transaction scoped context. 
 */
public class OrderDao {

	public List<Order> findAll() {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Query query = session.createQuery("select o from Order as o");
		List<Order> result = query.list();
		return result;
	}

	/**
	 * rollback is handled in filter.
	 * @param newOrder
	 * @return
	 * @throws HibernateException
	 */
	public Order save(Order newOrder) throws HibernateException{
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.save(newOrder);
		session.flush();
		return newOrder;
	}
	
	public void errorSave(Order newOrder) throws HibernateException{
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.save(newOrder);
		session.flush();
		// throw exception to test
		throw new HibernateException("error save");
	}
	/**
	 * Initialize lazy-loaded collection.
	 * session.refresh() re-read object's state from database that turn initialized collection 
	 * back to uninitialized.
	 * @param order
	 * @return
	 */
	public Order reload(Order order){
		return (Order)HibernateUtil.getSessionFactory().getCurrentSession().load(Order.class,order.getId());
	}
	
	public void saveInNewSession(Order newOrder) throws HibernateException{
		Session session = HibernateUtil.getSessionFactory().openSession();
		session.save(newOrder);
		session.flush();
		session.close();
	}
	
	public List<Order> findAllNewSession() {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Query query = session.createQuery("select o from Order as o");
		List<Order> result = query.list();
		session.flush();
		session.close();
		return result;
	}
}
