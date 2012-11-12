package org.zkoss.reference.developer.cdi.composer;


import java.util.List;

import org.zkoss.reference.developer.cdi.domain.ProductService;
import org.zkoss.reference.developer.cdi.scope.UserPreference;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Label;
import org.zkoss.zul.Window;

/**
 * @author Hawk
 *
 */
@VariableResolver(org.zkoss.zkplus.cdi.DelegatingVariableResolver.class)
public class MyComposer extends SelectorComposer<Window> {

	@WireVariable
	private UserPreference userPreference;
	@WireVariable
	private ProductService productService;
	@Wire
	private Label prefernceLabel;
	
	private List<String> productList;
	
	@Override
	public void doAfterCompose(Window comp) throws Exception {
		super.doAfterCompose(comp);
		prefernceLabel.setValue(userPreference.getValue());
		productList = productService.findAll();
	}
}
