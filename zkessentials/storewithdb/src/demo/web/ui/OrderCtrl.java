package demo.web.ui;

import java.util.List;

import demo.model.bean.CartItem;
import demo.model.bean.Order;

/**
 * @author zkessentials store
 * 
 *         This is an interface which is used to represent an {@code Order}
 *         control
 * 
 */
public interface OrderCtrl {

	public Order submitNewOrder(List<CartItem> items, String description);

}
