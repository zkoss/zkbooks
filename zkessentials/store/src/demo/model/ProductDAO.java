package demo.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import demo.model.bean.Product;

/**
 * @author zkessentials store
 * 
 *         This class provides functionality to access the {@code Product} model
 *         storage system
 * 
 */
public class ProductDAO {

	private static final Map<Long, Product> dbModel = new HashMap<Long, Product>();

	private static final long SEC = 1000;
	private static final long MIN = 60 * SEC;
	private static final long HOUR = 60 * MIN;
	private static final long DAY = 24 * HOUR;

	private static final long time = System.currentTimeMillis();
	public static final Product PROD1 = new Product(1, "Cookies", new Date(time
			- 3 * DAY + 5 * HOUR + 30 * MIN), 4F, 30, true, "/image/cookie.jpg");
	public static final Product PROD2 = new Product(2, "Toast", new Date(time
			- 5 * DAY + 3 * HOUR), 3F, 43, true, "/image/toast.jpg");
	public static final Product PROD3 = new Product(3, "Chocolate", new Date(
			time - 10 * DAY + 5 * HOUR + +33 * MIN), 5.1F, 12, true,
			"/image/chocolate.jpg");
	public static final Product PROD4 = new Product(4, "Butter", new Date(time
			- 7 * DAY + 8 * HOUR), 2.5F, 60, true, "/image/butter.jpg");
	public static final Product PROD5 = new Product(5, "Milk", new Date(time
			- 4 * DAY + 3 * HOUR), 3.1F, 71, true, "/image/milk.jpg");
	public static final Product PROD6 = new Product(6, "Coffee Powder",
			new Date(time - 4 * DAY + 3 * HOUR), 10.4F, 59, true,
			"/image/coffee.jpg");

	static {
		dbModel.put(PROD1.getId(), PROD1);
		dbModel.put(PROD2.getId(), PROD2);
		dbModel.put(PROD3.getId(), PROD3);
		dbModel.put(PROD4.getId(), PROD4);
		dbModel.put(PROD5.getId(), PROD5);
		dbModel.put(PROD6.getId(), PROD6);
	}

	public List<Product> findAll() {
		return new ArrayList<Product>(dbModel.values());
	}

	public List<Product> findAllAvailable() {
		ArrayList<Product> result = new ArrayList<Product>();
		for (Product prod : new ArrayList<Product>(dbModel.values())) {
			if (prod.isAvailable()) {
				result.add(prod);
			}
		}
		return result;
	}

	public Product remove(long productId) {
		Product prod = dbModel.get(productId);
		prod.setAvailable(false);
		return prod;
	}

	public Product putOn(long productId) {
		Product prod = dbModel.get(productId);
		prod.setAvailable(true);
		return prod;
	}

}
