package org.zkoss.reference.developer.cdi.composer;


import java.util.List;
import java.util.Set;

import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;

import org.zkoss.reference.developer.cdi.domain.OrderService;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zkplus.cdi.CDIUtil;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Grid;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Window;

/**
 * @author Hawk
 *
 */
@VariableResolver(org.zkoss.zkplus.cdi.DelegatingVariableResolver.class)
public class DynamicComposer extends SelectorComposer<Window> {

	@Wire
	private Checkbox specialBox;
	@Wire("grid")
	private Grid grid;
	private OrderService orderService;
	private List<String> orderList;
	
	
	@Listen("onClick = button")
	public void listOrder(){
		Set<Bean<?>> beans = null;
		BeanManager beanManager = CDIUtil.getBeanManager();
		if (specialBox.isChecked()){
			beans = beanManager.getBeans("specialOrderService");
		}else{
			beans = beanManager.getBeans("normalOrderService");
		}
		if (!beans.isEmpty()){
			 Bean bean = beans.iterator().next();
			 orderService = (OrderService)beanManager.getReference(bean, bean.getClass(), beanManager.createCreationalContext(bean));
			 orderList = orderService.findAll();
			 grid.setModel(new ListModelList(orderList));
		}
	}
}
