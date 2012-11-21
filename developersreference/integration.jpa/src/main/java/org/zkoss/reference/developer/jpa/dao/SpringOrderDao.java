package org.zkoss.reference.developer.jpa.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnitUtil;
import javax.persistence.Query;

import org.hibernate.HibernateException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.zkoss.reference.developer.jpa.domain.Order;

/**
 * Spring injected EntityManager and declarative transaction management.
 */
@Repository
public class SpringOrderDao {

	@PersistenceContext
	private EntityManager em;
	
	@Transactional(readOnly=true)
	public List<Order> queryAll() {
		Query query = em.createQuery("from Order as o");
		List<Order> result = query.getResultList();
		return result;
	}

	@Transactional
	public Order save(Order newOrder) throws HibernateException{
		em.persist(newOrder);
		em.flush();
		return newOrder;
	}
	
	@Transactional(rollbackFor=RuntimeException.class)
	public void errorSave(Order newOrder){
		em.persist(newOrder);
		em.flush(); //force flush
		// throw exception on purpose
		throw new RuntimeException("error save");
	}
	/**
	 * Initialize lazy-loaded collection.
	 * @param order
	 * @return
	 */
	@Transactional(readOnly=true)
	public Order reload(Order order){
		//check it's detached object
		PersistenceUnitUtil unitUtil = em.getEntityManagerFactory().getPersistenceUnitUtil();
		if (order.getId()!=null && !unitUtil.isLoaded(order, "items")){
			order = em.find(Order.class, order.getId());
		}
		return order;
	}
}
