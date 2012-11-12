package org.zkoss.reference.developer.cdi.composer;


import org.zkoss.reference.developer.cdi.domain.NormalOrderService;
import org.zkoss.reference.developer.cdi.domain.ProductService;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Label;
import org.zkoss.zul.Window;

/**
 * @author Hawk
 *
 */
public class ResolverComposer extends SelectorComposer<Window> {

	@WireVariable("normalOrderService")
	NormalOrderService orderService;
	
	@Wire("#number")
	private Label label;
	
	
	@Override
	public void doAfterCompose(Window comp) throws Exception {
		super.doAfterCompose(comp);
		label.setValue(Integer.toString(orderService.findAll().size()));
	}
	
}
