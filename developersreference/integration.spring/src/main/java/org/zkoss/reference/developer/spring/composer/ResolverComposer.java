package org.zkoss.reference.developer.spring.composer;

import org.zkoss.reference.developer.spring.domain.OrderService;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Label;
import org.zkoss.zul.Window;

/**
 * @author Hawk
 *
 */
public class ResolverComposer extends SelectorComposer<Window> {

	@WireVariable("orderService")
	private OrderService orderService;
	
	@Wire("#number")
	private Label label;
	
	@Override
	public void doAfterCompose(Window comp) throws Exception {
		super.doAfterCompose(comp);
		label.setValue(Integer.toString(orderService.list().size()));
	}

}
