package org.zkoss.reference.developer.mvvm.databinding.form;
/* SearchService.java

	Purpose:
		
	Description:
		
	History:
		2011/10/25 Created by Dennis Chen

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
 */


import java.util.List;

/**
 * @author dennis
 *
 */
public interface OrderService {
	public List<Order> list();
	public void save(Order order);
	public void delete(Order order);
}
