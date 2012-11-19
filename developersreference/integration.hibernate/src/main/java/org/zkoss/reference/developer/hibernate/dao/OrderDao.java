package org.zkoss.reference.developer.hibernate.dao;

import java.sql.SQLException;
import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.zkoss.reference.developer.hibernate.domain.Order;

/**
 * Follow Hibernate reference manual's suggestion (http://docs.jboss.org/hibernate/core/3.6/reference/en-US/html_single/#tutorial-webapp-servlet)
 * , implement session-per-request pattern.   
 * HibernateSessionRequestFilter open and begin transaction. 
 * sessionFactory.getCurrentSession() helps us to get the session under current transaction scoped context. 
 */
public class OrderDao {

	private SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
	
	public List<Order> findAll() {
		Session session = sessionFactory.getCurrentSession();
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
		Session session = sessionFactory.getCurrentSession();
		session.save(newOrder);
		return newOrder;
	}
	
	public void errorSave(Order newOrder) throws HibernateException{
		Session session = sessionFactory.getCurrentSession();
		session.save(newOrder);
		// throw exception to test
		throw new HibernateException("error save");
	}
	/**
	 * Initialize lazy-loaded collection.
	 * @param order
	 * @return
	 */
	public Order load(Order order){
		//check to avoid initializing again
		if (!Hibernate.isInitialized(order.getItems())){
			order = (Order)sessionFactory.getCurrentSession().load(Order.class, order.getId());
			Hibernate.initialize(order.getItems());
		}
		return order;
	}
	
	public void saveNonTransactional(Order newOrder) throws HibernateException{
		Session session = sessionFactory.openSession();
//		session.getTransaction().begin();
		try{
			System.out.println("autocommit:"+session.connection().getAutoCommit());
		}catch (SQLException e) {
			// TODO: handle exception
		}
		session.save(newOrder);
		session.close();
//		session.getTransaction().commit();
	}
	
	public List<Order> findAllNewSession() {
		Session session = sessionFactory.openSession();
		Query query = session.createQuery("select o from Order as o");
		List<Order> result = query.list();
		session.close();
		return result;
	}
}
