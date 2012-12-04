package org.zkoss.reference.developer.hibernate.dao;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.zkoss.reference.developer.hibernate.domain.Order;

/**
 * Spring injected EntityManager and declarative transaction management.
 */
@Repository
public class SpringOrderDao {

	@Autowired
	private SessionFactory sessionFactory;
	
	@Transactional(readOnly=true)
	public List<Order> queryAll() {
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery("select o from Order as o");
		List<Order> result = query.list();
		return result;
	}

	@Transactional
	public Order save(Order newOrder){
		Session session = sessionFactory.getCurrentSession();
		session.save(newOrder);
		session.flush();
		return newOrder;
	}
	
	@Transactional
	public void errorSave(Order newOrder){
		Session session = sessionFactory.getCurrentSession();
		session.save(newOrder);
		session.flush(); //force flush
		// throw exception
		throw new HibernateException("error save");
	}

	public Order reload(Order order){
		return (Order)sessionFactory.getCurrentSession().load(Order.class,order.getId());
	}
}
