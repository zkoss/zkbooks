package bigbank.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.Assert;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import bigbank.BankServiceScenario1;
import bigbank.BankServiceScenario1Impl;

public class ListAccountsScenario1 implements Controller {

    private BankServiceScenario1 bankServiceScenario1;

    public ListAccountsScenario1(BankServiceScenario1 bankService) {
        Assert.notNull(bankService);
        this.bankServiceScenario1 = bankService;
    }

    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        // Security check (this is unnecessary if Spring Security is performing the authorization)
//        if (request.getUserPrincipal() == null) {
//            throw new AuthenticationCredentialsNotFoundException("You must login to view the account list (Spring Security message)"); // only for Spring Security managed authentication
//        }

        // Actual business logic
        ModelAndView mav = new ModelAndView("listAccounts1");
        mav.addObject("accounts", bankServiceScenario1.findAccounts());
        return mav;
    }

}
