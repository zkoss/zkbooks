package org.zkoss.reference.developer.spring.domain;

import java.util.*;

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

	public Order newOrder() {
		int lastId = orderList.get(orderList.size() - 1).id;
		Calendar calendar = Calendar.getInstance();
		Date today = calendar.getTime();
		calendar.add(Calendar.DATE, 4);
		Date shippingDate = calendar.getTime();
		Order order = new Order(++lastId, "new order " + UUID.randomUUID().toString().substring(30), 20, 10, today, shippingDate);
		orderList.add(order);
		return order;
	}
}
