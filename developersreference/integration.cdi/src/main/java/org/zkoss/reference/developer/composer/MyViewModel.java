package org.zkoss.reference.developer.composer;


import java.util.List;

import org.zkoss.bind.annotation.Init;
import org.zkoss.reference.developer.domain.ProductService;
import org.zkoss.reference.developer.scope.UserPreference;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Window;

/**
 * @author Hawk
 *
 */
public class MyViewModel{

	@WireVariable
	private UserPreference userPreference;
	@WireVariable
	private ProductService productService;
	
	private List<String> productList;
	
	@Init
	public void doAfterCompose(Window comp) throws Exception {
		productList = productService.findAll();
	}

	public UserPreference getUserPreference() {
		return userPreference;
	}
	
}
