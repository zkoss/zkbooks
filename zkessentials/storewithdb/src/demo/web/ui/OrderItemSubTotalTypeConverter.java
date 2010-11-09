package demo.web.ui;

import org.zkoss.zk.ui.Component;
import org.zkoss.zkplus.databind.TypeConverter;

import demo.model.bean.OrderItem;

/**
 * @author zkessentials store
 * 
 *         This class provides a type converter for an {@code OrderItem}. This
 *         converter takes the OrderItem object and converts it into better
 *         representation for a user
 * 
 */
public class OrderItemSubTotalTypeConverter implements TypeConverter {

	public Object coerceToBean(Object val, Component comp) {
		throw new UnsupportedOperationException();
	}

	public Object coerceToUi(Object val, Component comp) {
		OrderItem item = (OrderItem) val;
		String subTotal = "$ " + (item.getQuantity() * item.getPrice());
		return subTotal;
	}

}
