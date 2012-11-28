package org.zkoss.reference.developer.hibernate.service;

import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.zkoss.reference.developer.hibernate.dao.SpringOrderDao;
import org.zkoss.reference.developer.hibernate.domain.Order;

@Service
public class OrderService {

	@Autowired
	private SpringOrderDao springOrderDao;
	
	public void createAndError(){
		create();
		createError();
	}
	
	@Transactional(readOnly=true, propagation=Propagation.SUPPORTS)
	public List<Order> findAll(){
		return springOrderDao.queryAll();
	}

	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public void create(){
		Order newOrder = new Order();
		newOrder.setDescription("Service layer added "+Calendar.getInstance().getTime());
		springOrderDao.save(newOrder);
	}
	
	/*
	 * Spring transaction roll-backs for all RuntimeException by default. 
	 */
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public void createError(){
		Order newOrder = new Order( );
		newOrder.setDescription("Service layer added "+Calendar.getInstance().getTime());
		springOrderDao.errorSave(newOrder);
	}
	
}
