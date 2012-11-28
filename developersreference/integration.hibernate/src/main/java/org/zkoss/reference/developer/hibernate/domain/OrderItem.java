package org.zkoss.reference.developer.hibernate.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * @author zkessentials store
 * 
 *         This class provides a representation of an {@code OrderItem}
 * 
 */
@Entity
@Table(name="OrderedItems")
public class OrderItem {

	@Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
	@ManyToOne @JoinColumn(name="orderId")
	private Order order;
//	private long orderId;
	private long prodId;

	private String name;
	private float price;
	private int quantity;

	public OrderItem() {
	}

	public OrderItem(Long id, long prodId, String name,
			float price, int quantity) {
		super();
		if (id != null) this.id = id;
		this.prodId = prodId;
		this.name = name;
		this.price = price;
		this.quantity = quantity;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

//	public long getOrderId() {
//		return orderId;
//	}
//
//	public void setOrderId(long orderId) {
//		this.orderId = orderId;
//	}

	@Override
	public boolean equals(Object obj) {
		return ((obj instanceof OrderItem) &&
				 this.id.equals(((OrderItem)obj).getId()));
	}
	
	@Override
	public int hashCode() {
		return id.hashCode();
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}
}
