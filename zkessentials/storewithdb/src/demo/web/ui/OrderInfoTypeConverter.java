package demo.web.ui;

import java.text.SimpleDateFormat;

import org.zkoss.zk.ui.Component;
import org.zkoss.zkplus.databind.TypeConverter;

import demo.model.bean.Order;

/**
 * @author zkessentials store
 * 
 *         This class provides a type converter for an {@code Order}. This
 *         converter takes the Order object and converts it into better
 *         representation for a user
 * 
 */
public class OrderInfoTypeConverter implements TypeConverter {

	/*
	 * Save
	 */
	public Object coerceToBean(Object val, Component comp) {
		throw new UnsupportedOperationException();
	}

	/*
	 * Load
	 */
	public Object coerceToUi(Object val, Component comp) {
		Order order = (Order) val;
		String info = order.getStatus()
				+ " : "
				+ new SimpleDateFormat("yyyy/MM/dd hh:mm").format(order
						.getCreateDate());
		return info;
	}

}
