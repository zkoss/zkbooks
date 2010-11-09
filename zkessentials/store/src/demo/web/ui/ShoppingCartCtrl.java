/* ShoppingCartCtrl.java

	Purpose:
		
	Description:
		
	History:
		2010/7/16, Created by Ian Tsai

Copyright (C) 2010 Potix Corporation. All Rights Reserved.
 */
package demo.web.ui;

import demo.model.bean.Product;

/**
 * @author zkessentials store
 * 
 *         This is an interface which is used to represent a ShoppingCart
 *         control
 * 
 */
public interface ShoppingCartCtrl {

	void addItem(Product prod, int amount);

	float getCurrentSubTotal();

}
