package org.zkoss.reference.developer.mvvm.databinding.form;
/* OrderVM.java

	Purpose:
		
	Description:
		
	History:
		2011/10/31 Created by Dennis Chen

Copyright (C) 2011 Potix Corporation. All Rights Reserved.
 */


import java.util.Calendar;
import java.util.Date;

import org.zkoss.bind.ValidationContext;
import org.zkoss.bind.Validator;

/**
 * @author dennis
 * 
 */
public class OrderVM2 extends OrderVM{

	//validators for command
	public Validator getCreationDateValidator(){
		return new Validator(){
			public void validate(ValidationContext ctx) {
				Date creation = (Date)ctx.getProperty().getValue();
				if(creation==null){
					ctx.setInvalid();// mark invalid
					validationMessages.put("creationDate", "must not null");
				}else{
					validationMessages.remove("creationDate");
				}
				//notify messages was changed.
				ctx.getBindContext().getBinder().notifyChange(validationMessages, "creationDate");
			}
		};
	}
	public Validator getShippingDateValidator(){
		return new Validator(){
			public void validate(ValidationContext ctx) {
				Date shipping = (Date)ctx.getProperty().getValue();//the main property
				Date creation = (Date)ctx.getProperties("creationDate")[0].getValue();//the collected
				//do mixed validation, shipping date have to large than creation more than 3 days.
				if(!CaldnearUtil.isDayAfter(creation,shipping,3)){
					ctx.setInvalid();
					validationMessages.put("shippingDate", "must large than creation date at least 3 days");
				}else{
					validationMessages.remove("shippingDate");
				}
				//notify the 'price' message in messages was changed.
				ctx.getBindContext().getBinder().notifyChange(validationMessages, "shippingDate");
			}

		};
	}
	
	static class CaldnearUtil{
		static public boolean isDayAfter(Date date, Date laterDay , int day) {
			if(date==null) return false;
			if(laterDay==null) return false;
			
			Calendar cal = Calendar.getInstance();
			Calendar lc = Calendar.getInstance();
			
			cal.setTime(date);
			lc.setTime(laterDay);
			
			int cy = cal.get(Calendar.YEAR);
			int ly = lc.get(Calendar.YEAR);
			
			int cd = cal.get(Calendar.DAY_OF_YEAR);
			int ld = lc.get(Calendar.DAY_OF_YEAR);
			
			return (ly*365+ld)-(cy*365+cd) >= day; 
		}
	}
	
	
	
}
