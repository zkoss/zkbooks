package org.zkoss.reference.developer.mvvm.databinding;

import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Init;

public class InitVM {

	@Init
	public void init(@BindingParam("arg1") String arg1){
		System.out.println("arg1: "+arg1);
	}
}
