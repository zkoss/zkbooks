package org.zkoss.reference.developer.spring.order.validator;

import org.springframework.stereotype.Component;
import org.zkoss.bind.ValidationContext;
import org.zkoss.bind.validator.AbstractValidator;

@Component("quantityValidator")
public class QuantityValidator extends AbstractValidator{

	public void validate(ValidationContext ctx) {
		Integer quantity = (Integer)ctx.getProperty().getValue();
		if(quantity==null || quantity<=0){
			addInvalidMessage(ctx, "must large than 0");
		}
	}
}
