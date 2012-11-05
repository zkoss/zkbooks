package org.zkoss.reference.developer.composer;

import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;
import org.zkoss.zul.Window;

/**
 * try to remove a variable resolver programmatically.
 * @author Hawk
 *
 */
public class ResolverComposer extends SelectorComposer<Window> {



	@Listen("onClick = button[label='Remove Resolver']")
	public void removeSpringVariableResolver(){
		System.out.println("composer's resolver number:"+_resolvers.size());
		for (org.zkoss.xel.VariableResolver r :_resolvers){
			System.out.println(r.toString());
		}
		System.out.println(Executions.getCurrent().getVariableResolver());
		for (Page page: Executions.getCurrent().getDesktop().getPages()){
			page.removeVariableResolver(new DelegatingVariableResolver());
		}
	}
}
