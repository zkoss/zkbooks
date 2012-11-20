package org.zkoss.reference.developer.hibernate.dao;

import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.zkoss.reference.developer.hibernate.domain.Order;

/**
 * get session manually
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

	/**
	 * rollback is handled in filter.
	 * @param newOrder
	 * @return
	 * @throws HibernateException
	 */
	@Transactional
	public Order save(Order newOrder) throws HibernateException{
		//FIXME handle rollback
		Session session = sessionFactory.getCurrentSession();
		session.save(newOrder);
		session.flush();
		return newOrder;
	}
	
	@Transactional(rollbackFor=HibernateException.class)
	public void errorSave(Order newOrder){
		Session session = sessionFactory.getCurrentSession();
		session.save(newOrder);
		session.flush(); //force flush
		// throw exception to test
		throw new HibernateException("error save");
	}
	/**
	 * Initialize lazy-loaded collection.
	 * @param order
	 * @return
	 */
	public Order refresh(Order order){
		//check it's detached object and to avoid initializing again
		if (order.getId()!=null && !Hibernate.isInitialized(order.getItems())){
			sessionFactory.getCurrentSession().refresh(order);
//			alternative:			
//			sessionFactory.getCurrentSession().load(Order.class, order.getId());
//			Hibernate.initialize(order.getItems());
		}
		return order;
	}
}
