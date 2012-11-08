package org.zkoss.reference.developer.spring.order.domain;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository("orderDao")
public class OrderDao{
	@PersistenceContext
	private EntityManager em;
	
	@Transactional
	public void newOrder(Order order) {
		
		em.persist(order);
	}
	public List findAll(){
		return em.createQuery("select O from Order O").getResultList();
	}
	
	@Transactional
	public void save(Order order) {
		em.merge(order);
	}
	
	@Transactional
	public void remove(Order order) {
		em.remove(em.merge(order));
	}
}
