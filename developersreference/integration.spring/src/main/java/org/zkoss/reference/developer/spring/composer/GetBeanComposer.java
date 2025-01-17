package org.zkoss.reference.developer.spring.composer;

import java.util.List;

import org.zkoss.reference.developer.spring.domain.Order;
import org.zkoss.reference.developer.spring.domain.OrderService;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Label;
import org.zkoss.zul.Window;

/**
 * ZK Developer's Reference/Integration/Middleware Layer/Spring#Retrieve_a_Spring_Bean_Programmatically
 * @author Hawk
 *
 */
public class GetBeanComposer extends SelectorComposer<Window> {

	private OrderService orderService;
	
	private List<Order> orderList;
	@Wire("#number")
	private Label label;
	
	@Override
	public void doAfterCompose(Window comp) throws Exception {
		super.doAfterCompose(comp);
		OrderService orderService = (OrderService)SpringUtil.getBean("orderService");
		label.setValue(Integer.toString(orderService.list().size()));
	}
	
	public void reload(){
		orderList = orderService.list();
	}
	public GetBeanComposer(){
		System.out.println("crate "+ GetBeanComposer.class+", "+hashCode());
	}
}
