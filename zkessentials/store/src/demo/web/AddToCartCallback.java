package demo.web;

import demo.model.bean.CartItem;

/**
 * @author zkessentials store
 * 
 *         This interface enables developers to define a callback which will be
 *         fired when a CartItem is added to the shopping cart.
 * 
 */
public interface AddToCartCallback {

	void afterAdd(boolean hasAddNewCartItem, CartItem item);

}
