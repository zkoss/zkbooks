package demo.model.bean;

/**
 * @author zkessentials store
 * 
 *         This class provides a representation of a {@code CartItem}
 * 
 */
public class CartItem {

	private Product product;
	private int amount;

	public CartItem(Product product) {
		super();
		this.product = product;
	}

	public Product getProduct() {
		return product;
	}

	public int getAmount() {
		return amount;
	}

	public void add(int amount) {
		this.amount += amount;
	}

	public float getSubTotal() {
		return product.getPrice() * amount;
	}
}
