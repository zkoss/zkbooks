package demo.model.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

/**
 * @author zkessentials store
 * 
 *         This class provides a representation of an {@code Order}
 * 
 */
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
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private long userId;
	private String status = PROCESSING;
	private String description;
	private Float adjust;

	@OneToMany
	@JoinTable(name = "OrderedItems", joinColumns = @JoinColumn(name = "orderid", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "orderitemid", referencedColumnName = "id"))
	@LazyCollection(value = LazyCollectionOption.FALSE)
	private List<OrderItem> items = new ArrayList<OrderItem>();

	@Temporal(TemporalType.TIMESTAMP)
	private Date createDate = new Date();

	@Temporal(TemporalType.TIMESTAMP)
	private Date updateDate = createDate;

	public Order() {
	}

	public Order(Long id, long userId, String status, Date createDate,
			String description) {
		super();
		this.id = id;
		this.userId = userId;
		this.status = status;
		this.description = description;
		this.createDate = createDate;
		this.updateDate = createDate;
	}

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

	public float getTotalAdjust() {
		return adjust;
	}

	public void setTotalAdjust(float adjust) {
		this.adjust = adjust;
	}

	public float getTotal() {
		float total = 0;
		for (OrderItem it : new ArrayList<OrderItem>(items)) {
			total += it.getPrice() * it.getQuantity();
		}
		return total;
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

	public float getAdjust() {
		return adjust;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

}
