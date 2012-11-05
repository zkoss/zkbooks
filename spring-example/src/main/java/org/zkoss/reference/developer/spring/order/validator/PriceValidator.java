package org.zkoss.reference.developer.spring.order.validator;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.zkoss.bind.ValidationContext;
import org.zkoss.bind.validator.AbstractValidator;

@Component("priceValidator")
public class PriceValidator extends AbstractValidator{

	public void validate(ValidationContext ctx) {
		Number price = (Number)ctx.getProperty().getValue();
		if(price==null || price.doubleValue()<=0){
			addInvalidMessage(ctx, "must large than 0");
		}
	}
}
