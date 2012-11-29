import java.util.Calendar;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;
import org.zkoss.reference.developer.hibernate.dao.OrderDao;
import org.zkoss.reference.developer.hibernate.dao.WrongOrderDao;
import org.zkoss.reference.developer.hibernate.domain.Order;


public class DaoTest {

	WrongOrderDao wrongOrderDao = new WrongOrderDao();
	OrderDao orderDao = new OrderDao();
	
//	@Test
	public void createOrder(){
		Order order = new Order();
		order.setCreateDate(Calendar.getInstance().getTime());
		order.setDescription("an order for test");
		order = orderDao.save(order);
		
		Assert.assertNotNull(order.getId());
	}
	@Test
	public void saveNonTransaction(){
		Order order = new Order();
		order.setCreateDate(Calendar.getInstance().getTime());
		order.setDescription("an order for test");
		orderDao.saveInNewSession(order);
		List result = orderDao.findAllNewSession();
		Assert.assertEquals(3, result.size());
	}
	
	@Test
	public void findAll(){
		List result = orderDao.findAllNewSession();
		Assert.assertEquals(2, result.size());
	}
	
}
