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

	private String name;
	private float price;
	private int quantity;

	public OrderItem() {
	}

	public OrderItem(Long id,String name,
			float price, int quantity) {
		super();
		if (id != null) this.id = id;
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

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof OrderItem))
			return false;
		OrderItem other = (OrderItem) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}
}
