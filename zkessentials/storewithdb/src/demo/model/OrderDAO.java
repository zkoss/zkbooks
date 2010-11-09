package demo.model;

import static demo.model.bean.Order.CANCELED;

import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import demo.model.bean.CartItem;
import demo.model.bean.Order;
import demo.model.bean.OrderItem;
import demo.model.bean.Product;

/**
 * @author zkessentials store
 * 
 *         This class provides functionality to access the {@code Order} model
 *         storage system
 * 
 */
public class OrderDAO {

	@SuppressWarnings("unchecked")
	public List<Order> findAll() {
		Session session = StoreHibernateUtil.openSession();
		Query query = session.createQuery("from Order");
		List orders = query.list();

		session.close();
		return orders;
	}

	@SuppressWarnings("unchecked")
	public List<Order> findByUser(long userId) {
		Session session = StoreHibernateUtil.openSession();
		Transaction t = session.beginTransaction();
		Criteria criteria = session.createCriteria(Order.class).add(
				Restrictions.eq("userId", userId));
		List orders = criteria.list();
		t.commit();
		session.close();

		return orders;
	}

	private void add(Order order) {
		Session session = StoreHibernateUtil.openSession();
		Transaction t = session.beginTransaction();

		session.persist(order);
		t.commit();

		session.close();
	}

	public Order createOrder(long userId, List<CartItem> items,
			String description) {

		Order order = new Order(null, userId, Order.PROCESSING, new Date(),
				description);

		this.add(order);

		Session session = StoreHibernateUtil.openSession();
		Transaction t = session.beginTransaction();

		for (CartItem item : items) {
			Product prod = item.getProduct();

			OrderItem oItem = new OrderItem(null, prod.getId(), prod.getName(),
					prod.getPrice(), item.getAmount());

			session.persist(oItem);
			order.addItem(oItem);
		}

		session.update(order);

		t.commit();
		session.close();

		return order;
	}

	public Order findById(long orderId) {
		Session session = StoreHibernateUtil.openSession();
		Criteria criteria = session.createCriteria(Order.class).add(
				Restrictions.eq("id", orderId));
		Order order = (Order) criteria.uniqueResult();
		session.close();

		return order;
	}

	public Order cancelOrder(long orderId) {
		Session session = StoreHibernateUtil.openSession();
		Order order = findById(orderId);
		order.setStatus(CANCELED);

		Transaction t = session.beginTransaction();

		session.update(order);
		session.flush();
		t.commit();

		session.close();

		return order;
	}

}
