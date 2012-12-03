package org.zkoss.reference.developer.jpa.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


@Entity
@Table(name="orders")
public class Order {
	public static final String COMPLETE = "complete";
	public static final String PROCESSING = "processing";
	public static final String CANCELED = "canceled";
	public static final String REJECTED = "rejected";
	private static final Set<String> STATUS = new HashSet<String>();
	static {
		STATUS.add(COMPLETE);
		STATUS.add(PROCESSING);
		STATUS.add(CANCELED);
		STATUS.add(REJECTED);
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String status = PROCESSING;
	private String description;

//	@OneToMany(mappedBy="orderId", fetch=FetchType.EAGER)
	@OneToMany(mappedBy="orderId", fetch=FetchType.LAZY)
	private List<OrderItem> items = new ArrayList<OrderItem>();

	@Temporal(TemporalType.TIMESTAMP)
	private Date createDate = new Date();

	@Temporal(TemporalType.TIMESTAMP)
	private Date updateDate = createDate;


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}


	public List<OrderItem> getItems() {
		return items;
	}

	public OrderItem getItem(int index) {
		return items.get(index);
	}

	public int size() {
		return items.size();
	}

	public void addItem(OrderItem item) {
		items.add(item);
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		if (!STATUS.contains(status)) {
			throw new IllegalArgumentException("status is incorrect: " + status);
		}
		this.status = status;
	}

	public boolean isComplete() {
		return COMPLETE.equals(status);
	}

	public boolean isProcessing() {
		return PROCESSING.equals(status);
	}

	public boolean isCanceled() {
		return CANCELED.equals(status);
	}

	public boolean isRejected() {
		return REJECTED.equals(status);
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Order))
			return false;
		Order other = (Order) obj;
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
}
