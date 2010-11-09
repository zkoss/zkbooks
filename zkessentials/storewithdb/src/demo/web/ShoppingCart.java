package demo.web;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import demo.model.bean.CartItem;
import demo.model.bean.Product;

/**
 * @author zkessentials store
 * 
 *         This class provides a representation of a shopping cart
 * 
 */
public class ShoppingCart {

	private Map<Long, CartItem> items = Collections
			.synchronizedMap(new LinkedHashMap<Long, CartItem>());

	public List<CartItem> getItems() {
		return new ArrayList<CartItem>(items.values());
	}

	public CartItem getItem(long prodId) {
		return items.get(prodId);
	}

	private void add(CartItem item) {
		items.put(item.getProduct().getId(), item);
	}

	public void add(Product prod, int amount, AddToCartCallback callback)
			throws OverQuantityException {

		CartItem item = this.getItem(prod.getId());
		validate(item, prod, amount);
		if (item == null) {
			this.add(item = new CartItem(prod));
			item.add(amount);
			callback.afterAdd(true, item);
		} else {
			item.add(amount);
			callback.afterAdd(false, item);
		}
	}

	private static void validate(CartItem item, Product prod, int amount)
			throws OverQuantityException {
		int oriAmount = item == null ? 0 : item.getAmount();
		int total = oriAmount + amount;
		if (total > prod.getQuantity()) {
			String errMesg = "too much: " + oriAmount + " + " + amount + " > "
					+ prod.getQuantity();
			throw new OverQuantityException(errMesg);
		}
	}

	public void remove(long id) {
		items.remove(id);
	}

	public void clear() {
		items.clear();
	}

	public float getTotalPrice() {
		float subTotal = 0;
		for (CartItem item : items.values()) {
			subTotal += item.getProduct().getPrice() * item.getAmount();
		}
		return subTotal;
	}

}
