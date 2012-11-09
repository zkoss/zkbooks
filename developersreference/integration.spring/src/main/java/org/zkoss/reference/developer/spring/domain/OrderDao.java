package org.zkoss.reference.developer.spring.domain;

import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.springframework.stereotype.Repository;

@Repository("orderDao")
public class OrderDao{
	
	private List<Order> orderList = new LinkedList<Order>();
	
	public OrderDao(){
		int id=1;
		Calendar calendar = Calendar.getInstance();
		Date today = calendar.getTime();
		calendar.add(Calendar.DATE, 4);
		Date shippingDate = calendar.getTime();
		orderList.add(new Order(id++,"part AF2 order",20,10,today,shippingDate));
		orderList.add(new Order(id++,"part BB2 order",35,10,today,shippingDate));
		orderList.add(new Order(id++,"part CX1 order",72,10,today,shippingDate));
		orderList.add(new Order(id++,"part DS34 order",88,10,today,shippingDate));
		orderList.add(new Order(id++,"part ZK99 order",85,10,today,shippingDate));
	}
	
	public List findAll(){
		return orderList;
	}
	
}
