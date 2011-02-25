package bigbank.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.Assert;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import bigbank.Account;
import bigbank.BankServiceScenario2;

public class PostAccountsScenario2 implements Controller {

	private BankServiceScenario2 bankService;
	
	public PostAccountsScenario2(BankServiceScenario2 bankService) {
		Assert.notNull(bankService);
		this.bankService = bankService;
	}

	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// Actual business logic
		Long id = ServletRequestUtils.getRequiredLongParameter(request, "id");
		Double amount = ServletRequestUtils.getRequiredDoubleParameter(request, "amount");
		Account a = bankService.readAccount(id);
		bankService.post(a, amount);
		return new ModelAndView("redirect:listAccounts2.html");
	}
}
