package bigbank;

import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;


public interface BankServiceScenario1 {

    public Account readAccount(Long id);

    public Account[] findAccounts();

    @PreAuthorize(
            "hasRole('ROLE_SUPERVISOR') or hasRole('ROLE_TELLER')" )
    public Account post(Account account, double amount);
}
