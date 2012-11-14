package org.zkoss.reference.developer.hibernate.dao;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.zkoss.reference.developer.hibernate.domain.Order;

/**
 * Simple implementation.
 * Get session and control transaction manually.
 */
public class SimpleOrderDao {

	private SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
	
	public List<Order> findAll() {
		Session session = sessionFactory.openSession();
		Query query = session.createQuery("select o from Order as o");
		List result = query.list();
		session.close();
		return result;
	}
	
	public Order save(Order newOrder) throws HibernateException{
		Session session = sessionFactory.openSession();
		Transaction tx = null;
		try{
			tx = session.beginTransaction();
			session.save(newOrder);
			tx.commit();
		}catch(HibernateException ex){
			if (tx != null) { 
				tx.rollback(); 
			} 
			throw ex; 
		}finally{
			session.close();
		}
		return newOrder;
	}
}
