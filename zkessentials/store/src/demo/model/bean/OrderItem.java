package demo.model.bean;

/**
 * @author zkessentials store
 * 
 *         This class provides a representation of an {@code OrderItem}
 * 
 */
public class OrderItem {

	private long id;
	private long orderId;
	private long prodId;

	private String name;
	private float price;
	private int quantity;

	public OrderItem() {
	}

	public OrderItem(Long id, long orderId, long prodId, String name,
			float price, int quantity) {
		super();
		if (id != null)
			this.id = id;
		this.orderId = orderId;
		this.prodId = prodId;
		this.name = name;
		this.price = price;
		this.quantity = quantity;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getOrderId() {
		return orderId;
	}

	public void setOrderId(long orderId) {
		this.orderId = orderId;
	}

	public long getProdId() {
		return prodId;
	}

	public void setProdId(long prodId) {
		this.prodId = prodId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

}
