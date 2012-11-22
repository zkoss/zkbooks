package org.zkoss.reference.developer.hibernate.dao;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.zkoss.reference.developer.hibernate.domain.Order;

/**
 * This is a implementation of session-per-operation which is an anti-pattern.
 * http://docs.jboss.org/hibernate/core/3.6/reference/en-US/html_single/#transactions-basics-uow
 * Get session and control transaction manually.
 */
public class WrongOrderDao {

	
	public List<Order> findAll() {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Query query = session.createQuery("select o from Order as o");
		List result = query.list();
		session.close();
		return result;
	}

	public Order save(Order newOrder) throws HibernateException{
		Session session = HibernateUtil.getSessionFactory().openSession();
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
		}
		return newOrder;
	}
}
