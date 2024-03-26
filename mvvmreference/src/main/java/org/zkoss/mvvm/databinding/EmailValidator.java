package org.zkoss.mvvm.databinding;

import org.zkoss.bind.ValidationContext;
import org.zkoss.bind.validator.AbstractValidator;

import java.util.regex.Pattern;

public class EmailValidator extends AbstractValidator {

	private static Pattern pattern = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,}", Pattern.CASE_INSENSITIVE);
	@Override
	public void validate(ValidationContext ctx) {
		String email = ctx.getProperty().getValue().toString();
		if (!pattern.matcher(email).matches()){
			addInvalidMessage(ctx, "invalid email");
		}
	}

}
