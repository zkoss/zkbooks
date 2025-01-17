package org.zkoss.reference.developer.spring.composer;

import org.zkoss.reference.developer.spring.domain.OrderService;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.*;
import org.zkoss.zul.Label;
import org.zkoss.zul.Window;

/**
 * demonstrate DelegatingVariableResolver + @WireVariable in a composer
 * @author Hawk
 *
 */
@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class OrderComposer extends SelectorComposer<Window> {

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
