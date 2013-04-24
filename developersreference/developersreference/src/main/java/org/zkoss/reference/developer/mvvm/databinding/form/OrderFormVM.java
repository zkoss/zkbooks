package org.zkoss.reference.developer.mvvm.databinding.form;
/* OrderVM.java

	Purpose:
		
	Description:
		
	History:
		2011/10/31 Created by Dennis Chen

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
 */


import java.util.Date;

import org.zkoss.bind.ValidationContext;
import org.zkoss.bind.Validator;


/**
 * @author Hawk
 * 
 */
public class OrderFormVM extends OrderVM3{

	@Override
	public Validator getShippingDateValidator() {
		return new Validator(){
			public void validate(ValidationContext ctx) {
				Date shipping = (Date)ctx.getProperties("shippingDate")[0].getValue();
				Date creation = (Date)ctx.getProperties("creationDate")[0].getValue();
				//do dependent validation, shipping date have to large than creation more than 3 days.
				if(!CaldnearUtil.isDayAfter(creation,shipping,3)){
					ctx.setInvalid();
					validationMessages.put("shippingDate", "must large than creation date at least 3 days");
				}else{
					validationMessages.remove("shippingDate");
				}
				//notify the binder of validation message changed
				ctx.getBindContext().getBinder().notifyChange(validationMessages, "shippingDate");
			}

		};
	}
	
}
