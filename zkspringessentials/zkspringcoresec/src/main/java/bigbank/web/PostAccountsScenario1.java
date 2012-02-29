package bigbank.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import bigbank.Account;
import bigbank.BankServiceScenario1;
import bigbank.BankServiceScenario2;

@Controller
public class PostAccountsScenario1  {
    private BankServiceScenario1 bankService;

    @Autowired
    public PostAccountsScenario1(BankServiceScenario1 bankService) {
        Assert.notNull(bankService);
        this.bankService = bankService;
    }
    @RequestMapping(value="/postscenario1.html", method=RequestMethod.GET)
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        // Actual business logic
        Long id = ServletRequestUtils.getRequiredLongParameter(request, "id");
        Double amount = ServletRequestUtils.getRequiredDoubleParameter(request, "amount");
        Account a = bankService.readAccount(id);
        bankService.post(a, amount);

        return new ModelAndView("redirect:listAccounts1.html");
    }
}
