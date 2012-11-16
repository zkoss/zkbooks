package org.zkoss.reference.developer.hibernate.dao;

import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.zkoss.reference.developer.hibernate.domain.Order;

/**
 * get session manually
 */
@Repository
public class SpringOrderDao {

//	private SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
	@Autowired
	private SessionFactory sessionFactory;
	
//	@Transactional
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
		//FIXME handle rollback
		Session session = sessionFactory.getCurrentSession();
		session.save(newOrder);
		return newOrder;
	}
	
	public void errorSave(Order newOrder) throws HibernateException{
		Session session = sessionFactory.getCurrentSession();
		try{
			session.save(newOrder);
			// throw exception to test
			//FIXME no rollback
			throw new HibernateException("error save");
		}catch (Exception e) {
			session.getTransaction().rollback();
		}
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
}
