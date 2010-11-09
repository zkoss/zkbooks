package demo.model.bean;

import java.util.Date;

/**
 * @author zkessentials store
 * 
 *         This class provides a representation of a {@code Product}
 * 
 */
public class Product {

	private long id;
	private String name;
	private Date createDate;
	private float price;
	private int quantity;
	private boolean available;
	private String imgPath;

	public Product() {
	}

	public Product(long id, String name, Date createDate, float price,
			int quantity, boolean available, String imgPath) {
		super();
		this.id = id;
		this.name = name;
		this.createDate = createDate;
		this.price = price;
		this.quantity = quantity;
		this.available = available;
		this.imgPath = imgPath;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
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

	public boolean isAvailable() {
		return available;
	}

	public void setAvailable(boolean available) {
		this.available = available;
	}

	public String getImgPath() {
		return imgPath;
	}

	public void setImgPath(String imgPath) {
		this.imgPath = imgPath;
	}

}
