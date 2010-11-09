package demo.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import demo.model.bean.CartItem;
import demo.model.bean.Order;
import demo.model.bean.OrderItem;
import demo.model.bean.Product;
import static demo.model.ProductDAO.*;
import static demo.model.bean.Order.*;

/**
 * @author zkessentials store
 * 
 *         This class provides functionality to access the {@code Order} model
 *         storage system
 * 
 */
public class OrderDAO {
	private static final Map<Long, Order> dbModel = new HashMap<Long, Order>();

	private static volatile long orderId = 0L;
	private static volatile long orderItemId = 0L;
	static {
		long time = System.currentTimeMillis();
		Order order = null;

		long user1 = 1L;
		long user2 = 2L;

		order = new Order(orderId++, user1, COMPLETE, new Date(
				time - 995420000L),
				"Customer needs this order delivered by 9:00pm");
		dbModel.put(order.getId(), order);
		order.addItem(new OrderItem(orderItemId++, orderId, PROD1.getId(),
				PROD1.getName(), PROD1.getPrice(), 1));
		order.addItem(new OrderItem(orderItemId++, orderId, PROD2.getId(),
				PROD2.getName(), PROD2.getPrice(), 1));
		order.addItem(new OrderItem(orderItemId++, orderId, PROD3.getId(),
				PROD3.getName(), PROD3.getPrice(), 5));
		order.addItem(new OrderItem(orderItemId++, orderId, PROD4.getId(),
				PROD4.getName(), PROD4.getPrice(), 2));
		order.addItem(new OrderItem(orderItemId++, orderId, PROD5.getId(),
				PROD5.getName(), PROD5.getPrice(), 5));
		order.addItem(new OrderItem(orderItemId++, orderId, PROD6.getId(),
				PROD6.getName(), PROD6.getPrice(), 1));

		order = new Order(orderId++, user2, PROCESSING, new Date(
				time - 965420000L), "No additional requests");
		dbModel.put(order.getId(), order);
		order.addItem(new OrderItem(orderItemId++, orderId, PROD1.getId(),
				PROD1.getName(), PROD1.getPrice(), 5));
		order.addItem(new OrderItem(orderItemId++, orderId, PROD2.getId(),
				PROD2.getName(), PROD2.getPrice(), 10));
		order.addItem(new OrderItem(orderItemId++, orderId, PROD4.getId(),
				PROD4.getName(), PROD4.getPrice(), 4));
		order.addItem(new OrderItem(orderItemId++, orderId, PROD5.getId(),
				PROD5.getName(), PROD5.getPrice(), 1));
		order.addItem(new OrderItem(orderItemId++, orderId, PROD6.getId(),
				PROD6.getName(), PROD6.getPrice(), 1));

		order = new Order(orderId++, user1, PROCESSING, new Date(
				time - 962420000L), "No additional requests");
		dbModel.put(order.getId(), order);
		order.addItem(new OrderItem(orderItemId++, orderId, PROD1.getId(),
				PROD1.getName(), PROD1.getPrice(), 2));
		order.addItem(new OrderItem(orderItemId++, orderId, PROD2.getId(),
				PROD2.getName(), PROD2.getPrice(), 2));
		order.addItem(new OrderItem(orderItemId++, orderId, PROD3.getId(),
				PROD3.getName(), PROD3.getPrice(), 2));
		order.addItem(new OrderItem(orderItemId++, orderId, PROD4.getId(),
				PROD4.getName(), PROD4.getPrice(), 1));
		order.addItem(new OrderItem(orderItemId++, orderId, PROD5.getId(),
				PROD5.getName(), PROD5.getPrice(), 2));
		order.addItem(new OrderItem(orderItemId++, orderId, PROD6.getId(),
				PROD6.getName(), PROD6.getPrice(), 2));
	}

	public List<Order> findAll() {
		return new ArrayList<Order>(dbModel.values());
	}

	public List<Order> findByUser(long userId) {
		ArrayList<Order> list = new ArrayList<Order>();
		for (Order order : dbModel.values()) {
			if (order.getUserId() == userId) {
				list.add(order);
			}
		}
		return list;
	}

	private void add(Order order) {
		order.setId(orderId++);
		dbModel.put(order.getId(), order);
	}

	public Order createOrder(long userId, List<CartItem> items,
			String description) {

		// SIMULATE transaction start

		Order order = new Order(null, userId, Order.PROCESSING, new Date(),
				description);

		this.add(order);

		for (CartItem item : items) {
			Product prod = item.getProduct();
			OrderItem oItem = new OrderItem(orderItemId++, order.getId(), prod
					.getId(), prod.getName(), prod.getPrice(), item.getAmount());
			order.addItem(oItem);
		}

		// SIMULATE transaction end
		return order;
	}

	public Order findById(long orderId) {
		return dbModel.get(orderId);
	}

	public Order cancelOrder(long orderId) {
		Order order = findById(orderId);
		order.setStatus(CANCELED);
		return order;
	}

}
