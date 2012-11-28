package org.zkoss.reference.developer.hibernate.model;

import java.util.LinkedList;
import java.util.List;

import org.zkoss.reference.developer.hibernate.dao.OrderDao;
import org.zkoss.reference.developer.hibernate.domain.Order;
import org.zkoss.zul.AbstractListModel;

public class ReloadListModel extends AbstractListModel<Order>{

	private static final long serialVersionUID = -7982684413905984053L;
	
	private OrderDao orderDao = new OrderDao();
	List<Order> orderList = new LinkedList<Order>();
	
	public ReloadListModel(List<Order> orders){
		this.orderList = orders;
	}
	
	@Override
	public Order getElementAt(int index) {
		Order renewOrder = orderDao.reload(orderList.get(index));
		return renewOrder;
	}

	@Override
	public int getSize() {
		return orderList.size();
	}
}
