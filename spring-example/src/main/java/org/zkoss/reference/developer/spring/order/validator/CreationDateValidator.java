package org.zkoss.reference.developer.spring.order.validator;

import java.util.Date;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.zkoss.bind.ValidationContext;
import org.zkoss.bind.validator.AbstractValidator;

@Component("creationDateValidator")
public class CreationDateValidator extends AbstractValidator{

	public void validate(ValidationContext ctx) {
		Date creation = (Date)ctx.getProperty().getValue();
		if(creation==null){
			addInvalidMessage(ctx, "must not null");
		}
	}
	
}
