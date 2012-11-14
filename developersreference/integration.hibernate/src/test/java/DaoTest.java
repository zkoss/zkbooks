import java.util.Calendar;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;
import org.zkoss.reference.developer.hibernate.dao.WrongOrderDao;
import org.zkoss.reference.developer.hibernate.domain.Order;


public class DaoTest {

	WrongOrderDao orderDao = new WrongOrderDao();

	@Test
	public void createOrder(){
		Order order = new Order();
		order.setCreateDate(Calendar.getInstance().getTime());
		order.setDescription("an order for test");
		order = orderDao.save(order);
		
		Assert.assertNotNull(order.getId());
	}
	
	@Test
	public void findAll(){
		List result = orderDao.findAll();
		Assert.assertEquals(2, result.size());
	}
}
